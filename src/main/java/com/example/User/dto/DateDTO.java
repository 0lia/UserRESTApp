package com.example.User.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class DateDTO {
    @NotNull
    Date from;

    @NotNull
    Date to;
}
