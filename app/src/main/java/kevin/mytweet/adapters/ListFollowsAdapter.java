package kevin.mytweet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;

/**
 * Custom adaptor for the timeline fragment to list tweets
 */
public class ListFollowsAdapter extends ArrayAdapter<Follow> {
  private Context context;
  public List<Follow> follows;
  private String followOrFollowing;

  /**
   * TimeLineAdapter constructor
   *
   * @param context   Context of where the adapter is constructed
   * @param follows ArrayList of users
   */
  public ListFollowsAdapter(Context context, List<Follow> follows, String followOrFollowing) {
    super(context, 0, follows);
    this.followOrFollowing = followOrFollowing;
    this.context = context;
    this.follows = follows;
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

    Follow follow = follows.get(position);

    // Set details based of whether list is a follower or following list
    if (followOrFollowing.equals("follower")) {
      setDetails(convertView, follow.follower);
    } else {
      setDetails(convertView, follow.following);
    }

    return convertView;
  }

  private void setDetails(View convertView, User user) {
    TextView userName = (TextView) convertView.findViewById(R.id.list_user_UserName);
    ImageView userImage = (ImageView) convertView.findViewById(R.id.list_user_userImage);
    userName.setText(user.firstName + " " + user.lastName);
    if (!user.image.equals("")) {
      Picasso.with(context).load(user.image).into(userImage);
    } else {
      userImage.setImageResource(R.mipmap.ic_launcher_round);
    }
  }

  @Override
  public int getCount() {
    return follows.size();
  }
}
