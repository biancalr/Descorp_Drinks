/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mycompany.test;

import com.mycompany.idrink.Cartao;
import com.mycompany.idrink.Cliente;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Bianca
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("JPQLValidation")
public class TestesCartao {

    private static EntityManagerFactory emf;
    private static Logger logger;
    private EntityManager em;
    private EntityTransaction et;

    public TestesCartao() {
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
//
//    @Test
//    public void t02_atualizarCartao() {
//        logger.info("Executando t02: Atualizar Cartao");
//        Cartao cartao = em.find(Cartao.class, new Long(3));
//        assertNotNull(cartao);
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, 2021);
//        c.set(Calendar.MONTH, Calendar.NOVEMBER);
//        c.set(Calendar.DAY_OF_MONTH, 19);
//        cartao.setDataExpiracao(c.getTime());
//        cartao.setBandeira("GOOD CARD");
//        cartao.setNumero("6161616161616155");
//        em.merge(cartao);
//        em.flush();
//        em.clear();
//        cartao = em.find(Cartao.class, new Long(3));
//        assertEquals("GOOD CARD", cartao.getBandeira());
//        logger.log(Level.INFO, "Cartao atualizado com sucesso", cartao);
//    }
//
//    @Test
//    public void t06_removerCartao() {
//        logger.log(Level.INFO, "Executando t06: Remover Cartao");
//        Cliente cliente = em.find(Cliente.class, new Long(4));
//        Cartao cartao = em.find(Cartao.class, cliente.getCartao().getId());
//        assertNotNull(cartao);
//        cartao = em.merge(cartao);
//        em.remove(cartao);
//        cliente.setCartao(null);
//        cliente = em.merge(cliente);
//        em.flush();
//        assertNull(cliente.getCartao());
//        logger.log(Level.INFO, "Cartao removido com sucesso", cartao);
//    }
//
//    @Test
//    public void t08_persistirCartao() {
//        logger.log(Level.INFO, "Executando t11 : Adicionar Cartao");
//        Cartao cartao = new Cartao();
//        Cliente cliente = em.find(Cliente.class, new Long(4));
//        assertNotNull(cliente);
//        cartao.setBandeira("MASTERCARD");
//        cartao.setNumero("7951301472583690");
//        cartao.setDataExpiracao(new Date());
//        cliente.setCartao(cartao);
//        em.merge(cliente);
//        em.persist(cartao);
//        em.flush();
//        assertNotNull(cliente.getCartao());
//        logger.log(Level.INFO, "Cartao adicionado com sucesso", cartao);
//    }
//
//    @Test
//    public void t10_cartoesExpirados() {
//        logger.log(Level.INFO, "Executando t08: SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE");
//        TypedQuery<Cartao> query = em.createQuery(
//                "SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE",
//                Cartao.class);
//        List<Cartao> cartoesExpirados = query.getResultList();
//        assertEquals(2, cartoesExpirados.size());
//    }
//
//    @Test
//    public void t11_bandeirasDistintas() {
//        logger.log(Level.INFO, "Executando t09: SELECT DISTINCT(c.bandeira) FROM CartaoCredito c ORDER BY c.bandeira");
//        TypedQuery<String> query
//                = em.createQuery("SELECT DISTINCT(c.bandeira) FROM Cartao c ORDER BY c.bandeira",
//                        String.class);
//        List<String> bandeiras = query.getResultList();
//        assertEquals(4, bandeiras.size());
//    }
//
//    @Test
//    public void t12_clientesVisaMastercard() {
//        logger.info("Executando t08: SELECT c FROM Cliente c "
//                + "WHERE c.cartao.bandeira LIKE ?1 OR c.cartao.bandeira LIKE ?2"
//                + " ORDER BY c.nome DESC");
//
//        TypedQuery<Cliente> query;
//        query = em.createQuery(
//                "SELECT c FROM Cliente c WHERE c.cartao.bandeira LIKE ?1 OR c.cartao.bandeira LIKE ?2 ORDER BY c.nome DESC", Cliente.class);
//        query.setParameter(1, "VISA");
//        query.setParameter(2, "MASTERCARD");
//        List<Cliente> clientes = query.getResultList();
//        for (Cliente cliente : clientes) {
//            switch (cliente.getCartao().getBandeira()) {
//                case "VISA":
//                    assertTrue(true);
//                    break;
//                case "MASTERCARD":
//                    assertTrue(true);
//                    break;
//                default:
//                    assertTrue(false);
//                    break;
//            }
//        }
//        assertEquals(4, clientes.size());
//    }
//
}
