package com.jie.befamiliewijzer.dtos;

import jakarta.validation.constraints.Positive;

public class RelationInputDto {
    @Positive
    public Integer personId;
    @Positive
    public Integer spouceId;
}
