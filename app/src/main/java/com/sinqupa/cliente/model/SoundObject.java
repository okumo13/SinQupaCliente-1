package com.sinqupa.cliente.model;

import android.net.Uri;

public class SoundObject {
    private String text;
    private String uri;

    public SoundObject() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString()
    {
        return text == null ? " " : text;
    }
}
