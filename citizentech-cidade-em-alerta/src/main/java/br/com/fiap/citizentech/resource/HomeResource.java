package br.com.fiap.citizentech.resource;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden
@Controller
public class HomeResource {

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
