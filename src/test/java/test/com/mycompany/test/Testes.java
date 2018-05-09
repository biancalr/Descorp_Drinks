/*
    Ajustar o dataset
*/
package test.com.mycompany.test;

import com.mycompany.idrink.BebidaAlcoolica;
//import com.mycompany.idrink.BebidaComum;
import com.mycompany.idrink.Cartao;
import com.mycompany.idrink.Cliente;
import com.mycompany.idrink.Endereco;
import com.mycompany.idrink.Pedido;
import com.mycompany.idrink.StatusCompra;
//import com.mycompany.idrink.Pedido;
//import com.mycompany.idrink.StatusCompra;
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
    
    private Date getData(Integer dia, Integer mes, Integer ano) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return c.getTime();
    }
    
    @Test
    public void t01_persistirCliente() {
        logger.info("Executando t01: persistir Cliente");
        Cliente cliente = new Cliente();
        cliente.setNome("Xuxa");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setSenha("xu6666");
        criarEndereco(cliente);
        criarCartao(cliente);
        em.persist(cliente);
        em.flush();
        assertNotNull(cliente.getId());
        assertNotNull(cliente.getCartao().getId());
        logger.log(Level.INFO, "Cliente {0} incluído com sucesso.", cliente);
    }

    @Test
    public void t02_atualizarCartao() {
        logger.info("Executando t02: Atualizar Cartao");
        Cartao cartao = em.find(Cartao.class, new Long(3));
        assertNotNull(cartao);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2021);
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        c.set(Calendar.DAY_OF_MONTH, 19);
        cartao.setDataExpiracao(c.getTime());
        cartao.setBandeira("GOOD CARD");
        cartao.setNumero("6161616161616155");
        em.merge(cartao);
        em.flush();
        em.clear();
        cartao = em.find(Cartao.class, new Long(3));
        assertEquals("GOOD CARD", cartao.getBandeira());
        logger.log(Level.INFO, "Cartao atualizado com sucesso", cartao);
    }

    @Test
    public void t03_removerClienteMerge() {
        /*
         * Remover 'Cliente' implica que seu 'Cartao' e seus 'Pedidos' serão
         * removidos do banco
        
         Checar se os pedidos também foram removidos;
         */
        logger.info("Executando t03: Remover Cliente");
        Cliente cliente = em.find(Cliente.class, new Long(2));
        Cartao cartao = cliente.getCartao();
        assertNotNull(cliente.getId());
        em.remove(cliente);
        cliente = em.find(Cliente.class, new Long(2));
        cartao = em.find(Cartao.class, cartao.getId());
        assertNull(cliente);
        assertNull(cartao);
        logger.log(Level.INFO, "Cliente removido com sucesso", cliente);
    }

    @Test
    public void t04_atualizarClienteMerge() {
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
    public void t05_atualizarEnderecoMerge() {
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
    public void t06_removerCartao(){
        logger.log(Level.INFO, "Executando t06: Remover Cartao");
        Cliente cliente = em.find(Cliente.class, new Long(4));
        Cartao cartao = em.find(Cartao.class, cliente.getCartao().getId());
        assertNotNull(cartao);
        cartao = em.merge(cartao);
        em.remove(cartao);
        cliente.setCartao(null);
        cliente = em.merge(cliente);
        em.flush();
        assertNull(cliente.getCartao());
        logger.log(Level.INFO, "Cartao removido com sucesso", cartao);
    }
    
    @Test
    public void t07_persistirBebida(){
        logger.log(Level.INFO, "Executando t07: Persistir Bebida");
        BebidaAlcoolica bebida = new BebidaAlcoolica();
        bebida.setEstoque(25);
        bebida.setNome("Hidromel");
        bebida.setPreco(45.00);
        bebida.setTeor(8.00f);
        em.persist(bebida);
        em.flush();
        assertNotNull(bebida.getId());
        logger.log(Level.INFO, "Bebida Adicionada com sucesso", bebida);
    }
    
    @Test
    public void t08_persistirCartao(){
        logger.log(Level.INFO, "Executando t11 : Adicionar Cartao");
        Cartao cartao = new Cartao();
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente);
        cartao.setBandeira("MASTERCARD");
        cartao.setNumero("7951301472583690");
        cartao.setDataExpiracao(new Date());
        cliente.setCartao(cartao);
        em.merge(cliente);
        em.persist(cartao);
        em.flush();
        assertNotNull(cliente.getCartao());
        logger.log(Level.INFO, "Cartao adicionado com sucesso", cartao);
    }
    
//    
    
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
    
    @Test
    public void t12_clientesVisaMastercard(){
        logger.info("Executando t08: SELECT c FROM Cliente c "
                + "WHERE c.cartao.bandeira LIKE ?1 OR c.cartao.bandeira LIKE ?2" 
                + " ORDER BY c.nome DESC");
        
        TypedQuery<Cliente> query;
        query = em.createQuery(
                 "SELECT c FROM Cliente c WHERE c.cartao.bandeira LIKE ?1 OR c.cartao.bandeira LIKE ?2 ORDER BY c.nome DESC", Cliente.class);
        query.setParameter(1, "VISA");
        query.setParameter(2, "MASTERCARD");
        List<Cliente> clientes = query.getResultList();
        for (Cliente cliente : clientes) {
            switch(cliente.getCartao().getBandeira()){
                case "VISA":
                    assertTrue(true);
                    break;
                case "MASTERCARD":
                    assertTrue(true);
                    break;
                default:
                    assertTrue(false);
                    break;
            }
        }
        assertEquals(4, clientes.size());
    }

    @Test
    public void t13_ClienteCartao(){
        
        logger.info("Executando t14: ClienteCartao");
        TypedQuery<Cliente> query;
        query = em.createNamedQuery("ClienteCartao", Cliente.class);
        List<Cliente> clientes = query.getResultList();
        assertNotNull(clientes);
    }
    
    @Test
    public void t14_clienteCartao(){
        
    }
    
    @Test
    public void t15_bebidasEmPedido(){
    
    }
            
    @Test       
    public void t16_pedidosNegados(){
        logger.info("Executando t16: SELECT p FROM Pedido p WHERE p.statusCompra = NEGADO");
        TypedQuery<Pedido> query;
        query = em.createQuery(""
                + "SELECT p FROM Pedido p "
                + "WHERE p.statusCompra = :negado "
                + ""
                + "ORDER BY p.id",
                Pedido.class);
        query.setParameter("negado", StatusCompra.NEGADO);
        List<Pedido> negados = query.getResultList();
        
        assertTrue(negados.get(0).getId() == 7);
        assertTrue(negados.get(1).getId() == 9);
        assertTrue(negados.get(2).getId() == 12);
        assertEquals(3, negados.size());
    }        
            
    @Test       
    public void t16_pedidoMaisRecente(){
        
    }        
            
            
            
}