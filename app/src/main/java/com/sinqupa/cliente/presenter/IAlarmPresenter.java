package com.sinqupa.cliente.presenter;

import android.content.Context;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.sinqupa.cliente.model.Alarm;
import com.sinqupa.cliente.model.DistanceObject;
import com.sinqupa.cliente.model.SoundObject;
import java.util.ArrayList;
import java.util.List;

public interface IAlarmPresenter {
    void getFragmentContext(Context context);
    boolean checkPermissions();
    void viewSound();
    void viewDistance();
    void viewUbication();
    void loadMap();
    void defaultUbication(GoogleMap googleMap);
    void isPlayingRingtone();
    void onDestroyViewUbication();
    ArrayList<DistanceObject> loadListDistance();
    ArrayList<SoundObject> loadListSound();
    void startService();
    void stopService();
    boolean existsAlarm();
    List<Alarm> getDataFromSharedPreferences();
    void setDataFromSharedPreferences(List<Alarm> list);
    void chooseUbication(GoogleMap googleMap);
    void saveAlarm();
    boolean doSaveAlarm(Alarm alarm);
    boolean validateAlarm(Alarm alarm);
    void activeSwitch(View view);
    void loadDataAlarm();
}
