package com.sinqupa.cliente.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sinqupa.cliente.R;
import com.sinqupa.cliente.presenter.HelpPresenterImpl;
import com.sinqupa.cliente.presenter.IHelpPresenter;

public class HelpFragment extends Fragment {
    IHelpPresenter helpPresenter = new HelpPresenterImpl();
    EditText txtSubjectHelp,txtDescriptionHelp;
    Button btnEnviarHelp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help,container,false);
        txtSubjectHelp = (EditText)view.findViewById(R.id.txtSubjectHelp);
        txtDescriptionHelp = (EditText)view.findViewById(R.id.txtDescriptionHelp);
        btnEnviarHelp = (Button)view.findViewById(R.id.btnEnviarHelp);
        btnEnviarHelp.setOnClickListener(sendListener);
        helpPresenter.getFragmentContext(view.getContext());
        return view;
    }


    Button.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            helpPresenter.sendEmail(txtSubjectHelp.getText().toString(),txtDescriptionHelp.getText().toString());
        }
    };
}
