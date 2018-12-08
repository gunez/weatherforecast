package com.finleap.weatherforecast.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class City extends JsonObjectBase {
    private int id;
    private String name;
    private Coord coord;
    private String country;
    private int population;
}
