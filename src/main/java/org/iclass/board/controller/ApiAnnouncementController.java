package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.AnnouncementDTO;
import org.iclass.board.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class ApiAnnouncementController {
    
    private final AnnouncementService announcementService;

    /* JPA 구간 */
    @GetMapping("/latest")
    public ResponseEntity<?> getAnnouncement() {
        log.info("AnnouncementController ::::::: {}", "받음");
        List<AnnouncementDTO> dtoList = announcementService.getAllAnnouncements();
        log.info("AnnouncementController ::::::: {}", dtoList.toString());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<?> getAnnouncementDetails(@PathVariable Long announcementId) {
        log.info("AnnouncementController ::::::: {}", announcementId);
        AnnouncementDTO dto = announcementService.getAnnouncement(announcementId);
        log.info("AnnouncementController ::::::: {}", dto.toString());
        return ResponseEntity.ok(dto);
    }

}

