package kevin.mytweet.adapters;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.models.User;

public class UserFilter extends Filter {
  private List<User> originalUserList;
  private ListUserAdapter adapter;

  public UserFilter(List<User> originalUserList,
                    ListUserAdapter adapter) {
    super();
    this.originalUserList = originalUserList;
    this.adapter = adapter;
  }

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
