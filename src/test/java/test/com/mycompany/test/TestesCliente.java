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
import java.util.Date;
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
public class TestesCliente {

    private static EntityManagerFactory emf;
    private static Logger logger;
    private EntityManager em;
    private EntityTransaction et;

    public TestesCliente() {
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
//    public Endereco criarEndereco(Cliente cliente) {
//        Endereco endereco = new Endereco();
//        endereco.setCep("50690-220");
//        endereco.setEstado("Pernambuco");
//        endereco.setCidade("Recife");
//        endereco.setBairro("Iputinga");
//        endereco.setLogradouro("Rua Iolanda Rodrigues Sobral");
//        endereco.setNumero(550);
//        endereco.setComplemento("Apto. 109");
//        cliente.setEndereco(endereco);
//        em.flush();
//        return endereco;
//    }
//
//    public void criarCartao(Cliente cliente) {
//        Cartao cartao = new Cartao();
//        cartao.setBandeira("VISA");
//        cartao.setNumero("1888828188900044");
//        cartao.setDataExpiracao(getData(4, 8, 2019));
//        cliente.setCartao(cartao);
//        em.flush();
//    }
//
//    private Date getData(Integer dia, Integer mes, Integer ano) {
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, ano);
//        c.set(Calendar.MONTH, mes);
//        c.set(Calendar.DAY_OF_MONTH, dia);
//        return c.getTime();
//    }
//
//    @Test
//    public void t01_persistirCliente() {
//        logger.info("Executando t01: persistir Cliente");
//        Cliente cliente = new Cliente();
//        cliente.setNome("Xuxa");
//        cliente.setTelefone("3016-2564");
//        cliente.setLogin("Xuxis");
//        cliente.setEmail("xuxa@gmail.com");
//        cliente.setSenha("xu6666");
//        criarEndereco(cliente);
//        criarCartao(cliente);
//        em.persist(cliente);
//        em.flush();
//        assertNotNull(cliente.getId());
//        assertNotNull(cliente.getCartao().getId());
//        logger.log(Level.INFO, "Cliente {0} incluído com sucesso.", cliente);
//    }
//
//    @Test
//    public void t03_removerClienteMerge() {
//        /*
//         * Remover 'Cliente' implica que seu 'Cartao' e seus 'Pedidos' serão
//         * removidos do banco
//        
//         Checar se os pedidos também foram removidos;
//         */
//        logger.info("Executando t03: Remover Cliente");
//        Cliente cliente = em.find(Cliente.class, new Long(2));
//        Cartao cartao = cliente.getCartao();
//        assertNotNull(cliente.getId());
//        em.remove(cliente);
//        cliente = em.find(Cliente.class, new Long(2));
//        cartao = em.find(Cartao.class, cartao.getId());
//        assertNull(cliente);
//        assertNull(cartao);
//        logger.log(Level.INFO, "Cliente removido com sucesso", cliente);
//    }
//
//    @Test
//    public void t04_atualizarClienteMerge() {
//        logger.log(Level.INFO, "Executando t04: Atualizar Cliente");
//        Cliente cliente = em.find(Cliente.class, new Long(4));
//        assertNotNull(cliente.getId());
//        cliente.setSenha("outraSenha54321");
//        cliente.setLogin("outroLogin");
//        cliente = em.merge(cliente);
//        em.flush();
//        assertNotNull(cliente.getLogin());
//        logger.log(Level.INFO, "Cliente atualizado com sucesso", cliente);
//    }
//
//    @Test
//    public void t05_atualizarEnderecoMerge() {
//        logger.log(Level.INFO, "Executando t05: Atualizar Endereco");
//        Cliente cliente = em.find(Cliente.class, new Long(6));
//        assertNotNull(cliente);
//        Endereco endereco = new Endereco();
//        endereco.setLogradouro("Rua Esquadrão");
//        endereco.setComplemento("Proximo ao Colegio Militar");
//        endereco.setNumero(74);
//        cliente.getEndereco().setLogradouro(endereco.getLogradouro());
//        cliente.getEndereco().setComplemento(endereco.getComplemento());
//        cliente.getEndereco().setNumero(endereco.getNumero());
//        cliente = em.merge(cliente);
//        em.flush();
//        assertNotNull(cliente.getEndereco().getLogradouro());
//        assertEquals(new Long(74), new Long(cliente.getEndereco().getNumero()));
//        logger.log(Level.INFO, "Endereco atualizado com sucesso", cliente.getEndereco());
//
//    }
//
}
