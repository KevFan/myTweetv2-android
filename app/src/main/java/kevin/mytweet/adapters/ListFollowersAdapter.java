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
public class ListFollowersAdapter extends ArrayAdapter<Follow> {
  private Context context;
  public List<Follow> followers;

  /**
   * TimeLineAdapter constructor
   *
   * @param context   Context of where the adapter is constructed
   * @param followers ArrayList of users
   */
  public ListFollowersAdapter(Context context, List<Follow> followers) {
    super(context, 0, followers);
    this.context = context;
    this.followers = followers;
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

    Follow follow = followers.get(position);

    TextView userName = (TextView) convertView.findViewById(R.id.list_user_UserName);
    userName.setText(follow.follower.firstName + " " + follow.follower.lastName);

    ImageView userImage = (ImageView) convertView.findViewById(R.id.list_user_userImage);
    if (!follow.follower.image.equals("")) {
      Picasso.with(context).load(follow.follower.image).into(userImage);
    } else {
      userImage.setImageResource(R.mipmap.ic_launcher_round);
    }

    return convertView;
  }

  @Override
  public int getCount() {
    return followers.size();
  }
}
