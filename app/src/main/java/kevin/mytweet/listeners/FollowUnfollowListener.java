package kevin.mytweet.listeners;

import android.widget.CompoundButton;

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
 * Created by kevin on 29/12/2017.
 */

public class FollowUnfollowListener implements CompoundButton.OnCheckedChangeListener {
  private MyTweetApp app = MyTweetApp.getApp();
  private User user;

  public FollowUnfollowListener(User user) {
    this.user = user;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
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
    } else {
      Call<Follow> call = (Call<Follow>) app.tweetService.unfollow(user._id);
      call.enqueue(new Callback<Follow>() {
        @Override
        public void onResponse(Call<Follow> call, Response<Follow> response) {
          for (Follow follow : app.followings) {
            if (follow.following._id.equals(user._id)) {
              app.followings.remove(follow);
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
}
