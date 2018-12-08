package com.finleap.weatherforecast.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityValidator implements ConstraintValidator<ValidCity, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^[a-zA-Z,]*$");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches() && value.length() < 50;
    }
}
