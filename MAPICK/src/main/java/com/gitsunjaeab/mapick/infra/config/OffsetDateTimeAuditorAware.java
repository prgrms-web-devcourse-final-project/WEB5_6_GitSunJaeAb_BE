package com.gitsunjaeab.mapick.infra.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;
import reactor.util.annotation.NonNull;

//OffsetDateTime 을 @CreatedDate, @LastModifiedDate 등에 사용할 수 있도록 지원하는 AuditorAware 구현체

@Component
public class OffsetDateTimeAuditorAware implements AuditorAware<OffsetDateTime> {

    @NonNull
    @Override
    public Optional<OffsetDateTime> getCurrentAuditor() {
        return Optional.of(OffsetDateTime.now());
    }
}