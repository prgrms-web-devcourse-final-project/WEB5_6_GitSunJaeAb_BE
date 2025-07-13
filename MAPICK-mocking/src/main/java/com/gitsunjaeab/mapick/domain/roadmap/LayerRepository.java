package com.gitsunjaeab.mapick.domain.roadmap;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LayerRepository extends JpaRepository<Layer, Long> {

    Layer findFirstByMember(Member member);

    Layer findFirstByRoadmap(Roadmap roadmap);

    List<Layer> findAllByRoadmap_Id(Long roadmapId);

    // 레이어 Id들로 해당 레이어들 조회 (사용자 찜 레이어 목록 조회)
    List<Layer> findAllByIdIn(List<Long> ids);


}
