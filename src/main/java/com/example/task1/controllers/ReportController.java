package com.example.task1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
//@Api(tags = "Report Generations Data")
@Tag(name = "Report APIs")
@RequestMapping(
        "api/v1/report-generation"
)
public class ReportController {

    private final DataSource dataSource;

    @GetMapping(path = "/download")
    @Operation(summary = "Generate the stored data as pdf")
    public ResponseEntity<Resource> generateMPOBankAdviceRemaining(HttpServletResponse response
    ) throws SQLException {
        Connection connection = dataSource.getConnection();
        try{

            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/ReportFiles/Card_Details.jrxml"));


            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "attachment; filename=customers_data.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());


        }catch (Exception e){
            System.out.println("Error Found while generating file: " + e.getMessage());
            String errorMessage = "An error occurred while processing the request."+ e.getMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(new InputStreamResource(new ByteArrayInputStream(errorMessage.getBytes())));
        }
        finally {
            connection.close();
        }

        return  null;
    }

}
