package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.CommentsDTO;
import org.iclass.board.entity.CommentsEntity;
import org.iclass.board.repository.CommentsRepository;
import org.iclass.board.repository.PostsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor

public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    public List<CommentsDTO> getComments(Long postId) {
        List<CommentsEntity> comments = commentsRepository.findByPostIdOrderByCreatedAt(postId);
        List<CommentsDTO> dtoList = new ArrayList<>();

        if(comments != null && !comments.isEmpty()) {
            comments.forEach(comment -> {
                dtoList.add(CommentsDTO.of(comment));
            });
        } else {
            return Collections.emptyList();
        }

        return dtoList;
    }

    public CommentsDTO createComment(CommentsDTO dto) {
        CommentsEntity saveEntity = commentsRepository.save(dto.toEntity());
        postsRepository.updateCommentCountPlus(dto.getPostId());
        return CommentsDTO.of(saveEntity);
    }

    public int deleteComment(long commentId) {
        if(commentsRepository.existsById(commentId)) {
            postsRepository.updateCommentCountMinus(commentId);
            commentsRepository.deleteById(commentId);
            return 1;
        } else {
            return 0;
        }
    }
}
