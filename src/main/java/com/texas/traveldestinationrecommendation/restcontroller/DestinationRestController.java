package com.texas.traveldestinationrecommendation.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.texas.traveldestinationrecommendation.Implementation.DestinationServicesImpl;
import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.services.DestinationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/destination")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", exposedHeaders = "Content-Disposition")

public class DestinationRestController {

    private final DestinationServices destinationServices;
    private final ObjectMapper objectMapper;
    private final DestinationServicesImpl destinationServicesImpl;

    @GetMapping("/list")
    public ResponseEntity<List<DestinationDto>> getListDestination() {
        return new ResponseEntity<>(destinationServices.getAllDestinations(), HttpStatus.OK);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DestinationDto> addDestination(
            @RequestPart("destination") String destinationJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {

            DestinationDto destinationDto = objectMapper.readValue(destinationJson, DestinationDto.class);
            destinationDto.setImageFile(imageFile);

            DestinationDto saved = destinationServices.addDestination(destinationDto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) {
        return new ResponseEntity<>(destinationServices.getDestination(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDestinationById(@PathVariable Long id) {
        destinationServices.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DestinationDto> updateDestination(
            @PathVariable Long id,
            @RequestPart("destination") String destinationJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            DestinationDto destinationDto = objectMapper.readValue(destinationJson, DestinationDto.class);
            destinationDto.setImageFile(imageFile);
            DestinationDto updated = destinationServices.updateDestination(destinationDto, id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/activity-tags")
    public ResponseEntity<Map<String, Long>> getActivityTagStats() {
        return new ResponseEntity<>(destinationServicesImpl.getActivityTagStatistics(), HttpStatus.OK);
    }

    @GetMapping("/season-stats")
    public ResponseEntity<Map<String, Long>> getSeasonStats() {
        return new ResponseEntity<>(destinationServicesImpl.countBySeason(), HttpStatus.OK);
    }
}