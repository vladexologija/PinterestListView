package com.vladimir.pinterestlistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.vladimir.pinterestlistview.adapters.ItemsAdapter;

public class ItemsActivity extends Activity {
	
	private ListView listViewLeft;
	private ListView listViewRight;
	private ItemsAdapter leftAdapter;
	private ItemsAdapter rightAdapter;

	int[] leftViewsHeights;
	int[] rightViewsHeights;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items_list);	
		
		listViewLeft = (ListView) findViewById(R.id.list_view_left);
		listViewRight = (ListView) findViewById(R.id.list_view_right);
		
		loadItems();
		
		listViewLeft.setOnTouchListener(touchListener);
		listViewRight.setOnTouchListener(touchListener);		
		listViewLeft.setOnScrollListener(scrollListener);
		listViewRight.setOnScrollListener(scrollListener);
	}
	
	// Passing the touch event to the opposite list
	OnTouchListener touchListener = new OnTouchListener() {					
		boolean dispatched = false;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(listViewLeft) && !dispatched) {
				dispatched = true;
				listViewRight.dispatchTouchEvent(event);
			} else if (v.equals(listViewRight) && !dispatched) {
				dispatched = true;
				listViewLeft.dispatchTouchEvent(event);
			}
			
			dispatched = false;
			return false;
		}
	};
	
	/**
	 * Synchronizing scrolling 
	 * Distance from the top of the first visible element opposite list:
	 * sum_heights(opposite invisible screens) - sum_heights(invisible screens) + distance from top of the first visible child
	 */
	OnScrollListener scrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView v, int scrollState) {	
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
			if (view.getChildAt(0) != null) {
				if (view.equals(listViewLeft) ){
					leftViewsHeights[view.getFirstVisiblePosition()] = view.getChildAt(0).getHeight();
					
					int h = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						h += rightViewsHeights[i];
					}
					
					int hi = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						hi += leftViewsHeights[i];
					}
					
					int top = h - hi + view.getChildAt(0).getTop();
					listViewRight.setSelectionFromTop(listViewRight.getFirstVisiblePosition(), top);
				} else if (view.equals(listViewRight)) {
					rightViewsHeights[view.getFirstVisiblePosition()] = view.getChildAt(0).getHeight();
					
					int h = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						h += leftViewsHeights[i];
					}
					
					int hi = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						hi += rightViewsHeights[i];
					}
					
					int top = h - hi + view.getChildAt(0).getTop();
					listViewLeft.setSelectionFromTop(listViewLeft.getFirstVisiblePosition(), top);
				}
				
			}
			
		}
	};
	
	private void loadItems(){
		Integer[] leftItems = new Integer[]{R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4, R.drawable.ic_5};
		Integer[] rightItems = new Integer[]{R.drawable.ic_6, R.drawable.ic_7, R.drawable.ic_8, R.drawable.ic_9, R.drawable.ic_10};
		
		leftAdapter = new ItemsAdapter(this, R.layout.item, leftItems);
		rightAdapter = new ItemsAdapter(this, R.layout.item, rightItems);
		listViewLeft.setAdapter(leftAdapter);
		listViewRight.setAdapter(rightAdapter);
		
		leftViewsHeights = new int[leftItems.length];
		rightViewsHeights = new int[rightItems.length];	
	}


}