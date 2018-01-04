package kevin.mytweet.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.List;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.timeline.TimeLineFragment;
import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;


public class RefreshUserTweetsService extends IntentService {
  MyTweetApp app;

  public RefreshUserTweetsService() {
    super("RefreshUserTweetsService");
    app = MyTweetApp.getApp();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Intent localIntent = new Intent(TimeLineFragment.BROADCAST_ACTION);
    Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserTweets(app.currentUser._id);
    try {
      Response<List<Tweet>> response = call.execute();
      info(String.valueOf(response.body().size()));
      app.timeLine = response.body();
      LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    } catch (IOException e) {

    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    info("RefreshUserTweetsService instance destroyed");
  }
}
