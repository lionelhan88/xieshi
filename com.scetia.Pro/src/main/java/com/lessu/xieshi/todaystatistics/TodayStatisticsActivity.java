package com.lessu.xieshi.todaystatistics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.bean.JinriItem;
import com.lessu.xieshi.bean.Jinritongji;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TodayStatisticsActivity extends NavigationActivity implements OnItemClickListener, View.OnClickListener {
	private ArrayList<JinriItem> list=new ArrayList<JinriItem>();
	String Type = "";
	private TextView tv_qianyitian;
	private TextView tv_houyitian;
	private LinearLayout ll_riqi;
	private TextView tv_riqi;
	private TextView tv_gcyanshouyp;
	private TextView tv_jdchoujianyp;
	private TextView tv_feigcyanshouyp;
	private TextView tv_tongjishijian;
	private PullToRefreshListView listView;
	private LinearLayout ll_today_satictics_title;
	private String riqi;
	private int Year;
	private int Month;
	private int Day;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.today_statistics_activity);
		this.setTitle("今日统计");
		navigationBar.setBackgroundColor(0xFF3598DC);
		initView();
		//设置状态栏高度实现沉浸式状态栏
		//setHeight(ll_today_satictics_title);
		//BarButtonItem	menuButtonitem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
		//menuButtonitem.setOnClickMethod(this,"menuButtonDidClick");
		// navigationBar.setLeftBarItem(menuButtonitem);
	}

	private void initView() {
		ll_today_satictics_title = (LinearLayout) findViewById(R.id.ll_today_satictics_title);
		tv_qianyitian = (TextView)findViewById(R.id.tv_qianyitian);
		tv_houyitian = (TextView) findViewById(R.id.tv_houyitian);
		ll_riqi = (LinearLayout)findViewById(R.id.ll_riqi);
		tv_riqi = (TextView) findViewById(R.id.tv_riqi);
		tv_gcyanshouyp = (TextView) findViewById(R.id.tv_gcyanshouyp);
		tv_jdchoujianyp = (TextView) findViewById(R.id.tv_jdchoujianyp);
		tv_feigcyanshouyp = (TextView) findViewById(R.id.tv_feigcyanshouyp);
		tv_tongjishijian = (TextView) findViewById(R.id.tv_tongjishijian);
		listView = (PullToRefreshListView) findViewById(R.id.pl_jinritongji);
		listView.setMode(PullToRefreshBase.Mode.DISABLED);
		settime();
		tv_riqi.setText(String.valueOf(Month+1)+"月"+String.valueOf(Day)+"日");
		formatdata(Year,Month,Day);
		//riqi = String.valueOf(Year)+String.valueOf(Month+1)+String.valueOf(Day);
		System.out.println(riqi);
		listView.setAdapter(adapter);
		tv_qianyitian.setOnClickListener(this);
		tv_houyitian.setOnClickListener(this);
		ll_riqi.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		requestnet();
	}

	/**
	 * 获取数据内容
	 */
	private void requestnet() {
		String token = LSUtil.valueStatic("Token");
		String type = "0";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		params.put("Type", type);
		params.put("SummDate",riqi);
		System.out.println(params.toString());
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/DetectionTodayStatis"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				System.out.println(result);
				list.clear();
				Jinritongji jinritongji = GsonUtil.JsonToObject(result.toString(), Jinritongji.class);
				Jinritongji.DataBean data = jinritongji.getData();
				Jinritongji.DataBean.JsonContentBean jsonContent = data.getJsonContent();
				final int day_importSampleCount = jsonContent.getDay_ImportSampleCount();
				final int day_jdSample = jsonContent.getDay_JdSample();
				final int day_fgSample = jsonContent.getDay_FgSample();
				final String recordTime = jsonContent.getRecordTime();
				System.out.println(recordTime);
				List<Jinritongji.DataBean.JsonContentBean.ItemListBean> itemList = jsonContent.getItemList();
				for (int i = 0; i < itemList.size() ; i++) {
					String itemName = itemList.get(i).getItemName();
					String itemSampleCount = itemList.get(i).getItemSampleCount()+"";
					String itemSampleFinishCount = itemList.get(i).getItemSampleFinishCount()+"";
					String itemBhgCount = itemList.get(i).getItemBhgCount()+"";
					String itemNoExam = itemList.get(i).getItemNoExam()+"";
					list.add(new JinriItem(itemName,itemSampleCount,itemSampleFinishCount,itemBhgCount,itemNoExam));
					System.out.println(itemList.get(i).getItemName());
				}
//				JsonObject jsonObject = result.getAsJsonObject().get("Data").getAsJsonObject();
//				JsonObject JsonContent = jsonObject.getAsJsonObject().get("JsonContent").getAsJsonObject();
////				System.out.println("day_importConsignCount.........."+day_importConsignCount);
//				list = JsonContent.get("ItemList").getAsJsonArray();
//				System.out.println("list........."+list);
				if (itemList==null||itemList.size()==0){
					LSAlert.showAlert(TodayStatisticsActivity.this, "无相关记录");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_gcyanshouyp.setText(day_importSampleCount+"");
						tv_jdchoujianyp.setText(day_jdSample+"");
						tv_feigcyanshouyp.setText(day_fgSample+"");
						String[] split = recordTime.split("\\s+");
						tv_tongjishijian.setText(split[0]);
					}
				});
				adapter.notifyDataSetChanged();
			}

			@Override
			public String onFailed(ApiError error) {
				ToastUtil.showShort("当前没有内容");
				return null;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
//		Intent intent = new Intent (TodayStatisticsActivity.this, SampleDetailActivity.class) ;
//		Bundle bundle = new Bundle();
//		Bundle bundelForData=this.getIntent().getExtras();
//		String Report_ID = bundelForData.getString("Report_id");
//		String Checksum = bundelForData.getString("Checksum");
//		bundle.putString("Report_id", Report_ID);
//		bundle.putString("Checksum", Checksum);
//		bundle.putString("Sample_id", GsonValidate.getStringByKeyPath(list.get(position), "Sample_Id",""));
//		intent.putExtras(bundle);
//		startActivity(intent);
	}

	protected BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			if (view == null){
				view = View.inflate(TodayStatisticsActivity.this, R.layout.today_statistics_item, null);
			}
			TextView tv_item1 = (TextView) view.findViewById(R.id.tv_item1);
			tv_item1.setText(list.get(position).itemName);
			TextView tv_item2 = (TextView) view.findViewById(R.id.tv_item2);
			tv_item2.setText(list.get(position).itemSampleCount);
			TextView tv_item3 = (TextView) view.findViewById(R.id.tv_item3);
			tv_item3.setText(list.get(position).itemSampleFinishCount);
			TextView tv_item4 = (TextView) view.findViewById(R.id.tv_item4);
			tv_item4.setText(list.get(position).itemBhgCount);

			TextView tv_item5 = (TextView) view.findViewById(R.id.tv_item5);
			tv_item5.setText(list.get(position).itemNoExam);


//			EasyUI.setTextViewText(view.findViewById(R.id.tv_item1), (JsonObject)itemList.get(position), "ItemName", "暂无");
//			EasyUI.setTextViewText(view.findViewById(R.id.SampleCollectionCountTextView), (JsonObject)list.get(position), "SampleCollectionCount", "暂无");
//			EasyUI.setTextViewText(view.findViewById(R.id.QualifiedRateTextView), (JsonObject)list.get(position), "QualifiedRate", "暂无");
//			EasyUI.setTextViewText(view.findViewById(R.id.DetectedSampleCountTextView), (JsonObject)list.get(position), "DetectedSampleCount", "暂无");
//			EasyUI.setTextViewText(view.findViewById(R.id.UnqualifiedSampleCountTextView), (JsonObject)list.get(position), "UnqualifiedSampleCount", "暂无");
//			EasyUI.setTextViewText(view.findViewById(R.id.CompletionRateTextView), (JsonObject)list.get(position), "CompletionRate", "暂无");
			return view;
		}


		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}


	};

//	public void menuButtonDidClick(){
//		menu.toggle();
//	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			//exitBy2Click();
//			return false;
//		} else {
//			return super.dispatchKeyEvent(event);
//		}
//	}
	private static Boolean isExit = false;
	private void exitBy2Click() {
		// TODO Auto-generated method stub
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.tv_qianyitian:
				clearcontent();
				cal.add(Calendar.DAY_OF_MONTH, -1);//取当前日期的前一天.
				Year = cal.get(Calendar.YEAR);
				Month= cal.get(Calendar.MONTH);
				Day = cal.get(Calendar.DAY_OF_MONTH);
				tv_riqi.setText(String.valueOf(Month+1)+"月"+String.valueOf(Day)+"日");
				formatdata(Year, Month, Day);
				//riqi = String.valueOf(Year)+String.valueOf(Month+1)+String.valueOf(Day);
				System.out.println(riqi);
				requestnet();
				break;
			case R.id.tv_houyitian:
				clearcontent();
				cal.add(Calendar.DAY_OF_MONTH, +1);//取当前日期的后一天.
				Year = cal.get(Calendar.YEAR);
				Month= cal.get(Calendar.MONTH);
				Day = cal.get(Calendar.DAY_OF_MONTH);
				tv_riqi.setText(String.valueOf(Month+1)+"月"+String.valueOf(Day)+"日");
				//riqi = String.valueOf(Year)+String.valueOf(Month+1)+String.valueOf(Day);
				formatdata(Year, Month, Day);
				System.out.println(riqi);
				requestnet();
				break;
			case R.id.ll_riqi:
				clearcontent();
				settime();
				new DatePickerDialog(this, mDateSetListener, Year, Month, Day).show();

				break;
		}
	}

	private void clearcontent() {
		list.clear();
		tv_gcyanshouyp.setText("");
		tv_jdchoujianyp.setText("");
		tv_feigcyanshouyp.setText("");
		tv_tongjishijian.setText("");
		adapter.notifyDataSetChanged();
	}

	private void settime() {
		//使用默认时区和语言环境获得一个日历。
		cal = Calendar.getInstance();
		Year = cal.get(Calendar.YEAR);
		Month= cal.get(Calendar.MONTH);
		Day = cal.get(Calendar.DAY_OF_MONTH);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			cal.set(year,monthOfYear,dayOfMonth);
			formatdata(year, monthOfYear, dayOfMonth);

			tv_riqi.setText(String.valueOf(monthOfYear+1)+"月"+String.valueOf(dayOfMonth)+"日");
			//riqi = String.valueOf(year)+String.valueOf(monthOfYear+1)+String.valueOf(dayOfMonth);
			System.out.println(riqi);
			requestnet();
		}

	};

	private void formatdata(int year, int monthOfYear, int dayOfMonth) {
		System.out.println(monthOfYear);
		String s1 = null;
		String s2 = null;
		if(monthOfYear<9){
			s1 = "0" + String.valueOf(monthOfYear + 1);
		}else if(monthOfYear>=9){
			s1=String.valueOf(monthOfYear + 1);
		}
		if(dayOfMonth<10){
			s2 = "0" + String.valueOf(dayOfMonth);
		}else if(dayOfMonth>=10){
			s2 = String.valueOf(dayOfMonth);
		}
		riqi = String.valueOf(year)+s1+s2;

	}
}