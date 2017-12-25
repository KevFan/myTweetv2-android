package kevin.mytweet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.fragments.GlobalTimeLineFragment;
import kevin.mytweet.fragments.TimeLineFragment;
import kevin.mytweet.fragments.UpdateAccountFragment;
import kevin.mytweet.models.User;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  public User currentUser = MyTweetApp.getApp().currentUser;
  public ImageView profilePhoto;

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
}
