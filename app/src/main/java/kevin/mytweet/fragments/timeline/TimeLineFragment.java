package kevin.mytweet.fragments.timeline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.dialogBox;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * TimeLine Fragment - lists the user tweets using list fragment and custom adapter
 * Created by kevin on 20/10/2017.
 */

public class TimeLineFragment extends BaseTimeLineFragment implements AbsListView.MultiChoiceModeListener {

  public static final String BROADCAST_ACTION = "kevin.mytweet.activities.TimeLineFragment";
  private IntentFilter intentFilter;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("TweetLineFragement created");
    super.onCreate(savedInstanceState);
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
    View view = super.onCreateView(inflater, parent, savedInstanceState);
    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    listView.setMultiChoiceModeListener(this);

    return view;
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
        if (app.timeLine.isEmpty()) {
          toastMessage(getActivity(), "Have no tweets to delete!!");
        } else {
          // Dialog box to confirm delete tweets
          dialogBox(getActivity(), "Delete all tweets", "Are you sure you want to delete your tweets in timeline?",
              null, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  // continue with delete
                  Call<Tweet> call2 = (Call<Tweet>) app.tweetService.deleteAllUserTweet(app.currentUser._id);
                  call2.enqueue(new Callback<Tweet>() {
                    @Override
                    public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                      updateTimeLine();
                      toastMessage(getActivity(), "All tweets cleared and deleted");
                    }

                    @Override
                    public void onFailure(Call<Tweet> call, Throwable t) {
                      toastMessage(getActivity(), "All Tweet deletion failed");
                    }
                  });
                }
              });
        }
        break;
      // Clear entire activity history when logging out so that user can use back button to return
      // to old activities if a different user sign's in
      // https://stackoverflow.com/questions/3473168/clear-the-entire-history-stack-and-start-a-new-activity-on-android
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
    setNoTweetMessage();
    updateTimeLine();
  }

  @Override
  public void updateTimeLine() {
    Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserFollowingTweets();
    call.enqueue(new GetAllUserFollowingTweets());
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
        Call<Tweet> call2 = (Call<Tweet>) app.tweetService.deleteTweet(adapter.timeLine.get(i)._id);
        call2.enqueue(new Callback<Tweet>() {
          @Override
          public void onResponse(Call<Tweet> call, Response<Tweet> response) {
            updateTimeLine();
          }

          @Override
          public void onFailure(Call<Tweet> call, Throwable t) {
            toastMessage(getActivity(), "Tweet deletion failed");
          }
        });
      }
    }
    actionMode.finish();
  }

  /**
   * Called on preparation of the action mode, sets the title
   */
  @Override
  public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
    actionMode.setTitle("Delete Tweets");
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
//  public void clearPreferenceSettings() {
//    // Sets shared preference values to current user
//    info("TimeLineFragment - Clearing shared preferences due to sign out");
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//    SharedPreferences.Editor editor = prefs.edit();
//    editor.putString("firstName", "");
//    editor.putString("lastName", "");
//    editor.putString("email", "");
//    editor.putString("password", "");
//    editor.apply();
//  }

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
      //refreshDonationList();
      adapter.timeLine = app.timeLine;
      adapter.notifyDataSetChanged();
    }
  }

  public class GetAllUserFollowingTweets implements Callback<List<Tweet>> {
    @Override
    public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
      if (mSwipeRefreshLayout != null)
        mSwipeRefreshLayout.setRefreshing(false);
      updateTimeLineData(response.body());
      toastMessage(getActivity(), "Successfully got all user and following tweets");
    }

    @Override
    public void onFailure(Call<List<Tweet>> call, Throwable t) {
      app.tweetServiceAvailable = false;
      mSwipeRefreshLayout.setRefreshing(false);
      toastMessage(getActivity(), "Failed getting all user and following tweets :(");
    }
  }
}