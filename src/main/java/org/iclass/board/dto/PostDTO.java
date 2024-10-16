package org.iclass.board.dto;

import lombok.Data;

@Data
public class PostDTO {

    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private char isPrivate;
    private String category;
    private String createdAt;
    private String updatedAt;
    private String filenames;
    private int readCount;
    private int commentCount;
    private String status;
}
