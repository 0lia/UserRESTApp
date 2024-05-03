package com.example.User.controllers;

import com.example.User.dto.DateDTO;
import com.example.User.entities.User;
import com.example.User.exceptions.UserNotCreatedException;
import com.example.User.exceptions.UserNotUpdatedException;
import com.example.User.services.UserService;
import com.example.User.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors)
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid User user, BindingResult bindingResult,
                                             @PathVariable("id") int id) {
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors)
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            throw new UserNotUpdatedException(errorMsg.toString());
        }
        userService.update(id, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/update-address")
    public ResponseEntity<HttpStatus> changeAddress( @PathVariable("id") int id,
                                                     @RequestParam(value = "address") String address) {

        userService.updateAddress(id, address);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/update-phone")
    public ResponseEntity<HttpStatus> changePhoneNumber( @PathVariable("id") int id,
                                                         @RequestParam(value = "phone") String phone) {

        userService.updatePhone(id, phone);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<User>> findByDateRange(@RequestBody @Valid DateDTO dateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(userService.findByDateRange(dateDTO.getFrom(), dateDTO.getTo()));
    }

}
