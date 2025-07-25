package com.gitsunjaeab.mapick.infra.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

// MAPICK 은 글로벌 지향 서비스로, 타임존이 저장되는 OffsetDateTime 지원 필요
// JPA 에서 OffsetDateTime 을 DB 에 저장하려면 아래 Converter 가 필요함
// 사용방법 : 엔티티에서 시간 관련 필드에 아래 어노테이션 추가
// @Convert(converter = OffsetDateTimeConverter.class)

@Converter(autoApply = true)
public class OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime attribute) {
        return attribute == null ? null : Timestamp.from(attribute.toInstant());
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : dbData.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }
}