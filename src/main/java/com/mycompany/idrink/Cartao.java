package com.mycompany.idrink;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "TB_CARTAO")
public class Cartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TXT_BANDEIRA", length = 20, nullable = false)
    private String bandeira;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EXPIRACAO", nullable = false)
    private Date dataExpiracao;
    @Column(name = "TXT_NUMERO", length = 20, nullable = false)
    private String numero;

    public Long getId() {
        return id;
    }
    
    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public StatusTransacao transacaoBancaria(boolean conectado){
        if(conectado){
            return StatusTransacao.APROVADO;
        }else{
            return StatusTransacao.NEGADO;
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
        final Cartao other = (Cartao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cartao: \n");
        sb.append(" Número:");
        sb.append(this.numero);
        sb.append("\n Bandeira:");
        sb.append(this.bandeira);
        sb.append("\n Data de Expiração:");
        sb.append(this.dataExpiracao);
        sb.append("\n");
        return sb.toString();
    }
    
}