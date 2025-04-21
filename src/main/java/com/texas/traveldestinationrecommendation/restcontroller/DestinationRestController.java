package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.Implementation.DestinationServicesImpl;
import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import com.texas.traveldestinationrecommendation.model.Destination;
import com.texas.traveldestinationrecommendation.services.DestinationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/destination")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DestinationRestController {

    private final DestinationServices destinationServices;
    private final DestinationServicesImpl destinationServicesImpl;


    @GetMapping("/list")
    public ResponseEntity<List<DestinationDto>> getListDestination() {

        return new ResponseEntity<>(destinationServices.getAllDestinations(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Destination> addDestination(@RequestBody Destination destination) {
        return new ResponseEntity<>(destinationServices.addDestination(destination), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {

        return new ResponseEntity<>(destinationServices.getDestination(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void DeleteDestinationById(@PathVariable Long id) {
        destinationServices.deleteDestination(id);
    }


    @PutMapping("update/{id}")
    public ResponseEntity<Destination> updateDestination(@RequestBody Destination destination, @PathVariable Long id) {
        return new ResponseEntity<>(destinationServices.updateDestination(destination,id),HttpStatus.OK);
    }


    @GetMapping("/activity-tags")
    public ResponseEntity<Map<String, Long>> getActivityTagStats(){

        return new ResponseEntity<>(destinationServicesImpl.getActivityTagStatistics(), HttpStatus.OK);
    }


    @GetMapping("/season-stats")
    public ResponseEntity<Map<String, Long>> getSeasonStats(){

        return new ResponseEntity<>(destinationServicesImpl.countBySeason(),
                HttpStatus.OK);
    }

}
