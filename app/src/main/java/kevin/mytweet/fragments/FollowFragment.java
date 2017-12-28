package kevin.mytweet.fragments;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.activities.Welcome;
import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.dialogBox;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * FollowFragment Fragment - lists the user follows using custom adapter
 * Created by kevin on 20/10/2017.
 */

public class FollowFragment extends Fragment {
  protected ListView listView;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("FollowFragment created");
    super.onCreate(savedInstanceState);
  }

  /**
   * Called to create the view hierarchy associated with the fragment
   *
   * @param inflater           Layout inflater
   * @param parent             Parent view group
   * @param savedInstanceState Bundle with saved data if any
   * @return View of the layout
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    super.onCreateView(inflater, parent, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_follow, parent, false);
    listView = (ListView) view.findViewById(R.id.followList);
    return view;
  }
}