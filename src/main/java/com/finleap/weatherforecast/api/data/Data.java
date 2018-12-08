package com.finleap.weatherforecast.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data extends JsonObjectBase {
    private int dt;
    private Main main;
    private List<Weather> weather = null;
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Sys sys;
    private String dtTxt;
}
