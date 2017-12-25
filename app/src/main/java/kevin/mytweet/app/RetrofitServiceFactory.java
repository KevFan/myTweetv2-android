package kevin.mytweet.app;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit.Builder;

/**
 * Created by kevin on 24/12/2017.
 */

public class RetrofitServiceFactory {
  public static final String API_BASE_URL = "http://192.168.0.8:4000";

  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  private static Builder builder = new Builder()
      .baseUrl(API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create());

  public static <S> S createService(Class<S> serviceClass) {
    Retrofit retrofit = builder.client(httpClient.build()).build();
    return retrofit.create(serviceClass);
  }

  public static <S> S createService(Class<S> serviceClass, final String authToken) {
    if (authToken != null) {
      httpClient.addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
          Request original = chain.request();
          Request.Builder requestBuilder = original.newBuilder()
              .header("Authorization", "bearer " + authToken)
              .method(original.method(), original.body());

          Request request = requestBuilder.build();
          return chain.proceed(request);
        }
      });
    }

    OkHttpClient client = httpClient.build();
    Retrofit retrofit = builder.client(client).build();
    return retrofit.create(serviceClass);
  }
}
