package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.MultimediaDto;
import com.jie.befamiliewijzer.dtos.MultimediaInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Multimedia;
import com.jie.befamiliewijzer.repositories.MultimediaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MultimediaService {
    private final MultimediaRepository multimediaRepository;

    public MultimediaService(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;
    }
    public MultimediaDto getMultimedia(Integer id) {
        Optional<Multimedia> multimediaOptional = multimediaRepository.findById(id);
        if (multimediaOptional.isPresent()) {
            return transfer(multimediaOptional.get());
        } else {
            throw new ResourceNotFoundException("The requested multimedia could not be found");
        }
    }

    public List<MultimediaDto> getAllMultimedias() {
        return transfer(multimediaRepository.findAll());
    }


    public MultimediaDto createMultimedia(MultimediaInputDto dto) {
        Multimedia multimedia = transfer(dto);
        multimediaRepository.save(multimedia);
        return transfer(multimedia);
    }


    public MultimediaDto updateMultimedia(Integer id, MultimediaInputDto dto) {
        Multimedia multimedia = multimediaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested multimedia could not be found"));
        multimedia.setDescription(dto.description);
        multimedia.setFilename(dto.filename);
        multimediaRepository.save(multimedia);
        return transfer(multimedia);
    }

    public void deleteMultimedia(Integer id) {
        if (multimediaRepository.existsById(id)) {
            multimediaRepository.deleteById(id);
        }
    }

    private MultimediaDto transfer(Multimedia multimedia) {
        MultimediaDto dto = new MultimediaDto();
        dto.id = multimedia.getId();
        dto.description = multimedia.getDescription();
        dto.filename = multimedia.getFilename();
        return dto;
    }

    private Multimedia transfer(MultimediaInputDto dto) {
        Multimedia multimedia = new Multimedia();
        multimedia.setDescription(dto.description);
        multimedia.setFilename(dto.filename);
        return multimedia;
    }

    private List<MultimediaDto> transfer(List<Multimedia> multimedia) {
        List<MultimediaDto> dtos = new ArrayList<>();
        for (Multimedia m : multimedia) {
            dtos.add(transfer(m));
        }
        return dtos;
    }
}
