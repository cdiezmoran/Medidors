package com.cdiez.medidors.Data;

import com.cdiez.medidors.Other.ParseConstants;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Carlos Diez
 * on 08/02/2016.
 */
@ParseClassName(ParseConstants.CLASS_TARIFAS)
public class Tarifa extends ParseObject{

    public String getName() {
        return getString(ParseConstants.KEY_NAME);
    }

    public void setName(String name) {
        put(ParseConstants.KEY_NAME, name);
    }

    public float getCargoFijo() {
        return getNumber(ParseConstants.KEY_CARGO_FIJO).floatValue();
    }

    public void setCargoFijo(float cargoFijo) {
        put(ParseConstants.KEY_CARGO_FIJO, cargoFijo);
    }

    public float getPrimerCargo() {
        return getNumber(ParseConstants.KEY_PRIMER_CARGO).floatValue();
    }

    public void setPrimerCargo(float primerCargo) {
        put(ParseConstants.KEY_PRIMER_CARGO, primerCargo);
    }

    public float getSegundoCargo() {
        return getNumber(ParseConstants.KEY_SEGUNDO_CARGO).floatValue();
    }

    public void setSegundoCargo(float segundoCargo) {
        put(ParseConstants.KEY_SEGUNDO_CARGO, segundoCargo);
    }

    public float getExcedente() {
        return getNumber(ParseConstants.KEY_EXCEDENTE).floatValue();
    }

    public void setExcedente(float excedente) {
        put(ParseConstants.KEY_EXCEDENTE, excedente);
    }

    public int getPrimerMaximo() {
        return getNumber(ParseConstants.KEY_PRIMER_MAXIMO).intValue();
    }

    public void setPrimerMaximo(int primerMaximo) {
        put(ParseConstants.KEY_PRIMER_MAXIMO, primerMaximo);
    }

    public int getSegundoMaximo() {
        return getNumber(ParseConstants.KEY_SEGUNDO_MAXIMO).intValue();
    }

    public void setSegundoMaximo(int segundoMaximo) {
        put(ParseConstants.KEY_SEGUNDO_MAXIMO, segundoMaximo);
    }

    public static ParseQuery<Tarifa> getQuery() {
        return ParseQuery.getQuery(Tarifa.class);
    }
}
