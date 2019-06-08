package com.sinqupa.cliente.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import com.sinqupa.cliente.R;
import com.sinqupa.cliente.model.Utility;
import com.sinqupa.cliente.view.AlarmFragment;
import com.sinqupa.cliente.view.HelpFragment;
import com.sinqupa.cliente.view.MapFragment;
import java.util.ArrayList;
import java.util.List;

public class MenuPresenterImpl implements IMenuPresenter {
    private Context context;
    @Override
    public void getActivityContext(Context context) {
        this.context = context;
    }

    @Override
    public void defaultMenu() {
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AlarmFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions())
                requestPermissions();
        }
    }

    @Override
    public void navigation(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_alarm:
                selectedFragment = new AlarmFragment();
                break;
            case R.id.navigation_map:
                selectedFragment = new MapFragment();
                break;
            case R.id.navigation_help:
                selectedFragment = new HelpFragment();
                break;
        }
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
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
    public void requestPermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : Utility.PERMISSIONS) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                remainingPermissions.add(permission);
        }
        ((Activity)context).requestPermissions( remainingPermissions.toArray(new String[remainingPermissions.size()]), Utility.REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void permissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Utility.REQUEST_PERMISSIONS_REQUEST_CODE){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    if(((Activity)context).shouldShowRequestPermissionRationale(permissions[i])){
                        Snackbar snackbar = Snackbar.make(((Activity)context).findViewById(R.id.fragment_container),Utility.PERMISSION_TEXT, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                        snackbar.setAction(Utility.OK, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestPermissions();
                            }
                        });
                        snackbar.show();
                    }
                    return;
                }
            }
        }
    }
}
