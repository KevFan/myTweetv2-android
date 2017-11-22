package kevin.mytweet.helpers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;

import java.io.Serializable;

/**
 * Intent helpers
 * Created by kevin on 19/10/2017.
 */

public class IntentHelper {
  /**
   * Navigate back to previous activity on activity stack
   *
   * @param parent Parent activity
   */
  public static void navigateUp(Activity parent) {
    Intent upIntent = NavUtils.getParentActivityIntent(parent);
    NavUtils.navigateUpTo(parent, upIntent);
  }

  /**
   * Start an activity through intent while also passing additional data to intent before stating
   *
   * @param parent    Parent activity
   * @param classname Class of activity
   * @param extraID   String ID to associated with extra data
   * @param extraData Serializable extra data to pass into intent
   */
  public static void startActivityWithData(Activity parent, Class classname, String extraID, Serializable extraData) {
    Intent intent = new Intent(parent, classname);
    intent.putExtra(extraID, extraData);
    parent.startActivity(intent);
  }
}
