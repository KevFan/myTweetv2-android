package kevin.mytweet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Login Activity to log in a user
 * Created by kevin on 09/10/2017.
 */

public class Login extends BaseActivity {
  private TextView email;
  private TextView password;
  private MyTweetApp app;

  /**
   * Called when activity is first created
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Login Activity created");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    email = (TextView) findViewById(R.id.loginEmail);
    password = (TextView) findViewById(R.id.loginPassword);
    app = (MyTweetApp) getApplication();
    Button login = (Button) findViewById(R.id.loginUser);
    login.setOnClickListener(loginListener);
  }

  /**
   * Anonymous class listener for the login button
   * Checks for all fields are filled, and starts the timeline activity is credentials are valid
   */
  private View.OnClickListener loginListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String emailString = email.getText().toString();
      String passwordString = password.getText().toString();
      if (emailString.isEmpty() || passwordString.isEmpty()) {
        toastMessage(view.getContext(), "Fill in all information to sign in !!");
      } else if (app.successLogin(emailString, passwordString)) {
        startActivity(new Intent(view.getContext(), TimeLineActivity.class));
        toastMessage(view.getContext(), "Login Successful !!");
      } else {
        toastMessage(view.getContext(), "Email/Password incorrect !!");
      }
    }
  };
}
