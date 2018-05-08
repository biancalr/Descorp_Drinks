/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.idrink;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Bianca
 */
public class ValidadorEstado implements ConstraintValidator<ValidaEstado, String>{
    private List<String> estados;

    @Override
    public void initialize(ValidaEstado constraintAnnotation) {
        this.estados = new ArrayList<>();
        this.estados.add("Acre");
        this.estados.add("Alagoas");
        this.estados.add("Amapá");
        this.estados.add("Amazonas");
        this.estados.add("Bahia");
        this.estados.add("Ceará");
        this.estados.add("Distrito Federal");
        this.estados.add("Espírito Santo");
        this.estados.add("Goiás");
        this.estados.add("Maranhão");
        this.estados.add("Mato Grosso");
        this.estados.add("Mato Grosso do Sul");
        this.estados.add("Minas Gerais");
        this.estados.add("Pará");
        this.estados.add("Paraíba");
        this.estados.add("Paraná");
        this.estados.add("Pernambuco");
        this.estados.add("Piauí");
        this.estados.add("Rio de Janeiro");
        this.estados.add("Rio Grande do Norte");
        this.estados.add("Rio Grande do Sul");
        this.estados.add("Rondônia");
        this.estados.add("Roraima");
        this.estados.add("Santa Catarina");
        this.estados.add("São Paulo");
        this.estados.add("Sergipe");
        this.estados.add("Tocantins");
    }
    
    @Override
    public boolean isValid(String valor, ConstraintValidatorContext context){
        return valor == null? false:estados.contains(valor);
    }
    
}
