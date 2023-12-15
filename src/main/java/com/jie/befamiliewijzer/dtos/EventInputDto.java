package com.jie.befamiliewijzer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class EventInputDto {
    @Size(min = 5, max = 10)
    public String eventType;
    @NotBlank
    @Size(min = 1, max = 1024)
    public String description;
    @Size(min = 0, max = 10240)
    public String text;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate beginDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate endDate;
    public Integer personId;
    public Integer relationId;
}
