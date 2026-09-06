package com.babakalizada.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoCreateCategoryRequest {

    @NotBlank(message = "Category can't be empty!")
    private String name;

    private String description;

    // Əgər bu kateqoriya Ana Kateqoriyadırsa, parentCategoryId NULL göndərilir.
    // Əgər Alt Kateqoriyadırsa, mənsub olduğu Parent-in ID-si göndərilir.
    private Long parentCategoryId;
}