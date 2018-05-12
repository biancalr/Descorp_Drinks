package com.mycompany.idrink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "TB_PEDIDO")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "DT_PEDIDO")
    @Temporal(TemporalType.DATE)
    private Date dataPedido;
    @NotNull
    @Column(name = "HR_PEDIDO")
    @Temporal(TemporalType.TIME)
    private Date horaPedido;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_PEDIDO_BEBIDA", joinColumns = {
        @JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID")}, 
            inverseJoinColumns = {
                @JoinColumn(name = "ID_BEBIDA", referencedColumnName = "ID")})
    private List<Bebida> bebidas;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    private Cliente cliente;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TXT_STATUS_COMPRA")
    private StatusCompra statusCompra;
    
    
    public Pedido(Bebida bebida) {
        this.addDataPedido();
        this.addHoraPedido();
        
    }

    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }
    
    private void addDataPedido() {
        Calendar c = Calendar.getInstance();
        this.dataPedido = c.getTime();
    }

    public Date getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(Date horaPedido) {
        this.horaPedido = horaPedido;
    }
    
    private void addHoraPedido() {
        Calendar c = Calendar.getInstance();
        this.horaPedido = c.getTime();
    }

    public List<Bebida> getBebidas() {
        return bebidas;
    }
    public boolean temBebidas(){
        return !this.bebidas.isEmpty();
    }

    public void addBebida(Bebida bebida) {
        if (this.bebidas == null) {
           this.bebidas = new ArrayList<>(); 
        }
        this.bebidas.add(bebida);
    }
    public boolean removerBebida(Bebida bebida){
        return this.bebidas.remove(bebida);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatusCompra getStatusCompra() {
        return statusCompra;
    }

    public void setStatusCompra(StatusCompra statusCompra) {
        this.statusCompra = statusCompra;
        if(cliente.getCartao() == null){
            this.statusCompra = StatusCompra.NEGADO;       
        }
        if (cliente.getCartao().getDataExpiracao().compareTo(new Date()) < 0) {
            this.statusCompra = StatusCompra.NEGADO;
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pedido: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Cliente:");
        sb.append(this.cliente.getNome());
        sb.append("\n Data do Pedido:");
        sb.append(this.dataPedido);
        sb.append("\n Hora do pedido:");
        sb.append(this.horaPedido);
        sb.append("\n ");
        sb.append(this.bebidas);
        return sb.toString();
    }
    
}