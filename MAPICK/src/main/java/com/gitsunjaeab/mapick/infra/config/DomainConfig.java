package com.gitsunjaeab.mapick.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.gitsunjaeab.mapick")
@EnableJpaRepositories("com.gitsunjaeab.mapick")
@EnableTransactionManagement
public class DomainConfig {
}
