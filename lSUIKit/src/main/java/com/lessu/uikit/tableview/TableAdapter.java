package com.lessu.uikit.tableview;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public class TableAdapter extends BaseAdapter{
	public static final int CellDefaultType   = 1;
	public static final int HeaderDefaultType = 15;
	public static final int HeaderTextType 	  = 2;
	public static class IndexPath{
		public int section;
		public int row;
		public static IndexPath newIndexPath(int section, int row){
			IndexPath indexPath = new IndexPath();
			indexPath.section = section;
			indexPath.row = row;
			return indexPath;
		}
	}
	public static abstract class TableViewDataSource{
		protected abstract int numberOfRowsInSection(int section);
		
		protected abstract View cellForRowAtIndexPath(View reuseView,IndexPath indexPath);
		
		
		public CharSequence titleForHeaderInSection(int section){
			return null;
		}
		public View viewForHeaderInSection(View reuseView,int section){
			return null; 
		};
		
		protected int numberOfSectionsInTableView(){
			return 1;
		};
		
		public int cellTypeRowAtIndexPath(IndexPath indexPath){
			return CellDefaultType;
		}
		public int viewTypeForHeaderInSection(int section){
			return HeaderDefaultType;
		}
	}
	
//	public static abstract class TableViewDelegate{
//		// custom view for header. will be adjusted to default or specified header height
//		
//		public void didSelectRowAtIndexPath(IndexPath indexPath){
//			
//		}
//	}
//	protected List<View> reuseViews;
	protected Context context;
	public TableAdapter(Context context) {
		super();
		this.context = context;
	}
	
	protected TableViewDataSource 	dataSource;
	
	private int[] rowCountInSection;
	private int sectionCount;
	private int total;
	
	public boolean isHeaderPosition(int position){
		int cursor = 0;
		for(int i = 0 ;i < sectionCount ;i++){
			if(position == cursor + i){
				return true;
			}
			if(cursor + i > position){
				return false;
			}
			cursor += rowCountInSection[i]; 
		}
		return false;
	}
	
	public IndexPath indexPathForPosition(int position){
		if(position > this.total){
			Log.e("TableAdapter", "given position is out of range return (-1,-1)");
			return IndexPath.newIndexPath(- 1 , -1);
		}
		int cursor = 0;
		for(int i = 0 ;i < sectionCount ;i++){
			if(position == cursor){
				return IndexPath.newIndexPath(i, -1);
			}
			if(cursor + rowCountInSection[i] >= position){
				return IndexPath.newIndexPath(i, position - (cursor) - 1);
			}
			cursor += rowCountInSection[i] + 1;
		}
		return IndexPath.newIndexPath(sectionCount - 1 , position - (cursor) + sectionCount - 1);
	}

	@Override
	public int getCount() {
		if(this.dataSource != null){
			sectionCount = this.dataSource.numberOfSectionsInTableView();
			total = 0 ;
			rowCountInSection = new int[sectionCount];
			for(int i = 0 ;i < sectionCount ;i++){
				rowCountInSection[i] = this.dataSource.numberOfRowsInSection(i); 
				total += rowCountInSection[i]; 
			}
			total += sectionCount;
			return total;
			
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(isHeaderPosition(position)){
			View headerView = dataSource.viewForHeaderInSection(convertView,indexPathForPosition(position).section);
			
			if(headerView == null){
				CharSequence title = dataSource.titleForHeaderInSection(indexPathForPosition(position).section);
				if(title!=null){
					HeaderCell headerCell;
					if(convertView!=null && HeaderCell.class.isAssignableFrom(convertView.getClass())){
						headerCell = (HeaderCell) convertView;
					}else{
						headerCell = new HeaderCell(context);
					}
					headerCell.setText(title);
					return headerCell;
				}else{
					View view = new View(context);
                    view.setLayoutParams(new AbsListView.LayoutParams(0,0));
                    return view;
                }
			}
			return headerView;
		}else{
			return dataSource.cellForRowAtIndexPath(convertView, indexPathForPosition(position));
		}
	}

	public boolean isEnabled(int position) {
        return true;//!isHeaderPosition(position);
    }
	
	public int getItemViewType(int position) {
		if(isHeaderPosition(position)){
			return this.dataSource.viewTypeForHeaderInSection(indexPathForPosition(position).section);
		}else{
			return this.dataSource.cellTypeRowAtIndexPath(indexPathForPosition(position));
		}
    }

    public int getViewTypeCount() {
        return 128;
//    	List<Integer> typeList = new ArrayList<Integer>();
//    	int typeCount = 0;
//    	if(this.dataSource != null){
//			sectionCount = this.dataSource.numberOfSectionsInTableView();
//			rowCountInSection = new int[sectionCount];
//			for(int i = 0 ;i < sectionCount ;i++){
//				Integer type = this.dataSource.viewTypeForHeaderInSection(i);
//
//				if(!typeList.contains(type)){
//					typeCount++;
//					typeList.add(type);
//				}
//				int cellCount = this.dataSource.numberOfRowsInSection(i);
//
//				for(int j = 0 ; j < cellCount ;j ++){
//					type = this.dataSource.cellTypeRowAtIndexPath(IndexPath.newIndexPath(i, j));
//
//					if(!typeList.contains(type)){
//						typeCount++;
//						typeList.add(type);
//					}
//
//				}
//
//			}
//			if (typeCount == 0){
//                return 1;
//            }
//			return typeCount;
//
//		}else{
//			return 1;
//		}
    }
	
	public TableViewDataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(TableViewDataSource dataSource) {
		this.dataSource = dataSource;
	}
//	public TableViewDelegate getDelegate() {
//		return delegate;
//	}
//	public void setDelegate(TableViewDelegate delegate) {
//		this.delegate = delegate;
//	}
}
