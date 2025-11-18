package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Hall name cannot be blank")
    private String name;

    @NotNull(message = "Hall capacity cannot be null")
    @Min(value = 1, message = "Hall capacity must be a positive number")
    private Integer capacity;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    @NotNull(message = "Hall accessibility flag must be not null")
    private Boolean accessibility = false;
}
