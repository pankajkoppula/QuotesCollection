/**
 * 
 */
package org.jaaps.quotes;

import java.util.List;

import org.jaaps.quotes.data.ContentInfo;
import org.jaaps.quotes.utils.NetworkUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * @author Pankaj
 *
 */
@SuppressLint("NewApi")
public class AuthorListActivity extends AppCompatActivity implements android.view.View.OnClickListener{
	private Toolbar toolbar;
	private Context context;
	private static final int REQUEST_CODE = 1;
//	public static Properties prop = new Properties();
//	public static AssetsPropertyReader assetsPropertyReader;
	int position;
	private ContentInfo contentInfo = new ContentInfo();
	ContentInfo authorInfo;
	private static String leftQuote;
	private static String rightQuote;
	private static String quoteAuthor;
	private static final String quoteAuthorSeparator = "--";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author_list);
		
		this.context = this;
		Intent intent = getIntent();
//      position = Integer.valueOf(intent.getExtras().getString("position"));
		position = intent.getExtras().getInt("position");   
		authorInfo = contentInfo.getContentInfoList().get(position);
		
		toolbar = (Toolbar) findViewById(R.id.app_bar); // Attaching the layout to the toolbar object
		toolbar.setTitle(authorInfo.getName());
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        if(NetworkUtil.isInternetAvailable(this.context)){
        	AdView adView = (AdView)this.findViewById(R.id.adView);
        	AdRequest adRequest = new AdRequest.Builder().build();
        	adView.loadAd(adRequest);
        }else{
        	LinearLayout ll = (LinearLayout) findViewById(R.id.ad_layout);
        	ll.setVisibility(View.GONE);
        }
        
            
        ScrollView mainScrollView = (ScrollView) findViewById(R.id.scrollview);
        
        LinearLayout.LayoutParams layoutParams;
        layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout mainlinearLayout = new LinearLayout(this);
        mainlinearLayout.setLayoutParams(layoutParams);
        mainlinearLayout.setOrientation(LinearLayout.VERTICAL);
        mainlinearLayout.setBackgroundColor(getResources().getColor(R.color.authorViewBackground));
        
        /*System.out.println("position : " + position);
        System.out.println("author name : " + authorInfo.getName());
        System.out.println("title : " + authorInfo.getTitle());
        System.out.println("sub title : " + authorInfo.getSubTitle());*/
        List<String> quoteList = authorInfo.getQuoteList(position);
        int size = quoteList.size();
        
        leftQuote = Html.fromHtml(getString(R.string.leftQuote)).toString();
		rightQuote = Html.fromHtml(getString(R.string.rightQuote)).toString();
		
        for(int i = 1; i <= size ; i++){
        	String quote = leftQuote + quoteList.get(i - 1) + rightQuote;
        	mainlinearLayout.addView(createLinearLayout(i, quote + quoteAuthorSeparator + authorInfo.getName()));
        }
        mainScrollView.addView(mainlinearLayout);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		/**
		 * Show the info icon only when internet connection is present.
		 */
		if(NetworkUtil.isInternetAvailable(this.context)){
			// Inflate the menu items for use in the action bar
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.info, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.info:
	        	Intent i = new Intent(getApplicationContext(), AuthorInfoActivity.class);
                // Pass image index
                i.putExtra("name", authorInfo.getName());
		        startActivity(i);
	            return true;
	        case android.R.id.home:
//		        NavUtils.navigateUpFromSameTask(this);
		    	 onBackPressed();
		        return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onClick(View v) {
		clickFunc(v);
	}
	
  	public void clickFunc(View v){
  		int id = ((View)((View) v.getParent()).getParent()).getId();
//  		System.out.println("id : " + id);
  		LinearLayout parentLayout = (LinearLayout)findViewById(id);
//  		System.out.println("linear layout id : " + parentLayout.getId());
//  		System.out.println("child count : " + parentLayout.getChildCount());
  		View view = parentLayout.getChildAt(0);
  		String quote = "";
  		if(view instanceof TextView){
//  			System.out.println("yes, its a text view : ");
  			TextView txtView = (TextView) view;
//  			System.out.println("text : " + txtView.getText());
  			quote = txtView.getText().toString();
  		}
  		switch(v.getId()) {
	        case R.id.fb:
	        	try {
        	        Intent fbIntent = new Intent();
        	        fbIntent.setAction(Intent.ACTION_SEND);
        	        fbIntent.putExtra(Intent.EXTRA_TEXT, quote+quoteAuthor);
        	        fbIntent.setType("text/plain");
        	        fbIntent.setPackage("com.facebook.katana");
        	        fbIntent.putExtra("return-data", true);
        	        fbIntent.putExtra("exit_on_sent", true);
        	        startActivityForResult(fbIntent, REQUEST_CODE);
        	    } catch (Exception e) {
        	        // If we failed (not native FB app installed), try share through SEND
        	        Intent intent = new Intent(Intent.ACTION_SEND);
        	        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + "";
        	        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        	        startActivityForResult(intent, REQUEST_CODE);
        	    }
	          break;
	        case R.id.whatsapp:
	        	Intent whatsAppIntent = new Intent();
	        	whatsAppIntent.setAction(Intent.ACTION_SEND);
	        	whatsAppIntent.putExtra(Intent.EXTRA_TEXT, quote+quoteAuthor);
	        	whatsAppIntent.setType("text/plain");
	        	whatsAppIntent.setPackage("com.whatsapp");
	        	whatsAppIntent.putExtra("return-data", true);
	        	whatsAppIntent.putExtra("exit_on_sent", true);
	        	startActivityForResult(whatsAppIntent, REQUEST_CODE);
	        	/*Toast.makeText(getApplicationContext(), "Clicked whatsapp Image",
						Toast.LENGTH_SHORT).show();*/
	          break;
	        case R.id.googleplus:
	        	Intent gPlusIntent = new Intent();
	        	gPlusIntent.setAction(Intent.ACTION_SEND);
	        	gPlusIntent.putExtra(Intent.EXTRA_TEXT, quote+quoteAuthor);
	        	gPlusIntent.setType("text/plain");
	        	gPlusIntent.setPackage("com.google.android.apps.plus");
	        	gPlusIntent.putExtra("return-data", true);
	        	gPlusIntent.putExtra("exit_on_sent", true);
	        	startActivityForResult(gPlusIntent, REQUEST_CODE);
	          break;
	        case R.id.sms:
				try {
					Intent smsIntent = new Intent();
		        	smsIntent.setAction(Intent.ACTION_SEND);
		        	smsIntent.putExtra(Intent.EXTRA_TEXT, quote+quoteAuthor);
		        	smsIntent.setType("text/plain");
		        	smsIntent.setPackage("com.google.android.talk");
		        	smsIntent.putExtra("return-data", true);
		        	smsIntent.putExtra("exit_on_sent", true);
		        	startActivityForResult(smsIntent, REQUEST_CODE);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), "Clicked sms Image",
					Toast.LENGTH_SHORT).show();
				}
	          break;
	      }
  	}
	
	private LinearLayout createLinearLayout(int id, String quote){
		LinearLayout.LayoutParams layoutParams ;
        layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout ll = new LinearLayout(this);
        ll.setId(id);
        layoutParams.setMargins(20, 20, 20, 20);
        ll.setLayoutParams(layoutParams);
        ll.setElevation(5.0f);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.border_shadow);
        ll.addView(createTextView(id, quote));
        ll.addView(createDivider(id));
        ll.addView(createImageLinearLayoutStatic(ll));
        return ll;
	}
	
	private TextView createTextView(int id, String quote){
		LinearLayout.LayoutParams layoutParams ;
        layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 15, 30, 15);
		TextView textView = new TextView(this);
		textView.setId(id);
		quoteAuthor = quote.substring(quote.indexOf(quoteAuthorSeparator));
		quote = quote.substring(0, quote.indexOf(quoteAuthorSeparator));
		textView.setText(quote);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(15, 15, 15, 15);
        textView.setMinLines(7);
        textView.setTextColor(getResources().getColor(R.color.textColor));
        /*Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");
        textView.setTypeface(font, Typeface.BOLD);*/
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setShadowLayer(1.5f, -1, 1, getResources().getColor(R.color.text_shadow));
        textView.setTranslationZ(5.0f);
        textView.setElevation(5.0f);
//        textView.setBackground(getResources().getDrawable(R.drawable.txt_view_gradient_bg, null));
        return textView;
	}
	
	@SuppressLint("InlinedApi")
	private ImageView createDivider(int id){
		ImageView divider = new ImageView(this);
		divider.setId(id);
		LinearLayout.LayoutParams lp = 
		    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
		lp.setMargins(3, 3, 3, 3);
		divider.setAdjustViewBounds(true);
		lp.setMarginStart(100);
		lp.setMarginEnd(100);
		divider.setLayoutParams(lp);
//		divider.setBackgroundColor(Color.LTGRAY);
		divider.setBackgroundColor(getResources().getColor(R.color.dividerColor));
		return divider;
	}

	private LinearLayout createImageLinearLayoutStatic(LinearLayout parent){
		LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.share_layout, parent, false);
		return ll; 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
		/*System.out.println("req-"+requestCode+"res-"+resultCode);
		System.out.println("Request code : " + requestCode + ", Result code : "
				+ resultCode );*/
		
		if(requestCode == REQUEST_CODE){
			System.out.println("req code : " + requestCode);
			if(resultCode == Activity.RESULT_OK) {
				System.out.println("activity result returned after calling.");
			}
			
			if(resultCode == Activity.RESULT_CANCELED) {
				System.out.println("result cancelled returned after calling.");
			}
		}
	}
}
