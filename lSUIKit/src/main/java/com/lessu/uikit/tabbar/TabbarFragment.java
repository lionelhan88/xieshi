package com.lessu.uikit.tabbar;
import java.util.ArrayList;
import java.util.List;

import com.lessu.uikit.easy.EasyCollection;
import com.lessu.uikit.Utils;







//import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TabbarFragment extends Fragment {
	
	public static final int ContentLayoutId = 1201;
	public static final int TabbarLayoutId  = 1202; 
	
	protected LinearLayout mainView;
	private FrameLayout contentLayout;
	private LinearLayout tabLayout;
	
	protected List<TabbarItem> tabItems;
	private int selectedTab = 0;
	private TabbarFragmentDelegate delegate;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		tabItems = new ArrayList<TabbarItem>();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mainView = new LinearLayout(getActivity()); 
		mainView.setOrientation(LinearLayout.VERTICAL);
		contentLayout = new FrameLayout(getActivity());
		contentLayout.setId(ContentLayoutId);
		LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		contentLayoutParams.weight = 1.0f;
		contentLayout.setLayoutParams(contentLayoutParams);
//		contentLayout.setBackgroundColor(Color.RED);
		mainView.addView(contentLayout);
		
		
		tabLayout = new Tabbar(getActivity());
		tabLayout.setId(TabbarLayoutId);
		LayoutParams tabLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(), 49));
		tabLayout.setLayoutParams(tabLayoutParams);
//		tabLayout.setBackgroundColor(Color.GREEN);
		mainView.addView(tabLayout);

		return mainView;
	}
	@Override
	public void onStart() {
		super.onStart();
		setTabItems(new ArrayList<TabbarItem>(tabItems));
		setSelectedTab(selectedTab,true);
	}
	
	public List<TabbarItem> getTabItems() {
		return tabItems;
	}
	
	protected void setTabItems(List<TabbarItem> tabItems) {
		this.tabItems.clear();
		if(tabLayout == null){
			return ;
		}
		tabLayout.removeAllViews();
		final TabbarFragment self = this;
        EasyCollection.enumlateList(tabItems, new EasyCollection.OnListEnumlateInterface() {
            @Override
            public boolean onEmulate(Object item, long index) {
                TabbarItem tabItem = (TabbarItem) item;

                self.addTabItems(tabItem);

                return true;
            }
        });

	}
	
	protected void addTabItems(TabbarItem tabItem) {
		final TabbarButton tabbarButton = new TabbarButton(getActivity());
		tabbarButton.setUpTabbarButton(tabItem.title, tabItem.imageUnselectedResourceId, tabItem.imageSelectedResourceId);
		tabItems.add(tabItem);
		if(tabLayout == null){
			return ;
		}
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		layoutParams.weight = 1.0f;
		
		tabbarButton.setLayoutParams(layoutParams);
		
		tabLayout.addView(tabbarButton);
		
		final int buttonIndex = (int) tabItems.indexOf(tabItem); 
		final TabbarFragment self = this;
		tabbarButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getDelegate() != null){
					if(getDelegate().onSelectedToIndex(buttonIndex) == true){
						self.setSelectedTab(buttonIndex);
					}	
				}else{
					self.setSelectedTab(buttonIndex);
				}
//				tabbarButton.setSelected(true);
			}
			
		});
		
	}
	public int getSelectedTab() {
		return selectedTab;
	}
	public void setSelectedTab(int selectedTab,boolean force){
		if (selectedTab == this.selectedTab && force == false){
			return ;
		}
		for (int i = 0 ;i < tabLayout.getChildCount() ;i++){
			if (i == selectedTab){
				tabLayout.getChildAt(i).setSelected(true);
			}else{
				tabLayout.getChildAt(i).setSelected(false);
			}
		}
		this.selectedTab = selectedTab;
		TabbarItem item = null;

		try {
			item = tabItems.get(selectedTab);	
		} catch (Exception e) {
			assert false : "selected tab invalid";
			return ;
		}
		
		getFragmentManager().beginTransaction()
			.replace(ContentLayoutId, item.fragment)
			.commit();
		
		
	}
	public void setSelectedTab(int selectedTab) {
		this.setSelectedTab(selectedTab, false);
	}
	public TabbarFragmentDelegate getDelegate() {
		return delegate;
	}
	public void setDelegate(TabbarFragmentDelegate delegate) {
		this.delegate = delegate;
	}
}
