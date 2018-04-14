package com.mycompany.idrink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 *
 * @author Bianca
 */
@Entity
@DiscriminatorValue(value = "SIM")
public class BebidaAlcoolica extends Bebida implements Serializable {
    
    @Column(name = "NUM_TEOR_ALCOOL", nullable = true)
    private Float teor;

    public Float getTeor() {
        return teor;
    }

    public void setTeor(Float teor) {
        this.teor = teor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" Alcoólica: SIM\n Teor Alcoólico:");
        sb.append(this.teor);
        return sb.toString();
    }
    
    
    
    
}