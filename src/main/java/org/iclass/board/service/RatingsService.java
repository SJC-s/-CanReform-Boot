package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import org.iclass.board.entity.RatingsEntity;
import org.iclass.board.repository.RatingsRepository;
import org.iclass.board.dto.RatingsDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    public RatingsDTO saveRating(RatingsDTO ratingsDTO) {
        RatingsEntity entity = ratingsDTO.toEntity();
        RatingsEntity savedEntity = ratingsRepository.save(entity);
        return RatingsDTO.of(savedEntity);
    }
}
