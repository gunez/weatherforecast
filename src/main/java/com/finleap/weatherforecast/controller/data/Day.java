package com.finleap.weatherforecast.controller.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finleap.weatherforecast.api.data.JsonObjectBase;
import com.finleap.weatherforecast.util.JsonLocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Day extends JsonObjectBase {
    @JsonLocalDate
    private LocalDate date;
    private int dailyAvgTemperature;
    private int nightlyAvgTemperature;
}
