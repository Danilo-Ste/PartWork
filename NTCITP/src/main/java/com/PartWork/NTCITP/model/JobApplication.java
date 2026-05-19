package com.PartWork.NTCITP.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private Long freelancerId;

    private String status = "В ОЧІКУВАННІ";

    private String reviewText;
    private Integer ratingValue;
}
