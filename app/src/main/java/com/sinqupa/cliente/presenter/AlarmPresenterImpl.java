package com.sinqupa.cliente.presenter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;
import com.sinqupa.cliente.R;
import com.sinqupa.cliente.model.Alarm;
import com.sinqupa.cliente.model.DistanceObject;
import com.sinqupa.cliente.model.SoundObject;
import com.sinqupa.cliente.model.Utility;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AlarmPresenterImpl implements IAlarmPresenter {
    private Context context;
    private Integer soundPosition = Utility.DEFAULT_INDEX_SOUND ,newSoundPosition = Utility.DEFAULT_INDEX_SOUND ,distancePosition = Utility.DEFAULT_INDEX_DISTANCE ,newDistancePosition = Utility.DEFAULT_INDEX_DISTANCE;
    private Alarm alarm = new Alarm();
    private MediaPlayer md;
    private SupportMapFragment mSupportMapFragment;
    private static final String PREFS_TAG = "SharedPrefs";
    private static final String ALARM_TAG = "MyAlarm";
    private Marker mDestinationMarker;
    private LatLng chooseDestination;

    @Override
    public void getFragmentContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean checkPermissions() {
        for(String permission : Utility.PERMISSIONS){
            if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void viewSound() {
        LayoutInflater inflater = ((FragmentActivity)context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_sound, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(dialogLayout);

        ListView listViewSound = (ListView) dialogLayout.findViewById(R.id.lvSound);

        final ArrayAdapter<SoundObject> adapterSound = new ArrayAdapter<>(context, android.R.layout.simple_list_item_checked, loadListSound());
        listViewSound.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewSound.setAdapter(adapterSound);

        if (existsAlarm()) {
            for (int i = 0; i < listViewSound.getAdapter().getCount(); i++ ){
                if (alarm.getSound().equals(adapterSound.getItem(i).getText())) {
                    listViewSound.setItemChecked(i, true);
                    break;
                }
            }
        } else {
            listViewSound.setItemChecked(newSoundPosition, true);
        }
        listViewSound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                isPlayingRingtone();
                md = MediaPlayer.create(context, adapterSound.getItem(position).getUri());
                md.start();
                soundPosition = position;
            }
        });
        alertDialog.setPositiveButton(Utility.ADD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newSoundPosition = soundPosition;
                alarm.setSound(adapterSound.getItem(newSoundPosition).getText());
                alarm.setUri(adapterSound.getItem(newSoundPosition).getUri());
                //lblSound.setText(alarm.getSound());
                isPlayingRingtone();
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton(Utility.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isPlayingRingtone();
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialogSound = alertDialog.create();
        dialogSound.show();
    }

    @Override
    public void viewDistance() {
        LayoutInflater inflater = ((FragmentActivity)context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_distance, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(dialogLayout);
        ListView listViewDistance = (ListView) dialogLayout.findViewById(R.id.lvDistance);
        final ArrayAdapter<DistanceObject> adapterDistance = new ArrayAdapter<>(context, android.R.layout.simple_list_item_checked, loadListDistance());
        listViewDistance.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewDistance.setAdapter(adapterDistance);
        if (existsAlarm()) {
            for (int i = 0; i < listViewDistance.getAdapter().getCount(); i++ ){
                if (alarm.getDistance() == (adapterDistance.getItem(i).getValue())) {
                    listViewDistance.setItemChecked(i, true);
                    break;
                }
            }
        } else {
            listViewDistance.setItemChecked(newDistancePosition, true);
        }

        listViewDistance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                distancePosition = position;
            }
        });

        alertDialog.setPositiveButton(Utility.ADD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newDistancePosition = distancePosition;
                alarm.setDistance(adapterDistance.getItem(newDistancePosition).getValue());
                //lblDistance.setText(String.valueOf(alarm.getDistance()) + " Metros");
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton(Utility.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialogRepeat = alertDialog.create();
        dialogRepeat.show();
    }

    @Override
    public ArrayList<DistanceObject> loadListDistance() {
        ArrayList<DistanceObject> list = new ArrayList<>();
        list.add(new DistanceObject("5 Metros",5));
        list.add(new DistanceObject("10 Metros",10));
        list.add(new DistanceObject("15 Metros",15));
        list.add(new DistanceObject("20 Metros",20));
        list.add(new DistanceObject("25 Metros",25));
        list.add(new DistanceObject("30 Metros",30));
        return  list;
    }

    @Override
    public ArrayList<SoundObject> loadListSound() {
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        final Cursor alarmsCursor = ringtoneManager.getCursor();
        ArrayList<SoundObject> list = new ArrayList<>();
        SoundObject soundObject;
        int index = 0;
        while (alarmsCursor.moveToNext()) {
            soundObject = new SoundObject();
            soundObject.setText(ringtoneManager.getRingtone(index).getTitle(context));
            soundObject.setUri(ringtoneManager.getRingtoneUri(index));
            list.add(soundObject);
            index++;
        }
        alarmsCursor.close();
        return list;
    }

    @Override
    public void viewUbication() {
        if (!checkPermissions()) {
            TastyToast.makeText(context, "Habilitar Permisos", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return;
        }
        LayoutInflater inflater = ((FragmentActivity)context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_ubication, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(dialogLayout);
        mSupportMapFragment = (SupportMapFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.map);// getChildFragmentManager().findFragmentById(R.id.map);
        loadMap();
        alertDialog.setPositiveButton(Utility.ADD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alarm.setLatitude(String.valueOf(chooseDestination.latitude));
                alarm.setLongitude(String.valueOf(chooseDestination.longitude));
                onDestroyViewUbication();
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton(Utility.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onDestroyViewUbication();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialogMap = alertDialog.create();
        dialogMap.show();
    }

    @Override
    public void loadMap() {
        mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                GoogleMap map = googleMap;
                if (map != null){
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setMyLocationEnabled(true);
                    if (existsAlarm()){
                        //Cargamos el marcador con la Ubicación
                        //LatLng destinationPosition = new LatLng(Double.parseDouble(alarm.getLatitude()),Double.parseDouble(alarm.getLongitude()));
                        //MarkerOptions markerOptionsDestination = new MarkerOptions();
                        //markerOptionsDestination.position(destinationPosition);
                        //markerOptionsDestination.title(Utility.TITLE_MARKER_CUSTOMER);
                        //markerOptionsDestination.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_house));
                        //mDestinationMarker = map.addMarker(markerOptionsDestination);
                        //CameraPosition cameraPosition = new CameraPosition.Builder().target(destinationPosition).zoom(19).bearing(45).tilt(70).build();
                        //CameraUpdate zoomCam = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        //map.animateCamera(zoomCam);
                    }else {
                        defaultUbication(map);
                    }
                    chooseUbication(map);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void defaultUbication(final GoogleMap googleMap) {
        FusedLocationProviderClient mFusedLocationClient =  LocationServices.getFusedLocationProviderClient((FragmentActivity)context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener((FragmentActivity)context, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng originPosition = new  LatLng(location.getLatitude(),location.getLongitude());
                chooseDestination = originPosition;
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
    public void onDestroyViewUbication() {
        if (mSupportMapFragment != null)
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().remove(mSupportMapFragment).commit();
    }

    @Override
    public void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, LocationUpdatesService.class));
        }
        context.startService(new Intent(context, LocationUpdatesService.class));
        TastyToast.makeText(context, "Aplicacion Iniciada", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
    }

    @Override
    public void stopService() {
        context.stopService(new Intent(context, LocationUpdatesService.class));
    }

    @Override
    public String loadTextDistance() {
        return String.valueOf(alarm.getDistance()) + " Metros";
    }

    @Override
    public boolean existsAlarm() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ALARM_TAG);
    }

    @Override
    public List<Alarm> getDataFromSharedPreferences() {
        Gson gson = new Gson();
        List<Alarm> alarmFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(ALARM_TAG, "");
        Type type = new TypeToken<List<Alarm>>() {}.getType();
        alarmFromShared = gson.fromJson(jsonPreferences, type);
        return alarmFromShared;
    }

    @Override
    public void setDataFromSharedPreferences(Alarm alarm) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(alarm);
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ALARM_TAG, jsonCurProduct);
        editor.commit();

    }

    @Override
    public void chooseUbication(final GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng destinationPosition) {
                if (mDestinationMarker != null)
                    mDestinationMarker.remove();
                chooseDestination = destinationPosition;
                //Colocamos el Marcador con la Ubicación
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(destinationPosition);
                markerOptions.title(Utility.TITLE_MARKER_CUSTOMER);
                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_house));
                mDestinationMarker = googleMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void saveAlarm() {
        if (existsAlarm()){
            setDataFromSharedPreferences(alarm);
            TastyToast.makeText(context, "Datos Guardados", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }else {
            if (validateAlarm(alarm)){
                if (doSaveAlarm(alarm)){
                    TastyToast.makeText(context, "Datos Guardados", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }else {
                    TastyToast.makeText(context, "No se pudo guardar", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }else {
                TastyToast.makeText(context, "Ingresar todos los datos", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
            }
        }
    }

    @Override
    public boolean doSaveAlarm(Alarm alarm) {
        setDataFromSharedPreferences(alarm);
        return existsAlarm();
    }

    @Override
    public boolean validateAlarm(Alarm alarm) {
        boolean result = true;
        if (alarm.getDistance() == null || alarm.getLatitude()== null  || alarm.getSound() == null || alarm.getUri() == null || alarm.getLongitude() == null){
            result = false;
        }
        return  result;
    }

    @Override
    public void activeSwitch(View view) {
        if (existsAlarm()){
            if (((Switch)view).isChecked()){
                startService();
            }else {
                stopService();
            }
        }else {
            if (((Switch)view).isChecked()){
                TastyToast.makeText(context, "Guardar todos los datos", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                ((Switch)view).setChecked(false);
            }
        }
    }

    @Override
    public void isPlayingRingtone() {
        if (md != null) {
            if (md.isPlaying())
                md.stop();
            md = null;
        }
    }
}
