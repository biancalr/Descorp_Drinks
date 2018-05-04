/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.idrink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Bianca
 */
@Entity
public class BebidaComum extends Bebida implements Serializable {

    @Column(name = "QUANT_GRAMAS_ACUCAR", nullable = true)
    private Integer quantidade;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" Alcoólica: Nao\n Percentual Teor Açúcar:");
        sb.append(this.quantidade);
        return sb.toString();
    }
}
