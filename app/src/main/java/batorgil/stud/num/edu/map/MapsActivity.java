package batorgil.stud.num.edu.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private int mapType = 1;
    public ArrayList<LatLng> tracer;
    public ArrayList<Location> myLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tracer = new ArrayList<>();
        myLocationList = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        mMap.setMyLocationEnabled(true);
        mMap.setMapType(mapType);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.remove();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optional, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                break;
            case R.id.hybrid:
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                break;
            case R.id.satellite:
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                break;
            case R.id.terrain:
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            case R.id.none:
                mapType = GoogleMap.MAP_TYPE_NONE;
                break;
            case R.id.draw:
                mMap.setOnMyLocationChangeListener(this);
                break;
            case R.id.pointDistance:
                calc();
                break;
        }
        mapFragment.getMapAsync(this);
        return true;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        tracer.add(point);
        mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(point)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
                )
        );

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        mMap.clear();
        myLocationList.add(location);

        Log.e("LOCATION CHANGE", location.getLatitude() +" " + location.getLongitude());

        PolylineOptions p = new PolylineOptions().width(5).color(Color.GREEN).geodesic(true);
        if(myLocationList.size() != 1 || myLocationList.size() != 0 ){
            for (int i = 1;  i<myLocationList.size(); i++){
                p = p.add(new LatLng(myLocationList.get(i-1).getLatitude(), myLocationList.get(i-1).getLongitude())
                        , new LatLng(myLocationList.get(i).getLatitude(), myLocationList.get(i).getLongitude()));
            }
        }

        Polyline polyline = mMap.addPolyline(p);

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    public void calc(){
        LatLng last = tracer.get(tracer.size() - 1);
        LatLng lastBefore = tracer.get(tracer.size() - 2);

        float[] results = new float[1];
        Location.distanceBetween(lastBefore.latitude, lastBefore.longitude, last.latitude, last.longitude, results);
        Toast.makeText(this,"Хоорондын зай: " + results[0], Toast.LENGTH_SHORT).show();
    }

}
