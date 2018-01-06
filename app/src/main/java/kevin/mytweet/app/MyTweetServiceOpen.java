package kevin.mytweet.app;

import kevin.mytweet.models.Token;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * MyTweetService API call to routes that are open and don't require JWT authentication
 * Created by kevin on 24/11/2017.
 */

public interface MyTweetServiceOpen {
  @POST("/api/users")
  Call<User> createUser(@Body User User);

  @POST("/api/users/authenticate")
  Call<Token> authenticate(@Body User user);
}
