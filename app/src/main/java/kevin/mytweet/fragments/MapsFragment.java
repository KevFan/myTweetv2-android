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
  private List<Tweet> mTweetList;
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

  public static MapsFragment newInstance() {
    MapsFragment fragment = new MapsFragment();
    return fragment;
  }

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

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);

//    app.mGoogleApiClient.registerConnectionCallbacks(this);
  }

  @Override
  public void onInfoWindowClick(Marker marker) {

  }

  @Override
  public void onMapClick(LatLng latLng) {

  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    return false;
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setMapType(MAP_TYPES[curMapTypeIndex]);

    initListeners();
    if (checkPermission()) {
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

  //http://www.journaldev.com/10409/android-handling-runtime-permissions-example
  private boolean checkPermission() {
    info("check permission");
    int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
    int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

    return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    info("request premission");
    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
        PERMISSION_REQUEST_CODE);
  }

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

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(getActivity())
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }


  private void createLocationRequest() {
    info("create location request");
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
  }

  /* Creates a callback for receiving location events.*/
  private void createLocationCallback() {
    mLocationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        app.mCurrentLocation = locationResult.getLastLocation();
        initCamera(app.mCurrentLocation);
      }
    };
    info("create location callback");
  }

  public void initListeners() {
    info("set listeners");
    mMap.setOnMarkerClickListener(this);
    mMap.setOnInfoWindowClickListener(this);
    mMap.setOnMapClickListener(this);
  }

  @Override
  public void onResume() {
    info("on resume");
    super.onResume();
    getMapAsync(this);

    Call<List<Tweet>> call = (Call<List<Tweet>>) app.tweetService.getAllUserTweets(app.currentUser._id);
    call.enqueue(new Callback<List<Tweet>>() {
      @Override
      public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
        info("got all tweets");
        addTweets(response.body());
      }

      @Override
      public void onFailure(Call<List<Tweet>> call, Throwable t) {
        info(t.toString());
        info("i have failed");
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

  private void initCamera(Location location) {
    info("init camera");
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

  public void startLocationUpdates() {
    info("start loaction upfate");
    try {
      mFusedLocationClient.requestLocationUpdates(mLocationRequest,
          mLocationCallback, Looper.myLooper());
    } catch (SecurityException se) {
      toastMessage(getActivity(), "Check Your Permissions on Location Updates");
    }
  }

  public void addTweets(List<Tweet> list){
    for(Tweet tweet : list)
      mMap.addMarker(new MarkerOptions()
          .position(new LatLng(tweet.marker.coords.latitude, tweet.marker.coords.longitude))
          .title(tweet.tweetDate.toString())
          .snippet(tweet.tweetText)
          .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
  }
}
