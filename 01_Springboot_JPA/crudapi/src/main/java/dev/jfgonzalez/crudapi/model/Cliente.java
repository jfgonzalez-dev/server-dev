package dev.jfgonzalez.crudapi.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    indexes = @Index (
        name = "idx_email",
        columnList = "email"
    )
)
@Getter @Setter @ToString @NoArgsConstructor 
public class Cliente {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellidos;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String telefono;

    @ToString.Exclude
    @OneToMany(
        mappedBy = "cliente",
        fetch = FetchType.EAGER
    )
    private List<Direccion> direcciones = new ArrayList<>();

    public void addDireccion(Direccion direccion) {
        direcciones.add(direccion);
        direccion.setCliente(this);
    }

    public void removeDireccion(Direccion direccion) {
        direcciones.remove(direccion);
        direccion.setCliente(null);
    }

    @ToString.Exclude
    @OneToMany(
        mappedBy = "cliente",
        fetch = FetchType.EAGER
    )
    private List<Pedido> pedidos = new ArrayList<>();

    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setCliente(this);
    }

    public void removePedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setCliente(null);
    }
}
