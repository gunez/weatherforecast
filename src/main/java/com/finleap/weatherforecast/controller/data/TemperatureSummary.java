package com.finleap.weatherforecast.controller.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finleap.weatherforecast.api.data.JsonObjectBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureSummary extends JsonObjectBase {
    private String cityName;
    private String timeZone;
    private List<Day> days;
}
