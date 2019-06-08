package com.sinqupa.cliente.presenter;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DatabaseReference;

public interface IMapPresenter {
    void getFragmentContext(Context context);
    void instanceFirebaseDataBase(DatabaseReference databaseReference);
    void loadMap(GoogleMap googleMap);
    void defaultUbication(GoogleMap googleMap);
    void loadEmployee(GoogleMap googleMap);
}
