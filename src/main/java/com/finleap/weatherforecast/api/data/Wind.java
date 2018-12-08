package com.finleap.weatherforecast.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Wind extends JsonObjectBase {
    private double speed;
    private double deg;
}
