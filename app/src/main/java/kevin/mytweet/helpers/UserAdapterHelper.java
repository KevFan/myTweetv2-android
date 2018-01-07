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
 * User adapter helper class - used to contain common code that is used in the follow adapter and user
 * adapter
 * Created by kevin on 29/12/2017.
 */

public class UserAdapterHelper {
  /**
   * Set the details of the list user row view with the user details passed in
   * @param context Context
   * @param convertView Row view from adapter
   * @param user User to set view with
   */
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
    // Set listener for switch
    followSwitch.setOnCheckedChangeListener(new FollowUnfollowListener(user));
    // TODO: Causes the switch to stay invisible for filter results ending in same position :/
//    if (user._id.equals(MyTweetApp.getApp().currentUser._id)) {
//      followSwitch.setVisibility(View.INVISIBLE);
//    }
  }
}
