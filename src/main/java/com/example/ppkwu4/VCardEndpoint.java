package com.example.ppkwu4;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VCardEndpoint {

    @GetMapping("/find")
    public String find(){
        return "find";
    }
}
