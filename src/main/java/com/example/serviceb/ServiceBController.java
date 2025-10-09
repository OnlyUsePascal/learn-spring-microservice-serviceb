package com.example.serviceb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class ServiceBController {

  DiscoveryClient discoveryClient;
  RestClient restClient;

  public ServiceBController(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
    this.discoveryClient = discoveryClient;
    this.restClient = restClientBuilder.build();
  }

  @GetMapping("/greeting")
  public String getGreeting() {
    String serviceAResponse = "";
    try {
      var serviceAInstance = discoveryClient
          .getInstances("servicea").getFirst();

      serviceAResponse = restClient.get()
          .uri(serviceAInstance.getUri() + "/greeting")
          .retrieve()
          .body(String.class);
    } catch (Exception e) {
      System.out.println("Failed to connect service A");
    }

    return "hello from service b \n response from serivce a is:" + serviceAResponse;
  }

  // openfeign approach
  @Autowired
  ServiceAClient serviceAClient;

  @GetMapping("/user")
  public User getUser() {
    var user = new User();

    try {
      var greet = serviceAClient.getGreeting();
      System.out.println("service a greet:" + greet);

      user = serviceAClient.getUser(0);
      System.out.println("service a user:" + user);
    } catch (Exception e) {
      System.out.println("Failed to connect service A");
    }

    return user;
  }

}
