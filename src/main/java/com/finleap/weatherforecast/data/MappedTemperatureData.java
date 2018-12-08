package com.finleap.weatherforecast.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finleap.weatherforecast.api.data.JsonObjectBase;
import com.finleap.weatherforecast.util.JsonInstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MappedTemperatureData extends JsonObjectBase {

    private String name;

    private String zoneId;

    @JsonInstant
    private Instant instant;

    private Map<String, List<Temperature>> dayTemperatures;
}
