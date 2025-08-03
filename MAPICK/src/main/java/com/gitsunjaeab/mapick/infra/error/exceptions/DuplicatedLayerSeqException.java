package com.gitsunjaeab.mapick.infra.error.exceptions;

import java.util.List;

// 레이어 시퀀스 중복 예외
// 같은 로드맵 내에서 layerSeq가 중복될 때 발생

public class DuplicatedLayerSeqException extends RuntimeException {
    
    private final List<Integer> duplicatedSeqs;
    
    public DuplicatedLayerSeqException(List<Integer> duplicatedSeqs) {
        super("중복된 layerSeq가 존재합니다: " + duplicatedSeqs);
        this.duplicatedSeqs = duplicatedSeqs;
    }
    
    public List<Integer> getDuplicatedSeqs() {
        return duplicatedSeqs;
    }
} 