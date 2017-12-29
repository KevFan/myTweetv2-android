package kevin.mytweet.fragments.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.AddTweetActivity;
import kevin.mytweet.activities.DetailTweetPagerActivity;
import kevin.mytweet.adapters.TimeLineAdapter;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.tweet.DetailTweetFragment;
import kevin.mytweet.helpers.IntentHelper;
import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Created by kevin on 24/11/2017.
 */

public abstract class BaseTimeLineFragment extends Fragment implements AdapterView.OnItemClickListener {
  protected TimeLineAdapter adapter;
  MyTweetApp app;
  protected ListView listView;
  protected TextView noTweetMessage;
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("BaseTweetLineFragement created");
    super.onCreate(savedInstanceState);
    getActivity().setTitle(R.string.app_name);
    app = MyTweetApp.getApp();
    adapter = new TimeLineAdapter(getActivity(), app.timeLine);
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
    View view = inflater.inflate(R.layout.fragment_home, parent, false);
    listView = (ListView) view.findViewById(R.id.tweetList);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(this);

    // If there are tweets, set the no tweets message to invisible
    noTweetMessage = (TextView) view.findViewById(R.id.noTweetsMessage);
    setNoTweetMessage();

    mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tweet_swipe_refresh_layout);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        updateTimeLine();
      }
    });

    FloatingActionButton newTweet = (FloatingActionButton) view.findViewById(R.id.newTweetAction);
    newTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(view.getContext(), AddTweetActivity.class));
      }
    });

    return view;
  }

  public void setNoTweetMessage() {
    if (!adapter.timeLine.isEmpty()) {
      noTweetMessage.setVisibility(View.INVISIBLE);
    } else {
      noTweetMessage.setVisibility(View.VISIBLE);
    }
  }

  public void updateTimeLineData(List<Tweet> updatedTimeline) {
    app.timeLine = updatedTimeline;
    adapter = new TimeLineAdapter(getActivity(), updatedTimeline);
    listView.setAdapter(adapter);
    setNoTweetMessage();
    adapter.notifyDataSetChanged();
  }

  /**
   * Called on click on item in the list view
   *
   * @param parent   Adapter view
   * @param view     view
   * @param position position of view
   * @param id       id
   */
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Tweet tweet = adapter.timeLine.get(position);
    IntentHelper.startActivityWithData(getActivity(), DetailTweetPagerActivity.class,
        DetailTweetFragment.EXTRA_TWEET_ID, tweet._id);
  }

  /**
   * Classes extending from this class must implement the update timeline method.
   * Should make relevant retrofit call, and call updateTimeLineData with the response body here
   */
  public abstract void updateTimeLine();

  // Class to get all user tweets and update app timeline and adapter timeline
  public class GetAllUserTweets implements Callback<List<Tweet>> {
    @Override
    public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
      if (mSwipeRefreshLayout != null)
        mSwipeRefreshLayout.setRefreshing(false);
      updateTimeLineData(response.body());
      toastMessage(getActivity(), "Successfully got all user tweets");
    }

    @Override
    public void onFailure(Call<List<Tweet>> call, Throwable t) {
      app.tweetServiceAvailable = false;
      mSwipeRefreshLayout.setRefreshing(false);
      toastMessage(getActivity(), "Failed getting all user tweets :(");
    }
  }
}
