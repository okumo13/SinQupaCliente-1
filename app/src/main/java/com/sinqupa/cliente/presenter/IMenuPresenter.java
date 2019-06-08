package com.sinqupa.cliente.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;

public interface IMenuPresenter {
    void getActivityContext(Context context);
    void defaultMenu();
    void navigation(@NonNull MenuItem item);
    boolean checkPermissions();
    void requestPermissions();
    void permissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
