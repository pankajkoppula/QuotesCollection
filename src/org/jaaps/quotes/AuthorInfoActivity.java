/**
 * 
 */
package org.jaaps.quotes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Pankaj
 *
 */
public class AuthorInfoActivity extends AppCompatActivity{
	private Toolbar toolbar;
	private WebView webView;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author_info);
		
		Intent intent = getIntent();
		String name = intent.getExtras().getString("name");  
		toolbar = (Toolbar) findViewById(R.id.app_bar); // Attaching the layout to the toolbar object
		toolbar.setTitle(name);
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        webView = (WebView) findViewById(R.id.webView);
		startWebView("https://en.wikipedia.org/wiki/A._P._J._Abdul_Kalam");
	}
	
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void startWebView(String url) {
         
        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {      
            ProgressDialog progressDialog;
          
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                return true;
            }
        
            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(AuthorInfoActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }else{
                	progressDialog.dismiss();
                }
            }
            
            public void onPageFinished(WebView view, String url) {
                try{
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode,
            		String description, String failingUrl) {
            	// TODO Auto-generated method stub
//            	super.onReceivedError(view, errorCode, description, failingUrl);
            	webView.loadUrl("file:///android_asset/errorPage.html");
            }
             
        }); 
        WebSettings webSettings = webView.getSettings();
        
         // Javascript inabled on webview  
        webSettings.setJavaScriptEnabled(true); 
         
        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);*/
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        
         
        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null); 
         */
         
        //Load url in webview
        webView.loadUrl(url);
          
          
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
//	        NavUtils.navigateUpFromSameTask(this);
	    	 onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
