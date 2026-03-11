package com.mycompany.cnp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnpController {

    @GetMapping("/api/parse")
    public CNP parse(@RequestParam String number) {
        return CnpParser.parseCNP(number);
    }
}