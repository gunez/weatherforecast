package com.finleap.weatherforecast.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finleap.weatherforecast.api.data.JsonObjectBase;
import com.finleap.weatherforecast.constants.TimeConstants;
import com.finleap.weatherforecast.util.JsonLocalDate;
import com.finleap.weatherforecast.util.JsonLocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperature extends JsonObjectBase {
    @JsonLocalDate
    private LocalDate localDate;
    @JsonLocalTime
    private LocalTime localTime;
    private double temp;

    public Temperature(int millisecond, double temp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(millisecond), ZoneId.of(TimeConstants.DEFAULT_TIMEZONE));
        this.localDate = localDateTime.toLocalDate();
        this.localTime = localDateTime.toLocalTime();
        this.temp = temp;
    }

    @JsonIgnore
    public String getRegardingLocalDate() {
        if (this.localTime.isBefore(TimeConstants.DAY_START_TIME)) {
            return localDate.minusDays(1).toString();
        } else {
            return localDate.toString();
        }
    }
}
