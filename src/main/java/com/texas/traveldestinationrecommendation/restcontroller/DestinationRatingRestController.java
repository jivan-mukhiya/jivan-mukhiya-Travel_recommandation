package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.dto.DestinationRatingDto;
import com.texas.traveldestinationrecommendation.services.DestinationRatingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destination-rating")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DestinationRatingRestController {

    private final DestinationRatingServices destinationRatingServices;

    @GetMapping("/list")
    public ResponseEntity<List<DestinationRatingDto>> getAllDestinationRatings() {
        return ResponseEntity.ok(destinationRatingServices.getAllDestinationsRating());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationRatingDto> getDestinationRatingById(@PathVariable Long id) {
        return ResponseEntity.ok(destinationRatingServices.getDestinationRating(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDestinationRatingById(@PathVariable Long id) {
        destinationRatingServices.deleteDestinationRating(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<DestinationRatingDto> addDestinationRating(
            @RequestBody DestinationRatingDto destinationRatingDto) {
        DestinationRatingDto createdRating = destinationRatingServices.addDestinationRating(destinationRatingDto);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DestinationRatingDto> updateDestinationRating(
            @PathVariable Long id,
            @RequestBody DestinationRatingDto destinationRatingDto) {
        destinationRatingDto.setId(id); // Ensure the ID from path is set
        DestinationRatingDto updatedRating = destinationRatingServices.updateDestinationRating(destinationRatingDto);
        return ResponseEntity.ok(updatedRating);
    }


    @GetMapping("/list/{id}")
    public ResponseEntity<List<DestinationRatingDto>> getAllDestinationRatingsByDestinationId(@PathVariable Long id) {

        return new ResponseEntity<>(destinationRatingServices.
                getDestinationsRatingByDestinationId(id), HttpStatus.OK);
    }
}