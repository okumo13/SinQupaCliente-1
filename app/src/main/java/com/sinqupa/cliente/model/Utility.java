package com.sinqupa.cliente.model;

import android.Manifest;
import android.location.Location;
import android.net.Uri;

public class Utility {
    public static Location locationCustomer;
    public static int distance;
    public static Uri uriNotification;
    public static final String CHANNEL_ID = "NOTIFICACION";
    public static final int NOTIFICATION_ID = 1008;
    private static final String PACKAGE_NAME = "com.sinqupa.cliente";
    public static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +".started_from_notification";
    public static final int DEFAULT_TIMEOUT = 1000;
    public static final int DEFAULT_INDEX_SOUND = 0;
    public static final int DEFAULT_INDEX_DISTANCE = 0;

    public static final String[] PERMISSIONS = { Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 101;
    public static final String PERMISSION_TEXT = "Permisos Requeridos para SinQupa";
    public static final String TITLE_MARKER_CUSTOMER = "Mi Ubicacion";
    public static final String TITLE_MARKER_EMPLOYEE = "CARRO DE BASURA";
    //----------------- Variables para los Botones de las  Ventanas de Dialogo -----------------//
    public static final String ADD = "Agregar";
    public static final String CANCEL = "Cancelar";
    public static final String OK = "Ok";
    //----------------- Fin Variables para los Botones de las  Ventanas de Dialogo -----------------//


}
