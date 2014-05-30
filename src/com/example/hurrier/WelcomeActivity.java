package com.example.hurrier;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends ActionBarActivity {

	private static SharedPreferences mSharedPreferences;
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Toast.makeText(WelcomeActivity.this, "New Mention Added Click Search Your Mentions", Toast.LENGTH_LONG).show();
		}
	};
	
	@Override
	protected void onResume() {
	    super.onResume();
	    registerReceiver(receiver, new IntentFilter(ToastService.NOTIFICATION));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_welcome);
		
		//get the shared preferences file
		mSharedPreferences = getApplicationContext().getSharedPreferences(Constant.PREF_FILE, 0);
		
		String username = mSharedPreferences.getString("user", "User");
		
		TextView text = (TextView) findViewById(R.id.username);
		
		text.setText(username);
		
		final Button logoutButton = (Button) findViewById(R.id.logout);
		final Button tweetButton = (Button) findViewById(R.id.tweet_button);
		final EditText tweet = (EditText) findViewById(R.id.tweet);
		final Button searchButton = (Button) findViewById(R.id.search_activity);
		final Button searchMention = (Button) findViewById(R.id.search_mentions);
		final Button searchUserMention = (Button) findViewById(R.id.search_mention_welcome);
		
		Intent serviceIntent = new Intent(WelcomeActivity.this, ToastService.class);
		startService(serviceIntent);
		
		logoutButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				logout();
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		tweetButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				String update = tweet.getText().toString();
				new UpdateStatus().execute(update);
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(WelcomeActivity.this, SearchActivity.class);
				startActivity(intent);
			}
		});
		
		searchMention.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(WelcomeActivity.this, MentionActivity.class);
				startActivity(intent);
			}
		});
		
		searchUserMention.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(WelcomeActivity.this, UserStatusActivity.class);
				startActivity(intent);
			}
		});
	}

	public void logout(){
		Editor e = mSharedPreferences.edit();
        e.remove("oauth_token");
        e.remove("oauth_token_secret");
        e.remove("isTwitterLogedIn");
        e.remove("user");
        e.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_welcome,
					container, false);
			return rootView;
		}
	}
	
	private class UpdateStatus extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... args) {
			String status = args[0];
			
			try {
	            ConfigurationBuilder cb = new ConfigurationBuilder();
	            
	            cb.setOAuthConsumerKey(Constant.OAUTH_CONSUMER_KEY);
	            cb.setOAuthConsumerSecret(Constant.OAUTH_CONSUMER_SECRET);
	              
	            String token = mSharedPreferences.getString(Constant.ACCESS_TOKEN, "");
	            String token_secret = mSharedPreferences.getString(Constant.ACCESS_TOKEN_SECRET, "");
	            AccessToken accessToken = new AccessToken(token, token_secret);
	            Twitter twitter = new TwitterFactory(cb.build()).getInstance(accessToken);
	            
	            if (status.trim().length() > 0){
	            	twitter.updateStatus(status);
	            }
	        } 
			catch (TwitterException e) {
	            Log.e("Twitter Error", e.getMessage());
	        }
			
	        return null;
		}
		
		@Override
		protected void onPostExecute(String file_url){
			super.onPostExecute(file_url);
			final EditText tweet = (EditText) findViewById(R.id.tweet);
			tweet.setText(null);
		}
	}
}
