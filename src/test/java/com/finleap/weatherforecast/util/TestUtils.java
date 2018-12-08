package com.finleap.weatherforecast.util;

import com.finleap.weatherforecast.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    private TestUtils() {
    }

    public static String getFile(String fileName) {
        try {
            ClassLoader classLoader = TestUtils.class.getClassLoader();
            URI uri = classLoader.getResource(fileName).toURI();
            String mainPath = Paths.get(uri).toString();
            return new String(Files.readAllBytes(Paths.get(mainPath)));
        } catch (URISyntaxException | IOException e) {
            throw new ResourceNotFoundException("Test file not found");
        }
    }
}
