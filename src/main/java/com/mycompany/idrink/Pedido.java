package com.mycompany.idrink;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
    private Calendar horaPedido;
    @JoinTable(name = "tb_pedido_bebida", joinColumns = {
        @JoinColumn(name = "TB_BEBIDA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TB_PEDIDO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Bebida> bebidas;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private Cliente cliente;
    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public SimpleDateFormat getDataPrevisaoEntrega() {
        Calendar previEntrega = Calendar.getInstance();
        previEntrega.setTime(this.dataPedido);
        previEntrega.add(Calendar.DAY_OF_MONTH, +20);
        SimpleDateFormat previsaoEntrega = new SimpleDateFormat("dd/MM/yy");
        return previsaoEntrega;
    }
    
    public Calendar getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(Calendar horaPedido) {
        this.horaPedido = horaPedido;
    }

    public Collection<Bebida> getBebidas() {
        return bebidas;
    }

    public void setBebidas(Bebida bebida) {
        bebidas.add(bebida);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Double getdTotal() {
        Double dtotal = total.doubleValue();
        return dtotal;
    }

    private void setTotal(Bebida b) {
        for (int i = 0; i < this.bebidas.size(); i++) {
            b = (Bebida) this.bebidas.get(i);
            BigDecimal temp = new BigDecimal(b.getQuantGarrafa());
            this.total = this.total.add((b.getPreco().multiply(temp)));
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
        sb.append(this.total);
        return sb.toString();
    }
    
}
