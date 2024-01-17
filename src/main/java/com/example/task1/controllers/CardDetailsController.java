package com.example.task1.controllers;

import com.example.task1.entity.CardDetails;
import com.example.task1.service.CardDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Data APIs")
@RequestMapping(
        "api/v1/card-details"
)
public class CardDetailsController {
    private final CardDetailsService service;
    @PostMapping(path = "/upload-card-data",// swagger link http://localhost:8024/swagger-ui/index.html
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Get the xls file like Sample_File.xls", description = "Store data from the file Sheet1 section as per the requirement logic.It gives error msg if the file is not xls ")
    public ResponseEntity<String> uploadCustomersData(@RequestPart("file") MultipartFile file){
        return service.saveCardsDataToDatabase(file);
    }

    @GetMapping("/all")
    @Operation(summary = "Return all the data store in the database")
    public ResponseEntity<List<CardDetails>> getAllCustomersData(){
        List<CardDetails> cardDetails = service.getCustomersCardDetails();
        return ResponseEntity.ok(cardDetails);
    }
}
