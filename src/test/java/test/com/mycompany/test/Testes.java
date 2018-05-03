/*
 * Onde colocar um indicador de quantidade de garrafas?
 * E o valor total de um Pedido?
 * Adicionar Enum StatusCompra com os valores Aprovado e Negado
 * Adicionar Cartao em JPQL
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

    public Endereco criarEndereco(Cliente cliente) {
        Endereco endereco = new Endereco();
        endereco.setCep("50690-220");
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

    public void criarCartao(Cliente cliente) {
        Cartao cartao = new Cartao();
        cartao.setBandeira("VISA");
        cartao.setNumero("1888828188900044");
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
        Cartao cartao = em.find(Cartao.class, new Long(3));
        assertNotNull(cartao.getId());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2021);
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        c.set(Calendar.DAY_OF_MONTH, 19);
        cartao.setDataExpiracao(c.getTime());
        cartao = em.merge(cartao);
        em.flush();
        logger.log(Level.INFO, "Cartao atualizado com sucesso", cartao);
    }

    @Test
    public void removerClienteMerge() {
        /*
         * Remover 'Cliente' implica que seu 'Cartao' e seus 'Pedidos' serão
         * removidos do banco
         */
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
    public void atualizarClienteMerge() {
        logger.log(Level.INFO, "Executando t04: Atualizar Cliente");
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente.getId());
        cliente.setSenha("outraSenha54321");
        cliente.setLogin("outroLogin");
        cliente = em.merge(cliente);
        em.flush();
        assertNotNull(cliente.getLogin());
        logger.log(Level.INFO, "Cliente atualizado com sucesso", cliente);
    }

    @Test
    public void atualizarEnderecoMerge() {
        logger.log(Level.INFO, "Executando t05: Atualizar Endereco");
        Cliente cliente = em.find(Cliente.class, new Long(6));
        assertNotNull(cliente);
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Esquadrão");
        endereco.setComplemento("Proximo ao Colegio Militar");
        endereco.setNumero(74);
        cliente.getEndereco().setLogradouro(endereco.getLogradouro());
        cliente.getEndereco().setComplemento(endereco.getComplemento());
        cliente.getEndereco().setNumero(endereco.getNumero());
        cliente = em.merge(cliente);
        em.flush();
        assertNotNull(cliente.getEndereco().getLogradouro());
        assertEquals(new Long(74), new Long(cliente.getEndereco().getNumero()));
        logger.log(Level.INFO, "Endereco atualizado com sucesso", cliente.getEndereco());
        
    }
    
    @Test
    public void removerCartao(){
        logger.log(Level.INFO, "Executando t06: Remover Cartao");
        Cliente cliente = em.find(Cliente.class, new Long(4));
        Cartao cartao = em.find(Cartao.class, cliente.getCartao().getId());
        assertNotNull(cartao.getId());
        cartao = em.merge(cartao);
        em.remove(cartao);
        cliente.setCartao(null);
        cliente = em.merge(cliente);
        assertNull(cliente.getCartao());
        em.flush();
        logger.log(Level.INFO, "Cartao removido com sucesso", cartao);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*@Test
    public void adicionarCartao(){
        logger.log(Level.INFO, "Executando t0: Adicionar Cartao");
        Cliente cliente = em.find(Cliente.class, new Long(1));
        assertNotNull(cliente);
        Cartao cartao = new Cartao();
        cartao.setNumero("2209764310875421");
        cartao.setBandeira("VISA");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2020);
        c.set(Calendar.MONTH, Calendar.MAY);
        c.set(Calendar.DAY_OF_MONTH, 8);
        cartao.setDataExpiracao(c.getTime());
        cliente.setCartao(cartao);
        em.persist(cartao);
        em.persist(cliente.getCartao());
        em.flush();
        assertNotNull(cartao.getId());
        assertNotNull(cliente.getCartao());
        logger.log(Level.INFO, "Cartao adicionado com sucesso", cartao);
    }*/
    
    

}
