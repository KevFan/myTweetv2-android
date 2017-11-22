package kevin.mytweet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import static kevin.mytweet.helpers.IntentHelper.navigateUp;
import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Base Activity to store common functionality such as navigate up
 * Created by kevin on 27/10/2017.
 */

public class BaseActivity extends AppCompatActivity {
  /**
   * Called when activity is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    info("Base Activity - Created");
  }

  /**
   * Menu Item selector - only used for navigate up to previous activity here
   *
   * @param item Menu item
   * @return Boolean
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    info("Base Activity - navigated up pressed");
    if (item.getItemId() == android.R.id.home) {
      navigateUp(this);
      return true;
    } else{
      return super.onOptionsItemSelected(item);
    }
  }
}
