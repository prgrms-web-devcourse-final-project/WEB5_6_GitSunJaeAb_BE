package com.gitsunjaeab.mapick.api.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


 // 멤버 목록 조회용 DTO
 
@Getter
@Setter
@AllArgsConstructor
public class MemberList {

    private Long id;

    private boolean isBlacklisted;

    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
    @MemberEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String role;
} 