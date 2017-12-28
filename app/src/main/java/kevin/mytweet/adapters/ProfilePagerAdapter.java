package kevin.mytweet.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kevin.mytweet.fragments.AddTweetFragment;
import kevin.mytweet.fragments.FollowFragment;
import kevin.mytweet.fragments.TimeLineFragment;

/**
 * https://stackoverflow.com/questions/42311123/how-to-add-tabbed-activity-into-navigation-drawer-activity
 * Created by kevin on 28/12/2017.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
  public ProfilePagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    Bundle args = new Bundle();
    Fragment fragment;
    switch (position){
      case 0:
        return new TimeLineFragment();
      case 1:
        args.putSerializable(FollowFragment.EXTRA_FOLLOW, "follower");
        fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
      case 2:
        args.putSerializable(FollowFragment.EXTRA_FOLLOW, "following");
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
