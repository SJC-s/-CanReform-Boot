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
public class PostsDTO {
    private Long post_id;
    private Long user_id;
    private String title;
    private String content;
    private char is_private;
    private String category;
    private String filenames;
    private Integer readCount;
    private Integer commentCount;
    private String status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // 업로드 파일을 저장하기 위한 객체
    private MultipartFile file;
    //한번에 여러개의 파일을 업로드
    private List<MultipartFile> fileS;


    public static PostsDTO of(PostsEntity entity) {
        return PostsDTO.builder()
                .post_id(entity.getPost_id())
                .user_id(entity.getUser_id())
                .title(entity.getTitle())
                .content(entity.getContent())
                .is_private(entity.getIs_private())
                .category(entity.getCategory())
                .filenames(entity.getFilenames())
                .readCount(entity.getReadCount())
                .commentCount(entity.getCommentCount())
                .status(entity.getStatus())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }

    public PostsEntity toEntity() {
        return PostsEntity.builder()
                .post_id(post_id)
                .user_id(user_id)
                .title(title)
                .content(content)
                .is_private(is_private)
                .category(category)
                .filenames(filenames)
                .readCount(readCount)
                .commentCount(commentCount)
                .status(status)
                .updated_at(updated_at)
                .build();
    }

}
