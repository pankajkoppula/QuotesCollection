package org.jaaps.quotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.jaaps.quotes.R;
import org.jaaps.quotes.adapter.CustomListAdapter;
import org.jaaps.quotes.data.ContentInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * 
 * @author Pankaj
 *
 */
@SuppressLint("NewApi") 
public class AuthorActivity extends AppCompatActivity{

	ListView list;
	static String shareInfo;
	private Toolbar toolbar;
	
	protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.author_view);
		Intent i = getIntent();
		int position = i.getExtras().getInt("id");
		
		toolbar = (Toolbar) findViewById(R.id.app_bar); // Attaching the layout to the toolbar object
        
		// Get intent data
        toolbar.setTitle(new ContentInfo().getContentInfoList().get(position).getTitle());
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        
        CustomListAdapter imageAdapter = new CustomListAdapter(this);
        ImageView imageView = (ImageView) findViewById(R.id.AuthorView);
        imageView.setImageResource(imageAdapter.imgid[position]);
        Log.d("customListView", ""+position);
//        TextView textView = (TextView) findViewById(R.id.textView1);
//        textView.setText(new ContentInfo().getContentInfoList().get(position).getContent());
        
   	 	/*Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_grow_fade_in_from_bottom);
   	 	imageView.startAnimation(animation1);*/
     
        shareInfo = Html.fromHtml(new StringBuilder().append("<b>Health Tips :</b>")
        		.append("<br><a>http://www.tutorialhub.in/</a>")
        		.append("<br><small>Hi Rahul, this is from your health tips app</small>")
        		.toString()).toString();
        
//        Transition t = new Explode();
        
        if(MainActivity.dialog != null){
        	if(MainActivity.dialog.isShowing()){
        		MainActivity.dialog.dismiss();
        	}
        }
        
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
        super.onStart();
        MainActivity.transitionToast.cancel();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
        
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    int id = item.getItemId();
        if (id == R.id.menu_item_share) {
        	String[] neededPackages = new String[]{"com.google.android.apps.plus", "com.google.android.talk"
        			, "com.google.android.gm", "com.whatsapp", "com.facebook.katana"};
        	 Intent sendIntent = new Intent();
             sendIntent.setAction(Intent.ACTION_SEND);
             sendIntent.putExtra(Intent.EXTRA_TEXT, shareInfo);
             sendIntent.setType("text/plain");
//             startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.action_share)));
             startActivity(generateCustomChooserIntent(sendIntent, neededPackages));
             overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            return true;
        }
        return super.onOptionsItemSelected(item);
	}
	
	 
	// Method:
	private Intent generateCustomChooserIntent(Intent prototype, String[] neededPackages) {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
		Intent chooserIntent;
	 
		Intent dummy = new Intent(prototype.getAction());
		dummy.setType(prototype.getType());
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(dummy, 0);
	 
		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				if (resolveInfo.activityInfo != null && Arrays.asList(neededPackages).contains(resolveInfo.activityInfo.packageName)){
					HashMap<String, String> info = new HashMap<String, String>();
					/*System.out.println("package name : " + resolveInfo.activityInfo.packageName);
					System.out.println("class name : " + resolveInfo.activityInfo.name);
					System.out.println("simple  name : " + String.valueOf(resolveInfo.activityInfo.loadLabel(getPackageManager())));*/
					info.put("packageName", resolveInfo.activityInfo.packageName);
					info.put("className", resolveInfo.activityInfo.name);
					info.put("simpleName", String.valueOf(resolveInfo.activityInfo.loadLabel(getPackageManager())));
					intentMetaInfo.add(info);
					Intent targetedShareIntent = (Intent) prototype.clone();
					targetedShareIntent.setPackage(info.get("packageName"));
					targetedShareIntent.setClassName(info.get("packageName"), info.get("className"));
					targetedShareIntents.add(targetedShareIntent);
				}else {
					continue;
				}
			}
	 
			if (!intentMetaInfo.isEmpty()) {
				// sorting for nice readability
				/*Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
					@Override
					public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
						return map.get("simpleName").compareTo(map2.get("simpleName"));
					}
				});*/
				
				Collections.sort(targetedShareIntents, new Comparator<Intent>() {
					@Override
					public int compare(Intent lhs, Intent rhs) {
						return lhs.getPackage().compareTo(rhs.getPackage());
					}
				});
	 
				chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1), getResources().getText(R.string.action_share));
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
				return chooserIntent;
			}
		}
	 
		return Intent.createChooser(prototype, getResources().getText(R.string.action_share));
	}
	
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    @Override
    protected void onPostResume() {
    	super.onPostResume();
    }
}
