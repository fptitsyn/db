package com.example.application.data.locations;

import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationsService {
    private final LocationsRepository locationsRepository;

    public LocationsService(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    public List<Locations> findAll() {
        return locationsRepository.findAll();
    }
}
