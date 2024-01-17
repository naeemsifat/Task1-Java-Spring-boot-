package com.example.task1.service;

import com.example.task1.entity.CardDetails;
import com.example.task1.repository.CardDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CardDetailsService {

    private final CardDetailsRepository repository;
    public static boolean isValidExcelFile(MultipartFile file){ //This method is created to check if the give file is xls or not
        // Check file extension
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        if (!fileName.toLowerCase().endsWith(".xls")) {
            return false;
        }

        // check the content type
        if (!file.getContentType().equals("application/vnd.ms-excel")) {
            return false;
        }

        return true;
    }
    public static List<CardDetails> getCustomersDataFromExcel(InputStream inputStream){
        List<CardDetails> customers = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1"); //Retrieves the sheet named "Sheet1" from the workbook
            int rowIndex =0;
            for (Row row : sheet){ //Iterates through each row in the sheet.
                if (rowIndex ==0){ //Skips the first row (header row) by checking if rowIndex is 0.As first ro is for header info
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator(); //Creates an iterator to go through each cell in the row.
                int cellIndex = 0;
                CardDetails customer = new CardDetails();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){ //Uses a switch statement to set corresponding properties in the CardDetails object based on the cell index.
                        case 0 -> customer.setTrNumber((int) cell.getNumericCellValue());
                        case 1 -> customer.setKnitCardNumber((int) cell.getNumericCellValue());
                        case 2 -> customer.setRequisitionQuantity((double) cell.getNumericCellValue());
                        case 3 -> customer.setKnitCardQuantity((double) cell.getNumericCellValue());
                        case 4 -> customer.setKnitCardBalance((double) cell.getNumericCellValue());
                        default -> {
                        }
                    }
                    cellIndex++; //Increments the cellIndex to move to the next cell
                }
                customers.add(customer); //Adds the populated CardDetails object to the list.
            }
        } catch (IOException e) { //Catches IOException and prints the stack trace if an error occurs while reading the Excel file.
            e.getStackTrace();
        }
        return customers;
    }


    public ResponseEntity<String> saveCardsDataToDatabase(MultipartFile file) {
        if (isValidExcelFile(file)) { //Check if the given file is valid
            try {
                List<CardDetails> customers = getCustomersDataFromExcel(file.getInputStream());

                int insertedCount = 0;
                int updatedCount = 0;
                int errorCount = 0;

                for (CardDetails customer : customers) {
                    Optional<CardDetails> existingCard = repository.findByKnitCardNumber(customer.getKnitCardNumber());

                    if (existingCard.isPresent()) {
                        // Update existing record if record is present
                        CardDetails existingCardDetails = existingCard.get();
                        existingCardDetails.setRequisitionQuantity(customer.getRequisitionQuantity());
                        existingCardDetails.setKnitCardQuantity(customer.getKnitCardQuantity());
                        existingCardDetails.setKnitCardBalance(customer.getKnitCardBalance());
                        repository.save(existingCardDetails);
                        updatedCount++;
                    } else {
                        // Insert new record if the record is not present
                        repository.save(customer);
                        insertedCount++;
                    }
                }
                // Returned total inserted, updated and errors
                String summary = "Inserted: " + insertedCount + ", Updated: " + updatedCount + ", Errors: " + errorCount;
                return new ResponseEntity<>(summary, HttpStatus.OK);
            } catch (IOException e) {
                // Handle IOException, e.g., log or rethrow as a different exception
                return new ResponseEntity<>("An error occurred while processing the file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("The file is not a valid Excel file", HttpStatus.BAD_REQUEST);
        }
    }

    public List<CardDetails> getCustomersCardDetails(){ //Return all the value stored in the database
        return repository.findAll();
    }

}
