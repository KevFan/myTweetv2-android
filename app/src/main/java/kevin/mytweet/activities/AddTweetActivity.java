package kevin.mytweet.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import kevin.mytweet.R;
import kevin.mytweet.fragments.tweet.AddTweetFragment;

import static kevin.mytweet.helpers.IntentHelper.navigateUp;
import static kevin.mytweet.helpers.MessageHelpers.dialogBox;
import static kevin.mytweet.helpers.MessageHelpers.info;


/**
 * Add Tweet activity
 * Created by kevin on 25/10/2017.
 */

public class AddTweetActivity extends BaseActivity {
  /**
   * Called when activity is first created
   * Creates the add tweet fragment if savedInstanceState is null
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Add Tweet Activity - Created");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_container);

    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
    if (fragment == null) {
      fragment = new AddTweetFragment();
      manager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }
  }

  /**
   * Menu Item selector - only used for navigate up to previous activity here
   *
   * @param item Menu item
   * @return Boolean
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    info("Add Tweet Activity - navigated up pressed");
    final Activity activity = this;
    if (item.getItemId() == android.R.id.home) {
      dialogBox(this, "Tweet not saved !!", "Return to timeline and discard tweet?",
          null, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
              navigateUp(activity);
            }
          });
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Override the on back pressed to display dialog to alert user that tweet will be discarded if
   * they continue
   * https://stackoverflow.com/questions/6413700/android-proper-way-to-use-onbackpressed-with-toast
   */
  @Override
  public void onBackPressed() {
    dialogBox(this, "Tweet not saved !!", "Return to timeline and discard tweet?",
        null, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface arg0, int arg1) {
            AddTweetActivity.super.onBackPressed();
          }
        });
  }

  /**
   * https://stackoverflow.com/questions/35989288/onrequestpermissionsresult-not-being-called-in-fragment-if-defined-in-both-fragm
   * Called after asking for permissions
   * https://developer.android.com/training/permissions/requesting.html
   * Calls super on request permission result as associated fragment permission requests first gets returned
   * to activity before passed to fragment on request permission result
   *
   * @param requestCode  Request code passed in by requestPermissions
   * @param permissions  requested permissions
   * @param grantResults result of granting permissions
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
                                         int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
