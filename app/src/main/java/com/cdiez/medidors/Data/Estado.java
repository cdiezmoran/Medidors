package com.cdiez.medidors.Data;

import com.cdiez.medidors.Other.ParseConstants;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Carlos Diez
 * on 2/1/16.
 */
public class Estado extends ParseObject {

    public String getName() {
        return getString(ParseConstants.KEY_NAME);
    }

    public void setName(String name) {
        put(ParseConstants.KEY_NAME, name);
    }

    public String getShortName() {
        return getString(ParseConstants.KEY_SHORT_NAME);
    }

    public void setShortName(String shortName) {
        put(ParseConstants.KEY_SHORT_NAME, shortName);
    }

    public static ParseQuery<Estado> getQuery() {
        return ParseQuery.getQuery(Estado.class);
    }
}
