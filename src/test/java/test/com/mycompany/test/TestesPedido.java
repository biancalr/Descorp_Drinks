/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mycompany.test;

import com.mycompany.idrink.Pedido;
import com.mycompany.idrink.StatusCompra;
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
public class TestesPedido {

    private static EntityManagerFactory emf;
    private static Logger logger;
    private EntityManager em;
    private EntityTransaction et;

    public TestesPedido() {
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
//    public void t16_pedidosNegados() {
//        logger.info("Executando t16: SELECT p FROM Pedido p WHERE p.statusCompra = NEGADO");
//        TypedQuery<Pedido> query;
//        query = em.createQuery(""
//                + "SELECT p FROM Pedido p "
//                + "WHERE p.statusCompra = :negado "
//                + ""
//                + "ORDER BY p.id",
//                Pedido.class);
//        query.setParameter("negado", StatusCompra.NEGADO);
//        List<Pedido> negados = query.getResultList();
//
//        assertTrue(negados.get(0).getId() == 7);
//        assertTrue(negados.get(1).getId() == 9);
//        assertTrue(negados.get(2).getId() == 12);
//        assertEquals(3, negados.size());
//    }
//
}
