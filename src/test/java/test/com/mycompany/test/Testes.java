/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mycompany.test;

import com.mycompany.idrink.Cartao;
import com.mycompany.idrink.Cliente;
import com.mycompany.idrink.Endereco;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Bianca
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("JPQLValidation")
public class Testes {
    
    private static EntityManagerFactory emf;
    private static Logger logger;
    private EntityManager em;
    private EntityTransaction et;
    
    public Testes() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        logger = Logger.getGlobal();
        //logger.setLevel(Level.INFO);
        logger.setLevel(Level.SEVERE);
        emf = Persistence.createEntityManagerFactory("idrinkPU");
        DbUnitUtil.inserirDados();
    }
    
    @AfterClass
    public static void tearDownClass() {
        emf.close();
    }
    
    @Before
    public void setUp() {
        em = emf.createEntityManager();
        beginTransaction();
    }
    
    @After
    public void tearDown() {
        commitTransaction();
        em.close();
    }

    private void beginTransaction() {
        et = em.getTransaction();
        et.begin();
    }

    private void commitTransaction() {
        try {
            et.commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            et.rollback();
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void persistirCliente() {
        logger.info("Executando t01: persistir Cliente");
        Cliente cliente = new Cliente();
        cliente.setNome("Xuxa");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setSenha("6666");
        criarEndereco(cliente);
        criarCartao(cliente);
        em.persist(cliente);
        em.flush();
        assertNotNull(cliente.getId());
        logger.log(Level.INFO, "Cliente {0} incluído com sucesso.", cliente);
    }
    
    private Endereco criarEndereco(Cliente cliente){
        Endereco endereco = new Endereco();
        endereco.setCep("50690220");
        endereco.setEstado("Pernambuco");
        endereco.setCidade("Recife");
        endereco.setBairro("Iputinga");
        endereco.setLogradouro("Rua Iolanda Rodrigues Sobral");
        endereco.setNumero(550);
        endereco.setComplemento("Apto. 109");
        cliente.setEndereco(endereco);
        em.flush();
        return endereco;
    }
    
    private void criarCartao(Cliente cliente){
        Cartao cartao = new Cartao();
        cartao.setBandeira("OUROCARD");
        cartao.setNumero("18888281889");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2019);
        c.set(Calendar.MONTH, Calendar.AUGUST);
        c.set(Calendar.DAY_OF_MONTH, 04);
        cartao.setDataExpiracao(c.getTime());
        cliente.setCartao(cartao);
        em.flush();
    }
    
    @Test
     public void atualizarCartao() {
        logger.info("Executando t02: Atualizar Cartao");
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente.getId());
        Cartao cartao = new Cartao();
        cartao.setNumero("120000-100");
        cartao.setBandeira("VISA");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2020);
        c.set(Calendar.MONTH, Calendar.JULY);
        c.set(Calendar.DAY_OF_MONTH, 13);
        cartao.setDataExpiracao(c.getTime());
        cliente.setCartao(cartao);
        em.merge(cartao);
        em.flush();
        em.clear();
        assertNotNull(cartao.getId());
        logger.log(Level.INFO, "Cartao atualizado com sucesso", cartao);
        
    }
    
    @Test
    public void removerCliente() {
        logger.info("Executando t03: Remover Cliente");
        Cliente cliente = em.find(Cliente.class, new Long(2));
        assertNotNull(cliente.getId());
        cliente = em.merge(cliente);
        em.remove(cliente);
        cliente = em.find(Cliente.class, new Long(2));
        assertNull(cliente);
        logger.log(Level.INFO, "Cliente removido com sucesso", cliente);
    }
    
     @Test
     public void removerCartao(){
         logger.log(Level.INFO, "Executando t04: Remover Cartão");
     }
    
}
