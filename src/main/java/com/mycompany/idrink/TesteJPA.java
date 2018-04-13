package com.mycompany.idrink;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Bianca
 */
public class TesteJPA {
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("idrink");
    private static final Logger logger = Logger.getGlobal();
    
    static {
        logger.setLevel(Level.INFO);
    }
    
    public static void main(String[] args) {
        try {
            Long id = inserirCliente();
            System.out.println("ID: " + id);
            consultarCliente(id);
        } finally {
            emf.close();
        }
    }
    
    
    public static Long inserirCliente(){
        Cliente cliente = criarCliente();
        
        EntityManager em = null;
        EntityTransaction et = null;
        
        try {
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();
            em.persist(cliente);
            et.commit();
        } catch (Exception e) {
            if(et != null && et.isActive()){
                Logger.getGlobal().log(Level.SEVERE,
                       "Cancelando transação com erro. Mensagem: {0}", e.getMessage());
                et.rollback();
                Logger.getGlobal().info("Transção cancelada");
            }
        }finally{
            if (em != null) {
                em.close();
            }
        }
        
        return cliente.getId();
        
    }
    
    
    private static Cliente criarCliente(){
        Cliente cliente = new Cliente();
        cliente.setNome("Fulana da silva");
        cliente.setTelefone("3456-2525");
        cliente.setLogin("Fulana");
        cliente.setEmail("fulana@gmail.com");
        cliente.setSenha("1234");
        criarEndereco(cliente);
        criarCartao();
        return cliente;
    
    }
    
    public static void criarEndereco(Cliente cliente){
        Endereco endereco = new Endereco();
        endereco.setCep("50690220");
        endereco.setEstado("Pernambuco");
        endereco.setCidade("Recife");
        endereco.setBairro("Iputinga");
        endereco.setLogradouro("Rua Iolanda Rodrigues Sobral");
        endereco.setNumero(550);
        cliente.setEndereco(endereco);
    }

    private static Cartao criarCartao() {
        Cartao cartao = new Cartao();
        cartao.setBandeira("VISA");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2020);
        c.set(Calendar.MONTH, Calendar.JUNE);
        c.set(Calendar.DAY_OF_MONTH, 13);
        cartao.setDataExpiracao(c.getTime());
        cartao.setNumero("120000-100");
        return cartao;
    }

    private static int consultarCliente(Long id) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            System.out.println("Consultando cliente na base...");
            Cliente cliente = em.find(Cliente.class, id);
            if(cliente == null){
                System.out.println("cliente não encontrado");
                return 1;
            }
            System.out.println("Imprimindo usuário (dados serão recuperados agora)...");
            System.out.println(cliente.toString());
        } finally {
            if (em != null) {
                em.close();
            }            
        }
        return 0;
    
    }
    
}
