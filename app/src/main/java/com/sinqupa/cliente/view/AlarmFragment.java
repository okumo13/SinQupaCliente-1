package com.sinqupa.cliente.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sinqupa.cliente.R;
import com.sinqupa.cliente.presenter.AlarmPresenterImpl;
import com.sinqupa.cliente.presenter.IAlarmPresenter;

public class AlarmFragment extends Fragment {
    private IAlarmPresenter alarmPresenter = new AlarmPresenterImpl();
    private LinearLayout lytSoundAlarm,lytDistanceAlarm,lytUbicationAlarm;
    private TextView lblSound,lblDistance;
    private Button btnSaveAlarm;
    private Switch swActiveAlarm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm,container,false);
        btnSaveAlarm = (Button)view.findViewById(R.id.btnSaveAlarm);
        lytSoundAlarm = (LinearLayout)view.findViewById(R.id.lytSoundAlarm);
        lytDistanceAlarm = (LinearLayout)view.findViewById(R.id.lytDistanceAlarm);
        lytUbicationAlarm = (LinearLayout)view.findViewById(R.id.lytUbicationAlarm);
        swActiveAlarm = (Switch)view.findViewById(R.id.swActiveAlarm);
        btnSaveAlarm.setOnClickListener(saveListener);
        lytSoundAlarm.setOnClickListener(soundListener);
        lytDistanceAlarm.setOnClickListener(distanceListener);
        lytUbicationAlarm.setOnClickListener(ubicationListener);
        lblSound = (TextView)view.findViewById(R.id.lblSound);
        lblDistance = (TextView)view.findViewById(R.id.lblDistance);
        swActiveAlarm.setOnClickListener(switchListener);
        alarmPresenter.getFragmentContext(container.getContext());
        alarmPresenter.loadDataAlarm();
        return view;
    }


    private Switch.OnClickListener switchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alarmPresenter.activeSwitch(view);
        }
    };

    private Button.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alarmPresenter.saveAlarm();
        }
    };

    private LinearLayout.OnClickListener soundListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alarmPresenter.viewSound();
        }
    };

    private LinearLayout.OnClickListener distanceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alarmPresenter.viewDistance();
        }
    };

    private LinearLayout.OnClickListener ubicationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alarmPresenter.viewUbication();
        }
    };
}
