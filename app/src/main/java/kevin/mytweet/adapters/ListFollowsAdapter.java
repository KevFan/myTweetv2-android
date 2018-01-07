package kevin.mytweet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.models.Follow;

import static kevin.mytweet.helpers.UserAdapterHelper.setDetails;

/**
 * Custom adaptor for the follow fragment to list follow details
 */
public class ListFollowsAdapter extends ArrayAdapter<Follow> {
  private Context context;
  public List<Follow> follows;
  private String followOrFollowing;

  /**
   * ListFollowAdaptor constructor
   *
   * @param context Context of where the adapter is constructed
   * @param follows ArrayList of users
   */
  public ListFollowsAdapter(Context context, List<Follow> follows, String followOrFollowing) {
    super(context, 0, follows);
    this.followOrFollowing = followOrFollowing;
    this.context = context;
    this.follows = follows;
  }

  /**
   * Call list_item_user for each follow in ArrayList to display user data at specific position
   * depending on whether to list follower or following details
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

    // Calls helper method to set user details based of whether list is a follower or following list
    if (followOrFollowing.equals("follower")) {
      setDetails(context, convertView, follow.follower);
    } else {
      setDetails(context, convertView, follow.following);
    }

    return convertView;
  }

  /**
   * Return the size of the follow list
   *
   * @return Size of the follow list
   */
  @Override
  public int getCount() {
    return follows.size();
  }
}
