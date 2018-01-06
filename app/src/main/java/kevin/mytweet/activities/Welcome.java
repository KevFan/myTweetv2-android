package kevin.mytweet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.app.MyTweetService;
import kevin.mytweet.app.RetrofitServiceFactory;
import kevin.mytweet.models.Token;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.SaveLoadHelper.loadToken;

/**
 * Welcome Activity
 */

public class Welcome extends AppCompatActivity implements View.OnClickListener {
  public MyTweetApp app = MyTweetApp.getApp();

  /**
   * Called when activity is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    // Load token - and if valid start Home activity instead to not require user to login again
    Token token = loadToken(this);
    if (token != null) {
      info("Got valid saved token");
      app.currentUser = token.user;
      app.tweetService = RetrofitServiceFactory.createService(MyTweetService.class, token.token);
      info("Authenticated by load: " + app.currentUser.firstName + ' ' + app.currentUser.lastName);
      startActivity(new Intent(this, HomeActivity.class));
    } else {
      info("Token is null - launching into welcome activity");
    }

    Button loginButton = (Button) findViewById(R.id.welcomeLogin);
    loginButton.setOnClickListener(this);
    Button signupButton = (Button) findViewById(R.id.welcomeSignup);
    signupButton.setOnClickListener(this);
  }

  /**
   * On click listener for the login and sign up buttons in view
   *
   * @param view View to listen to
   */
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.welcomeSignup:
        startActivity(new Intent(this, SignUp.class));
        break;
      case R.id.welcomeLogin:
        startActivity(new Intent(this, Login.class));
        break;
      default:
        toastMessage(this, "Welcome - Something is wrong :/ ");
        break;
    }
  }
}
