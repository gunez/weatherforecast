package com.finleap.weatherforecast.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CityValidator.class)
public @interface ValidCity {
    String message() default "City name is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
