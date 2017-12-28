package kevin.mytweet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kevin.mytweet.fragments.AddTweetFragment;
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
    switch (position){
      case 0:
        return new TimeLineFragment();
      case 1:
        return new AddTweetFragment();
      case 2:
        return new TimeLineFragment();
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
