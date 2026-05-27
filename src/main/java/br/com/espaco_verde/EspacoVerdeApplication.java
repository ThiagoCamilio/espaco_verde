package br.com.espaco_verde;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAsync
public class EspacoVerdeApplication {

  public static void main(String[] args) {
    SpringApplication.run(EspacoVerdeApplication.class, args);
  }

  @GetMapping("/api/health-check")
  public String healthCheck() {
    return "ok";
  }
}
