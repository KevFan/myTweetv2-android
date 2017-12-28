package kevin.mytweet.fragments;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.Welcome;
import kevin.mytweet.adapters.ListFollowersAdapter;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.dialogBox;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * FollowFragment Fragment - lists the user follows using custom adapter
 * Created by kevin on 20/10/2017.
 */

public class FollowFragment extends Fragment {
  public TextView noFollowsMessage;
  public ListView listView;
  public List<Follow> followers = new ArrayList<>();
  public MyTweetApp app = MyTweetApp.getApp();
  public ListFollowersAdapter adapter;
  public SwipeRefreshLayout mSwipeRefreshLayout;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("FollowFragment created");
    super.onCreate(savedInstanceState);
    adapter = new ListFollowersAdapter(getActivity(), followers);
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
    View view = inflater.inflate(R.layout.fragment_follow, parent, false);
    noFollowsMessage = (TextView) view.findViewById(R.id.noFollowMessage);
    listView = (ListView) view.findViewById(R.id.followList);
    listView.setAdapter(adapter);
    mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tweet_swipe_refresh_layout);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        updateFollowList();
      }
    });
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateFollowList();
    setNoTweetMessage();
  }

  public void setNoTweetMessage() {
    if (!adapter.followers.isEmpty()) {
      noFollowsMessage.setVisibility(View.INVISIBLE);
    } else {
      noFollowsMessage.setVisibility(View.VISIBLE);
    }
  }

  public void updateFollowList() {
    Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowers(app.currentUser._id);
    call.enqueue(new GetFollowers());
  }

  public class GetFollowers implements Callback<List<Follow>> {
    @Override
    public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
      followers = response.body();
      if (mSwipeRefreshLayout != null)
        mSwipeRefreshLayout.setRefreshing(false);
      adapter = new ListFollowersAdapter(getActivity(), followers);
      listView.setAdapter(adapter);
      adapter.notifyDataSetChanged();
      setNoTweetMessage();
      toastMessage(getActivity(), "Got all Followers!!");
    }

    @Override
    public void onFailure(Call<List<Follow>> call, Throwable t) {
      info(t.toString());
      mSwipeRefreshLayout.setRefreshing(false);
      toastMessage(getActivity(), "Failed to get all followers :(");
    }
  }
}