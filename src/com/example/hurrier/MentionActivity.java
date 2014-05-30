package com.example.hurrier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v4.app.Fragment;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MentionActivity extends ListActivity{
	private static SharedPreferences mSharedPreferences;
	ArrayList<HashMap<String, String>> mentionList = new ArrayList<HashMap<String, String>>();
	ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mention);
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", 0);
		listview = (ListView) findViewById(android.R.id.list);
		populateMentions();
	}

	public void populateMentions(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
        
        cb.setOAuthConsumerKey(Constant.OAUTH_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(Constant.OAUTH_CONSUMER_SECRET);
          
        cb.setOAuthAccessToken(mSharedPreferences.getString(Constant.ACCESS_TOKEN, ""));
        cb.setOAuthAccessTokenSecret(mSharedPreferences.getString(Constant.ACCESS_TOKEN_SECRET, ""));
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        
        try {
        	User user = twitter.showUser(mSharedPreferences.getLong("userID", 0L));
            List<Status> statuses = twitter.getMentionsTimeline();
            System.out.println("Showing @" + user.getScreenName() + "'s mentions.");
            for (Status status : statuses) {
            	HashMap<String, String> map = new HashMap<String, String>();
            	map.put("name", status.getUser().getName());
            	map.put("status", status.getText());
            	for (MediaEntity mediaentity: status.getMediaEntities()){
            		if (mediaentity.getType().equals("photo")){
            			map.put("photo", mediaentity.getMediaURL());
            			break;
            		}
            	}
            	mentionList.add(map);
            }
            
            SimpleAdapter mSchedule = new SimpleAdapter(this, mentionList, R.layout.custom_row,
		            new String[] {"name", "status", "photo"}, new int[] {R.id.first, R.id.second, R.id.third});
			listview.setAdapter(mSchedule);
        } 
        catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mention, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_mention,
					container, false);
			return rootView;
		}
	}

}
