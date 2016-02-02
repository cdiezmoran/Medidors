package com.cdiez.medidors.Data;

import android.net.Uri;

import com.cdiez.medidors.Other.ParseConstants;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */

@ParseClassName(ParseConstants.CLASS_LECTURAS)
public class Lectura extends ParseObject{

    public int getLectura() {
        return getNumber(ParseConstants.KEY_LECTURA).intValue();
    }

    public void setLectura(int lectura) {
        put(ParseConstants.KEY_LECTURA, lectura);
    }

    public Date getFechaLectura() {
        return getDate(ParseConstants.KEY_FECHA_LECTURA);
    }

    public void setFechaLectura(Date fechaLectura) {
        put(ParseConstants.KEY_FECHA_LECTURA, fechaLectura);
    }

    public int getLecturaAnterior() {
        return getNumber(ParseConstants.KEY_LECTURA_ANTERIOR).intValue();
    }

    public void setLecturaAnterior(int lecturaAnterior) {
        put(ParseConstants.KEY_LECTURA_ANTERIOR, lecturaAnterior);
    }

    public float getCosto() {
        return getNumber(ParseConstants.KEY_COSTO).floatValue();
    }

    public void setCosto(float costo) {
        put(ParseConstants.KEY_COSTO, costo);
    }

    public String getImageString() {
        ParseFile file = getParseFile(ParseConstants.KEY_IMAGE);
        return Uri.parse(file.getUrl()).toString();
    }

    public static ParseQuery<Lectura> getQuery() {
        return ParseQuery.getQuery(Lectura.class);
    }
}