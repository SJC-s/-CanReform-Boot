package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.AnnouncementDTO;
import org.iclass.board.entity.AnnouncementEntity;
import org.iclass.board.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AnnouncementService {

    /* JPA 구간 */
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

    public AnnouncementDTO createAnnouncement(AnnouncementDTO announcementDTO) {
        AnnouncementEntity entity = announcementDTO.toEntity();
        announcementRepository.save(entity);
        return AnnouncementDTO.of(entity);
    }

    public AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO, List<MultipartFile> files, List<String> existingFiles) throws IOException, NotFoundException {
        AnnouncementEntity existingAnnouncement = announcementRepository.findByAnnouncementId(announcementDTO.getAnnouncementId());

        // 공지사항이 존재하지 않으면 예외 처리
        if (existingAnnouncement == null) {
            throw new NotFoundException("Announcement not found with ID: " + announcementDTO.getAnnouncementId());
        }

        // 업데이트할 필드만 변경
        existingAnnouncement.setTitle(announcementDTO.getTitle());
        existingAnnouncement.setContent(announcementDTO.getContent());
        existingAnnouncement.setCategory(announcementDTO.getCategory());

        // 기존 파일 처리
        List<String> currentFiles = existingAnnouncement.getFilenames() != null ? Arrays.asList(existingAnnouncement.getFilenames().split(",")) : new ArrayList<>();
        List<String> updatedFiles = existingFiles != null ? new ArrayList<>(existingFiles) : new ArrayList<>();

        // null 체크 후 빈 리스트로 초기화
        if (files == null) {
            files = new ArrayList<>();
        }
        if (existingFiles == null) {
            existingFiles = new ArrayList<>();
        }

        // 기존 파일 중 제거된 파일 삭제 처리
        List<String> finalExistingFiles = existingFiles;
        currentFiles.stream()
                .filter(file -> !finalExistingFiles.contains(file))
                .forEach(this::deleteFile); // 파일 삭제 로직 호출

        // 새로운 파일 저장
        if (!files.isEmpty()) {
            List<String> savedFilePaths = saveFiles(files);
            updatedFiles.addAll(savedFilePaths);
        }

        // 업데이트된 파일명 리스트를 저장
        existingAnnouncement.setFilenames(String.join(",", updatedFiles));
        announcementRepository.save(existingAnnouncement);
        return AnnouncementDTO.of(existingAnnouncement);
    }

    public List<String> saveFiles(List<MultipartFile> files) throws IOException {
        List<String> savedFilePaths = new ArrayList<>();
        // 허용할 확장자 목록
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                throw new IllegalArgumentException("파일 이름을 찾을 수 없습니다.");
            }

            // 파일 확장자 확인
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다. 허용된 확장자: " + String.join(", ", allowedExtensions));
            }

            // 파일명 인코딩(띄어쓰기 정규화)
            String encodedFilename = URLEncoder.encode(filename.replaceAll("[,%\\s+]", "_"), StandardCharsets.UTF_8);

            // 파일 저장 경로 지정
            String filePath = "C:/upload/" + encodedFilename;
            File dest = new File(filePath);
            file.transferTo(dest); // 파일 저장

            savedFilePaths.add(encodedFilename);
            log.info("파일 업로드 완료: {}", encodedFilename);
        }
        return savedFilePaths;
    }

    public void deleteFile(String filename) {
        try {
            String decodedFilename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
            String filePath = "C:/upload/" + decodedFilename;
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    log.info("파일 삭제 완료: {}", decodedFilename);
                } else {
                    log.warn("파일 삭제 실패: {}", decodedFilename);
                }
            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", filename, e);
        }
    }

    public void deleteAnnouncement(Long announcementId) {
        try {
            AnnouncementEntity announcement = announcementRepository.findByAnnouncementId(announcementId);
            if(announcement == null) {
                throw new NotFoundException("Announcement not found with ID: " + announcementId);
            }
            // 삭제 진행
            announcementRepository.delete(announcement);
            log.info("공지 삭제 완료: ID = {}", announcementId);
        } catch (Exception e) {
            log.error("공지 삭제 중 오류 발생. ID = {}", announcementId, e);
            throw new RuntimeException("공지 삭제 중 오류가 발생했습니다. ID: " + announcementId, e);
        }
    }
}