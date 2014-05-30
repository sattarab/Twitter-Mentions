package com.example.hurrier;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private static SharedPreferences mSharedPreferences;
    private static Twitter mTwitter;
    private static RequestToken mRequestToken;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		if (android.os.Build.VERSION.SDK_INT > 11) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	    }
		
		final Button loginButton = (Button) findViewById(R.id.login_button);
		mSharedPreferences = getApplicationContext().getSharedPreferences(Constant.PREF_FILE, 0);
		loginButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				logIn();
			}
		});
		
		if (!isLoggedIn()){
			Uri uri = getIntent().getData();
			
			if (uri != null && uri.toString().startsWith("oauth://t4jsample")){				
				try{
					AccessToken accessToken = mTwitter.getOAuthAccessToken(mRequestToken, uri.getQueryParameter("oauth_verifier"));
					long userID = accessToken.getUserId();
                    User user = mTwitter.showUser(userID);
                    
                    Editor e = mSharedPreferences.edit();
                    e.putString(Constant.ACCESS_TOKEN, accessToken.getToken());
                    e.putString(Constant.ACCESS_TOKEN_SECRET, accessToken.getTokenSecret());
                    e.putBoolean("isTwitterLogedIn", true);
                    e.putString("user", user.getName());
                    e.putLong("userID", userID);
                    e.commit();
                    
            		Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            		startActivity(intent);
				}
				catch(Exception e){
					Log.e("Twitter Login Error" + e.getMessage(), null);
				}
			}
		}
		
	}
	
	public void logIn(){
		if (isLoggedIn()){
			return;
		}
		
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setOAuthConsumerKey(Constant.OAUTH_CONSUMER_KEY);
		cb.setOAuthConsumerSecret(Constant.OAUTH_CONSUMER_SECRET);

		Configuration configuration = cb.build();
		TwitterFactory factory = new TwitterFactory(configuration);

		mTwitter = factory.getInstance();
		try {
			mRequestToken = mTwitter.getOAuthRequestToken("oauth://t4jsample");
			System.out.println(mRequestToken.getToken()); 
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mRequestToken.getAuthenticationURL())));
		} 
		catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLoggedIn(){
		return mSharedPreferences.getBoolean("isTwitterLogedIn", false);
	}
	
	@Override
	protected void onStart(){   
	    super.onStart();
	    
	    if (isLoggedIn()){
	    	Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
			startActivity(intent);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
