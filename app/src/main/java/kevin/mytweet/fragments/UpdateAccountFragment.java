package kevin.mytweet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
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
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.ContactHelper.sendEmail;
import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Add Tweet Fragment - used to add tweet
 * Created by kevin on 20/10/2017.
 */

public class UpdateAccountFragment extends Fragment implements Callback<Tweet> {
  private EditText firstName;
  private EditText lastName;
  private EditText email;
  private EditText password;

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
    View view = inflater.inflate(R.layout.fragment_update_account, parent, false);
    firstName = (EditText) view.findViewById(R.id.updateFirstName);
    lastName = (EditText) view.findViewById(R.id.updateLastName);
    email = (EditText) view.findViewById(R.id.updateEmail);
    password = (EditText) view.findViewById(R.id.updatePassword);
    updateView(MyTweetApp.getApp().currentUser);

    Button updateButton = (Button) view.findViewById(R.id.updateAccountButton);
    updateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toastMessage(getActivity(), "Update Button Pressed");
      }
    });
    return view;
  }

  /**
   * Updates the text view and edit text view with the user details
   *
   * @param user User to update views with
   */
  public void updateView(User user) {
    firstName.setText(user.firstName);
    lastName.setText(user.lastName);
    email.setText(user.email);
    password.setText(user.password);
  }

  @Override
  public void onResponse(Call<Tweet> call, Response<Tweet> response) {

  }

  @Override
  public void onFailure(Call<Tweet> call, Throwable t) {

  }
}