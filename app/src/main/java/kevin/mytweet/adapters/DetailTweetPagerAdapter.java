package kevin.mytweet.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import kevin.mytweet.fragments.DetailTweetFragment;
import kevin.mytweet.models.Tweet;

/**
 * Created by kevin on 28/12/2017.
 */

public class DetailTweetPagerAdapter extends FragmentStatePagerAdapter {
  private List<Tweet> tweetArrayList;

  /**
   * PagerAdapter constructor
   *
   * @param fm             Fragment manager
   * @param tweetArrayList ArrayList of tweets
   */
  public DetailTweetPagerAdapter(FragmentManager fm, List<Tweet> tweetArrayList) {
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
