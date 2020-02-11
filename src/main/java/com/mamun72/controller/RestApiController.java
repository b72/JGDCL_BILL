package com.mamun72.controller;

import com.mamun72.repo.BillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Controller
@RequestMapping("/api")
public class RestApiController {
    @Autowired
    BillRepo billRepo;
//, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
    @RequestMapping(value = "/ajaxcall", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ajaxCall(@Valid @RequestBody Long id) {
        System.out.println("ID: " + id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"text/html");
        if (billRepo.existsById(id)) {

            return ResponseEntity.ok().headers(headers).body("Exists");
        }
        return ResponseEntity.accepted().headers(headers).body("Doesn't Exist");
    }
}
