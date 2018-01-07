package kevin.mytweet.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.FollowFragment;
import kevin.mytweet.fragments.timeline.GlobalTimeLineFragment;
import kevin.mytweet.fragments.timeline.TimeLineFragment;

import static kevin.mytweet.activities.HomeActivity.setUserIdToFragment;

/**
 * Profile Pager Adapter for horizontal paging of profile fragment
 * https://stackoverflow.com/questions/42311123/how-to-add-tabbed-activity-into-navigation-drawer-activity
 * Created by kevin on 28/12/2017.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
  private String userId;

  /**
   * Profile page adapter constructor
   *
   * @param fm     Fragment manager
   * @param userId User id to display profile of
   */
  public ProfilePagerAdapter(FragmentManager fm, String userId) {
    super(fm);
    this.userId = userId;
  }

  /**
   * Get fragment based on the page number currently on
   *
   * @param position Position of the page currently on
   * @return Fragment of corresponding position
   */
  @Override
  public Fragment getItem(int position) {
    Bundle args = new Bundle();
    Fragment fragment;
    switch (position) {
      case 0:
        // TimeLine - depending on if userID is the current user display the TimeLineFragment or
        // Global timeline fragment (have no delete tweet option for when viewing another user profile)
        if (userId.equals(MyTweetApp.getApp().currentUser._id)) {
          fragment = new TimeLineFragment();
        } else {
          fragment = new GlobalTimeLineFragment();
        }
        setUserIdToFragment(fragment, userId);
        return fragment;
      case 1:
        // Follow Fragment - Display Follower listing
        args.putString(FollowFragment.EXTRA_FOLLOW, "follower");
        args.putString("userid", userId);
        fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
      case 2:
        // Follow Fragment - Display Following listing
        args.putString(FollowFragment.EXTRA_FOLLOW, "following");
        args.putString("userid", userId);
        fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
    }
    return null;
  }

  /**
   * Returns the number of horizontal pages to be created
   *
   * @return number of horizontal pages
   */
  @Override
  public int getCount() {
    return 3;
  }


  /**
   * Sets the page title depending on the page number
   *
   * @param position Page number
   * @return Title to set page
   */
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "Timeline";
      case 1:
        return "Followers";
      case 2:
        return "Following";
      default:
        return null;
    }
  }
}
