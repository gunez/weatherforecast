package com.finleap.weatherforecast.api.data;

public class JsonObjectBase {
    @Override
    public String toString() {
        return Misc.serializeSafeJson(this);
    }
}
