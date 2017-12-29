package kevin.mytweet.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;

import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * UpdateAccountFragment - used to update account details
 * Created by kevin on 20/10/2017.
 */

public class UpdateAccountFragment extends Fragment implements Callback<User> {
  private EditText firstName;
  private EditText lastName;
  private EditText email;
  private EditText password;
  public MyTweetApp app = MyTweetApp.getApp();

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
        Call<User> call = (Call<User>) app.tweetService.updateUser(app.currentUser._id,
            new User(firstName.getText().toString(), lastName.getText().toString()
                , email.getText().toString(), password.getText().toString()));
        call.enqueue(UpdateAccountFragment.this);
      }
    });
    setHasOptionsMenu(true);

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
  public void onResponse(Call<User> call, Response<User> response) {
    app.currentUser = response.body();
    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_name))
        .setText(app.currentUser.firstName + ' ' + app.currentUser.lastName);
    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email)).setText(app.currentUser.email);
    toastMessage(getActivity(), "Successfully Updated");
  }

  @Override
  public void onFailure(Call<User> call, Throwable t) {
    toastMessage(getActivity(), "Failed to update");
    info(t.toString());
  }
}