package com.example.task1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "SAMPLE_FILE")
@NoArgsConstructor
public class CardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "TRNUMBER")
    private Integer trNumber;
    @Column(name = "KNIT_CARD_NUMBER")
    private Integer knitCardNumber;

    @Column(name = "REQUISITION_QUANTITY")
    private Double requisitionQuantity;

    @Column(name = "KNIT_CARD_QUANTITY")
    private Double knitCardQuantity;

    @Column(name = "KNIT_CARD_BALANCE")
    private Double knitCardBalance;
}
