package com.mycompany.idrink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Bianca
 */
@Embeddable
public class Endereco implements Serializable {

    @Column(name = "END_TXT_CEP", length = 9, nullable = false)
    private String cep;
    @Column(name = "END_TXT_ESTADO", length = 50, nullable = false)
    private String estado;
    @Column(name = "END_TXT_CIDADE", length = 50, nullable = false)
    private String cidade;
    @Column(name = "END_TXT_BAIRRO", length = 50, nullable = false)
    private String bairro;
    @Column(name = "END_TXT_LOGRADOURO", length = 100, nullable = false)
    private String logradouro;
    @Column(name = "END_TXT_COMPLEMENTO", length = 255, nullable = true)
    private String complemento;
    @Column(name = "END_INT_NUMERO", length = 5, nullable = false)
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
