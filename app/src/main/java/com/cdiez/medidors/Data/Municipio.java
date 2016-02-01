package com.cdiez.medidors.Data;

import com.cdiez.medidors.Other.ParseConstants;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Carlos Diez
 * on 2/1/16.
 */
public class Municipio extends ParseObject {

    public String getName() {
        return getString(ParseConstants.KEY_NAME);
    }

    public void setName(String name) {
        put(ParseConstants.KEY_NAME, name);
    }

    public Estado getEstado() {
        return (Estado) getParseObject(ParseConstants.KEY_ESTADO);
    }

    public void setEstado(Estado estado) {
        put(ParseConstants.KEY_ESTADO, estado);
    }

    public int getTarifaId() {
        return getNumber(ParseConstants.KEY_TARIFA).intValue();
    }

    public void setTarifaId(int tarifaId) {
        put(ParseConstants.KEY_TARIFA, tarifaId);
    }

    public static ParseQuery<Municipio> getQuery() {
        return ParseQuery.getQuery(Municipio.class);
    }
}
