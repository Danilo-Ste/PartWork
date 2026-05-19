package com.PartWork.NTCITP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Назва обов'язкова")
    private String title;

    private String description;
    private String category;

    @Min(value = 1, message = "Оплата повинна бути більшою за 0")
    private Double salary;

    private String address;
    private String deadline;


    private String status = "ВІДКРИТЕ";

    private String paymentStatus = "НЕ ОПЛАЧЕНО";
}
