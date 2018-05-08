package com.mycompany.idrink;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "TB_BEBIDA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ALCOOLICO",
        discriminatorType = DiscriminatorType.STRING, length = 3)
@Access(AccessType.FIELD)
public class Bebida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NotBlank
    @Column(name = "TXT_NOME", length = 50)
    protected String nome;
    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "NUM_PRECO", length = 5)
    protected Double preco;
    @NotNull
    @Min(value = 0)
    @Column(name = "NUM_ESTOQUE")
    protected Integer estoque;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
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
        StringBuilder sb = new StringBuilder("Bebida: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Nome:");
        sb.append(this.nome);
        sb.append("\n Preco Unitário:");
        sb.append(this.preco);
        sb.append("\n");
        return sb.toString();
    }
    
}