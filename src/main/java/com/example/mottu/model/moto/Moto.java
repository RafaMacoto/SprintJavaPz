package com.example.mottu.model.moto;


import com.example.mottu.model.ala.Ala;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String modelo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String posicao;
    private String problema;
    private String placa;

    @ManyToOne
    @JoinColumn(name = "ala_id")
    private Ala ala;
}
