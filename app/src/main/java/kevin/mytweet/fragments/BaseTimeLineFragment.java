package kevin.mytweet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.AddTweetActivity;
import kevin.mytweet.activities.DetailTweetPagerActivity;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.helpers.IntentHelper;
import kevin.mytweet.models.Tweet;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Created by kevin on 24/11/2017.
 */

public class BaseTimeLineFragment extends Fragment implements AdapterView.OnItemClickListener {
  protected TimeLineAdapter adapter;
  MyTweetApp app;
  protected ListView listView;
  protected TextView noTweetMessage;

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

    FloatingActionButton newTweet = (FloatingActionButton) view.findViewById(R.id.newTweetAction);
    newTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(view.getContext(), AddTweetActivity.class));
      }
    });

    return view;
  }

  /**
   * Custom adaptor for the timeline fragment to list tweets
   */
  class TimeLineAdapter extends ArrayAdapter<Tweet> {
    private Context context;
    protected List<Tweet> timeLine;

    /**
     * TimeLineAdapter constructor
     *
     * @param context Context of where the adapter is constructed
     * @param tweets  ArrayList of tweets
     */
    private TimeLineAdapter(Context context, List<Tweet> tweets) {
      super(context, 0, tweets);
      this.context = context;
      this.timeLine = tweets;
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
        convertView = inflater.inflate(R.layout.list_item_tweet, null);
      }

      Tweet tweet = timeLine.get(position);

      TextView tweetText = (TextView) convertView.findViewById(R.id.list_item_tweetText);
      tweetText.setText(tweet.tweetText);
      tweetText.setMaxLines(1);

      TextView tweetDate = (TextView) convertView.findViewById(R.id.list_item_tweetDate);
      tweetDate.setText(tweet.tweetDate.toString());

      return convertView;
    }

    @Override
    public int getCount() {
      return timeLine.size();
    }
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
    adapter.timeLine = updatedTimeline;
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

}
