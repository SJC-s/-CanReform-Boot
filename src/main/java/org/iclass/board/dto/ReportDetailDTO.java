package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class ReportDetailDTO {
    private PostsDTO post;
    private List<ReportsDTO> reports;

    public ReportDetailDTO(PostsDTO post, List<ReportsDTO> reports) {
        this.post = post;
        this.reports = reports;
    }

}