package com.jie.befamiliewijzer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PersonInputDto {
    @NotBlank
    @Size(min =1, max = 120)
    public String givenNames;
    @NotBlank
    @Size(min =1, max = 120)
    public String surname;
    @NotBlank
    @Size(min =1, max = 1)
    public String sex;
}
