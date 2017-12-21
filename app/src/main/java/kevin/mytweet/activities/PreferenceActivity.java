package kevin.mytweet.activities;

import android.os.Bundle;

import kevin.mytweet.fragments.PreferenceFragment;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Preference activity
 * Created by kevin on 25/10/2017.
 */

public class PreferenceActivity extends BaseActivity {
  /**
   * Called when activity is first created
   * Creates the preferences fragment if savedInstanceState is null
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Settings Activity - Created");
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      PreferenceFragment fragment = new PreferenceFragment();
      getFragmentManager().beginTransaction()
          .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
          .commit();
    }
  }
}
