package com.mycompany.idrink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Bianca
 */
//@NamedQueries(
//        {
//            @NamedQuery(
//                    name = "QuantidadePedidos.PorCliente",
//                    query = "SELECT c.nome "
//                            + "FROM Cliente c "
//                            + "JOIN c.numPedidos "
//                            + "ORDER BY c.id"
//            )
//        }
//)
@Entity
@Table(name = "TB_CLIENTE")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "TXT_NOME", length = 255, nullable = false)
    private String nome;
    @Column(name = "TXT_EMAIL", length = 50, nullable = false)
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Size(max = 15)
    @Column(name = "TXT_LOGIN")
    private String login;
    @NotBlank
    @Size(min = 6, max = 20)
    @Column(name = "TXT_SENHA")
    private String senha;
    @NotBlank
    @Size(min = 8, max = 15)
    @Column(name = "TXT_TELEFONE")
    private String telefone;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, 
            optional = false, orphanRemoval = true)
    @JoinColumn(name = "ID_CARTAO", referencedColumnName = "ID")
    private Cartao cartao;
    @NotNull
    @Embedded
    private Endereco endereco;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "cliente", 
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;
    @Min(value = 0)
    @Transient
    private int numPedidos = 0; 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
    public boolean temPedidos(){
        return this.pedidos.isEmpty();
    }

    public void addPedidos(Pedido pedido) {
        if (this.pedidos == null) {
            this.pedidos = new ArrayList<>();
        }
        pedido.setCliente(this);
        this.pedidos.add(pedido);
        this.numPedidos++;
    }
    
    public boolean removerPedido(Pedido pedido){
        this.numPedidos--;
        return this.pedidos.remove(pedido);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Usuário: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Nome:");
        sb.append(this.nome);        
        sb.append("\n Telefone:");
        sb.append(this.telefone);
        sb.append("\n Login:");
        sb.append(this.login);
        sb.append("\n ");
        if (this.cartao != null) {
            sb.append(this.cartao.toString());
            sb.append("\n ");

        }
        sb.append(this.endereco.toString());
        sb.append("\n");
        return sb.toString();
    }
    
}
