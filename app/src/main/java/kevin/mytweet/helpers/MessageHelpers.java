package kevin.mytweet.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import kevin.mytweet.R;

/**
 * Message Helpers
 * Contains helpers are used across classes that logs messages to console or interact with application through toast
 * or dialog box messages
 * Created by kevin on 13/10/2017.
 */

public class MessageHelpers {
  /**
   * Log messages and tag with "MyTweet"
   *
   * @param message Message to log
   */
  public static void info(String message) {
    Log.v("MyTweet", message);
  }

  /**
   * Toast helper
   *
   * @param context      Context of where method was called
   * @param toastMessage Toast message to display
   */
  public static void toastMessage(Context context, String toastMessage) {
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
  }

  /**
   * Helper for creating dialog box to reduce code duplication
   *
   * @param context     context of where the method was called
   * @param title       title to display on dialog box
   * @param message     message to display on dialog box
   * @param noListener  DialogInterface.OnClickListener listener - action on no for dialog box
   * @param yesListener DialogInterface.OnClickListener listener - action on yes for dialog box
   */
  public static void dialogBox(Context context, String title, String message, DialogInterface.OnClickListener noListener,
                               DialogInterface.OnClickListener yesListener) {
    new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(android.R.string.no, noListener)
        .setPositiveButton(android.R.string.yes, yesListener)
        .setIcon(R.drawable.ic_error_outline_black_24dp)
        .create().show();
  }
}
