package com.mycompany.idrink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Bianca
 */
@Embeddable
public class Endereco implements Serializable {

    @NotBlank
    @Size(min = 8, max = 9)
    @Column(name = "END_TXT_CEP")
    private String cep;
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(name = "END_TXT_ESTADO")
    private String estado;
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(name = "END_TXT_CIDADE")
    private String cidade;
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(name = "END_TXT_BAIRRO")
    private String bairro;
    @NotBlank
    @Size(min = 5, max = 100)
    @Column(name = "END_TXT_LOGRADOURO")
    private String logradouro;
    @Size(max = 100)
    @Column(name = "END_TXT_COMPLEMENTO", nullable = true)
    private String complemento;
    @NotBlank
    @Min(value = 1)
    @Max(value = 999)
    @Column(name = "END_INT_NUMERO")
    private Integer numero;

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Endereço: \n");
        sb.append(" CEP:");
        sb.append(this.cep);
        sb.append("\n Estado:");
        sb.append(this.estado);
        sb.append("\n Cidade:");
        sb.append(this.cidade);
        sb.append("\n Bairro:");
        sb.append(this.bairro);
        sb.append("\n Logradouro:");
        sb.append(this.logradouro);
        sb.append("\n Número:");
        sb.append(this.numero);
        sb.append("\n Complemento:");
        sb.append(this.complemento);
        sb.append("\n");
        return sb.toString();
    }
    
}