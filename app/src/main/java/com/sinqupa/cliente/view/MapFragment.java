package com.sinqupa.cliente.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.FirebaseDatabase;
import com.sinqupa.cliente.R;
import com.sinqupa.cliente.presenter.IMapPresenter;
import com.sinqupa.cliente.presenter.MapPresenterImpl;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    IMapPresenter mapPresenter = new MapPresenterImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.viewMap);
        mapFragment.getMapAsync(this);
        mapPresenter.getFragmentContext(container.getContext());
        mapPresenter.instanceFirebaseDataBase(FirebaseDatabase.getInstance().getReference());
        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
       mapPresenter.loadMap(googleMap);
    }
}
