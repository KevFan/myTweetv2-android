package kevin.mytweet.helpers;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.User;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Validator helpers used across more than one class
 * Currently used to validate user email
 * Created by kevin on 04/11/2017.
 */

public class ValidatorHelpers {
  /**
   * Checks string is in a valid email format
   * https://stackoverflow.com/questions/31262250/how-to-check-whether-email-is-valid-format-or-not-in-android
   * @param target String to check
   * @return Boolean of if target string passed is in a valid email format
   */
  public static boolean isValidEmail(String target) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
  }

  /**
   * Check is email already used by a user
   * @param email email to check for
   * @return Boolean of if email is already used
   */
  public static boolean isEmailUsed(String email) {
    if (MyTweetApp.getApp().users != null) {
      for (User user : MyTweetApp.getApp().users) {
        info(user.email);
        if (user.email.equals(email)) {
          return true;
        }
      }
    }
    return false;
  }
}
