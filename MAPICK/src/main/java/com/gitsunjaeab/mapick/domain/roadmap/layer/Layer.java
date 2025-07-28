package com.gitsunjaeab.mapick.domain.roadmap.layer;


import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.infra.converter.OffsetDateTimeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.request.LayerRequest;
import com.gitsunjaeab.mapick.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Layers")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) // 생성,수정,삭제 시간 서버 자동처리용
public class Layer {

    @Id
    @Column(nullable = false, updatable = false)
        @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 100
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(name = "layer_temp_id", unique = true)
    private Long layerTempId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private Integer layerSeq;


    @CreatedDate // 생성 시간 서버 자동처리
    @Convert(converter = OffsetDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate // 수정 시간 서버 자동처리
    @Convert(converter = OffsetDateTimeConverter.class)
    @Column
    private OffsetDateTime updatedAt;

    @Convert(converter = OffsetDateTimeConverter.class)
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "is_blocked")
    private Boolean isBlocked = false; // 관리자 블록 여부

    @Convert(converter = OffsetDateTimeConverter.class)
    @Column(name = "blocked_at")
    private OffsetDateTime blockedAt; // 블록 시간

    @Column(name = "block_reason")
    private String blockReason; // 블록 사유 (관리자용 메모)

    @Column(name = "blocked_by")
    private Long blockedBy; // 블록 처리한 관리자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @OneToMany(mappedBy = "layer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Marker> layerMarkers = new HashSet<>();

    @OneToMany(mappedBy = "layer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<LayerLibrary> layerLayerLibraries = new HashSet<>();

    public void updateFromRequest(LayerRequest request, Member member, Roadmap roadmap) {
        // 레이어 기본 수정
        this.name = request.getName();
        this.description = request.getDescription();
        this.layerSeq = request.getLayerSeq();

        // 연관 관계 수정
        this.member = member;
        this.roadmap = roadmap;
        
        // updatedAt은 JPA Auditing으로 자동 처리됨
    }
}
