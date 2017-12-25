package kevin.mytweet.app;

import java.util.List;

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

public interface MyTweetService {
  @GET("/api/users")
  Call<List<User>> getAllUsers();

  @GET("/api/users/{id}")
  Call<User> getUser(@Path("id") String id);

  @PUT("/api/users/{id}")
  Call<User> updateUser(@Path("id") String id, @Body User User);

  @GET("/api/tweets")
  Call<List<Tweet>> getAllTweets();

  @GET("/api/tweets/users/{userid}")
  Call<List<Tweet>> getAllUserTweets(@Path("userid") String id);

  @POST("/api/tweets")
  Call<Tweet> createTweet(@Body Tweet tweet);

  @DELETE("/api/tweets/{id}")
  Call<Tweet> deleteTweet(@Path("id") String id);

  @DELETE("/api/tweets/users/{userid}")
  Call<Tweet> deleteAllUserTweet(@Path("userid") String id);
}
