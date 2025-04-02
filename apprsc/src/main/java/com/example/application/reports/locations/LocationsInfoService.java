package com.example.application.reports.locations;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationsInfoService {
    private final LocationsInfoRepository repository;

    public LocationsInfoService(LocationsInfoRepository repository) {this.repository = repository;}

    public List<LocationsInfoDTO> getAllLocationInfo() {
        return repository.findAllLocationInfo();
    }
}
