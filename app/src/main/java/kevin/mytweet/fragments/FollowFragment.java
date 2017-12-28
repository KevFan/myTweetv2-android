package kevin.mytweet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.adapters.ListFollowsAdapter;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Follow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * FollowFragment Fragment - lists the user follows using custom adapter
 * Created by kevin on 20/10/2017.
 */

public class FollowFragment extends Fragment {
  public static final String EXTRA_FOLLOW = "FOLLOW_OR_FOLLOWING";

  public TextView noFollowsMessage;
  public ListView listView;
  public List<Follow> follows = new ArrayList<>();
  public MyTweetApp app = MyTweetApp.getApp();
  public ListFollowsAdapter adapter;
  public SwipeRefreshLayout mSwipeRefreshLayout;

  private String followOrFollowing;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("FollowFragment created");
    super.onCreate(savedInstanceState);
    followOrFollowing = (String) getArguments().getSerializable(EXTRA_FOLLOW);
    adapter = new ListFollowsAdapter(getActivity(), follows, followOrFollowing);
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
    if (!adapter.follows.isEmpty()) {
      noFollowsMessage.setVisibility(View.INVISIBLE);
    } else {
      noFollowsMessage.setVisibility(View.VISIBLE);
    }
  }

  public void updateFollowList() {
    if (followOrFollowing.equals("follower")) {
      Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowers(app.currentUser._id);
      call.enqueue(new GetFollows());
    } else {
      Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowings(app.currentUser._id);
      call.enqueue(new GetFollows());
    }
  }

  public class GetFollows implements Callback<List<Follow>> {
    @Override
    public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
      follows = response.body();
      if (mSwipeRefreshLayout != null)
        mSwipeRefreshLayout.setRefreshing(false);
      adapter = new ListFollowsAdapter(getActivity(), follows, followOrFollowing);
      listView.setAdapter(adapter);
      adapter.notifyDataSetChanged();
      setNoTweetMessage();
      toastMessage(getActivity(), "Got all " + followOrFollowing + "!!");
    }

    @Override
    public void onFailure(Call<List<Follow>> call, Throwable t) {
      info(t.toString());
      mSwipeRefreshLayout.setRefreshing(false);
      toastMessage(getActivity(), "Failed to get all " + followOrFollowing +" :(");
    }
  }
}