package com.babakalizada.product.entity;

import com.babakalizada.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private Integer stock;

    @Column(
            nullable = false,
            length = 100
    )
    private String sku;

    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Long categoryId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(
            length = 30
    )
    private ProductStatus status = ProductStatus.ACTIVE;

    @CreationTimestamp
    @Column(
            updatable = false
    )
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
