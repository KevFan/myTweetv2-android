package kevin.mytweet.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.User;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.ValidatorHelpers.*;

/**
 * Settings Fragment
 * Created by kevin on 25/10/2017.
 */

public class SettingsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  private SharedPreferences prefs;

  /**
   * Called when fragment is first created - sets the preference values from settings xml
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Settings Fragment - created");
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);
    setHasOptionsMenu(true);
  }

  /**
   * Called when the fragment is becoming visible to the user
   * Instantiates the shared preferences and registers the listener
   */
  @Override
  public void onStart() {
    super.onStart();
    prefs = PreferenceManager
        .getDefaultSharedPreferences(getActivity());
    prefs.registerOnSharedPreferenceChangeListener(this);
  }

  /**
   * Called when the fragment is no longer visible to the user
   * Un-registers the listener
   */
  @Override
  public void onStop() {
    super.onStop();
    prefs.unregisterOnSharedPreferenceChangeListener(this);
  }

  /**
   * Listener for changes in the shared preferences values through keys
   * Used to update user details through use of keys
   *
   * @param sharedPreferences Shared preference object
   * @param key               Key to a shared preference field/value
   */
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    User currentUser = MyTweetApp.getApp().currentUser;
    String keyValue = sharedPreferences.getString(key, "");
    info("Setting change - key : value = " + key + " : " + keyValue);
    switch (key) {
      case "firstName":
        if (keyValue.isEmpty()) {
          toastMessage(getActivity(), "First name cannot be empty - change not saved");
        } else {
          currentUser.firstName = keyValue;
        }
        break;
      case "lastName":
        if (keyValue.isEmpty()) {
          toastMessage(getActivity(), "Last name cannot be empty - change not saved");
        } else {
          currentUser.lastName = keyValue;
        }
        break;
      case "email":
        if (keyValue.isEmpty()) {
          toastMessage(getActivity(), "Email cannot be empty - change not saved");
        } else if (isEmailUsed(keyValue) && !keyValue.equals(currentUser.email)) {
          toastMessage(getActivity(), "Email used by another user - change not saved");
        } else if (!isValidEmail(keyValue)) {
          toastMessage(getActivity(), "Email not in email format - change not saved");
        } else {
          currentUser.email = keyValue;
        }
        break;
      case "password":
        if (keyValue.isEmpty()) {
          toastMessage(getActivity(), "Password cannot be empty - change not saved");
        } else {
          currentUser.password = keyValue;
        }
        break;
      default:
        info("Settings Fragment - Something went wrong in OnSharedPreferenceChanged :(");
        break;
    }
    // Ensures the current shared preference values are specific to user
    MyTweetApp.getApp().setPreferenceSettings();
    MyTweetApp.getApp().save();
  }
}
