/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.idrink;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "TB_ITEMSELECIONADO")
public class ItemSelecionado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quantidade", length = 5, nullable = false)
    private Integer quantidade;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            optional = false, orphanRemoval = true)
    @JoinColumn(name = "ID_BEBIDA", referencedColumnName = "ID", 
            nullable = false)
    private Bebida bebida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Bebida getBebida() {
        return bebida;
    }

    public void setBebida(Bebida bebida) {
        this.bebida = bebida;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemSelecionado)) {
            return false;
        }
        ItemSelecionado other = (ItemSelecionado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ITEM SELECIONADO: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Bebida:");
        sb.append(this.bebida.getId());
        sb.append("\n");
        return sb.toString();
    }
    
}
