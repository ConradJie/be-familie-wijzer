package com.jie.befamiliewijzer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MultimediaInputDto {
    @NotBlank
    @Size(min = 1, max = 128)
    public String description;
    @Size(min = 0, max = 128)
    public String filename;
    public Integer eventId;
}
