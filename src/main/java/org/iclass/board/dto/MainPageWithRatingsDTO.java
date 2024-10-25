package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainPageWithRatingsDTO {
    private List<PostsDTO> mainPage;
    private List<AvgRatingDTO> avgRatings;
}
