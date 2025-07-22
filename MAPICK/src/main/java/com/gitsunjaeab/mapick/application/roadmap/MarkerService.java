package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final SupabaseStorageService supabaseStorageService;

    @Transactional
    public Long create(final MarkerDTO markerDTO) {
        final Marker marker = new Marker();
        return markerRepository.save(marker).getId();
    }

    public MarkerDTO get(final Long markerId) {
        Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("해당 마커가 존재하지 않습니다."));

        return new MarkerDTO(marker);
    }

    @Transactional
    public void update(final Long markerId, final MarkerUpdateRequest request, final MultipartFile imageFile) {
        final Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 마커 ID 입니다."));

        marker.updateInfo(request);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            marker.updateImage(imageUrl);
        }
    }

    @Transactional
    public void delete(final Long id) {
        markerRepository.deleteById(id);
    }
}
