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
import javax.persistence.TypedQuery;
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
        Cliente cliente = em.find(Cliente.class, new Long(1));
        cliente.setNome("Xuxa");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setSenha("6666");
        criarEndereco(cliente);
        cliente.setCartao(criarCartao());
        em.persist(cliente);
        em.flush();
        assertNotNull(cliente.getId());
        logger.log(Level.INFO, "Cliente {0} incluído com sucesso.", cliente);
    }
    
    public void criarEndereco(Cliente cliente){
        Endereco endereco = new Endereco();
        endereco.setCep("50690220");
        endereco.setEstado("Pernambuco");
        endereco.setCidade("Recife");
        endereco.setBairro("Iputinga");
        endereco.setLogradouro("Rua Iolanda Rodrigues Sobral");
        endereco.setNumero(550);
        cliente.setEndereco(endereco);
        em.flush();
        
    }
    
     private Cartao criarCartao() {
        Cartao cartao = new Cartao();
        cartao.setBandeira("VISA");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2020);
        c.set(Calendar.MONTH, Calendar.JUNE);
        c.set(Calendar.DAY_OF_MONTH, 13);
        cartao.setDataExpiracao(c.getTime());
        cartao.setNumero("120000-100");
        em.flush();
        return cartao;
    }
    
    @Test
    public void removerCliente() {
        logger.info("Executando t02: remover Cliente");
        TypedQuery<Cliente> query = em.createNamedQuery("Cliente.PorNome", Cliente.class);
        query.setParameter("nome", "Beltrano Silva");
        Cliente cliente = query.getSingleResult();
        assertNotNull(cliente);
        em.remove(cliente);
        em.flush();
        assertEquals(0, query.getResultList().size());
    }
     
    @Test
    public void atualizarCliente() {
        //Não atualiza endereco nem cartão
        logger.info("Executando t03: atualizar Cliente");
        TypedQuery<Cliente> query = em.createNamedQuery("Cliente.PorNome", Cliente.class);
        query.setParameter("nome", "Beltrano Silva");
        Cliente cliente = query.getSingleResult();
        assertNotNull(cliente);
        cliente.setNome("Josh José");
        cliente.setTelefone("3356-9694");
        cliente.setLogin("jojo");
        cliente.setSenha("55321");
        em.flush();
        assertEquals(0, query.getResultList().size());
    }
    
    public void atualizarEndereco() {
        //Não atualiza endereco nem cartão
        logger.info("Executando t04: atualizar Endereco");
        TypedQuery<Cliente> query = em.createNamedQuery("Cliente.PorNome", Cliente.class);
        query.setParameter("nome", "Beltrano Silva");
        Cliente cliente = query.getSingleResult();
        Endereco endereco = new Endereco();
        assertNotNull(cliente);
        endereco.setCep("35197114");
        endereco.setEstado("Bahia");
        endereco.setCidade("Pina");
        endereco.setBairro("Favela do Bode");
        endereco.setComplemento("bloco 13");
        endereco.setLogradouro("Rua Encanta Moça");
        endereco.setNumero(45);
        em.flush();
        assertEquals(0, query.getResultList().size());
    }
    
    
}
