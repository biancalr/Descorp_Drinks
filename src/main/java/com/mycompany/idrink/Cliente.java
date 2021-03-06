package com.mycompany.idrink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Bianca
 */
@Entity
@Table(name = "TB_CLIENTE")
@NamedQueries(
        {
            @NamedQuery(
                    name = "Nomes.Clientes",
                    query = "SELECT c.id, c.nome FROM Cliente c ORDER BY c.id"
            )
        }
)
@NamedNativeQueries(
        {
            @NamedNativeQuery(
                    name = "Nomes.ClientesSQL",
                    query = "SELECT id, txt_nome FROM TB_CLIENTE ORDER BY id",
                    resultClass = Cliente.class
            ),
            @NamedNativeQuery(
                    name = "Cliente.QuantidadePedidosSQL",
                    query = "SELECT c.ID, c.TXT_NOME, COUNT(p.ID) AS TOTAL_PEDIDOS FROM TB_CLIENTE c, TB_PEDIDO p WHERE c.TXT_NOME LIKE ? AND c.ID = p.ID_CLIENTE GROUP BY c.ID",
                    resultSetMapping = "Cliente.QuantidadePedidos"
            )
        }
)
@SqlResultSetMapping(
        name = "Cliente.QuantidadePedidos",
        entities = {
            @EntityResult(entityClass = Cliente.class)},
        columns = {
            @ColumnResult(name = "TOTAL_PEDIDOS", type = Long.class)}
)
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "TXT_NOME", length = 255)
    private String nome;
    @NotBlank
    @Email(message = "{idrink.Cliente.email}")
    @Column(name = "TXT_EMAIL")
    private String email;
    @NotBlank
    @Size(min = 5, max = 20, message = "{idrink.Cliente.login}")
    @Column(name = "TXT_LOGIN")
    private String login;
    @NotBlank
    @Size(min = 6, max = 20, message = "{idrink.Cliente.senha}")
    @Column(name = "TXT_SENHA")
    private String senha;
    @NotBlank
    @Column(name = "TXT_TELEFONE", length = 10)
    private String telefone;
    @Valid
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            optional = false, orphanRemoval = true)
    @JoinColumn(name = "ID_CARTAO", referencedColumnName = "ID")
    private Cartao cartao;
    @Valid
    @Embedded
    private Endereco endereco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente",
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

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

    public boolean temPedidos() {
        return this.pedidos.isEmpty();
    }

    public void addPedidos(Pedido pedido) {
        if (this.pedidos == null) {
            this.pedidos = new ArrayList<>();
        }
        pedido.setCliente(this);
        this.pedidos.add(pedido);
    }

    public boolean removerPedido(Pedido pedido) {
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
