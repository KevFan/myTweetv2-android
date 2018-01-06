package kevin.mytweet.listeners;

import android.widget.CompoundButton;

import java.util.Iterator;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.User;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Custom listener for switch used in the listing of users in the follow and user adapter to
 * minimize code duplication
 * Used to make calls to follow or unfollow a user on switch state change
 * Created by kevin on 29/12/2017.
 */

public class FollowUnfollowListener implements CompoundButton.OnCheckedChangeListener {
  private MyTweetApp app = MyTweetApp.getApp();
  private User user;

  /**
   * Constructor for listener
   *
   * @param user User to follow or unfollow
   */
  public FollowUnfollowListener(User user) {
    this.user = user;
  }

  /**
   * On state change of switch
   *
   * @param buttonView Switch
   * @param isChecked  Boolean of whether switch is checked or not
   */
  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    // If is checked - make call to follow user
    if (isChecked) {
      followUser();
    } else {
      // Else make call to unfollow user
      unFollowUser();
    }
  }

  /**
   * Private helper to construct form data and make call to follow user - on response the user is
   * added to app following list
   */
  private void followUser() {
    RequestBody following =
        RequestBody.create(
            MediaType.parse("multipart/form-data"), user._id);
    Call<Follow> call = (Call<Follow>) app.tweetService.follow(following);
    call.enqueue(new Callback<Follow>() {
      @Override
      public void onResponse(Call<Follow> call, Response<Follow> response) {
        app.followings.add(response.body());
        info("Following " + user.firstName);
      }

      @Override
      public void onFailure(Call<Follow> call, Throwable t) {
        info(t.toString());
        info("failed to follow");
      }
    });
  }

  /**
   * Private helper to make call to unfollow user - on response the following is removed from
   * app following list
   */
  private void unFollowUser() {
    Call<Follow> call = (Call<Follow>) app.tweetService.unfollow(user._id);
    call.enqueue(new Callback<Follow>() {
      @Override
      public void onResponse(Call<Follow> call, Response<Follow> response) {
        Iterator<Follow> iterator = app.followings.iterator();
        while (iterator.hasNext()) {
          Follow currentFollow = iterator.next();
          if (currentFollow.following._id.equals(user._id)) {
            iterator.remove();
          }
        }
        info("unfollowed " + user.firstName);
      }

      @Override
      public void onFailure(Call<Follow> call, Throwable t) {
        info(t.toString());
        info("failed to unfollow " + user.firstName);
      }
    });
  }
}
