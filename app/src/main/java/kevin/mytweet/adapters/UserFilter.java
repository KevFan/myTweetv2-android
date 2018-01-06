package kevin.mytweet.adapters;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.models.User;

/**
 * User Filter to filer user listing for search fragment
 */
public class UserFilter extends Filter {
  private List<User> originalUserList;
  private ListUserAdapter adapter;

  /**
   * User Filter constructor
   *
   * @param originalUserList List of users
   * @param adapter          User adapter
   */
  public UserFilter(List<User> originalUserList,
                    ListUserAdapter adapter) {
    super();
    this.originalUserList = originalUserList;
    this.adapter = adapter;
  }

  /**
   * Perform the filtering of user list based on the entered character sequence
   * Filters users if matching user name
   *
   * @param prefix User filter character sequence
   * @return Result of filtering
   */
  @Override
  protected FilterResults performFiltering(CharSequence prefix) {
    FilterResults results = new FilterResults();

    if (originalUserList == null) {
      originalUserList = new ArrayList<User>();
    }
    String prefixString = prefix.toString().toLowerCase();
    List<User> newUsers = new ArrayList<User>();

    for (User user : originalUserList) {
      String itemName = user.firstName.toLowerCase() + " " + user.lastName.toLowerCase();
      if (itemName.contains(prefixString)) {
        newUsers.add(user);
      }
    }
    results.values = newUsers;
    results.count = newUsers.size();

    return results;
  }

  /**
   * On result of filtering, set the adapter to the results to display filter results
   *
   * @param prefix  Character sequence for filter
   * @param results Results of Filtering
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void publishResults(CharSequence prefix, FilterResults results) {

    adapter.users = (ArrayList<User>) results.values;

    if (results.count >= 0)
      adapter.notifyDataSetChanged();
    else {
      adapter.notifyDataSetInvalidated();
      adapter.users = originalUserList;
    }
  }
}
