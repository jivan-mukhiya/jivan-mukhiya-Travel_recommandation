package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.dto.DestinationRatingDto;
import com.texas.traveldestinationrecommendation.model.DestinationRating;
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
    public ResponseEntity<List<DestinationRatingDto>> getAllDestinationRating() {

        return new ResponseEntity<>(destinationRatingServices.getAllDestinationsRating(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationRating> getDestinationRatingById(@PathVariable  Long id) {
        return new ResponseEntity<>(destinationRatingServices.getDestinationRating(id),
                HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteDestinationRatingById(@PathVariable Long id) {
        destinationRatingServices.deleteDestinationRating(id);
    }

    @PostMapping("/add")
    public ResponseEntity<DestinationRating> addDestinationRating(@RequestBody DestinationRating dseDestinationRating) {

        return new ResponseEntity<>(destinationRatingServices.addDestination(dseDestinationRating)
                ,HttpStatus.CREATED);
    }

}
