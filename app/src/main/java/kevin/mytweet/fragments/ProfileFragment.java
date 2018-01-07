package kevin.mytweet.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kevin.mytweet.R;
import kevin.mytweet.adapters.ProfilePagerAdapter;

/**
 * Profile Fragment - Holds a view pager to hold timeline, following and follower fragments
 * Created by kevin on 28/12/2017.
 */

public class ProfileFragment extends Fragment {
  /**
   * Called to create the view hierarchy associated with the fragment
   *
   * @param inflater           Inflater to inflate layout
   * @param container          container for view
   * @param savedInstanceState Bundle with saved data if any
   * @return View of inflated layout
   */
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    String userId = (String) getArguments().getSerializable("userid");
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
    ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
    viewPager.setAdapter(new ProfilePagerAdapter(getChildFragmentManager(), userId));
    tabLayout.setupWithViewPager(viewPager);
    return view;
  }
}
