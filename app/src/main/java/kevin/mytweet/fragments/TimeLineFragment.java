package kevin.mytweet.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.DetailTweetPagerActivity;
import kevin.mytweet.activities.SettingsActivity;
import kevin.mytweet.activities.Welcome;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.helpers.IntentHelper;
import kevin.mytweet.models.TimeLine;
import kevin.mytweet.models.Tweet;

import android.widget.AbsListView;
import android.view.ActionMode;

import static kevin.mytweet.helpers.MessageHelpers.*;

/**
 * TimeLine Fragment - lists the user tweets using list fragment and custom adapter
 * Created by kevin on 20/10/2017.
 */

public class TimeLineFragment extends ListFragment implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {
  private TimeLine timeLine;
  private TimeLineAdapter adapter;
  MyTweetApp app;
  private ListView listView;
  private TextView noTweetMessage;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("TweetLineFragement created");
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    getActivity().setTitle(R.string.app_name);

    app = MyTweetApp.getApp();
    timeLine = app.currentUser.timeLine;

    adapter = new TimeLineAdapter(getActivity(), timeLine.tweets);
    setListAdapter(adapter);
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
    View view = super.onCreateView(inflater, parent, savedInstanceState);
    listView = (ListView) view.findViewById(android.R.id.list);
    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    listView.setMultiChoiceModeListener(this);

    // If there are tweets, set the no tweets message to invisible
    noTweetMessage = (TextView) getActivity().findViewById(R.id.noTweetsMessage);
    if (!timeLine.tweets.isEmpty()) {
      noTweetMessage.setVisibility(View.INVISIBLE);
    }

    return view;
  }

  /**
   * Called when an item in the list fragment is clicked
   *
   * @param l        list view
   * @param view     view
   * @param position position of the item
   * @param id       id
   */
  @Override
  public void onListItemClick(ListView l, View view, int position, long id) {
    Tweet tweet = ((TimeLineAdapter) getListAdapter()).getItem(position);
    Intent intent = new Intent(getActivity(), DetailTweetPagerActivity.class);
    intent.putExtra(DetailTweetFragment.EXTRA_TWEET_ID, tweet.id);
    startActivityForResult(intent, 0);
  }

  /**
   * Overflow Menu item selector
   *
   * @param item item selected on the menu
   * @return Boolean value
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Deletes all tweets in the timeline of the current user and saves
      case R.id.clearTimeLine:
        if (timeLine.tweets.isEmpty()) {
          toastMessage(getActivity(), "Have no tweets to delete!!");
        } else {
          // Dialog box to confirm delete tweets
          dialogBox(getActivity(), "Delete all tweets", "Are you sure you want to delete all tweets in timeline?",
              null, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  // continue with delete
                  timeLine.tweets.clear();
                  app.save();
                  adapter.notifyDataSetChanged();
                  noTweetMessage.setVisibility(View.VISIBLE);
                  toastMessage(getActivity(), "All tweets cleared and deleted");
                }
              });
        }
        break;
      // Starts the settings activity
      case R.id.menuSettings:
        startActivity(new Intent(getActivity(), SettingsActivity.class));
        break;
      // Clear entire activity history when logging out so that user can use back button to return
      // to old activities if a different user sign's in
      // https://stackoverflow.com/questions/3473168/clear-the-entire-history-stack-and-start-a-new-activity-on-android
      case R.id.menuLogout:
        clearPreferenceSettings();
        startActivity(new Intent(getActivity(), Welcome.class)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        toastMessage(getActivity(), "Signing out");
        break;
      default:
        info("Time Line Fragment - Something is wrong :(");
        break;
    }
    return true;
  }

  /**
   * Menu Item inflater - inflates the menu items in the overflow menu for use in the action bar
   *
   * @param menu     Overflow menu
   * @param inflater Menu inflater
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_tweet, menu);
  }

  /**
   * Whenever the fragment is paused, check should the no tweets message be visible or not
   */
  @Override
  public void onResume() {
    super.onResume();
    if (!timeLine.tweets.isEmpty()) {
      noTweetMessage.setVisibility(View.INVISIBLE);
    } else {
      noTweetMessage.setVisibility(View.VISIBLE);
    }
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
    Tweet tweet = adapter.getItem(position);
    IntentHelper.startActivityWithData(getActivity(), DetailTweetPagerActivity.class,
        DetailTweetFragment.EXTRA_TWEET_ID, tweet.id);
  }

  /**
   * Custom adaptor for the timeline fragment to list tweets
   */
  class TimeLineAdapter extends ArrayAdapter<Tweet> {
    private Context context;

    /**
     * TimeLineAdapter constructor
     *
     * @param context Context of where the adapter is constructed
     * @param tweets  ArrayList of tweets
     */
    private TimeLineAdapter(Context context, List<Tweet> tweets) {
      super(context, 0, tweets);
      this.context = context;
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

      Tweet tweet = getItem(position);

      TextView tweetText = (TextView) convertView.findViewById(R.id.list_item_tweetText);
      tweetText.setText(tweet.tweetMessage);
      tweetText.setMaxLines(1);

      TextView tweetDate = (TextView) convertView.findViewById(R.id.list_item_tweetDate);
      tweetDate.setText(tweet.tweetDate.toString());

      return convertView;
    }
  }

  /* ************ MultiChoiceModeListener methods (begin) *********** */

  /**
   * Called when the action mode is first created and inflates the menu to delete tweets
   *
   * @param actionMode Action mode created
   * @param menu       Menu to inflate
   * @return Boolean of if action mode was created
   */
  @Override
  public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
    MenuInflater inflater = actionMode.getMenuInflater();
    inflater.inflate(R.menu.delete_list_context, menu);
    return true;
  }

  /**
   * Called on click of the delete tweet button on action bar
   *
   * @param actionMode Action mode
   * @param menuItem   Item clicked
   * @return Boolean of whether event was handled
   */
  @Override
  public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
    // Action bar needs to be declared final to be accessed in inner dialog box class
    final ActionMode action = actionMode;
    if (menuItem.getItemId() == R.id.menu_item_delete_tweet) {
      // Dialog box to confirm delete tweets
      dialogBox(getActivity(), "Delete tweets", "Are you sure you want to delete these tweets?",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // Close action bar
              action.finish();
            }
          }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // continue with delete
              deleteTweet(action);
              toastMessage(getActivity(), "Tweets deleted");
            }
          });
      return true;
    } else {
      return false;
    }
  }

  /**
   * Helper method to delete all selected tweets from the MultiChoiceModeListener
   *
   * @param actionMode Action mode
   */
  private void deleteTweet(ActionMode actionMode) {
    for (int i = adapter.getCount() - 1; i >= 0; i--) {
      if (listView.isItemChecked(i)) {
        timeLine.deleteTweet(adapter.getItem(i));
        app.save();
      }
    }
    actionMode.finish();
    adapter.notifyDataSetChanged();
  }

  /**
   * Method stub to implement MultiChoiceModeListener interface - unused
   */
  @Override
  public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
    return false;
  }

  /**
   * Method stub to implement MultiChoiceModeListener interface - unused
   */
  @Override
  public void onDestroyActionMode(ActionMode actionMode) {
  }

  /**
   * Method stub to implement MultiChoiceModeListener interface - unused
   */
  @Override
  public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
  }

  /* ************ MultiChoiceModeListener methods (end) *********** */

  /**
   * Clears the shared preference setiings of the current user details - used in user log out
   */
  public void clearPreferenceSettings() {
    // Sets shared preference values to current user
    info("TimeLineFragment - Clearing shared preferences due to sign out");
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("firstName", "");
    editor.putString("lastName", "");
    editor.putString("email", "");
    editor.putString("password", "");
    editor.apply();
  }
}