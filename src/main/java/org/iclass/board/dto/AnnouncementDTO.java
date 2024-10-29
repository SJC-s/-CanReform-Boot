package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iclass.board.entity.PostsEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AnnouncementDTO {
    private Long postId;
    private String userId;
    private String title;
    private String content;
    private String category;
    private String filenames;
    private Integer readCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 업로드 파일을 저장하기 위한 객체
    private MultipartFile file;
    //한번에 여러개의 파일을 업로드
    private List<MultipartFile> fileS;


    public static PostsDTO of(PostsEntity entity) {
        return PostsDTO.builder()
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .filenames(entity.getFilenames())
                .readCount(entity.getReadCount())
                .commentCount(entity.getCommentCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PostsEntity toEntity() {
        return PostsEntity.builder()
                .postId(postId)
                .userId(userId)
                .title(title)
                .content(content)
                .category(category)
                .filenames(filenames)
                .readCount(readCount)
                .commentCount(commentCount)
                .updatedAt(updatedAt)
                .build();
    }

}