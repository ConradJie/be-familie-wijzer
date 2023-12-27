package com.jie.befamiliewijzer.dtos;

import java.time.LocalDate;

public class EventDto {
    public Integer id;
    public String eventType;
    public String description;
    public String text;
    public LocalDate beginDate;
    public LocalDate endDate;
    public String dateText;
    public Integer personId;
    public Integer relationId;
}
