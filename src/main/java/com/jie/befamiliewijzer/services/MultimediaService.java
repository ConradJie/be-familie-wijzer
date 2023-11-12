package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.MultimediaDto;
import com.jie.befamiliewijzer.dtos.MultimediaInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Multimedia;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.MultimediaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MultimediaService {
    private final MultimediaRepository multimediaRepository;
    private final EventRepository eventRepository;

    public MultimediaService(MultimediaRepository multimediaRepository, EventRepository eventRepository) {
        this.multimediaRepository = multimediaRepository;
        this.eventRepository = eventRepository;
    }

    public MultimediaDto getMultimedia(Integer id) {
        Multimedia multimedia = multimediaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested multimedia could not be found"));
        return transfer(multimedia);
    }
    public MultimediaDto getMultimediaFromEvent(Integer eventId, Integer id) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
        Multimedia multimedia = multimediaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested multimedia could not be found"));
        return transfer(multimedia);
    }

    public List<MultimediaDto> getAllMultimediasFromEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
        return transfer(multimediaRepository.findAllByEventId(eventId));
    }

    public MultimediaDto createMultimedia(MultimediaInputDto dto) {
        if (!eventRepository.existsById(dto.eventId)) {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
        Multimedia multimedia = transfer(dto);
        multimediaRepository.save(multimedia);
        return transfer(multimedia);
    }
    public MultimediaDto createMultimediaFromEvent(Integer eventId, MultimediaInputDto dto) {
        if (!eventRepository.existsById(eventId) || !Objects.equals(dto.eventId, eventId)) {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
        Multimedia multimedia = transfer(dto);
        multimediaRepository.save(multimedia);
        return transfer(multimedia);
    }


    public MultimediaDto updateMultimediaFromEvent(Integer eventId, Integer id, MultimediaInputDto dto) {
        if (!eventRepository.existsById(eventId) || !Objects.equals(dto.eventId, eventId)) {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
        Multimedia multimedia = multimediaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The requested multimedia could not be found"));
        multimedia.setDescription(dto.description);
        multimedia.setFilename(dto.filename);
        multimediaRepository.save(multimedia);
        return transfer(multimedia);
    }

    public void deleteMultimediaFromEvent(Integer eventId, Integer id) {
        if (eventRepository.existsById(eventId)) {
            Optional<Multimedia> multimediaOptional = multimediaRepository.findById(id);
            if (multimediaOptional.isPresent() && Objects.equals(multimediaOptional.get().getEvent().getId(), eventId)) {
                multimediaRepository.deleteById(id);
            }
        }
    }

    private MultimediaDto transfer(Multimedia multimedia) {
        MultimediaDto dto = new MultimediaDto();
        dto.id = multimedia.getId();
        dto.description = multimedia.getDescription();
        dto.filename = multimedia.getFilename();
        dto.eventId = multimedia.getEvent().getId();
        return dto;
    }

    private Multimedia transfer(MultimediaInputDto dto) {
        Multimedia multimedia = new Multimedia();
        multimedia.setDescription(dto.description);
        multimedia.setFilename(dto.filename);
        multimedia.setEvent(eventRepository.findById(dto.eventId).get());
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
