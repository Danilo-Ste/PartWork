package com.PartWork.NTCITP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ім'я обов'язкове")
    private String name;

    @Email(message = "Некоректний email")
    @NotBlank(message = "Email обов'язковий")
    private String email;

    @NotBlank(message = "Пароль обов'язковий")
    private String password;

    private String role;

    private Double averageRating = 5.0;
}

