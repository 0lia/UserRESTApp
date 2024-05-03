package com.example.User.util;

import com.example.User.entities.User;
import com.example.User.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Date;

@Component
@PropertySource("classpath:application.properties")
public class UserValidator implements Validator {
    private final UserService userService;
    private final Environment environment;

    @Autowired
    public UserValidator(UserService userService, Environment environment) {
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        int age = Integer.parseInt(environment.getProperty("age"));
        LocalDate max = LocalDate.now().minusYears(age);
        Date date = new Date(max.getYear() - 1900, max.getMonthValue(), max.getDayOfMonth());
        if(user.getBirthDate().after(date))
            errors.rejectValue("birthDate", "", "User should be more than 18 y o");
    }
}
