package org.jaaps.quotes.adapter;

import org.jaaps.quotes.R;
import org.jaaps.quotes.data.ContentInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter{

	private Activity context;
	private LayoutInflater inflater;
	static ContentInfo contentInfo = new ContentInfo();
	public Integer[] imgid = contentInfo.getImageIds().toArray(new Integer[contentInfo.getImageIds().size()]);
	
	public CustomListAdapter(Activity context) {
		this.context = context;
	}
	
	@SuppressLint({ "InflateParams" }) 
	public View getView(int position,View view,ViewGroup parent) {
		 if (inflater == null){
			 inflater = (LayoutInflater) this.context
					 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 }
		 if (view == null){
			 view = inflater.inflate(R.layout.myview, parent, false);
		 }
		 
		 TextView txtTitle = (TextView) view.findViewById(R.id.item);
		 ImageView imageView = (ImageView) view.findViewById(R.id.icon);
		 TextView txtSubTitle = (TextView) view.findViewById(R.id.textView1);
		 txtTitle.setText(contentInfo.getContentInfoList().get(position).getTitle());
//		 Bitmap icon = BitmapFactory.decodeResource(this.context.getResources(), imgid[position]);
		 Bitmap icon = BitmapFactory.decodeResource(this.context.getResources(), contentInfo.getContentInfoList().get(position).getImageId());
		 imageView.setImageBitmap(icon);
//		 imageView.setImageBitmap(decodeSampledBitmapFromResource(this.context.getResources(), imgid[position], 100, 100));
		 /*try {
			BitmapScaler scaler = new BitmapScaler(this.context.getResources(), contentInfo.getContentInfoList().get(position).getImageId(), 50);
			 imageView.setImageBitmap(scaler.getScaled());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		 
		 txtSubTitle.setText(contentInfo.getContentInfoList().get(position).getSubTitle());
		 return view;
	}
	
	@SuppressWarnings("unused")
	private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}

	
	private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
	@Override
	public int getCount() {
		return contentInfo.getContentInfoList().size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
