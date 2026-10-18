package dev.jfgonzalez.crudapi.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString @AllArgsConstructor 
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String codigoPostal;

    @ManyToOne
    @JoinColumn(
        name = "cliente_id",
        nullable = false
    )
    private Cliente cliente;

    @OneToMany(
        mappedBy = "direccion",
        fetch = FetchType.EAGER
    )
    private List<Pedido> pedidos; 

    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setDireccion(this);
    }
}
