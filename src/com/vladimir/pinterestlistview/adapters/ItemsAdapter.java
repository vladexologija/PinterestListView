package com.vladimir.pinterestlistview.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ItemsAdapter extends ArrayAdapter<Integer>{

    Context context; 
    LayoutInflater inflater;
    int layoutResourceId;
    int imageWidth;
    
    public ItemsAdapter(Context context, int layoutResourceId, Integer[] items) {
        super(context, layoutResourceId, items);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        
        int width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
		int margin = (int)convertDpToPixel(10f, (Activity)context);
		imageWidth = (width - (3 * margin));
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView row = (ImageView) convertView;
        ItemHolder holder;
        Integer item = getItem(position);
        
		if (row == null) {
			holder = new ItemHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (ImageView) inflater.inflate(layoutResourceId, parent, false);
			holder.itemImage = row;
		} else {
			holder = (ItemHolder) row.getTag();
		}
		
		row.setTag(holder);
		setImageBitmap(item, holder.itemImage);
        return row;
    }
	
	
	private void setImageBitmap(Integer item, ImageView imageView){
		Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), item);
		if (imageWidth != 0){
			float i = ((float)imageWidth)/((float)bitmap.getWidth());
			float imageHeight = i * (bitmap.getHeight());
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
			params.height = (int) imageHeight;
			params.width = imageWidth;
			imageView.setLayoutParams(params);
		}
		imageView.setImageBitmap(bitmap);
	}
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}

    public static class ItemHolder
    {
        ImageView itemImage;
    }
}