/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.idrink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

/**
 *
 * @author Bianca
 */
@Entity
@DiscriminatorValue(value = "NAO")
public class BebidaComum extends Bebida implements Serializable {

    @Min(value = 0, message = "{idrink.BebidaComum.gramas}")
    @Column(name = "QUANT_GRAMAS_ACUCAR", nullable = true)
    private Integer gramas;

    public Integer getQuantidade() {
        return gramas;
    }

    public void setQuantidade(Integer gramas) {
        this.gramas = gramas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" Alcoólica: Nao\n Percentual Teor Açúcar:");
        sb.append(this.gramas);
        return sb.toString();
    }
}