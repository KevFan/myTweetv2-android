package kevin.mytweet.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.TimeLine;
import kevin.mytweet.models.Tweet;

import static kevin.mytweet.helpers.ContactHelper.getContact;
import static kevin.mytweet.helpers.ContactHelper.getEmail;
import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Base Tweet Fragment to store common codebase between add tweet and detail tweet fragment such as
 * select contact and email contact functionality to reduce duplicate code
 * Created by kevin on 03/11/2017.
 */

public class BaseTweetFragment extends Fragment {
  protected static final int REQUEST_CONTACT = 1;

  protected TimeLine timeLine;
  protected MyTweetApp app;
  protected Tweet tweet;
  protected Intent data;
  protected String emailAddress = "";
  protected int buttonId = 0;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Base Tweet Fragment created");
    super.onCreate(savedInstanceState);
    app = MyTweetApp.getApp();
    timeLine = app.currentUser.timeLine;
  }

  /**
   * Receive the result from a previous call to startActivityForResult
   *
   * @param requestCode Integer of request code supplied by startActivityForResult
   * @param resultCode  integer result code
   * @param data        Intent
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // If result code is not an ok result - exit
    if (resultCode != Activity.RESULT_OK) {
      return;
    }

    // If request code is the request contact, set the intent to data passed in and check
    // for permissions
    if (requestCode == REQUEST_CONTACT) {
      this.data = data;
      checkContactsReadPermission();
    }
  }

  /**
   * Reads the contact details and sets the email address to the contact email and set select
   * contact button text with contact name and email
   */
  private void readContact() {
    String name = getContact(getActivity(), data);
    emailAddress = getEmail(getActivity(), data);
    Button selectContactButton = (Button) getActivity().findViewById(buttonId);
    selectContactButton.setText(name + " : " + emailAddress);
  }

  /**
   * Check for permission to read contacts
   * https://developer.android.com/training/permissions/requesting.html
   */
  private void checkContactsReadPermission() {
    // Here, thisActivity is the current activity
    if (ContextCompat.checkSelfPermission(getActivity(),
        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      //We can request the permission.
      ActivityCompat.requestPermissions(getActivity(),
          new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
    } else {
      //We already have permission, so go head and read the contact
      readContact();
    }
  }

  /**
   * Called after asking for permissions
   * https://developer.android.com/training/permissions/requesting.html
   *
   * @param requestCode  Request code passed in by requestPermissions
   * @param permissions  requested permissions
   * @param grantResults result of granting permissions
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
                                         int[] grantResults) {
    if (requestCode == REQUEST_CONTACT) {
      if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // permission was granted
        readContact();
      }
    }
  }
}
