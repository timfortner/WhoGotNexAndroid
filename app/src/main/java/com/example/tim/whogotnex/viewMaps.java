package com.example.tim.whogotnex;

        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.text.InputType;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Toast;
        import android.support.v4.content.ContextCompat;
        import android.app.AlertDialog;
        import android.content.DialogInterface;

        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

        import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
        import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class viewMaps extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    //Our Map
    private GoogleMap mMap;

    /**
     * Id to identity permission requests.
     */
    private static final int REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 0;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    //Buttons
    private ImageButton buttonSave;
    private ImageButton buttonCurrent;
    private ImageButton buttonView;
    private ImageButton buttonSignOut;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //map marker
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Initializing views and adding onclick listeners
        buttonSave = (ImageButton) findViewById(R.id.buttonSave);
        buttonCurrent = (ImageButton) findViewById(R.id.buttonCurrent);
        buttonView = (ImageButton) findViewById(R.id.buttonView);
        buttonSignOut = (ImageButton) findViewById(R.id.buttonSignOut);
        buttonSave.setOnClickListener(this);
        buttonCurrent.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation() {

        if (!mayRequestCourseLocation())
        {
            return;
        }

        else if(!mayRequestFineLocation())
        {
            return;
        }
        else if(mayRequestFineLocation() == true && mayRequestCourseLocation() == true)
        {
            //Creating a location object
            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    //moving the map to location
                    moveMap();
                }
            }
            catch(SecurityException e) {
                getCurrentLocation();
            }

        }
    }

    private boolean mayRequestCourseLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
        }
        return false;
    }
    private boolean mayRequestFineLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }




    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Displaying current coordinates in toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(41.38, 83.63);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerClickListener(this);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(47.1,52.6))
                .title("BGSU Rec")
                .snippet("Recenter Court")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(myMarker))
        {
            Intent intent = new Intent(viewMaps.this,createGame.class);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        if(v == buttonCurrent){
            getCurrentLocation();
            moveMap();
        }
        else if(v == buttonSave) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            final AlertDialog.Builder builder1 = new AlertDialog.Builder(viewMaps.this);
            builder1.setMessage("Would you like to register this court?\n" + "(You must be at the court to register it)\n" + latitude + ", " + longitude);
            builder1.setCancelable(true);
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder1.setView(input);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            builder1.setMessage("You have sent a registration request!");
                            //TODO: place code here to send lat and long to our database
                            //
                            //
                            //
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else if(v == buttonView){
            /* TODO: once we get a database of courts we can pull locations from them*/

            /*
            ArrayList<MarkerData> markersArray = new ArrayList<MarkerData>();

            for(int i = 0 ; i < markersArray.size() ; i++ ) {

                createMarker(markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getTitle(), markersArray.get(i).getSnippet(), markersArray.get(i).getIconResID());
            }

            protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

                return googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .anchor(0.5f, 0.5f)
                        .title(title)
                        .snippet(snippet);
            .icon(BitmapDescriptorFactory.fromResource(iconResID)));
            }*/
        }
        else if(v == buttonSignOut)
        {
            //signout button , it will take you to initial loginActivity
            Intent intent = new Intent(viewMaps.this,homePage.class);
            startActivity(intent);
        }

    }
}
