package com.mycompany.idrink;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "tb_pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DT_PEDIDO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataPedido;
    @Column(name = "HR_PEDIDO", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date horaPedido;
    @ElementCollection
    @JoinTable(name = "tb_pedido_bebida", joinColumns = {
        @JoinColumn(name = "TB_BEBIDA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TB_PEDIDO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Bebida> bebidas;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private Cliente cliente;
    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    
    public Pedido() {
        this.addDataPedido();
        this.addHoraPedido();
    }

    public Long getId() {
        return id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }
    
    private void addDataPedido() {
        Calendar c = Calendar.getInstance();
        this.dataPedido = c.getTime();
    }

    public Date getDataPrevisaoEntrega() {
        Calendar previEntrega = Calendar.getInstance();
        previEntrega.setTime(this.dataPedido);
        previEntrega.add(Calendar.DAY_OF_MONTH, +20);
        this.dataPedido = previEntrega.getTime();
        return this.dataPedido;
    }

    public Date getHoraPedido() {
        return horaPedido;
    }

    private void addHoraPedido() {
        Calendar c = Calendar.getInstance();
        this.horaPedido = c.getTime();
    }

    public Collection<Bebida> getBebidas() {
        return bebidas;
    }

    public void addBebidas(Bebida bebida) {
        if (bebidas == null) {
            bebidas = new HashSet<>();
        }
        bebidas.add(bebida);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal calculaTotalCompras() {
        List<Bebida> bebida = (List<Bebida>) bebidas;
        for (int i = 0; i < bebida.size(); i++) {
            Bebida b = (Bebida) bebida.get(i);
            BigDecimal temp = new BigDecimal(b.getQuantGarrafa());
            this.total = this.total.add((b.getPreco().multiply(temp)));
        }
        return this.total;
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
        StringBuffer sb = new StringBuffer("Pedido: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Cliente:");
        sb.append(this.cliente.getNome());
        sb.append("\n Data do Pedido:");
        sb.append(this.dataPedido);
        sb.append("\n Hora do pedido:");
        sb.append(this.horaPedido);
        sb.append("\n Data de previsão de entrega:");
        sb.append(this.getDataPrevisaoEntrega());
        sb.append("\n ");
        sb.append(this.bebidas);
        sb.append("\n Total:");
        sb.append(this.calculaTotalCompras());
        return sb.toString();
    }
    
}
