package com.gitsunjaeab.mapick.application.category;

import com.gitsunjaeab.mapick.api.category.dto.CategoryRequest;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.infra.storage.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final FileStorageService fileStorageService;

    public CategoryService(final CategoryRepository categoryRepository,
        final MemberInterestRepository memberInterestRepository,
        FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.memberInterestRepository = memberInterestRepository;
        this.fileStorageService = fileStorageService;
    }

    // 전체 카테고리 조회
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("id"));
    }

    // 특정 카테고리 조회
    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }


    // 카테고리 생성
    public Category create(final CategoryRequest request, final MultipartFile imageFile) {
        final Category category = new Category();
        roadmapToEntity(request, category);
        category.setCreatedAt(OffsetDateTime.now());

        // 파일 업로드 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = fileStorageService.upload(imageFile);
                category.setCategoryImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
            }
        }

        return categoryRepository.save(category);
    }


    // 카테고리 수정
    @Transactional
    public Category update(Long id, CategoryRequest request, final MultipartFile imageFile) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. id=" + id));
        roadmapToEntity(request, category);

        // 파일 업로드 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 기존 파일 삭제
                if (category.getCategoryImage() != null) {
                    fileStorageService.delete(category.getCategoryImage());
                }
                // 새 파일 업로드
                String imageUrl = fileStorageService.upload(imageFile);
                category.setCategoryImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
            }
        }

        return categoryRepository.save(category);
    }

    // 카테고리 삭제는 구현하지않음 (카테고리 삭제 시 연관된 로드맵 처리 문제 발생)


    private Category roadmapToEntity(final CategoryRequest request, final Category category) {
        // null이 아닌 경우에만 업데이트 (부분 업데이트 지원)
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getCategoryImage() != null) {
            category.setCategoryImage(request.getCategoryImage());
        }
        if (request.getCreatedAt() != null) {
            category.setCreatedAt(request.getCreatedAt());
        }

        return category;
    }

//    public ReferencedWarning getReferencedWarning(final Long id) {
//        final ReferencedWarning referencedWarning = new ReferencedWarning();
//        final Category category = categoryRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        final MemberInterest interestMemberInterest = memberInterestRepository.findFirstByCategory(category);
//        if (interestMemberInterest != null) {
//            referencedWarning.setKey("category.memberInterest.interest.referenced");
//            referencedWarning.addParam(interestMemberInterest.getId());
//            return referencedWarning;
//        }
//        return null;
//    }

}
