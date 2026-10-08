package dev.jfgonzalez.crudapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Getter @Setter @ToString @AllArgsConstructor 
public class Pedido {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.RECIBIDO;

    @ManyToOne
    @JoinColumn(
        name = "direccion_id",
        nullable = false
    )
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(
        name = "pedido_id",
        nullable = false
    )
    private Cliente cliente;
}
