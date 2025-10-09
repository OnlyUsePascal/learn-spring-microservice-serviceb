package com.example.serviceb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "servicea")
public interface ServiceAClient {
  @GetMapping("/greeting")
  String getGreeting();

  @GetMapping("/user/{id}")
  User getUser(@PathVariable int id);

}
