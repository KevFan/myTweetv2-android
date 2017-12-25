package kevin.mytweet.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.GlobalTimeLineFragment;
import kevin.mytweet.fragments.TimeLineFragment;
import kevin.mytweet.fragments.UpdateAccountFragment;
import kevin.mytweet.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  public User currentUser = MyTweetApp.getApp().currentUser;
  public ImageView profilePhoto;
  public static final int PICK_IMAGE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Home Activity Stared");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_name)).setText(currentUser.firstName + ' ' + currentUser.lastName);
    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email)).setText(currentUser.email);
    profilePhoto = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profilePhoto);
    profilePhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkContactsReadPermission();
      }
    });

    if (!currentUser.image.equals("")) {
      Picasso.with(this).load(currentUser.image).into(profilePhoto);
    }

    // Set home view to timeline fragment
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = new TimeLineFragment();
    manager.beginTransaction().replace(R.id.homeFrame, fragment).commit();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.home, menu);
//    return true;
//  }
//
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    // Handle action bar item clicks here. The action bar will
//    // automatically handle clicks on the Home/Up button, so long
//    // as you specify a parent activity in AndroidManifest.xml.
//    int id = item.getItemId();
//
//    //noinspection SimplifiableIfStatement
//    if (id == R.id.action_settings) {
//      return true;
//    }
//
//    return super.onOptionsItemSelected(item);
//  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    FragmentManager manager = getSupportFragmentManager();

    if (id == R.id.nav_home) {
      Fragment fragment = new TimeLineFragment();
      manager.beginTransaction().replace(R.id.homeFrame, fragment).commit();
      toastMessage(this, "Nav Home Selected");
    } else if (id == R.id.nav_global_timeline) {
      Fragment fragment = new GlobalTimeLineFragment();
      manager.beginTransaction().replace(R.id.homeFrame, fragment).commit();
      toastMessage(this, "Nav TimeLine Selected");
    } else if (id == R.id.nav_preferences) {
      toastMessage(this, "Nav Settings Selected");
      startActivity(new Intent(this, PreferenceActivity.class));
    } else if (id == R.id.nav_settings) {
      Fragment fragment = new UpdateAccountFragment();
      manager.beginTransaction().replace(R.id.homeFrame, fragment).commit();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  // TODO: Should use async task for image upload - currently will give timeout if image is large
  // https://stackoverflow.com/questions/39953457/how-to-upload-image-file-in-retrofit-2
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PICK_IMAGE) {
      try {
        Uri selectedImage = data.getData();
        File file = new File(getRealPathFromURI_API19(this, selectedImage));

        RequestBody requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
            MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<User> call = (Call<User>) MyTweetApp.getApp().tweetService.updateProfilePicture(currentUser._id, body);
        call.enqueue(new Callback<User>() {
          @Override
          public void onResponse(Call<User> call, Response<User> response) {
            currentUser = response.body();
            Picasso.with(HomeActivity.this).load(currentUser.image).into(profilePhoto);
            toastMessage(HomeActivity.this, "updated profile picture");
          }

          @Override
          public void onFailure(Call<User> call, Throwable t) {
            toastMessage(HomeActivity.this, "i have failed !!!");
            info(t.toString());
          }
        });
      } catch (Exception e) {
        info(e.toString());
      }
    }
  }

  // https://stackoverflow.com/questions/29646975/how-to-get-file-path-of-image-from-uri-in-android-lollipop
  public static String getRealPathFromURI_API19(Context context, Uri uri) {
    info(uri.getPath());
    String filePath = "";
    if (DocumentsContract.isDocumentUri(context, uri)) {
      String wholeID = DocumentsContract.getDocumentId(uri);
      info(wholeID);
      // Split at colon, use second item in the array
      String[] splits = wholeID.split(":");
      if (splits.length == 2) {
        String id = splits[1];

        String[] column = {MediaStore.Images.Media.DATA};
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
          filePath = cursor.getString(columnIndex);
        }
        cursor.close();
      }
    } else {
      filePath = uri.getPath();
    }
    return filePath;
  }

  /**
   * Check for permission to read contacts
   * https://developer.android.com/training/permissions/requesting.html
   */
  private void checkContactsReadPermission() {
    // Here, thisActivity is the current activity
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      //We can request the permission.
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
    } else {
      //We already have permission, so go head and read the contact
      selectImage();
    }
  }

  /**
   * Called after asking for permissions
   * https://developer.android.com/training/permissions/requesting.html
   *
   * @param requestCode  Request code passed in by requestPermissions
   * @param permissions  requested permissions
   * @param grantResults result of granting permissions
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
                                         int[] grantResults) {
    if (requestCode == PICK_IMAGE) {
      if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // permission was granted
        selectImage();
      }
    }
  }

  public void selectImage() {
    // https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
  }
}
