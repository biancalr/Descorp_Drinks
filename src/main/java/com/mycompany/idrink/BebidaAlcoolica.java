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
@DiscriminatorValue(value = "SIM")
public class BebidaAlcoolica extends Bebida implements Serializable {
    
    @Min(value = 0)
    @Column(name = "PERCENT_TEOR_ALCOOL", nullable = true)
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
        sb.append(" Alcoólica: SIM\n Percentual Teor Alcoólico:");
        sb.append(this.teor);
        return sb.toString();
    }
    
    
    
    
}