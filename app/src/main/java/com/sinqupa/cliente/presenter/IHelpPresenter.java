package com.sinqupa.cliente.presenter;

import android.content.Context;

public interface IHelpPresenter {
    void getFragmentContext(Context context);
    void sendEmail(String subject,String description);
}
