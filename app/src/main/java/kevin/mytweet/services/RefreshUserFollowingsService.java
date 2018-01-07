package kevin.mytweet.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.List;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.FollowFragment;
import kevin.mytweet.models.Follow;
import retrofit2.Call;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Service to refresh user following list in app
 */
public class RefreshUserFollowingsService extends IntentService {
  MyTweetApp app;

  /**
   * Constructor for RefreshUserFollowingsService
   */
  public RefreshUserFollowingsService() {
    super("RefreshUserFollowingsService");
    app = MyTweetApp.getApp();
  }

  /**
   * On intent - make the call to get the list of user followings and update the user following list
   * in app with response and send broadcast of update
   *
   * @param intent Intent to refresh user following list
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    Intent localIntent = new Intent(FollowFragment.BROADCAST_ACTION);
    Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowings(app.currentUser._id);
    try {
      Response<List<Follow>> response = call.execute();
      info("Follow " + String.valueOf(response.body().size()));
      app.followings = response.body();
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
    info("RefreshUserFollowingsService instance destroyed");
  }
}
