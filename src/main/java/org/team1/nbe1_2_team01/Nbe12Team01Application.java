package org.team1.nbe1_2_team01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Nbe12Team01Application {

    public static void main(String[] args) {
        SpringApplication.run(Nbe12Team01Application.class, args);
    }

}
