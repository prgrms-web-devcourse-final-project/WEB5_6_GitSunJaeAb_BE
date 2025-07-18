package com.gitsunjaeab.mapick.infra.storage;

import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupabaseStorageService {

    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public String upload(MultipartFile file) {
        try {
            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
            }

            String safeFilename = UUID.randomUUID() + "_" + originalFilename;

            // Supabase는 object key에 URL encoding을 하지 않아야 함
            String objectPath = supabaseProperties.getBucket() + "/" + safeFilename;

            String uploadUrl = supabaseProperties.getUrl() + "/storage/v1/object/" + objectPath;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            headers.set("Authorization", "Bearer " + supabaseProperties.getServiceRoleKey());

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Supabase 업로드 실패: {}", response);
                throw new RuntimeException("Supabase 파일 업로드 실패");
            }

            return generatePublicUrl(safeFilename);

        } catch (Exception e) {
            log.error("Supabase 업로드 중 예외 발생", e);
            throw new RuntimeException("Supabase 파일 업로드 중 오류", e);
        }
    }

    private String generatePublicUrl(String filename) {
        return supabaseProperties.getUrl()
            + "/storage/v1/object/public/"
            + supabaseProperties.getBucket()
            + "/" + filename;
    }
}

