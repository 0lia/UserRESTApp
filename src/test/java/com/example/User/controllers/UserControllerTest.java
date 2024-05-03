package com.example.User.controllers;

import com.example.User.dto.DateDTO;
import com.example.User.entities.User;
import com.example.User.services.UserService;
import com.example.User.util.UserValidator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService service;

    @MockBean
    private UserValidator userValidator;

    @InjectMocks
    private UserController userController;


    @Mock
    private Validator validator;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setValidator(validator)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void createUserTest() throws Exception {
        String content = "{\n"
                + "  \"email\": \"mail@mail.com\",\n"
                + "  \"lastName\": \"Last\",\n"
                + "  \"firstName\": \"First\",\n"
                + "  \"birthDate\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        Mockito.doNothing().when(service).save(any(User.class));
        Mockito.doNothing().when(userValidator).validate(any(), any());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserInvalidEmailTest() throws Exception {
        String content = "{\n"
                + "  \"email\": \"NOT_VALID\",\n"
                + "  \"lastName\": \"Last\",\n"
                + "  \"firstName\": \"First\",\n"
                + "  \"birthDate\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        Mockito.doNothing().when(userValidator).validate(any(), any());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createUserNoFirstNameTest() throws Exception {
        String content = "{\n"
                + "  \"email\": \"mail@mail.com\",\n"
                + "  \"lastName\": \"Last\",\n"
                + "  \"birthDate\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        Mockito.doNothing().when(userValidator).validate(any(), any());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateUserTest() throws Exception {
        String content = "{\n"
                + "  \"email\": \"mail@mail.com\",\n"
                + "  \"lastName\": \"Last\",\n"
                + "  \"firstName\": \"First\",\n"
                + "  \"birthDate\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        Mockito.doNothing().when(service).update(anyInt(), any(User.class));
        Mockito.doNothing().when(userValidator).validate(any(), any());
        mvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserInvalidEmailTest() throws Exception {
        String content = "{\n"
                + "  \"email\": \"NOT_VALID\",\n"
                + "  \"lastName\": \"Last\",\n"
                + "  \"firstName\": \"First\",\n"
                + "  \"birthDate\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        Mockito.doNothing().when(userValidator).validate(any(), any());
        mvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createUserNoLastNameTest() throws Exception {
        String content = "{\n"
                + "  \"email\": \"mail@mail.com\",\n"
                + "  \"firstName\": \"First\",\n"
                + "  \"birthDate\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        Mockito.doNothing().when(userValidator).validate(any(), any());
        mvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void changeAddressTest() throws Exception {
        Mockito.doNothing().when(service).updateAddress(anyInt(), any());

        mvc.perform(patch("/users/1/update-address?address=newAddress"))
                .andExpect(status().isOk());
    }

    @Test
    public void changePhoneTest() throws Exception {
        Mockito.doNothing().when(service).updatePhone(anyInt(), any());

        mvc.perform(patch("/users/1/update-phone?phone=+38086375417"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTest() throws Exception {
        Mockito.doNothing().when(service).delete(1);

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findByDateRangeTest() throws Exception {
        User user = User.builder()
                .email("mail@mail.com")
                .lastName("Last")
                .firstName("First")
                .birthDate(new Date(100, 5, 26))
                .build();

        String expectedContent = "[{" +
                "\"id\":0," +
                "\"email\":\"mail@mail.com\"," +
                "\"lastName\":\"Last\"," +
                "\"firstName\":\"First\"," +
                "\"birthDate\":\"2000-06-25T21:00:00.000+00:00\"," +
                "\"address\":null," +
                "\"phoneNumber\":null" +
                "}]";

        when(service.findByDateRange(any(), any())).thenReturn(List.of(user));
        String date = "{\n"
                + "  \"from\": \"1997-12-10T18:00:00.000Z\",\n"
                + "  \"to\": \"2027-12-10T18:00:00.000Z\"\n"
                + "}";

        mvc.perform(get("/users/date-range")
                .contentType(MediaType.APPLICATION_JSON)
                .content(date))
                .andExpect(content().bytes(expectedContent.getBytes()))
                .andExpect(status().isOk());
    }

    @Test
    public void findByDateRangeInvalidInputTest() throws Exception {
        when(service.findByDateRange(any(), any())).thenThrow(new IllegalArgumentException());
        String date = "{\n"
                + "  \"from\": \"2027-12-10T18:00:00.000Z\",\n"
                + "  \"to\": \"1997-12-10T18:00:00.000Z\"\n"
                + "}";

        mvc.perform(get("/users/date-range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(date))
                .andExpect(status().is4xxClientError());
    }
}
