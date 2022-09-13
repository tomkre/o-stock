package cz.tomek.ostock.bar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("bar")
@RequiredArgsConstructor
public class BarController {

    @GetMapping
    public Map<String, Object> getBar() {
        return Map.of("bar", "bar1");
    }

}
