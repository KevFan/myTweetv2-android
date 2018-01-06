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
import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * https://stackoverflow.com/questions/42311123/how-to-add-tabbed-activity-into-navigation-drawer-activity
 * Created by kevin on 28/12/2017.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
  private String userId;
  public ProfilePagerAdapter(FragmentManager fm, String userId) {
    super(fm);
    this.userId = userId;
  }

  @Override
  public Fragment getItem(int position) {
    Bundle args = new Bundle();
    Fragment fragment;
    switch (position){
      case 0:
        if (userId.equals(MyTweetApp.getApp().currentUser._id)) {
          fragment = new TimeLineFragment();
        } else {
          fragment = new GlobalTimeLineFragment();
        }
        setUserIdToFragment(fragment, userId);
        return fragment;
      case 1:
        args.putString(FollowFragment.EXTRA_FOLLOW, "follower");
        args.putString("userid", userId);
        fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
      case 2:
        args.putString(FollowFragment.EXTRA_FOLLOW, "following");
        args.putString("userid", userId);
        fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
    }
    return null;
  }

  @Override
  public int getCount() {
    return 3;
  }


  @Override
  public CharSequence getPageTitle(int position) {
    switch (position){
      //
      //Your tab titles
      //
      case 0:return "Timeline";
      case 1:return "Followers";
      case 2: return "Following";
      default:return null;
    }
  }
}
