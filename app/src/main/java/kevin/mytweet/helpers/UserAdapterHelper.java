package kevin.mytweet.helpers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.listeners.FollowUnfollowListener;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.User;

/**
 * Created by kevin on 29/12/2017.
 */

public class UserAdapterHelper {
  public static void setDetails(Context context, View convertView, User user) {
    TextView userName = (TextView) convertView.findViewById(R.id.list_user_UserName);
    ImageView userImage = (ImageView) convertView.findViewById(R.id.list_user_userImage);
    userName.setText(user.firstName + " " + user.lastName);
    if (!user.image.equals("")) {
      Picasso.with(context).load(user.image).into(userImage);
    } else {
      userImage.setImageResource(R.mipmap.ic_launcher_round);
    }

    Switch followSwitch = (Switch) convertView.findViewById(R.id.list_user_followSwitch);
    // Set switch state by comparing to following list
    for (Follow follow : MyTweetApp.getApp().followings) {
      if (follow.following._id.equals(user._id)) {
        followSwitch.setChecked(true);
        break;
      }
    }
    followSwitch.setOnCheckedChangeListener(new FollowUnfollowListener(user));
    if (user._id.equals(MyTweetApp.getApp().currentUser._id)) {
      followSwitch.setVisibility(View.INVISIBLE);
    }
  }
}
