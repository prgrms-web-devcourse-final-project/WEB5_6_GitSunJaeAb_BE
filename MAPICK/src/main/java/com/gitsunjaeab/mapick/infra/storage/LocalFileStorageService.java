//package com.gitsunjaeab.mapick.infra.storage;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class LocalFileStorageService implements FileStorageService {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Override
//    public String upload(MultipartFile file) {
//        try {
//            String originalFilename = file.getOriginalFilename();
//            String fileExtension = "";
//            if (originalFilename != null && originalFilename.contains(".")) {
//                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            }
//            String storedFilename = UUID.randomUUID().toString() + fileExtension;
//
//            // 절대 경로로 uploads 폴더 생성
//            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            Path filePath = uploadPath.resolve(storedFilename);
//            file.transferTo(filePath.toFile());
//
//            // URL 경로로 반환
//            return "/uploads/" + storedFilename;
//
//        } catch (IOException e) {
//            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void delete(String filePath) {
//        try {
//            if (filePath != null && !filePath.isEmpty()) {
//                String actualPath = filePath.replace("/uploads/", "");
//                Path path = Paths.get(uploadDir, actualPath);
//                if (Files.exists(path)) {
//                    Files.delete(path);
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("파일 삭제 실패: " + filePath);
//        }
//    }
//}