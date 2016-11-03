package com.example.example;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private WebView mWebView;    // 웹뷰 선언
	   @Override 

	    public void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.activity_main);

	        

	        mWebView = (WebView) findViewById(R.id.WebView1);

	        mWebView.getSettings().setJavaScriptEnabled(true);      // 웹뷰에서 자바 스크립트 사용

	        mWebView.loadUrl( "192.168.0.1" );            // 웹뷰에서 불러올 URL 입력

	        mWebView.setWebViewClient(new WishWebViewClient());

	    }
	   @Override

	    public boolean onKeyDown(int keyCode, KeyEvent event) {

	    	if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){

	    		mWebView.goBack();

	    		return true;    		

	    	}

	        return super.onKeyDown(keyCode, event);

	    }



	    private class WishWebViewClient extends WebViewClient{

	    	@Override

	    	public boolean shouldOverrideUrlLoading(WebView view, String url){

	    		view.loadUrl(url);

	    		return true;

	    	}

	    }


}
