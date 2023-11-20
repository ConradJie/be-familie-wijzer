package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.MultimediaDto;
import com.jie.befamiliewijzer.dtos.MultimediaInputDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Media;
import com.jie.befamiliewijzer.models.Multimedia;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.MediaRepository;
import com.jie.befamiliewijzer.repositories.MultimediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MultimediaService {
    private final MultimediaRepository multimediaRepository;
    private final EventRepository eventRepository;
    private final MediaRepository mediaRepository;

    public MultimediaService(MultimediaRepository multimediaRepository,
                             EventRepository eventRepository,
                             MediaRepository mediaRepository) {
        this.multimediaRepository = multimediaRepository;
        this.eventRepository = eventRepository;
        this.mediaRepository = mediaRepository;
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

    public MultimediaDto assignMediaToMultiMedia(String name, Integer multimediaId) {
        Optional<Multimedia> multimediaOptional = multimediaRepository.findById(multimediaId);
        Optional<Media> mediaOptional = mediaRepository.findByFilename(name);
        if (multimediaOptional.isPresent() && mediaOptional.isPresent()) {
            Media media = mediaOptional.get();
            Multimedia multimedia = multimediaOptional.get();
            multimedia.setMedia(media);
            multimedia.setFilename(media.getFilename());
            multimediaRepository.save(multimedia);
            return transfer(multimedia);
        } else {
            throw new ResourceNotFoundException("The requested multimedia could not be found");
        }

    }

    @Transactional
    public void deleteMultimediaFromEvent(Integer eventId, Integer id) {
        if (eventRepository.existsById(eventId)) {
            Optional<Multimedia> multimediaOptional = multimediaRepository.findById(id);
            if (multimediaOptional.isPresent() && Objects.equals(multimediaOptional.get().getEvent().getId(), eventId)) {
                Multimedia multimedia = multimediaOptional.get();
                multimedia.setEvent(null);
                multimediaRepository.save(multimedia);
                mediaRepository.deleteByFilename(multimedia.getFilename());
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
        if (multimedia.getMedia() != null) {
            dto.media = multimedia.getMedia();
        }
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
