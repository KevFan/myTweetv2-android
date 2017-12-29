package kevin.mytweet.app;

import java.util.List;
import java.util.stream.Stream;

import kevin.mytweet.models.Follow;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

  @Multipart
  @PUT("/api/profilePicture/{id}")
  Call<User> updateProfilePicture(@Path("id") String id, @Part MultipartBody.Part image);

  @DELETE("/api/profilePicture/{id}")
  Call<User> deleteProfilePicture(@Path("id") String id);

  @GET("/api/tweets")
  Call<List<Tweet>> getAllTweets();

  @GET("/api/tweets/users/{userid}")
  Call<List<Tweet>> getAllUserTweets(@Path("userid") String id);

  @Multipart
  @POST("/api/tweets")
  Call<Tweet> createTweetWithPicture(@Part("tweetText") RequestBody tweetText,
                                     @Part("tweetDate") RequestBody tweetDate, @Part MultipartBody.Part image);

  @POST("/api/tweets")
  Call<Tweet> createTweet(@Body Tweet tweet);

  @DELETE("/api/tweets/{id}")
  Call<Tweet> deleteTweet(@Path("id") String id);

  @DELETE("/api/tweets/users/{userid}")
  Call<Tweet> deleteAllUserTweet(@Path("userid") String id);

  @GET("/api/follow/followers/{id}")
  Call<List<Follow>> getFollowers(@Path("id") String id);

  @GET("/api/follow/following/{id}")
  Call<List<Follow>> getFollowings(@Path("id") String id);

  @Multipart
  @POST("/api/follow")
  Call<Follow> follow(@Part("following") RequestBody following);

  @DELETE("/api/follow/{id}")
  Call<Follow> unfollow(@Path("id") String id);
}
