package kevin.mytweet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import kevin.mytweet.R;
import kevin.mytweet.models.Tweet;

import static kevin.mytweet.helpers.ContactHelper.sendEmail;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Add Tweet Fragment - used to add tweet
 * Created by kevin on 20/10/2017.
 */

public class AddTweetFragment extends BaseTweetFragment implements View.OnClickListener, TextWatcher {

  private TextView charCount;
  private TextView tweetDate;
  private EditText tweetText;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Add Tweet Fragment created");
    super.onCreate(savedInstanceState);
    tweet = new Tweet("", new Date());
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
    View view = inflater.inflate(R.layout.fragment_add_tweet, parent, false);
    charCount = (TextView) view.findViewById(R.id.charCount);
    tweetDate = (TextView) view.findViewById(R.id.tweetDate);
    tweetText = (EditText) view.findViewById(R.id.tweetText);
    tweetText.addTextChangedListener(this);
    updateView(tweet);
    setListeners(view);
    // Set BaseTweetFragment buttonId to selectContactButton R id to get correct button
    buttonId = R.id.selectContactButton;
    return view;
  }

  /**
   * Updates the text view and edit text view with the tweet details
   *
   * @param tweet Tweet to update views with
   */
  public void updateView(Tweet tweet) {
    tweetDate.setText(tweet.tweetDate.toString());
    tweetText.setText(tweet.tweetMessage);
  }

  /**
   * Helper to set listeners on fragment view
   *
   * @param view Fragment view
   */
  public void setListeners(View view) {
    Button tweetButton = (Button) view.findViewById(R.id.tweetButton);
    Button selectContactButton = (Button) view.findViewById(R.id.selectContactButton);
    Button emailViaButton = (Button) view.findViewById(R.id.emailViaButton);

    tweetButton.setOnClickListener(this);
    selectContactButton.setOnClickListener(this);
    emailViaButton.setOnClickListener(this);
  }

  /**
   * On click listener for the tweet, select contact and email buttons
   *
   * @param view View clicked
   */
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tweetButton:
        // If tweet message is empty
        if (tweet.tweetMessage.equals("")) {
          toastMessage(getActivity(), "Write your message to send tweet");
        } else {
          timeLine.addTweet(tweet);
          app.save();
          toastMessage(getActivity(), "Message Sent !! ");
          // Finish the activity to reload timeline activity and prevents adding the add tweet
          // to the back stack
          getActivity().finish();
        }
        break;
      case R.id.selectContactButton:
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACT);
        break;
      case R.id.emailViaButton:
        sendEmail(getActivity(), emailAddress, getString(R.string.tweet_report_title), tweet.getTweetReport());
        break;
      default:
        toastMessage(getActivity(), "Add Tweet Fragment - Something is wrong :/ ");
        break;
    }
  }

  /* ************ TextWatcher Listener methods (begin) *********** */

  /**
   * Called after text changed - sets the character count to be 140 - character sequence length and
   * sets the tweet message to be the tweetText
   *
   * @param s      Character sequence
   * @param start  start
   * @param before before
   * @param count  count
   */
  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    int remainingCarCount = 140 - s.toString().length();
    charCount.setText(String.valueOf(remainingCarCount));
    tweet.tweetMessage = tweetText.getText().toString();
  }

  /**
   * Method stub to implement TextWatcher Listener interface - unused
   */
  @Override
  public void afterTextChanged(Editable s) {
  }

  /**
   * Method stub to implement TextWatcher Listener interface - unused
   */
  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }
}