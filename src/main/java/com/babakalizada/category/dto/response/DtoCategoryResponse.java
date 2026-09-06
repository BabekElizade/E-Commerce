package com.babakalizada.category.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoCategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Long parentCategoryId;

    private String parentCategoryName;

    // Rekursiv olaraq bu kateqoriyaya bağlı bütün alt kateqoriyalar siyahısı
    private List<DtoCategoryResponse> subCategories;
}