package com.mycompany.idrink;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "TB_BEBIDA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ALCOOLICO",
        discriminatorType = DiscriminatorType.CHAR, length = 3)
@Access(AccessType.FIELD)
@DiscriminatorValue(value = "NAO")
public class Bebida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(name = "TXT_NOME", length = 50, nullable = false)
    protected String nome;
    @Column(name = "NUM_PRECO", length = 5, nullable = false)
    protected BigDecimal preco;
    @Column(name = "NUM_QUANTIDADE", nullable = false)
    protected Integer quantGarrafa;
    @Column(name = "NUM_ESTOQUE", nullable = false)
    protected Integer estoque;
    @ElementCollection
    @ManyToMany(mappedBy = "bebidas")
    protected Collection<Pedido> pedidos;

    public Bebida() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getQuantGarrafa() {
        return quantGarrafa;
    }

    public void setQuantGarrafa(Integer quantGarrafa) {
        this.quantGarrafa = quantGarrafa;
    }
    
    public Integer getEstoque() {
        return estoque;
    }

    public Collection<Pedido> getPedidos() {
        return pedidos;
    }

    public void addPedidos(Collection<Pedido> pedidos) {
        if (this.pedidos == null) {
            pedidos = new HashSet<>();
        }
        this.pedidos = pedidos;
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
        final Bebida other = (Bebida) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Bebida: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Nome:");
        sb.append(this.nome);
        sb.append("\n Preco:");
        sb.append(this.preco);
        sb.append("\n Quantidade:");
        sb.append(this.quantGarrafa);
        sb.append("\n");
        return sb.toString();
    }
    
}
