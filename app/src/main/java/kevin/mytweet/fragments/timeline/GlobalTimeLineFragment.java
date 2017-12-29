package kevin.mytweet.fragments.timeline;

import java.util.List;

import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Created by kevin on 24/11/2017.
 */

public class GlobalTimeLineFragment extends BaseTimeLineFragment {
  /**
   * Whenever the fragment is paused, check should the no tweets message be visible or not
   */
  @Override
  public void onResume() {
    super.onResume();
    setNoTweetMessage();
    updateTimeLine();
  }

  @Override
  public void updateTimeLine() {
    String userId = (String) getArguments().getSerializable("userid");
    if (userId.equals(app.currentUser._id)) {
      Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllTweets();
      call.enqueue(new GetAllUserTweets());
    } else {
      Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserTweets(userId);
      call.enqueue(new GetAllUserTweets());
    }
  }
}
