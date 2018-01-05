package kevin.mytweet.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.ProfileActivity;
import kevin.mytweet.adapters.ListFollowsAdapter;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Follow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.IntentHelper.startActivityWithData;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.SaveLoadHelper.saveFollowers;

/**
 * FollowFragment Fragment - lists the user follows using custom adapter
 * Created by kevin on 20/10/2017.
 */

public class FollowFragment extends Fragment implements AdapterView.OnItemClickListener {
  public static final String EXTRA_FOLLOW = "FOLLOW_OR_FOLLOWING";
  public static final String BROADCAST_ACTION = "kevin.mytweet.activities.FollowFragment";
  private IntentFilter intentFilter;


  public TextView noFollowsMessage;
  public ListView listView;
  public MyTweetApp app = MyTweetApp.getApp();
  public ListFollowsAdapter adapter;
  public SwipeRefreshLayout mSwipeRefreshLayout;

  private String followOrFollowing;
  public String userId;

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
    if (followOrFollowing.equals("follower")) {
      adapter = new ListFollowsAdapter(getActivity(), app.followers, followOrFollowing);
    } else {
      adapter = new ListFollowsAdapter(getActivity(), app.followings, followOrFollowing);
    }
    userId = (String) getArguments().getSerializable("userid");
    registerBroadcastReceiver();

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
    listView.setOnItemClickListener(this);
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
      Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowers(userId);
      call.enqueue(new GetFollows());
    } else {
      Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowings(userId);
      call.enqueue(new GetFollows());
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Follow follow = adapter.follows.get(position);
    if (followOrFollowing.equals("follower")) {
      startActivityWithData(getActivity(), ProfileActivity.class, "userid", follow.follower._id);
    } else {
      startActivityWithData(getActivity(), ProfileActivity.class, "userid", follow.following._id);
    }
  }

  public class GetFollows implements Callback<List<Follow>> {
    @Override
    public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
      if (followOrFollowing.equals("follower")) {
        app.followers = response.body();
        adapter = new ListFollowsAdapter(getActivity(), app.followers, followOrFollowing);
        saveFollowers(getActivity(), response.body());
      } else {
        app.followings = response.body();
      }
      if (mSwipeRefreshLayout != null)
        mSwipeRefreshLayout.setRefreshing(false);

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

  private void registerBroadcastReceiver() {
    intentFilter = new IntentFilter(BROADCAST_ACTION);
    ResponseReceiver responseReceiver = new ResponseReceiver();
    // Registers the ResponseReceiver and its intent filters
    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(responseReceiver, intentFilter);
  }

  private class ResponseReceiver extends BroadcastReceiver {
    //private void ResponseReceiver() {}
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {
      if (followOrFollowing.equals("following")) {
        adapter.follows = app.followings;
        adapter.notifyDataSetChanged();
      }
    }
  }
}