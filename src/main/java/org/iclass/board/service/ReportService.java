package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.dto.ReportsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.ReportsEntity;
import org.iclass.board.repository.PostsRepository;
import org.iclass.board.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional

public class ReportService {

    private final ReportRepository reportRepository;
    private final PostsRepository postsRepository;

    public Integer getPostReportCount(Long postId) throws NotFoundException {
        Optional<PostsEntity> entity = postsRepository.findByPostId(postId);
        PostsDTO dto = entity.map(PostsDTO::of).orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));
        return dto.getReportCount();
    }

    public ReportsDTO saveReport(ReportsDTO dto) {
        ReportsEntity entity = dto.toEntity();
        reportRepository.updateReportsCountPlus(entity.getPostId());
        reportRepository.save(entity);
        return ReportsDTO.of(entity);
    }

    public List<PostsEntity> getReportList(Integer reportCount) {
        return reportRepository.findPostsByReportCountGreaterThan(0);
    }

    public Map<PostsDTO, List<ReportsDTO>> getReportDetail(long postId) {
        PostsEntity post = postsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        PostsDTO pDTO = PostsDTO.of(post);

        // 여러 리포트를 가져오는 메서드 사용
        List<ReportsEntity> reports = reportRepository.findByPostId(postId);
        List<ReportsDTO> rDTOs = reports.stream()
                .map(ReportsDTO::of)
                .collect(Collectors.toList());

        Map<PostsDTO, List<ReportsDTO>> map = new HashMap<>();
        map.put(pDTO, rDTOs);
        return map;
    }

    public int deleteReports(Long postId) {
        List<ReportsEntity> dtoList = reportRepository.findByPostId(postId);
        if(!dtoList.isEmpty()) {
            reportRepository.deleteAll(dtoList);
            return 1;
        }
        else {
            return 0;
        }

    }

    public PostsEntity updateReportStatus(Long postId, String status) {
        log.info("updateService : 받음, {}, {}" ,postId ,status);
        if (postId == null) {
            throw new NullPointerException();
        } else {
            Optional<PostsEntity> entity = postsRepository.findByPostId(postId);
            if(entity.isPresent()) {
                PostsEntity newEntity = entity.get();
                newEntity.setReportStatus(status);
                return postsRepository.save(newEntity);
            } else {
                log.warn("게시글이 존재하지 않습니다. postId: {}", postId);
                return null; // 게시글이 존재하지 않는 경우
            }
        }
    }
}
