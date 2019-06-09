package com.sinqupa.cliente.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sinqupa.cliente.model.Employee;
import com.sinqupa.cliente.model.Utility;

import java.util.ArrayList;

public class MapPresenterImpl implements IMapPresenter{
    private Context context;
    private DatabaseReference databaseReference;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();

    @Override
    public void getFragmentContext(Context context) {
        this.context = context;
    }

    @Override
    public void instanceFirebaseDataBase(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Override
    public void loadMap(GoogleMap googleMap) {
        GoogleMap map = googleMap;
        if (map != null) {
            defaultUbication(map);
            loadEmployee(map);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void defaultUbication(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        FusedLocationProviderClient mFusedLocationClient =  LocationServices.getFusedLocationProviderClient((FragmentActivity)context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener((FragmentActivity)context, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng originPosition = new  LatLng(location.getLatitude(),location.getLongitude());
                MarkerOptions markerOptionsDestination = new MarkerOptions();
                markerOptionsDestination.position(originPosition);
                //markerOptionsDestination.title(Utility.TITLE_MARKER_EMPLOYEE);
                //markerOptionsDestination.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_house));
                //mDestinationMarker = mGoogleMap.addMarker(markerOptionsDestination);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(originPosition).zoom(19).bearing(45).tilt(70).build();
                CameraUpdate zoomCam = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(zoomCam);
            }
        }).addOnFailureListener((FragmentActivity)context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void loadEmployee(final GoogleMap googleMap) {
        databaseReference.child("Employee").orderByChild("activated").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker : realTimeMarkers){
                    marker.remove();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Employee employee = snapshot.getValue(Employee.class);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(employee.getLatitudeTravel(),employee.getLongitudeTravel()));
                    tmpRealTimeMarkers.add(googleMap.addMarker(markerOptions));
                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
