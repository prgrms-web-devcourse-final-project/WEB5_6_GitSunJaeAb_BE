package com.gitsunjaeab.mapick;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapickApplication {

    public static void main(final String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        System.setProperty("JDBC_DATABASE_USERNAME", dotenv.get("JDBC_DATABASE_USERNAME"));
        System.setProperty("JDBC_DATABASE_PASSWORD", dotenv.get("JDBC_DATABASE_PASSWORD"));

        SpringApplication.run(MapickApplication.class, args);
    }

}
