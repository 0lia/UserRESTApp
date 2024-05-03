package com.example.User.services;

import com.example.User.entities.User;
import com.example.User.exceptions.UserNotFoundException;
import com.example.User.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private static User user;

    @BeforeAll
    static void init() {
        user = User.builder()
                .email("mail@mail.com")
                .lastName("Last")
                .firstName("First")
                .birthDate(new Date(2000, 6, 26))
                .build();
    }

    @Test
    public void saveTest() {
        when(userRepository.save(user)).thenReturn(user);
        userService.save(user);
        verify(userRepository).save(user);
    }

    @Test
    public void updateTest() {
        when(userRepository.save(user)).thenReturn(user);
        userService.update(1, user);
        verify(userRepository).save(user);
    }

    @Test
    public void deleteTest() {
        doNothing().when(userRepository).deleteById(1);
        userService.delete(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    public void updateAddressTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateAddress(1, "new address");

        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    @Test
    public void updateAddressUserNotFoundTest() {
        when(userRepository.findById(1)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> userService.updateAddress(1, "new address"));

        verify(userRepository).findById(1);
    }

    @Test
    public void updatePhoneTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateAddress(1, "+38093763462");

        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    @Test
    public void updatePhoneUserNotFoundTest() {
        when(userRepository.findById(1)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> userService.updateAddress(1, "new address"));

        verify(userRepository).findById(1);
    }

    @Test
    public void findByDateRangeTest() {
        when(userRepository.findUsersByBirthDateAfterAndBirthDateBefore(any(), any())).thenReturn(List.of(user));

        assertEquals(List.of(user), userService.findByDateRange(new Date(), new Date()));

        verify(userRepository).findUsersByBirthDateAfterAndBirthDateBefore(any(), any());

    }

    @Test
    public void findByDateRangeInvalidInputTest() {
        Date before = new Date(1900, 12, 12);
        Date after = new Date(1800, 12, 12);

        assertThrows(IllegalArgumentException.class, () -> userService.findByDateRange(before, after));
    }





}
