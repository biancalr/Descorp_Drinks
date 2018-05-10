package test.com.mycompany.test;

import com.mycompany.idrink.BebidaAlcoolica;
import com.mycompany.idrink.BebidaComum;
import com.mycompany.idrink.Cartao;
import com.mycompany.idrink.Cliente;
import com.mycompany.idrink.Endereco;
import com.mycompany.idrink.Item;
import com.mycompany.idrink.Pedido;
import com.mycompany.idrink.StatusCompra;
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
    
    private Date getData(Integer dia, Integer mes, Integer ano) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return c.getTime();
    }
    
    @Test
    public void t01_persistirClienteValido() {
        logger.info("Executando t01: Persistir Cliente Valido");
        Cliente cliente = new Cliente();
        cliente.setTelefone("3433-7252");
        cliente.setLogin("Xuxa");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setNome("Xuxa Meneghel");
        cliente.setSenha("xuxa666");
        Endereco endereco = new Endereco();
        endereco.setCep("50670-230");
        endereco.setBairro("Cidade Universitaria");
        endereco.setCidade("Recife");
        endereco.setEstado("Pernambuco");
        endereco.setNumero(20);
        endereco.setComplemento("Ap 301");
        endereco.setLogradouro("Av. Professor Moraes Rego");
        cliente.setEndereco(endereco);
        em.persist(cliente);
        em.flush();
        em.clear();
        cliente = em.find(Cliente.class, cliente.getId());
        assertNotNull(cliente.getId());
        logger.log(Level.INFO, "Cliente persistido com sucesso", cliente);
    }
       
    @Test
    public void t02_atualizarCartao() {
        logger.info("Executando t02: Atualizar Cartao");
        Cartao cartao = em.find(Cartao.class, new Long(3));
        assertNotNull(cartao);
        cartao.setDataExpiracao(getData(19, 11, 2021));
        cartao.setBandeira("GOOD CARD");
        cartao.setNumero("6161616161616155");
        em.flush();
        em.clear();
        cartao = em.find(Cartao.class, new Long(3));
        assertEquals("GOOD CARD", cartao.getBandeira());
        logger.log(Level.INFO, "Cartao atualizado com sucesso", cartao);
    }
    
    @Test
    public void t03_persistirBebidaAlcoolica(){
        logger.log(Level.INFO, "Executando t07: Persistir Bebida Alcoolica");
        BebidaAlcoolica bebida = new BebidaAlcoolica();
        bebida.setEstoque(25);
        bebida.setNome("Hidromel");
        bebida.setPreco(45.00);
        bebida.setTeor(8.00f);
        em.persist(bebida);
        em.flush();
        em.clear();
        bebida = em.find(BebidaAlcoolica.class, bebida.getId());
        assertNotNull(bebida.getId());
        logger.log(Level.INFO, "Bebida Adicionada com sucesso", bebida);
    }
    
    @Test
    public void t04_persistirBebidaComum(){
        logger.log(Level.INFO, "Executando t07: Persistir Bebida Comum");
        BebidaComum bebida = new BebidaComum();
        bebida.setEstoque(13);
        bebida.setNome("Leite de Soja");
        bebida.setAcucar(4);
        bebida.setPreco(20.8);
        em.persist(bebida);
        em.flush();
        em.clear();
        bebida = em.find(BebidaComum.class, bebida.getId());
        assertNotNull(bebida.getId());
        logger.log(Level.INFO, "Bebida Adicionada com sucesso", bebida);
    }
    
    @Test
    public void t05_anexarItem(){
        logger.log(Level.INFO, "Executando t05: Anexar Item");
        BebidaAlcoolica bebida = em.find(BebidaAlcoolica.class, new Long(7));
        Item item = new Item(bebida, 2);
        Pedido pedido = em.find(Pedido.class, new Long(3));
        item.setPedido(pedido);
        em.persist(item);
        em.flush();
        em.clear();
        item = em.find(Item.class, item.getId());
        assertNotNull(item.getId());
        logger.log(Level.INFO, "Item anexado com sucesso", item);
    }
    
    @Test
    public void t10_cartoesExpirados(){
        logger.log(Level.INFO, "Executando t08: SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE");
        TypedQuery<Cartao> query = em.createQuery(
                "SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE",
                Cartao.class);
        List<Cartao> cartoesExpirados = query.getResultList();
        assertEquals(2, cartoesExpirados.size());
    }
    
    @Test
    public void t11_bandeirasDistintas(){
        logger.log(Level.INFO, "Executando t09: SELECT DISTINCT(c.bandeira) FROM CartaoCredito c ORDER BY c.bandeira");
        TypedQuery<String> query = 
                em.createQuery("SELECT DISTINCT(c.bandeira) FROM Cartao c ORDER BY c.bandeira",
                        String.class);
        List<String> bandeiras = query.getResultList();
        assertEquals(4, bandeiras.size());
    }
            
            
}