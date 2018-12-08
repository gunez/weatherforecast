package com.finleap.weatherforecast.constants;

import java.time.LocalTime;

public class TimeConstants {
    public static final String DEFAULT_TIMEZONE = "UTC";
    public static final LocalTime DAY_START_TIME = LocalTime.of(6, 0, 0);
    public static final LocalTime NIGHT_START_TIME = LocalTime.of(18, 0, 0);
    private TimeConstants() {
    }
}
