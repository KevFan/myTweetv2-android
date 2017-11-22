package kevin.mytweet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kevin.mytweet.R;
import kevin.mytweet.models.Tweet;

import static kevin.mytweet.helpers.ContactHelper.sendEmail;
import static kevin.mytweet.helpers.MessageHelpers.*;

/**
 * Detail Tweet Fragment - used to detail saved tweets
 * Created by kevin on 20/10/2017.
 */

public class DetailTweetFragment extends BaseTweetFragment implements View.OnClickListener {
  public static final String EXTRA_TWEET_ID = "TWEET_ID";

  private TextView detailCharCount;
  private TextView detailTweetDate;
  private EditText detailTweetText;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Detail Tweet Fragment created");
    super.onCreate(savedInstanceState);

    Long tweetId = (Long) getArguments().getSerializable(EXTRA_TWEET_ID);
    tweet = timeLine.getTweet(tweetId);
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
    View view = inflater.inflate(R.layout.fragment_detail_tweet, parent, false);
    detailCharCount = (TextView) view.findViewById(R.id.detailCharCount);
    detailTweetDate = (TextView) view.findViewById(R.id.detailTweetDate);
    detailTweetText = (EditText) view.findViewById(R.id.detailTweetText);
    updateView(tweet);
    setListeners(view);
    // Set BaseTweetFragment buttonId to detailSelectContactButton R id to get correct button
    buttonId = R.id.detailSelectContactButton;

    // Set edit view to be non editable
    detailTweetText.setEnabled(false); // Set tweet message to read only in view tweets
    return view;
  }

  /**
   * Updates the text view and edit text view with the tweet details
   *
   * @param tweet Tweet to update views with
   */
  public void updateView(Tweet tweet) {
    detailCharCount.setText(String.valueOf(140 - tweet.tweetMessage.length()));
    detailTweetDate.setText(tweet.tweetDate.toString());
    detailTweetText.setText(tweet.tweetMessage);
  }

  /**
   * On click listener for the tweet, select contact and email buttons
   *
   * @param view View clicked
   */
  public void setListeners(View view) {
    Button detailSelectContactButton = (Button) view.findViewById(R.id.detailSelectContactButton);
    Button detailEmailViaButton = (Button) view.findViewById(R.id.detailEmailViaButton);

    detailSelectContactButton.setOnClickListener(this);
    detailEmailViaButton.setOnClickListener(this);
  }

  /**
   * On click listener for the tweet, select contact and email buttons
   *
   * @param view View clicked
   */
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.detailSelectContactButton:
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACT);
        break;
      case R.id.detailEmailViaButton:
        sendEmail(getActivity(), emailAddress, getString(R.string.tweet_report_title), tweet.getTweetReport());
        break;
      default:
        toastMessage(getActivity(), "Detail Tweet Fragment - Something is wrong :/ ");
        break;
    }
  }
}