package com.gitsunjaeab.mapick.application.roadmap.Layer;

import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibrary;

public class ForkResult {

    private final LayerLibrary library = null; // 포크에서는 library 정보가 필요 없으므로 null
    private final Layer forkedLayer;
    private final Layer forkOriginLayer; // 포크 원본 레이어
    private final Roadmap forkOriginRoadmap; // 포크 원본 로드맵
    private final Roadmap targetRoadmap; // 포크 타겟 로드맵

    public ForkResult(Layer forkOriginLayer, Roadmap forkOriginRoadmap, Layer forkedLayer, Roadmap targetRoadmap) {
        this.forkOriginLayer = forkOriginLayer;
        this.forkOriginRoadmap = forkOriginRoadmap;
        this.forkedLayer = forkedLayer;
        this.targetRoadmap = targetRoadmap;
    }

    public LayerLibrary library() {
        return library;
    }

    public Layer forkedLayer() {
        return forkedLayer;
    }

    public Roadmap targetRoadmap() {
        return targetRoadmap;
    }

    public Layer forkOriginLayer() { return forkOriginLayer; }
    public Roadmap forkOriginRoadmap() { return forkOriginRoadmap; }
}