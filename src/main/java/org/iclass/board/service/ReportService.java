package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.repository.PostsRepository;
import org.iclass.board.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
}
