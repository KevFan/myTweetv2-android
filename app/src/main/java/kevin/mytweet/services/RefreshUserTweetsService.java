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


/**
 * Service to refresh user tweet list in app
 */
public class RefreshUserTweetsService extends IntentService {
  MyTweetApp app;

  /**
   * Constructor for RefreshUserTweetsService
   */
  public RefreshUserTweetsService() {
    super("RefreshUserTweetsService");
    app = MyTweetApp.getApp();
  }

  /**
   * On intent - make the call to get the list of user tweets and update the user tweets list
   * in app with response and send broadcast of update
   *
   * @param intent Intent to refresh user following list
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    Intent localIntent = new Intent(TimeLineFragment.BROADCAST_ACTION);
    Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserFollowingTweets();
    try {
      Response<List<Tweet>> response = call.execute();
      info(String.valueOf(response.body().size()));
      app.timeLine = response.body();
      LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    } catch (IOException e) {

    }
  }

  /**
   * On destroy of service
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    info("RefreshUserTweetsService instance destroyed");
  }
}
