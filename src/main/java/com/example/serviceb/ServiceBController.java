package com.example.serviceb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.servicea.client.GreetingDto;
import com.example.servicea.client.ServiceaClient;

@RestController
public class ServiceBController {
  @Autowired
  ServiceaClient serviceAClient;

  @GetMapping("/greeting")
  public String getGreeting() {
    var greet = "Hello from service B, lemme ping service A \nResult:";

    try {
      var aResult = serviceAClient.sendGreeting(GreetingDto.builder()
          .name("service b")
          .message("watsup?")
          .build());

      greet += aResult;

    } catch (Exception e) {
      greet += "NaN";
    }

    return greet;
  }
}
