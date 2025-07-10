package com.gitsunjaeab.mapick.layer.entity;

import com.gitsunjaeab.mapick.layer_library.entity.LayerLibrary;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.member.entity.Member;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Layers")
@Getter
@Setter
public class Layer {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private Integer layerSeq;

    @Column
    private LocalDate layerTime;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @OneToMany(mappedBy = "layer")
    private Set<Marker> layerMarkers = new HashSet<>();

    @OneToMany(mappedBy = "layer")
    private Set<LayerLibrary> layerLayerLibraries = new HashSet<>();

}
