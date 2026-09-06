package com.babakalizada.category.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoUpdateCategoryRequest {

    private String name;

    private String description;

    private Long parentCategoryId;
}