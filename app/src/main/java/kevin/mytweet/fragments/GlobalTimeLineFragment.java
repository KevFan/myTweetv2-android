package kevin.mytweet.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Created by kevin on 24/11/2017.
 */

public class GlobalTimeLineFragment extends BaseTimeLineFragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, parent, savedInstanceState);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        updateTimeLine();
      }
    });

    return view;
  }

  /**
   * Whenever the fragment is paused, check should the no tweets message be visible or not
   */
  @Override
  public void onResume() {
    super.onResume();
    setNoTweetMessage();
    updateTimeLine();
  }

  public void updateTimeLine() {
    Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllTweets();
    call.enqueue(new Callback<List<Tweet>>() {
      @Override
      public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
        if (mSwipeRefreshLayout != null)
          mSwipeRefreshLayout.setRefreshing(false);
        updateTimeLineData(response.body());
        toastMessage(getActivity(), "Successfully got all tweets");
      }

      @Override
      public void onFailure(Call<List<Tweet>> call, Throwable t) {
        app.tweetServiceAvailable = false;
        mSwipeRefreshLayout.setRefreshing(false);
        toastMessage(getActivity(), "Failed getting all user tweets :(");
      }
    });
  }
}
