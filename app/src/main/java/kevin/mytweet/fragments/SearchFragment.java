package kevin.mytweet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.ProfileActivity;
import kevin.mytweet.adapters.ListUserAdapter;
import kevin.mytweet.adapters.UserFilter;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.IntentHelper.startActivityWithData;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Search Fragment - used to filter through user list for searching based of user name
 * Created by kevin on 29/12/2017.
 */

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, TextWatcher {
  public ListView listView;
  public EditText search;
  public List<User> users = new ArrayList<>();
  public MyTweetApp app = MyTweetApp.getApp();
  public ListUserAdapter adapter;
  public SwipeRefreshLayout mSwipeRefreshLayout;
  public UserFilter filter;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("SearchFragment created");
    super.onCreate(savedInstanceState);
  }

  /**
   * Called to create the view hierarchy associated with the fragment
   *
   * @param inflater           Layout inflater
   * @param parent             Parent view group
   * @param savedInstanceState Bundle with saved data if any
   * @return View of the layout
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    super.onCreateView(inflater, parent, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_search, parent, false);
    search = (EditText) view.findViewById(R.id.searchUser);
    search.addTextChangedListener(this);
    listView = (ListView) view.findViewById(R.id.userList);
    listView.setOnItemClickListener(this);
    mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tweet_swipe_refresh_layout);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        updateUserList();
      }
    });
    setHasOptionsMenu(true);

    return view;
  }

  /**
   * On resume of fragment - update user list
   */
  @Override
  public void onResume() {
    super.onResume();
    updateUserList();
  }

  /**
   * Helper method to make call to get all user list
   */
  public void updateUserList() {
    Call<List<User>> call = (Call<List<User>>) app.tweetService.getAllUsers();
    call.enqueue(new GetUsers());
  }

  /**
   * On item click listener for list view - get the user and pass the user id to start the profile
   * activity to view user profile
   *
   * @param parent   Parent view
   * @param view     View
   * @param position Position of item
   * @param id       Id of item
   */
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    User user = adapter.users.get(position);
    startActivityWithData(getActivity(), ProfileActivity.class, "userid", user._id);

  }

  /**
   * Method stub to implement TextWatcher Listener interface - unused
   */

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  /**
   * Called after text changed - calls the filter to filter user list results
   *
   * @param s      Character sequence
   * @param start  start
   * @param before before
   * @param count  count
   */
  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    filter.filter(s);
  }

  /**
   * Method stub to implement TextWatcher Listener interface - unused
   */
  @Override
  public void afterTextChanged(Editable s) {

  }

  /**
   * Helper class to get all users, update the user list, adapter and filter with new list
   */
  public class GetUsers implements Callback<List<User>> {
    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
      users = response.body();
      users.remove(app.currentUser);
      if (mSwipeRefreshLayout != null) {
        mSwipeRefreshLayout.setRefreshing(false);
      }
      adapter = new ListUserAdapter(getActivity(), users);
      filter = new UserFilter(users, adapter);
      listView.setAdapter(adapter);
      adapter.notifyDataSetChanged();
      info("Search - Got all users !!");
    }

    @Override
    public void onFailure(Call<List<User>> call, Throwable t) {
      info(t.toString());
      mSwipeRefreshLayout.setRefreshing(false);
      info("Failed to get all users :(");
    }
  }
}
