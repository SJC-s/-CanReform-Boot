package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvgRatingDTO {
    private Long postId;
    private Double averageRating;

    // Constructor, Getter, Setter 생략
}
