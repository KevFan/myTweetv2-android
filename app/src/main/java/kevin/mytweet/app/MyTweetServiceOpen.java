package kevin.mytweet.app;

import java.util.List;

import kevin.mytweet.models.Token;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by kevin on 24/11/2017.
 */

public interface MyTweetServiceOpen {
  @POST("/api/users")
  Call<User> createUser(@Body User User);

  @POST("/api/users/authenticate")
  Call<Token> authenticate(@Body User user);
}
