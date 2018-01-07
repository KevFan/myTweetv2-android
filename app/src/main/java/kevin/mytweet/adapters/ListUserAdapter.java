package kevin.mytweet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.models.User;

import static kevin.mytweet.helpers.UserAdapterHelper.setDetails;

/**
 * Custom adaptor for the search fragment to list users
 */
public class ListUserAdapter extends ArrayAdapter<User> implements Filterable {
  private Context context;
  public List<User> users;

  /**
   * TimeLineAdapter constructor
   *
   * @param context Context of where the adapter is constructed
   * @param users   ArrayList of users
   */
  public ListUserAdapter(Context context, List<User> users) {
    super(context, 0, users);
    this.users = users;
    this.context = context;
  }

  /**
   * Call list_item_user for each user in ArrayList to display user data at specific position
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

    // Get user at position and call helper to set details to view
    User user = users.get(position);
    setDetails(context, convertView, user);

    return convertView;
  }

  /**
   * Return size of users array list
   *
   * @return size of user array list
   */
  @Override
  public int getCount() {
    return users.size();
  }
}
