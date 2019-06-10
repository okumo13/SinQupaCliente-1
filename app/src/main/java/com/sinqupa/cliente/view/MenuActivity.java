package com.sinqupa.cliente.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.sinqupa.cliente.R;
import com.sinqupa.cliente.presenter.IMenuPresenter;
import com.sinqupa.cliente.presenter.MenuPresenterImpl;

public class MenuActivity extends AppCompatActivity {
    private IMenuPresenter menuPresenter = new MenuPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menuPresenter.getActivityContext(this);
        menuPresenter.defaultMenu();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuPresenter.navigation(item);
            return true;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        menuPresenter.permissionsResult(requestCode,permissions,grantResults);
    }
}
