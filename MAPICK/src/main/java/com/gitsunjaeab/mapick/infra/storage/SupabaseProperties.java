package com.gitsunjaeab.mapick.infra.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "supabase")
@Getter
@Setter
public class SupabaseProperties {
    private String url;
    private String anonKey;
    private String serviceRoleKey;
    private String bucket;
}

