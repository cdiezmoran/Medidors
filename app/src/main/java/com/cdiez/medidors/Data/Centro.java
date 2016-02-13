package com.cdiez.medidors.Data;

import com.cdiez.medidors.Other.ParseConstants;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Carlos Diez
 * on 11/02/2016.
 */
@ParseClassName(ParseConstants.CLASS_CENTROS)
public class Centro extends ParseObject {

    public String getName() {
        return getString(ParseConstants.KEY_NAME);
    }

    public void setName(String name) {
        put(ParseConstants.KEY_NAME, name);
    }

    public String getAddress() {
        return getString(ParseConstants.KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(ParseConstants.KEY_ADDRESS, address);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(ParseConstants.KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint location) {
        put(ParseConstants.KEY_LOCATION, location);
    }

    public static ParseQuery<Centro> getQuery() {
        return ParseQuery.getQuery(Centro.class);
    }
}
