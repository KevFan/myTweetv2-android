package kevin.mytweet.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

public class MapsFragment extends SupportMapFragment implements
    GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener,
    OnMapReadyCallback {

  private LocationRequest mLocationRequest;
  private FusedLocationProviderClient mFusedLocationClient;
  private LocationCallback mLocationCallback;
  private long UPDATE_INTERVAL = 5000; /* 5 secs */
  private long FASTEST_INTERVAL = 1000; /* 1 sec */
  private GoogleMap mMap;
  private float zoom = 13f;

  public MyTweetApp app = MyTweetApp.getApp();

  private static final int PERMISSION_REQUEST_CODE = 200;

  private final int[] MAP_TYPES = {
      GoogleMap.MAP_TYPE_SATELLITE,
      GoogleMap.MAP_TYPE_NORMAL,
      GoogleMap.MAP_TYPE_HYBRID,
      GoogleMap.MAP_TYPE_TERRAIN,
      GoogleMap.MAP_TYPE_NONE
  };

  private int curMapTypeIndex = 1;

  /**
   * Called when fragment is first created
   *
   * @param savedInstanceState Bundle with saved data if any
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    info("Map fragment created");
    super.onCreate(savedInstanceState);
    try {
      mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
      createLocationCallback();
      createLocationRequest();
    } catch (SecurityException se) {
      toastMessage(getActivity(), "Check Your Permissions");
    }
  }

  /**
   * Called after the view is created
   *
   * @param view               View
   * @param savedInstanceState Bundle
   */
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
  }

  /**
   * Call back interface for tap events on a marker's info window info - unused
   *
   * @param marker Marker
   */
  @Override
  public void onInfoWindowClick(Marker marker) {
  }

  /**
   * Call back interface for tap events on a map - unused
   *
   * @param latLng Latitude and longitude of click
   */
  @Override
  public void onMapClick(LatLng latLng) {
  }

  /**
   * Called when a marker has been clicked or tapped.
   *
   * @param marker Marker clicked
   * @return Boolean of click event
   */
  @Override
  public boolean onMarkerClick(Marker marker) {
    return false;
  }

  /**
   * Called when the map is ready to be use - sets the map type, initialise listeners and set up the
   * map up
   *
   * @param googleMap Google map object
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setMapType(MAP_TYPES[curMapTypeIndex]);

    initListeners();
    if (checkPermission()) {
      startLocationUpdates();
      mMap.setMyLocationEnabled(true);
      initCamera(app.mCurrentLocation);
    } else if (!checkPermission()) {
      requestPermission();
    }
    mMap.getUiSettings().setMapToolbarEnabled(true);
    mMap.getUiSettings().setCompassEnabled(true);
    mMap.getUiSettings().setMyLocationButtonEnabled(true);
    mMap.getUiSettings().setAllGesturesEnabled(true);
    mMap.setTrafficEnabled(true);
    mMap.setBuildingsEnabled(true);
    mMap.getUiSettings().setZoomControlsEnabled(true);
  }

  /**
   * Checks whether app has fine location and camera permission for map usage
   * http://www.journaldev.com/10409/android-handling-runtime-permissions-example
   *
   * @return Boolean of whether permission are granted or not
   */
  private boolean checkPermission() {
    info("check permission");
    int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
    int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

    return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Request permissions for fine location and camera
   */
  private void requestPermission() {
    info("request premission");
    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
        PERMISSION_REQUEST_CODE);
  }

  /**
   * On result of permission request for fine location and camera access
   *
   * @param requestCode  Permission request code
   * @param permissions  Permissions
   * @param grantResults Permission grant results
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    info("on request permission result");
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0) {

          boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
          boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

          if (locationAccepted && cameraAccepted) {
            Snackbar.make(getView(), "Permission Granted, Now you can access location data and camera.",
                Snackbar.LENGTH_LONG).show();
            if (checkPermission())
              mMap.setMyLocationEnabled(true);
            startLocationUpdates();
          } else {

            Snackbar.make(getView(), "Permission Denied, You cannot access location data and camera.",
                Snackbar.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel("You need to allow access to both the permissions",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                              PERMISSION_REQUEST_CODE);
                        }
                      }
                    });
                return;
              }
            }
          }
        }
        break;
    }
  }

  /**
   * Dialog to tell user that user must all access for permissions for map usage
   *
   * @param message    Message for dialog
   * @param okListener Positive button click listener
   */
  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(getActivity())
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }


  /**
   * Create location requests for mapping device location
   */
  private void createLocationRequest() {
    info("map - create location request");
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
  }

  /**
   * Creates a callback for receiving location events
   */
  private void createLocationCallback() {
    mLocationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        app.mCurrentLocation = locationResult.getLastLocation();
        initCamera(app.mCurrentLocation);
      }
    };
    info("map - create location callback");
  }

  /**
   * Set listeners to map
   */
  public void initListeners() {
    info("map - set listeners");
    mMap.setOnMarkerClickListener(this);
    mMap.setOnInfoWindowClickListener(this);
    mMap.setOnMapClickListener(this);
  }

  /**
   * On resume of fragment lifecycle - get all tweets of user tweets of user, check for location and
   * camera access and find current location
   */
  @Override
  public void onResume() {
    info("on resume");
    super.onResume();
    getMapAsync(this);

    Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserTweets(app.currentUser._id);
    call.enqueue(new Callback<List<Tweet>>() {
      @Override
      public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
        info("map - got all tweets");
        addTweets(response.body());
      }

      @Override
      public void onFailure(Call<List<Tweet>> call, Throwable t) {
        info(t.toString());
        info("map - failed getting tweets");
      }
    });
    if (checkPermission()) {
      if (app.mCurrentLocation != null) {
        toastMessage(getActivity(), "GPS location was found!");
      } else {
        toastMessage(getActivity(), "Current location was null, Setting Default Values!");
        app.mCurrentLocation = new Location("Waterford City Default (WIT)");
        app.mCurrentLocation.setLatitude(52.2462);
        app.mCurrentLocation.setLongitude(-7.1202);
      }
      if (mMap != null) {
        initCamera(app.mCurrentLocation);
        mMap.setMyLocationEnabled(true);
      } else {
        info("mMap is null");
      }
    } else if (!checkPermission()) {
      requestPermission();
    }

  }

  /**
   * Move camera to current device location on map
   *
   * @param location Location of device
   */
  private void initCamera(Location location) {
    info("map - init camera");
    if (zoom != 13f && zoom != mMap.getCameraPosition().zoom)
      zoom = mMap.getCameraPosition().zoom;

    CameraPosition position = CameraPosition.builder()
        .target(new LatLng(location.getLatitude(),
            location.getLongitude()))
        .zoom(zoom).bearing(0.0f)
        .tilt(0.0f).build();

    mMap.animateCamera(CameraUpdateFactory
        .newCameraPosition(position), null);
  }

  /**
   * Starts call backs for receiving device location updates
   */
  public void startLocationUpdates() {
    info("map - start location update");
    try {
      mFusedLocationClient.requestLocationUpdates(mLocationRequest,
          mLocationCallback, Looper.myLooper());
    } catch (SecurityException se) {
      toastMessage(getActivity(), "Check Your Permissions on Location Updates");
    }
  }

  /**
   * Plot tweet list markers onto map
   *
   * @param list List of tweets
   */
  public void addTweets(List<Tweet> list) {
    for (Tweet tweet : list) {
      // Don't plot tweet if LatLong is (0,0) - Generated by default be server if no marker is passed
      if ((tweet.marker.coords.latitude != 0) && tweet.marker.coords.longitude != 0) {
        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(tweet.marker.coords.latitude, tweet.marker.coords.longitude))
            .title(tweet.tweetDate.toString())
            .snippet(tweet.tweetText)
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
      }
    }
  }
}
