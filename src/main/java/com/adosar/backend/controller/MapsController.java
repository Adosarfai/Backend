package com.adosar.backend.controller;

import com.adosar.backend.business.GetAllMapsUseCase;
import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.request.GetAllUsersRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.business.response.GetAllUsersResponse;
import com.adosar.backend.domain.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/map")
@AllArgsConstructor
@Builder
public class MapsController {
    private final GetAllMapsUseCase getAllMapsUseCase;
    
    @GetMapping(path = "/all/{page}")
    public @ResponseBody ResponseEntity<Iterable<Map>> getAllUsers(@PathVariable Integer page) {
        GetAllMapsRequest request = new GetAllMapsRequest(page);
        GetAllMapsResponse response = getAllMapsUseCase.getAllMaps(request);
        return new ResponseEntity<>(response.getMaps(), response.getHttpStatus());
    }

}
