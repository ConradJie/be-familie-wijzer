package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.dtos.MultimediaBlobDto;
import com.jie.befamiliewijzer.dtos.MultimediaDto;
import com.jie.befamiliewijzer.dtos.MultimediaInputDto;
import com.jie.befamiliewijzer.dtos.MultimediaNoMediaDto;
import com.jie.befamiliewijzer.exceptions.ResourceNotFoundException;
import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Media;
import com.jie.befamiliewijzer.models.Multimedia;
import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.repositories.EventRepository;
import com.jie.befamiliewijzer.repositories.MediaRepository;
import com.jie.befamiliewijzer.repositories.MultimediaRepository;
import com.jie.befamiliewijzer.repositories.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MultimediaService {
    private final MultimediaRepository multimediaRepository;
    private final EventRepository eventRepository;
    private final MediaRepository mediaRepository;
    private final MediaService mediaService;
    private final PersonRepository personRepository;

    public MultimediaService(MultimediaRepository multimediaRepository,
                             EventRepository eventRepository,
                             MediaRepository mediaRepository,
                             MediaService mediaService,
                             PersonRepository personRepository) {
        this.multimediaRepository = multimediaRepository;
        this.eventRepository = eventRepository;
        this.mediaRepository = mediaRepository;
        this.mediaService = mediaService;
        this.personRepository = personRepository;
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

    public List<MultimediaBlobDto> getAllMultimediaBlobsFromEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("The requested event could not be found");
        }
        List<Multimedia> multimediaList = multimediaRepository.findAllByEventId(eventId);
        List<MultimediaBlobDto> dtos = new ArrayList<>();
        for (Multimedia multimedia : multimediaList) {
            if (multimedia.getMedia() != null) {
                String blob;
                try {
                    String path = mediaService.downLoadFile(multimedia.getFilename())
                            .getFile().getAbsolutePath();

                    byte[] imageBytes = Files.readAllBytes(Paths.get(path));
                    blob = Base64.getEncoder().encodeToString(imageBytes);
                } catch (Exception e) {
                    blob = null;
                }
                if (blob != null) {
                    MultimediaBlobDto dto = new MultimediaBlobDto();
                    dto.id = multimedia.getId();
                    dto.description = multimedia.getDescription();
                    dto.filename = multimedia.getFilename();
                    dto.eventId = multimedia.getId();
                    dto.contentType = multimedia.getMedia().getContentType();
                    dto.blob = blob;
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }

    public List<MultimediaNoMediaDto> getAllMultimediaWithoutMediaAssigned() {
        List<MultimediaNoMediaDto> dtos = new ArrayList<>();
        List<Multimedia> list = multimediaRepository.findAllByFilenameIs("");
        for (Multimedia multimedia : list) {
            Optional<Event> eventOptional = eventRepository.findAllById(multimedia.getEvent().getId());
            if (eventOptional.isPresent()) {
                Event event = eventOptional.get();
                MultimediaNoMediaDto dto = new MultimediaNoMediaDto();
                dto.id = multimedia.getId();
                dto.description = multimedia.getDescription();
                dto.eventId = event.getId();
                dto.eventType = event.getEventType();
                dto.eventDescription = event.getDescription();
                if (event.getPerson() != null) {
                    Optional<Person> personOptional = personRepository.findById(event.getPerson().getId());
                    if (personOptional.isPresent()) {
                        Person person = personOptional.get();
                        dto.personId = person.getId();
                        dto.givenNames = person.getGivenNames();
                        dto.surname = person.getSurname();
                        dto.sex = person.getSex();
                    }
                }
                dtos.add(dto);
            }
        }
        return dtos;
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
        Optional<Event> eventOptional = eventRepository.findById(dto.eventId);
        if (eventOptional.isPresent()) {
            multimedia.setEvent(eventOptional.get());
        }
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
