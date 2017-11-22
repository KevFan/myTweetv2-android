package kevin.mytweet.activities;

import android.os.Bundle;

import kevin.mytweet.fragments.SettingsFragment;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Settings activity
 * Created by kevin on 25/10/2017.
 */

public class SettingsActivity extends BaseActivity {
  /**
   * Called when activity is first created
   * Creates the settings fragment if savedInstanceState is null
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Settings Activity - Created");
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      SettingsFragment fragment = new SettingsFragment();
      getFragmentManager().beginTransaction()
          .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
          .commit();
    }
  }
}
