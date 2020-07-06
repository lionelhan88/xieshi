package com.lessu.xieshi;

import android.os.Bundle;
import android.widget.ImageView;

import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiBase;
import com.lessu.uikit.views.LSAlert;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends NavigationActivity {
	String serviceText = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_activity);
		
		setTitle("选择服务器");
		
		navigationBar.setBackgroundColor(0xFF3598DC);
		
		BarButtonItem	completeButtonitem = new BarButtonItem(this ,"完成");
		completeButtonitem.setOnClickMethod(this,"completeButtonDidClick");	
        navigationBar.setRightBarItem(completeButtonitem);
        
        BarButtonItem	nullButtonitem = new BarButtonItem(this,"");
        navigationBar.setLeftBarItem(nullButtonitem);
        
        HashMap<String, ImageView> serviceMap = new HashMap<String, ImageView>();
        serviceMap.put("www.scetia.com/scetia.app.ws",(ImageView) findViewById(R.id.telecomIcon));
        serviceMap.put("www.schetia.com/scetia.app.ws",(ImageView) findViewById(R.id.unicomIcon));
        serviceText = ApiBase.sharedInstance().apiUrl;
        if (serviceMap.containsKey(serviceText))
        {
        	serviceMap.get(serviceText).setImageResource(R.drawable.icon_service_selected);
        }
        else{
        	LSAlert.showAlert(this, "当前服务器错误");
        }
      //  ButterKnife.bind(this);
	}
	
	public void completeButtonDidClick(){
		ApiBase.sharedInstance().apiUrl = serviceText;
		
		/*Intent intent =  new Intent(ServiceActivity.this,SettingActivity.class);
		startActivity(intent);*/
		finish();
    }

	@OnClick(R.id.telecomButton)
	public void telecomButtonDidClick(){
		serviceText = "www.scetia.com/scetia.app.ws";
		LSUtil.setValueStatic("service", "telecom");
		((ImageView) findViewById(R.id.telecomIcon)).setImageResource(R.drawable.icon_service_selected);
		((ImageView) findViewById(R.id.unicomIcon)).setImageResource(R.drawable.icon_service_unselected);
	}
	
	@OnClick(R.id.unicomButton)
	public void unicomButtonDidClick(){
		serviceText = "www.schetia.com/scetia.app.ws";
		LSUtil.setValueStatic("service", "unicom");
		((ImageView) findViewById(R.id.unicomIcon)).setImageResource(R.drawable.icon_service_selected);
		((ImageView) findViewById(R.id.telecomIcon)).setImageResource(R.drawable.icon_service_unselected);
	}
	
}
