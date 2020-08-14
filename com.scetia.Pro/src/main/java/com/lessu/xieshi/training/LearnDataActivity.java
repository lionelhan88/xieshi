package com.lessu.xieshi.training;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.LongString;
import com.lessu.xieshi.bean.CourseScore;
import com.lessu.xieshi.bean.PaidItem;
import com.lessu.xieshi.bean.Project;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.http.RetrofitManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LearnDataActivity extends NavigationActivity {
    private List<CourseScore> courseScores;
    private ListView dataListView;
    private Map<String ,String> paidItemMap = new HashMap<>();
    private LearnDataAdapter adapter;
    private List<CourseScore> noFinishCourse = new ArrayList<>();
    /**
     * 没有观看的课程
     */
    private List<CourseScore> noneCourse = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_learn);
        setTitle("学习情况");
        navigationBar.setBackgroundColor(0xFF3598DC);
        initView();
        initData();
    }
    private void initView(){
        dataListView = (ListView) findViewById(R.id.learn_data_list);
    }
    private void initData(){
        courseScores = new ArrayList<>();
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void receiverPushToDx(LearnDataEvent learnDataEvent){
        EventBus.getDefault().removeStickyEvent(learnDataEvent);
        PushToDx pushToDx = learnDataEvent.getPushToDx();
        paidItemMap.putAll(learnDataEvent.getPaidItemMap());
        getCourseScores(pushToDx);
    }
    private void getCourseScores(PushToDx pushToDx){
        LSAlert.showProgressHud(this,"正在加载数据...");
        List<Project> projects = pushToDx.getProjects();
        final Observable<BaseResponse<List<CourseScore>>>[] observables = new Observable[1];
        final String userId = pushToDx.getUserId();
        Observable.fromArray(projects)
                .flatMap(new Function<List<Project>, ObservableSource<BaseResponse<List<CourseScore>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<CourseScore>>> apply(List<Project> projects) throws Exception {
                        List<Observable<BaseResponse<List<CourseScore>>>> arrs = new ArrayList<>();
                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String date = sdf.format(new Date());
                        for (Project project:projects) {
                            String paramStr = "planNo="+project.getPlanNo()+"&projectCode="+project.getProjectCode()+
                                    "&timestamp="+date+"&userId="+userId+"&secret=Rpa00Wcw9yaI";
                            String sign = LongString.md5(paramStr).toUpperCase();
                            Observable<BaseResponse<List<CourseScore>>> courseScores = RetrofitManager.getInstance().getService().getCourseScores(userId, project.getProjectCode(),
                                    project.getPlanNo(), date, sign);
                            arrs.add(courseScores);
                       }
                        return Observable.mergeArray(arrs.toArray(observables));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BaseResponse<List<CourseScore>>>() {
                    @Override
                    public void success(BaseResponse<List<CourseScore>> courseScoreBaseResponse) {
                        List<CourseScore> records = courseScoreBaseResponse.getRecord();
                        //有数据时才加入集合
                        if(records.size()>0) {
                            CourseScore courseScore = records.get(0);
                            courseScore.setProjectName(paidItemMap.get(courseScore.getProjectCode()));
                            paidItemMap.remove(courseScore.getProjectCode());
                            if(courseScore.isFinish()){
                                //完成课程了
                                courseScores.add(courseScore);
                            }else{
                                //未完成课程
                                noFinishCourse.add(courseScore);
                            }
                        }
                    }

                    @Override
                    public void failure(String errorMsg) {
                        LSAlert.dismissProgressHud();
                        LSAlert.showAlert(LearnDataActivity.this,errorMsg);
                    }

                    @Override
                    public void onComplete() {
                        //这里需要按 完成，未完成，为观看课程排序
                        showCourse();
                        LSAlert.dismissProgressHud();
                        super.onComplete();
                    }
                });
    }

    private void showCourse(){
        //三个层次内部按名称排序
        final Comparator<CourseScore> comparator = new Comparator<CourseScore>() {
            @Override
            public int compare(CourseScore courseScore, CourseScore t1) {
                return courseScore.getProjectName().compareTo(t1.getProjectName());
            }
        };
        //按课程名称排序集合
        if(courseScores.size()>0) {
            Collections.sort(courseScores, comparator);
        }
        if(noFinishCourse.size()>0) {
            Collections.sort(noFinishCourse, comparator);
        }
        //集合合并
        courseScores.addAll(noFinishCourse);
        //加入未观看的课程
        Set<Map.Entry<String, String>> entries = paidItemMap.entrySet();
        for (Map.Entry<String,String> entry:entries){
            CourseScore courseScore = new CourseScore();
            courseScore.setProjectName(entry.getValue());
            courseScore.setProjectCode(entry.getKey());
            noneCourse.add(courseScore);
        }
        //内部排序
        if(noneCourse.size()>0) {
            Collections.sort(noneCourse, comparator);
        }
        //合并集合
        courseScores.addAll(noneCourse);
        adapter = new LearnDataAdapter(courseScores);
        dataListView.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    private class LearnDataAdapter extends BaseAdapter{
        List<CourseScore> courseScores;
        public LearnDataAdapter(List<CourseScore> courseScores) {
            this.courseScores = courseScores;
        }

        @Override
        public int getCount() {
            return courseScores.size();
        }

        @Override
        public Object getItem(int i) {
            return courseScores.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder  viewHolder = null;
            if(view==null){
                viewHolder = new MyViewHolder();
                view = LayoutInflater.from(LearnDataActivity.this).inflate(R.layout.learn_data_item,viewGroup,false);
                viewHolder.learn_data_item_name = (TextView) view.findViewById(R.id.learn_data_item_name);
                viewHolder.learn_data_item_sign_date = (TextView) view.findViewById(R.id.learn_data_item_sign_date);
                viewHolder.learn_data_item_result = (TextView) view.findViewById(R.id.learn_data_item_result);
                view.setTag(viewHolder);
            }else{
                viewHolder = (MyViewHolder) view.getTag();
            }
            CourseScore courseScore = courseScores.get(i);
            viewHolder.learn_data_item_name.setText(courseScore.getProjectName());
            if(courseScore.getUserId()!=null) {
                viewHolder.learn_data_item_sign_date.setText(courseScore.getFirstEnterTime());
                boolean finish = courseScore.isFinish();
                if (finish) {
                    viewHolder.learn_data_item_result.setTextColor(getResources().getColor(R.color.textcolor));
                    viewHolder.learn_data_item_result.setText("符合要求");
                } else {
                    viewHolder.learn_data_item_result.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    viewHolder.learn_data_item_result.setText("未完成在线教育");
                }
            }else{
                viewHolder.learn_data_item_sign_date.setText("");
                viewHolder.learn_data_item_result.setText("");
            }
            return view;
        }
        class MyViewHolder{
            TextView learn_data_item_name;
            TextView learn_data_item_sign_date;
            TextView learn_data_item_result;
        }
    }
}
