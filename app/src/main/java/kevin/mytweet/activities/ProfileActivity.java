package kevin.mytweet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import kevin.mytweet.R;
import kevin.mytweet.fragments.ProfileFragment;

import static kevin.mytweet.activities.HomeActivity.setUserIdToFragment;
import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Profile activity that is used when viewing another user's profile - used with Profile Fragment
 * Created by kevin on 29/12/2017.
 */

public class ProfileActivity extends BaseActivity {
  /**
   * Called when activity is first created
   * Creates the profile fragment if savedInstanceState is null
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Profile Activity - Created");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_container);
    String userid = (String) getIntent().getSerializableExtra("userid");
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
    if (fragment == null) {
      fragment = new ProfileFragment();
      setUserIdToFragment(fragment, userid);
      manager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }
  }
}
