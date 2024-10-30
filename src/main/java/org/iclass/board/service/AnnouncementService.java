package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.AnnouncementDTO;
import org.iclass.board.entity.AnnouncementEntity;
import org.iclass.board.repository.AnnouncementRepository;
import org.iclass.board.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AnnouncementService {

    /* JPA 구간 */
    private final PostsRepository postsRepository;
    private final AnnouncementRepository announcementRepository;

    public List<AnnouncementDTO> getAllAnnouncements() {
        List<AnnouncementEntity> entityList = announcementRepository.findTop3ByOrderByCreatedAtDesc();
        List<AnnouncementDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(AnnouncementDTO.of(entity)));
        return dtoList;
    }

    public AnnouncementDTO getAnnouncement(Long announcementId) {
        log.info("AnnouncementService :::::: {}", "요청받음");
        AnnouncementEntity entity = announcementRepository.findByAnnouncementId(announcementId);
        log.info("AnnouncementService :::::: {}", entity);
        return AnnouncementDTO.of(entity);
    }
}