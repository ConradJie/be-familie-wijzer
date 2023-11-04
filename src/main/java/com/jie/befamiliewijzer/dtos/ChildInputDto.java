package com.jie.befamiliewijzer.dtos;

import jakarta.validation.constraints.Positive;

public class ChildInputDto {
    @Positive
    public Integer personId;
    @Positive
    public Integer relationId;
}
