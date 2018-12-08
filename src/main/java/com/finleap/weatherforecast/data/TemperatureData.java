package com.finleap.weatherforecast.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finleap.weatherforecast.api.data.JsonObjectBase;
import com.finleap.weatherforecast.util.JsonInstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureData extends JsonObjectBase {
    private String name;
    private String zoneId;
    @JsonInstant
    private Instant instant;
    private List<Temperature> temperatures;

    public TemperatureData(String name, Instant instant, String zoneId) {
        this.name = name;
        this.instant = instant;
        this.zoneId = zoneId;
    }
}
