package kevin.mytweet.fragments.tweet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.Date;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.models.Tweet;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.ContactHelper.sendEmail;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.PictureHelper.PICK_IMAGE;
import static kevin.mytweet.helpers.PictureHelper.getRealPathFromURI_API19;
import static kevin.mytweet.helpers.PictureHelper.setGetPictureIntent;

/**
 * Add Tweet Fragment - used to add tweet
 * Created by kevin on 20/10/2017.
 */

public class AddTweetFragment extends BaseTweetFragment implements View.OnClickListener,
    TextWatcher, Callback<Tweet>, OnMapReadyCallback {

  private TextView charCount;
  private TextView tweetDate;
  private EditText tweetText;
  private ImageView tweetImage;
  private TextView tweetUserName;

  private File imageFile = null;

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
    tweetUserName = (TextView) view.findViewById(R.id.tweetUserName);
    tweetImage = (ImageView) view.findViewById(R.id.tweetImage);
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
    tweetText.setText(tweet.tweetText);
    tweetUserName.setText(app.currentUser.firstName + " " + app.currentUser.lastName);
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
    Button selectImageButton = (Button) view.findViewById(R.id.selectImageButton);

    tweetButton.setOnClickListener(this);
    selectContactButton.setOnClickListener(this);
    emailViaButton.setOnClickListener(this);
    selectImageButton.setOnClickListener(this);
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
        if (tweet.tweetText.equals("")) {
          toastMessage(getActivity(), "Write your message to send tweet");
        } else {
          // Set location to app location coordinates
          Call<Tweet> call;
          tweet.marker.coords.latitude = app.mCurrentLocation.getLatitude();
          tweet.marker.coords.longitude = app.mCurrentLocation.getLongitude();
          // If no image file is selected
          if (imageFile == null) {
            call = (Call<Tweet>) app.tweetService.createTweet(tweet);
          } else {
            // Otherwise create the form data to upload image and tweet data
            RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", imageFile.getName(), requestFile);
            RequestBody tweetText =
                RequestBody.create(
                    MediaType.parse("multipart/form-data"), tweet.tweetText);
            RequestBody tweetDate =
                RequestBody.create(
                    MediaType.parse("multipart/form-data"), tweet.tweetDate.toString());
            call = (Call<Tweet>) app.tweetService.createTweetWithPicture(tweetText, tweetDate, body, tweet.marker);
          }
          call.enqueue(this);
        }
        break;
      case R.id.selectContactButton:
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACT);
        break;
      case R.id.emailViaButton:
        sendEmail(getActivity(), emailAddress, getString(R.string.tweet_report_title), tweet.getTweetReport());
        break;
      case R.id.selectImageButton:
        checkExternalStorageReadPermission();
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
    tweet.tweetText = tweetText.getText().toString();
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

  /**
   * On response of creating tweet
   *
   * @param call     Tweet call
   * @param response Tweet Response
   */
  @Override
  public void onResponse(Call<Tweet> call, Response<Tweet> response) {
    toastMessage(getActivity(), "Message saved !! ");
    // Finish the activity to reload timeline activity and prevents adding the add tweet
    // to the back stack
    getActivity().finish();
  }

  /**
   * On Failure of creating tweet
   *
   * @param call Tweet call
   * @param t    Error
   */
  @Override
  public void onFailure(Call<Tweet> call, Throwable t) {
    info(t.toString());
    toastMessage(getActivity(), "Message fail to save, please try again !! ");
  }

  /**
   * Overides on activity result - Used after getting a profile picture from the select picture
   * intent and uploads and updates the current user's profile picture
   * https://stackoverflow.com/questions/39953457/how-to-upload-image-file-in-retrofit-2
   *
   * @param requestCode Request code for picture selection
   * @param resultCode  Result code
   * @param data        Data of the intent result
   */
  // TODO: Should use async task for image upload - currently will give timeout if image is large
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PICK_IMAGE) {
      Uri selectedImage = data.getData();
      imageFile = new File(getRealPathFromURI_API19(getActivity(), selectedImage));
      tweetImage.setImageURI(selectedImage);
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * Check for permission to read storage permission for picture selection
   * Ask for permission if don't have permission, otherwise select photo
   * https://developer.android.com/training/permissions/requesting.html
   */
  private void checkExternalStorageReadPermission() {
    // Here, thisActivity is the current activity

    if (ContextCompat.checkSelfPermission(getActivity(),
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      //We can request the permission.
      requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
    } else {
      //We already have permission, so go head and read the contact
      selectImage();
    }
  }

  /**
   * Called after asking for permissions - Used for checking was external storage read permission
   * was granted for picture selection
   * https://developer.android.com/training/permissions/requesting.html
   *
   * @param requestCode  Request code passed in by requestPermissions
   * @param permissions  requested permissions
   * @param grantResults result of granting permissions
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
                                         int[] grantResults) {
    if (requestCode == PICK_IMAGE) {
      if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // permission was granted
        selectImage();
      }
    }
  }

  /**
   * Call the helper setGetPictureIntent to set intent to get a picture and starts activity for
   * result
   */
  public void selectImage() {
    startActivityForResult(Intent.createChooser(setGetPictureIntent(), "Select Picture"), PICK_IMAGE);
  }

  /**
   * On Map Ready of Add Tweet Fragment to display current device location
   *
   * @param googleMap Google Map object
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    googleMap.clear();
    addTweets(app.timeLine, googleMap);
  }

  /**
   * To display tweet markers to from current tweet listing onto map
   *
   * @param list      Tweet List
   * @param googleMap Google map
   */
  public void addTweets(List<Tweet> list, GoogleMap googleMap) {
    for (Tweet tweet : list)
      googleMap.addMarker(new MarkerOptions()
          .position(new LatLng(tweet.marker.coords.latitude, tweet.marker.coords.longitude))
          .title(tweet.tweetDate.toString())
          .snippet(tweet.tweetText)
          .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
  }
}