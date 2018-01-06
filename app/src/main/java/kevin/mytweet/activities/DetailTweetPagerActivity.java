package kevin.mytweet.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.adapters.DetailTweetPagerAdapter;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.tweet.DetailTweetFragment;
import kevin.mytweet.models.Tweet;

import android.view.MenuItem;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Detail Tweet Paper Activity
 * Created by kevin on 16/10/2017.
 */

public class DetailTweetPagerActivity extends BaseActivity {
  private ViewPager viewPager;
  private List<Tweet> tweetArrayList;

  /**
   * Called when activity is first created - creates tweet fragment if savedBundleInstance is null
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Detail Tweet Activity created");
    super.onCreate(savedInstanceState);
    viewPager = new ViewPager(this);
    viewPager.setId(R.id.viewPager);
    setContentView(viewPager);
    tweetArrayList = MyTweetApp.getApp().timeLine;
    DetailTweetPagerAdapter pagerAdapter = new DetailTweetPagerAdapter(getSupportFragmentManager(), tweetArrayList);
    viewPager.setAdapter(pagerAdapter);
    setCurrentItem();
  }


  /**
   * Sets current item in the view pager
   * Ensure selected tweet is shown in detail tweet view
   */
  private void setCurrentItem() {
    String tweetId = (String) getIntent().getSerializableExtra(DetailTweetFragment.EXTRA_TWEET_ID);
    for (int i = 0; i < tweetArrayList.size(); i++) {
      if (tweetArrayList.get(i)._id.equals(tweetId)) {
        viewPager.setCurrentItem(i);
        break;
      }
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
    info("Detail Pager Activity - navigated up pressed");
    if (item.getItemId() == android.R.id.home) {
      this.finish();
      return true;
    } else{
      return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Called after asking for permissions
   * https://developer.android.com/training/permissions/requesting.html
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
