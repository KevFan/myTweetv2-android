package kevin.mytweet.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.MapsFragment;
import kevin.mytweet.fragments.ProfileFragment;
import kevin.mytweet.fragments.SearchFragment;
import kevin.mytweet.fragments.UpdateAccountFragment;
import kevin.mytweet.fragments.timeline.GlobalTimeLineFragment;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.PictureHelper.PICK_IMAGE;
import static kevin.mytweet.helpers.PictureHelper.getRealPathFromURI_API19;
import static kevin.mytweet.helpers.PictureHelper.setGetPictureIntent;
import static kevin.mytweet.helpers.SaveLoadHelper.saveToken;

/**
 * Home Activity to serve as the main activity after the signed up
 * Allows the user to navigate to other fragments/activities by navigation drawer
 */
public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  public MyTweetApp app = MyTweetApp.getApp();
  public User currentUser = app.currentUser;
  public ImageView profilePhoto;
  private DrawerLayout drawer;

  /**
   * Called when activity is first created
   * Creates the add tweet fragment if savedInstanceState is null
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    info("Home Activity Stared");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_name)).setText(currentUser.firstName + ' ' + currentUser.lastName);
    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email)).setText(currentUser.email);
    profilePhoto = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profilePhoto);
    profilePhoto.setOnClickListener(ProfilePhotoListener);

    // Set current user image if any
    if (!currentUser.image.equals("")) {
      Picasso.with(this).load(currentUser.image).into(profilePhoto);
    }

    getFollowingList();
    setToHomeView();
  }

  /**
   * Override on back pressed to close drawer if open
   */
  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  /**
   * Navigation drawer on item selection that navigates to other activities / fragments depending
   * on the item the user selects
   *
   * @param item Menu item selected by the user
   * @return Boolean of the selection result
   */
  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    FragmentManager manager = getSupportFragmentManager();

    if (id == R.id.nav_home) {
      setToHomeView();
    } else if (id == R.id.nav_global_timeline) {
      Fragment fragment = new GlobalTimeLineFragment();
      setUserIdToFragment(fragment, currentUser._id);
      manager.beginTransaction().replace(R.id.homeFrame, fragment).addToBackStack(null).commit();
    } else if (id == R.id.nav_preferences) {
      startActivity(new Intent(this, PreferenceActivity.class));
    } else if (id == R.id.nav_settings) {
      Fragment fragment = new UpdateAccountFragment();
      manager.beginTransaction().replace(R.id.homeFrame, fragment).addToBackStack(null).commit();
    } else if (id == R.id.nav_search) {
      Fragment fragment = new SearchFragment();
      manager.beginTransaction().replace(R.id.homeFrame, fragment).addToBackStack(null).commit();
    } else if (id == R.id.nav_signout) {
      info("Signing out - clearing back stack & token");
      saveToken(this, null); // Save null token to that current token would be cleared
      startActivity(new Intent(this, Welcome.class)
          .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
      toastMessage(this, "Signing out");
    } else if (id == R.id.nav_map) {
      Fragment fragment = new MapsFragment();
      manager.beginTransaction().replace(R.id.homeFrame, fragment).addToBackStack(null).commit();
    }

    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  /**
   * Overides on activity result - Used after getting a profile picture from the select picture
   * intent and uploads and updates the current user's profile picture
   *
   * @param requestCode Request code for picture selection
   * @param resultCode  Result code
   * @param data        Data of the intent result
   */
  // TODO: Should use async task for image upload - currently may give timeout if image is large
  // https://stackoverflow.com/questions/39953457/how-to-upload-image-file-in-retrofit-2
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PICK_IMAGE) {
      File file = new File(getRealPathFromURI_API19(this, data.getData()));
      RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

      // MultipartBody.Part is used to send also the actual file name
      MultipartBody.Part body =
          MultipartBody.Part.createFormData("image", file.getName(), requestFile);
      Call<User> call = (Call<User>) app.tweetService.updateProfilePicture(currentUser._id, body);
      call.enqueue(new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
          currentUser = response.body();
          Picasso.with(HomeActivity.this).load(response.body().image).into(profilePhoto);
          toastMessage(HomeActivity.this, "Profile photo updated");
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
          toastMessage(HomeActivity.this, "Failed to update profile photo");
          info(t.toString());
        }
      });
    }
  }

  /**
   * Check for permission to read storage permission for picture selection
   * Ask for permission if don't have permission, otherwise select photo
   * https://developer.android.com/training/permissions/requesting.html
   */
  private void checkExternalStorageReadPermission() {
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
   * Called after asking for permissions - Used for checking was external storage read permission
   * was granted for picture selection
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
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  /**
   * Call the helper setGetPictureIntent to set intent to get a picture and starts activity for
   * result
   */
  public void selectImage() {
    startActivityForResult(Intent.createChooser(setGetPictureIntent(), "Select Picture"), PICK_IMAGE);
  }

  /**
   * Helper to get userId string to fragment to pass userId as argument to fragment
   *
   * @param fragment Fragment to pass userId to
   * @param userId   String of userId
   */
  public static void setUserIdToFragment(Fragment fragment, String userId) {
    Bundle args = new Bundle();
    args.putSerializable("userid", userId);
    fragment.setArguments(args);
  }

  /**
   * Helper to set return to home view by replacing the current home frame container with the
   * profile fragment of the current user
   */
  private void setToHomeView() {
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = new ProfileFragment();
    setUserIdToFragment(fragment, currentUser._id);
    manager.beginTransaction().replace(R.id.homeFrame, fragment).commit();
  }

  /**
   * Private on click listener for the user profile picture
   * Gives a dialog to the user to either update profile photo or delete profile photo
   */
  private View.OnClickListener ProfilePhotoListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      // https://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
      CharSequence colors[] = new CharSequence[]{"Update Profile Picture", "Delete Profile Picture"};

      AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
      builder.setTitle("Profile Picture Options");
      builder.setItems(colors, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          drawer.closeDrawer(GravityCompat.START);
          if (which == 0) {
            // User selection update profile photo - Check read external storage permission first
            checkExternalStorageReadPermission();
          } else {
            // User selects delete profile picture - make call by retrofit for deletion and update
            // profile picture to default
            Call<User> call = (Call<User>) app.tweetService.deleteProfilePicture(currentUser._id);
            call.enqueue(new Callback<User>() {
              @Override
              public void onResponse(Call<User> call, Response<User> response) {
                currentUser = response.body();
                profilePhoto.setImageResource(R.mipmap.ic_launcher_round);
                toastMessage(HomeActivity.this, "Profile photo deleted");
              }

              @Override
              public void onFailure(Call<User> call, Throwable t) {
                toastMessage(HomeActivity.this, "Failed to delete profile photo");
                info(t.toString());
              }
            });
          }
        }
      });
      builder.show();
    }
  };

  /**
   * Helper method to make call to get user's following list
   */
  private void getFollowingList() {
    Call<List<Follow>> call = (Call<List<Follow>>) app.tweetService.getFollowings(app.currentUser._id);
    call.enqueue(new Callback<List<Follow>>() {
      @Override
      public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
        app.followings = response.body();
        info("Home Activity: Got Followings");
      }

      @Override
      public void onFailure(Call<List<Follow>> call, Throwable t) {
        info(t.toString());
        info("Home Activity: Failed to get Followings");
      }
    });
  }
}
