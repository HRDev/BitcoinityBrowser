package com.mycompany.webview;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity 
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private RadioGroup radioGroupId;
    private RadioButton radioGenderButton;
    private Button button;
    final Activity activity = this; 
	ProgressBar progressBar;
	WebView webview; 
	String url = "https://bitcoinity.org/markets/btce/USD";
	private long lastPressedTime;
	private static final int PERIOD = 2000;
	
	AlertDialog settingDialog;
	Context context = MainActivity.this;
	Editor toEdit;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
     

        //Window and WebView start
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		progressBar();
		webview();
		
		Log.i(TAG, "Layout Start");
	}
    
    
	private class MyWebViewClient extends WebViewClient {  
        @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
               Toast.makeText(getApplicationContext(), getString(R.string.please_wait_loading), Toast.LENGTH_LONG).show();
               return true;
           }

        
        @Override
       public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
               MainActivity.this.progressBar.setProgress(100);
           super.onPageFinished(view, url);
		Toast.makeText(getApplicationContext(), "URL: \n" + url + "\n" + getString(R.string.load), Toast.LENGTH_LONG).show();
		 SharedPreferences sharedPref = context.getSharedPreferences("URL", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPref.edit();
         editor.putString("url", url);
         editor.commit();
       }

        @Override
       public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	progressBar.setVisibility(View.VISIBLE);
           super.onPageStarted(view, url, favicon);
       }
   }
    
    private void socialMenu(String url){
    	SharedPreferences sharedPref;
    	sharedPref = context.getSharedPreferences("URL", Context.MODE_PRIVATE);
    	String storedUrl = sharedPref.getString("url", "");
        //Toast.makeText(getApplicationContext(), "Store Url: " + storedUrl + "\n"+ url + "\n" + StoreUrl, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Store Url: " + storedUrl + "\n"+ url);
		    	Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link_text) + storedUrl);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
	}
 


	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.share_title:
            	socialMenu(url);
            return true;
            
           /* case R.id.setting:            
            	setContentView(R.layout.settings);
            	addButtonListener();
            	return true;
            */	
            case R.id.refresh_title:
            	SharedPreferences sharedPref;
            	sharedPref = context.getSharedPreferences("URL", Context.MODE_PRIVATE);
            	String storedUrl = sharedPref.getString("url", "");
            	webview.loadUrl(storedUrl);
            	return true;
            case R.id.exit_title:
            	finish();     
            	return true;
        }
      return false;
    }


    public void addButtonListener() {
    	 
    	radioGroupId = (RadioGroup) findViewById(R.id.radioGenderGroup);
    	
    	button = (Button) findViewById(R.id.button);
     
    	button.setOnClickListener(new OnClickListener() {
     
    		@Override
    		public void onClick(View v) {
    		    // get the selected radio button from the group
    			int selectedOption = radioGroupId.getCheckedRadioButtonId();
     
    			// find the radiobutton by the previously returned id
    		    radioGenderButton = (RadioButton) findViewById(selectedOption);
    		    
    			Toast.makeText(MainActivity.this, radioGenderButton.getText(), Toast.LENGTH_SHORT).show();
    			finish();
    			startActivity(getIntent());
    		}
     
    	});
    }
private void  webview() {
		webview = (WebView)findViewById(R.id.webview);
        //webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new MyWebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLoadWithOverviewMode(false);
		webview.getSettings().setAppCacheEnabled(true);
		webview.getSettings().setSupportZoom(true);									//Webseiten Zoom aktivieren false=aus true=ein
		webview.getSettings().setBuiltInZoomControls(false);							//Webseiten Zoom Buttons aktivieren false=aus true=ein
		webview.setDrawingCacheEnabled(true);
		webview.loadUrl(url);
}
private void progressBar() {
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);
		progressBar.bringToFront();
}
protected void saveSelectedTypes(Editor prefEditor,
			CharSequence[] settings, boolean[] selectedTypes) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Layout Start");
		progressBar();
        webview();
	}


	public void setValue(int progressBar) {
		MainActivity.this.progressBar.setProgress(progressBar);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        switch (event.getAction()) {
	        case KeyEvent.ACTION_DOWN:
	            if (event.getDownTime() - lastPressedTime < PERIOD) {
	                finish();
	            } else {
	                Toast.makeText(getApplicationContext(), R.string.exit_press_back_twice_message,
	                        Toast.LENGTH_SHORT).show();
	                lastPressedTime = event.getEventTime();
	            }
	            return true;
	        }
	    }
	    return false;
	}
	
}
