package com.example.hurrier;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.conf.ConfigurationBuilder;

public class ToastService extends Service{

	private TwitterStream mTwitterStream;
	private static Context context;
	public final static String NOTIFICATION = "STATUSCHANGED";
	
	public ToastService(){
		super();  
	}
	
	@Override
	public void onCreate() {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constant.PREF_FILE, 0);
		ConfigurationBuilder cb = new ConfigurationBuilder();
        
        cb.setOAuthConsumerKey(Constant.OAUTH_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(Constant.OAUTH_CONSUMER_SECRET);
          
        cb.setOAuthAccessToken(sharedPreferences.getString(Constant.ACCESS_TOKEN, ""));
        cb.setOAuthAccessTokenSecret(sharedPreferences.getString(Constant.ACCESS_TOKEN_SECRET, ""));
        ToastService.context = getApplicationContext();
        mTwitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        mTwitterStream.addListener(listener);
        mTwitterStream.user();
	}
	
	@Override
    public void onStart(Intent intent, int StartId) {
        Log.i("DEBUG", "Starting twitter service");
	}
		
	private static final UserStreamListener listener = new UserStreamListener(){

		@Override
		public void onDeletionNotice(StatusDeletionNotice arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStallWarning(StallWarning arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatus(Status status) {
			Intent intent = new Intent(NOTIFICATION);
			context.sendBroadcast(intent);
		}

		@Override
		public void onTrackLimitationNotice(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onException(Exception arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBlock(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeletionNotice(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDirectMessage(DirectMessage arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFavorite(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFollow(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFriendList(long[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUnblock(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUnfavorite(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUnfollow(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListCreation(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListDeletion(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserListUpdate(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUserProfileUpdate(User arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
