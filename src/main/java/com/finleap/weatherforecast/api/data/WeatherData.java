package com.finleap.weatherforecast.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData extends JsonObjectBase {
    private String cod;
    private double message;
    private int cnt;
    private List<Data> list = null;
    private City city;
}
