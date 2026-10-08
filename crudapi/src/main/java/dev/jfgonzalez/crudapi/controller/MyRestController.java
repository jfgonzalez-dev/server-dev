package dev.jfgonzalez.crudapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping ("/crudapi")
public class MyRestController {
    @GetMapping
    public String index() {
        return "Página principal";
    }
    
}
