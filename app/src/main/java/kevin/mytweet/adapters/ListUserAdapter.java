package kevin.mytweet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.User;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Custom adaptor for the timeline fragment to list tweets
 */
public class ListUserAdapter extends ArrayAdapter<User> {
  private Context context;
  public List<User> users;
  public MyTweetApp app = MyTweetApp.getApp();

  /**
   * TimeLineAdapter constructor
   *
   * @param context   Context of where the adapter is constructed
   * @param users ArrayList of users
   */
  public ListUserAdapter(Context context, List<User> users) {
    super(context, 0, users);
    this.users = users;
    this.context = context;
  }

  /**
   * Call list_item_tweet for each tweet in ArrayList to display tweet data at specific position
   *
   * @param position    position of tweet item
   * @param convertView View to reuse
   * @param parent      View parent of where convert view will be attached
   * @return View with tweet data at specific position
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.list_item_user, null);
    }

    final User user = users.get(position);
    final Context context = getContext();
    Switch followSwitch = (Switch) convertView.findViewById(R.id.list_user_followSwitch);
    // Set switch state by comparing to following list
    for (Follow follow : MyTweetApp.getApp().followings) {
      if (follow.following._id.equals(user._id)) {
        followSwitch.setChecked(true);
        break;
      }
    }
    followSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
              info("Following " + user.firstName);
            }

            @Override
            public void onFailure(Call<Follow> call, Throwable t) {
              info(t.toString());
              info("failed to follow");
            }
          });
        } else {
          info("unfollowed");
        }
      }
    });
    TextView userName = (TextView) convertView.findViewById(R.id.list_user_UserName);
    ImageView userImage = (ImageView) convertView.findViewById(R.id.list_user_userImage);
    userName.setText(user.firstName + " " + user.lastName);
    if (!user.image.equals("")) {
      Picasso.with(context).load(user.image).into(userImage);
    } else {
      userImage.setImageResource(R.mipmap.ic_launcher_round);
    }

    return convertView;
  }

  @Override
  public int getCount() {
    return users.size();
  }
}
