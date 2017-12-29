package kevin.mytweet.fragments.timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Created by kevin on 24/11/2017.
 */

public class GlobalTimeLineFragment extends BaseTimeLineFragment {
  private String userId;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("GlobalTweetLineFragement created");
    super.onCreate(savedInstanceState);
    userId = (String) getArguments().getSerializable("userid");
  }

  /**
   * Whenever the fragment is paused, check should the no tweets message be visible or not
   */
  @Override
  public void onResume() {
    super.onResume();
    if (!userId.equals(MyTweetApp.getApp().currentUser._id)) {
      newTweet.setVisibility(View.INVISIBLE);
    }
    setNoTweetMessage();
    updateTimeLine();
  }

  @Override
  public void updateTimeLine() {
    if (userId.equals(app.currentUser._id)) {
      Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllTweets();
      call.enqueue(new GetAllUserTweets());
    } else {
      Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserTweets(userId);
      call.enqueue(new GetAllUserTweets());
    }
  }
}
