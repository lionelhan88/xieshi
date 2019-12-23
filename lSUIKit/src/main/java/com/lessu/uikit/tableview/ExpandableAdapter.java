package com.lessu.uikit.tableview;

import android.content.Context;

public class ExpandableAdapter extends TableAdapter{
	public ExpandableAdapter(Context context) {
		super(context);

	}

	public static abstract class ExpandableDataSource extends TableViewDataSource{
		private boolean[] isExpanded;
		@Override
		protected int numberOfRowsInSection(int section) {
			if(this.isExpanded(section)){
				return 0 ;
			}else{
				return 0;
			}
		}
		
		public boolean isExpanded(int groupIndex) {
			return isExpanded[groupIndex];
		}
		public void setExpanded(int groupIndex,boolean isExpand) {
			this.isExpanded[groupIndex] = isExpand;
		}
		public void toggleExpanded(int groupIndex){
			setExpanded(groupIndex, !isExpanded(groupIndex));
		}
	}
	
	public boolean isEnabled(int position) {
        return true;
    }
	
	
}
