package com.example.serviceb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.servicea.client.GreetingDto;

@FeignClient(value = "servicea")
public interface ServiceAClient {
  @PostMapping("/")
  String sendGreeting(@RequestBody GreetingDto dto);
}
