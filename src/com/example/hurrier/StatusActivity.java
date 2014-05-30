package com.example.hurrier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v4.app.Fragment;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class StatusActivity extends ListActivity{
	ArrayList<HashMap<String, String>> tweetText = new ArrayList<HashMap<String, String>>();
	private static SharedPreferences mSharedPreferences;
	String search;
	ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_status);
		mSharedPreferences = getApplicationContext().getSharedPreferences(Constant.PREF_FILE, 0);
		listview = (ListView) findViewById(android.R.id.list);
		search = this.getIntent().getStringExtra("search");
		new SearchMentions().execute(search);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_status,
					container, false);
			return rootView;
		}
	}
	
	public class SearchMentions extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... args) {
			 Query query = new Query(args[0]);
			
			try {
	            ConfigurationBuilder cb = new ConfigurationBuilder();
	            
	            cb.setOAuthConsumerKey(Constant.OAUTH_CONSUMER_KEY);
	            cb.setOAuthConsumerSecret(Constant.OAUTH_CONSUMER_SECRET);
	              
	            cb.setOAuthAccessToken(mSharedPreferences.getString(Constant.ACCESS_TOKEN, ""));
	            cb.setOAuthAccessTokenSecret(mSharedPreferences.getString(Constant.ACCESS_TOKEN_SECRET, ""));
	            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
	             
	            QueryResult result;
	            List<twitter4j.Status> tweets;
	            
	            result = twitter.search(query);
	            tweets = result.getTweets();
	            for (twitter4j.Status tweet : tweets) {
	            	HashMap<String, String> map = new HashMap<String, String>();
	            	map.put("name", tweet.getUser().getName());
	            	map.put("status", tweet.getText());
	            	for (MediaEntity mediaentity: tweet.getMediaEntities()){
	            		if (mediaentity.getType().equals("photo")){
	            			map.put("photo", mediaentity.getMediaURL());
	            			break;
	            		}
	            	}
	            	tweetText.add(map);
	            }   
	        } 
			catch (TwitterException e) {
	            Log.e("Twitter Update Error", e.getMessage());
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String tweet){
			super.onPostExecute(tweet);
			SimpleAdapter mSchedule = new SimpleAdapter(StatusActivity.this, tweetText, R.layout.custom_row,
		            new String[] {"name", "status", "photo"}, new int[] {R.id.first, R.id.second, R.id.third});
			listview.setAdapter(mSchedule);
		}
	}
}
