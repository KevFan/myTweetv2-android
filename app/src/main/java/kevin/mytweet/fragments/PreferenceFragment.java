package kevin.mytweet.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import kevin.mytweet.R;
import kevin.mytweet.fragments.timeline.TimeLineFragment;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Settings Fragment
 * Created by kevin on 25/10/2017.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  private SharedPreferences prefs;

  /**
   * Called when fragment is first created - sets the preference values from preferences xml
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Settings Fragment - created");
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
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
   * Used to update user preference for background update interval for updating tweets + following
   * services
   *
   * @param sharedPreferences Shared preference object
   * @param key               Key to a shared preference field/value
   */
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    String keyValue = sharedPreferences.getString(key, "");
    info("Setting change - key : value = " + key + " : " + keyValue);
    String refreshIntervalKey = getActivity().getResources().getString(R.string.refresh_interval_preference_key);
    if (key.equals(refreshIntervalKey)) {
      getActivity().sendBroadcast(new Intent(FollowFragment.BROADCAST_ACTION));
      getActivity().sendBroadcast(new Intent(TimeLineFragment.BROADCAST_ACTION));
    }
  }
}
