package kevin.mytweet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.ValidatorHelpers.isEmailUsed;
import static kevin.mytweet.helpers.ValidatorHelpers.isValidEmail;

/**
 * Sign up Activity to register a new user
 * Created by kevin on 09/10/2017.
 */

public class SignUp extends BaseActivity implements Callback<User> {
  private TextView firstName;
  private TextView lastName;
  private TextView email;
  private TextView password;
  private MyTweetApp app;

  /**
   * Called when activity is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("SignUp Activity - Created");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    firstName = (TextView) findViewById(R.id.firstName);
    lastName = (TextView) findViewById(R.id.lastName);
    email = (TextView) findViewById(R.id.email);
    password = (TextView) findViewById(R.id.password);
    app = MyTweetApp.getApp();
    Button signup = (Button) findViewById(R.id.signupMyTweet);
    signup.setOnClickListener(signupListener);
  }

  /**
   * Success response of user creation - validates user to start home activity
   *
   * @param call     User call
   * @param response Response with new user
   */
  @Override
  public void onResponse(Call<User> call, Response<User> response) {
    app.users.add(response.body());
    app.currentUser = response.body();
    toastMessage(this, "Successfully Registered");
    app.validUser(email.getText().toString(), password.getText().toString());
  }

  /**
   * On call failure
   *
   * @param call User call
   * @param t    Error
   */
  @Override
  public void onFailure(Call<User> call, Throwable t) {
    toastMessage(this, "MyTweet Service Unavailable. Try again later");
    info("Welcome Activty:" + t.toString());
    startActivity(new Intent(this, Welcome.class));
  }

  /**
   * Anonymous class listener for the login button
   * Checks for all fields are filled, if the email is in a valid email format and whether the email
   * is already used before signing up. Starts HomeActivity if successfully registered
   */
  private View.OnClickListener signupListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String firstNameString = firstName.getText().toString();
      String lastNameString = lastName.getText().toString();
      String emailString = email.getText().toString();
      String passwordString = password.getText().toString();
      if (firstNameString.isEmpty() || lastNameString.isEmpty() || emailString.isEmpty()
          || passwordString.isEmpty()) {
        toastMessage(view.getContext(), "Fill in all information to complete signup");
      } else if (!isValidEmail(emailString)) {
        toastMessage(view.getContext(), "Email is not a valid format");
      } else if (isEmailUsed(emailString)) {
        toastMessage(view.getContext(), "Email already used by another user");
      } else {
        // Make call by by retrofit to create new user
        Call<User> call = (Call<User>) app.tweetServiceOpen.createUser
            (new User(firstNameString, lastNameString, emailString, passwordString));
        call.enqueue(SignUp.this);
      }
    }
  };
}
