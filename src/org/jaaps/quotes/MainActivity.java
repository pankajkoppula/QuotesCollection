package org.jaaps.quotes;

import org.jaaps.quotes.R;
import org.jaaps.quotes.adapter.CustomListAdapter;
import org.jaaps.quotes.data.ContentInfo;
import org.jaaps.quotes.utils.NetworkUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * This class used to display Custom List View with Images
 * source : http://www.androidinterview.com/android-custom-listview-with-image-and-text-using-arrayadapter/
 * @author Pankaj
 *
 */
public class MainActivity extends AppCompatActivity {
	ListView list;
	ContentInfo contentInfo;;
	ShareActionProvider mShareActionProvider;
	Intent mShareIntent;
	static ProgressDialog dialog;
	public static Toast transitionToast;
	private Toolbar toolbar;
//	private AdView adView;
//	private final String unitidBanner = "ca-app-pub-3940256099942544/6300978111"; //for banner
//	private final String unitid = "ca-app-pub-3940256099942544/1033173712"; //interstitial
	private static InterstitialAd interstitial;
	static AdRequest adRequest = new AdRequest.Builder().build();
	
	public MainActivity() {	}
	
	@SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentInfo = new ContentInfo(getApplicationContext());
        
        toolbar = (Toolbar) findViewById(R.id.app_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        
        if(NetworkUtil.isInternetAvailable(getApplicationContext())){
        	AdView adView = (AdView)this.findViewById(R.id.adView);
        	AdRequest adRequest = new AdRequest.Builder().build();
        	adView.loadAd(adRequest);
        	
        	/*View adDivider = (View) this.findViewById(R.id.adDivider);
        adDivider.setVisibility(View.VISIBLE);*/
        	
        	AdRequest adFullScreenRequest = new AdRequest.Builder().build();
        	interstitial = new InterstitialAd(this);
        	interstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        	interstitial.loadAd(adFullScreenRequest);
        	interstitial.setAdListener(new AdListener() {
        		@Override
        		public void onAdClosed() {
        			super.onAdClosed();
        			finish();
        			Log.d("addClosed", "addClosed");
        		}
        		
        		@Override
        		public void onAdFailedToLoad(int errorCode) {
        			super.onAdFailedToLoad(errorCode);
        			finish();
        			Log.d("addFailedToLoad", "addFailedToLoad");
        		}
        	});
        }
        
        list = (ListView)findViewById(R.id.listV);
        list.setAdapter(new CustomListAdapter(this));
        
        list.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	 public void onItemClick(AdapterView<?> parent, View view,
        	 int position, long id) {
        		
        		 // Send intent to Author View
                Intent i = new Intent(getApplicationContext(), AuthorListActivity.class);
                // Pass image index
                i.putExtra("position", position);
                transitionToast = Toast.makeText(getApplicationContext(), R.string.loading, Toast.LENGTH_LONG);
                transitionToast.show();
		        startActivity(i);
		        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        	 }
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

	    // Return true so Android will know we want to display the menu
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_rate_me) {
//        	Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        	Uri uri = Uri.parse("http://play.google.com/store/apps");
        	Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        	try {
        	  startActivity(goToMarket);
        	} catch (ActivityNotFoundException e) {
//        	  	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps")));
        	}
            return true;
        }
        
        if (id == R.id.menu_other_apps) {
//        	Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        	Uri uri = Uri.parse("http://play.google.com/store/apps");
        	Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        	try {
        	  startActivity(goToMarket);
        	} catch (ActivityNotFoundException e) {
//        	  	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps")));
        	}
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @SuppressLint("NewApi") @Override
    public void onBackPressed() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
    	builder.setTitle(R.string.app_name);
    	builder.setMessage(R.string.exit_msg);
    	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	displayInterstitial();
//            	android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    	builder.setIcon(R.drawable.ic_exit_to_app_white_24dp);
    	builder.setNegativeButton("No", null);
    	builder.show();
    }
    
    /*@Override
    public void onBackPressed() {
    	displayInterstitial();
    	super.onBackPressed();
    }*/
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if(dialog != null){
    		dialog.dismiss();
    	}
    	
    	/*if(list !=  null){
    		Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
    		list.startAnimation(animation1);
    	}*/
    }
    
    @Override
    protected void onPostResume() {
    	// TODO Auto-generated method stub
    	super.onPostResume();
    }
    
    /**
     * Invoke displayInterstitial() when you are ready to display an interstitial.
     */
    public void displayInterstitial() {
        if (interstitial!= null && interstitial.isLoaded()) {
            interstitial.show();
        } else {
        	Log.d("adNotLoaded", "Ad is not loaded.");
            finish();
        }
    }
}
