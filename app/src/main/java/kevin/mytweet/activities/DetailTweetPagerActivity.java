package kevin.mytweet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.DetailTweetFragment;
import kevin.mytweet.models.Tweet;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuItem;

import static kevin.mytweet.helpers.IntentHelper.navigateUp;
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
    PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tweetArrayList);
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
   * Pager adapter
   */
  class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Tweet> tweetArrayList;

    /**
     * PagerAdapter constructor
     *
     * @param fm             Fragment manager
     * @param tweetArrayList ArrayList of tweets
     */
    private PagerAdapter(FragmentManager fm, List<Tweet> tweetArrayList) {
      super(fm);
      this.tweetArrayList = tweetArrayList;
    }

    /**
     * Return the size of the ArrayList of tweets
     *
     * @return Integer of the number of tweets in the ArrayList of tweets
     */
    @Override
    public int getCount() {
      return tweetArrayList.size();
    }

    /**
     * Returns the detail tweet fragment corresponding to the position of the tweet in the arraylist
     *
     * @param position Position of the tweet in the ArrayList
     * @return Detail tweet fragment
     */
    @Override
    public Fragment getItem(int position) {
      Tweet tweet = tweetArrayList.get(position);
      Bundle args = new Bundle();
      args.putSerializable(DetailTweetFragment.EXTRA_TWEET_ID, tweet._id);
      DetailTweetFragment fragment = new DetailTweetFragment();
      fragment.setArguments(args);
      return fragment;
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
