package kevin.mytweet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.app.MyTweetService;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * Welcome Activity
 */

public class Welcome extends AppCompatActivity implements View.OnClickListener, Callback<List<User>> {
  public MyTweetApp app = MyTweetApp.getApp();

  /**
   * Called when activity is first created
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    Button loginButton = (Button) findViewById(R.id.welcomeLogin);
    loginButton.setOnClickListener(this);
    Button signupButton = (Button) findViewById(R.id.welcomeSignup);
    signupButton.setOnClickListener(this);
  }

  /**
   * On click listener for the login and sign up buttons in view
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

  @Override
  public void onResume()
  {
    super.onResume();
    app.currentUser = null;
    Call<List<User>> call1 = (Call<List<User>>) app.tweetService.getAllUsers();
    call1.enqueue(this);
  }

  @Override
  public void onResponse(Call<List<User>> call, Response<List<User>> response) {
    serviceAvailableMessage();
    app.users = response.body();
    app.tweetServiceAvailable = true;
  }

  @Override
  public void onFailure(Call<List<User>> call, Throwable t) {
    app.tweetServiceAvailable = false;
    serviceUnavailableMessage();
  }

  void serviceUnavailableMessage() {
    toastMessage(this, "MyTweet Service Unavailable. Try again later");
  }

  void serviceAvailableMessage() {
    toastMessage(this, "MyTweet Service Contacted Successfully");
  }
}
