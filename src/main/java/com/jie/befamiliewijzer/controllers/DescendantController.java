package com.jie.befamiliewijzer.controllers;

import com.jie.befamiliewijzer.dtos.DescendantDto;
import com.jie.befamiliewijzer.services.DescendantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class DescendantController {
    final DescendantService descendantService;

    DescendantController(DescendantService descendantService) {
        this.descendantService = descendantService;
    }

    @GetMapping("/descendants/{id}")
    public ResponseEntity<List<DescendantDto>> getDescendantsById(@PathVariable Integer id) {
        List<DescendantDto> descendants = descendantService.findDescendantsById(id);
        return ResponseEntity.ok(descendants);
    }
}
