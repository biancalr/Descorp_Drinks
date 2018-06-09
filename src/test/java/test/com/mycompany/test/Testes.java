package test.com.mycompany.test;

import com.mycompany.idrink.BebidaAlcoolica;
import com.mycompany.idrink.BebidaComum;
import com.mycompany.idrink.Cartao;
import com.mycompany.idrink.Cliente;
import com.mycompany.idrink.Endereco;
import com.mycompany.idrink.Pedido;
import com.mycompany.idrink.StatusCompra;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
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
            if (et.isActive())
                et.rollback();
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
    
    private Endereco criarEnderecoInvalido(Cliente cliente) {
        Endereco endereco = new Endereco();
        endereco.setCep("50690-220");
        endereco.setEstado("Pernambuco1");
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
    public void t01_persistirClienteValido() {
        logger.info("Executando t01: persistir Cliente Válido");
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
        em.clear();
        cliente = em.find(Cliente.class, cliente.getId());
        assertNotNull(cliente.getId());
        assertNotNull(cliente.getCartao().getId());
        assertEquals(new Long(550), new Long(cliente.getEndereco().getNumero()));
        logger.log(Level.INFO, "Cliente incluído com sucesso.", cliente);
    }

    @Test
    public void t02_persistirClienteInValido() {
        logger.info("Executando t02: persistir Cliente InVálido");
        Cliente cliente = new Cliente();
        cliente.setNome("Xuxa");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setSenha("xu6666");
        criarEnderecoInvalido(cliente);
        criarCartao(cliente);
        try {
            em.persist(cliente);
            assertTrue(false);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
            assertEquals(1, constraintViolations.size());
            ConstraintViolation violation = constraintViolations.iterator().next();
            assertEquals(violation.getMessage(), "Estado invalido");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        
        logger.log(Level.INFO, "Cliente invalido invalidado com sucesso.", cliente);
    }

    @Test
    public void t03_persistirClienteInValido2() {
        logger.info("Executando t03: checando cliente inválido 2");
        Cliente cliente = new Cliente();
        cliente.setNome("Xuxa");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setSenha("xu6666");
        criarEnderecoInvalido(cliente);
        criarCartao(cliente);
        
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();       
        
        Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(cliente);
        assertEquals(1, constraintViolations.size());
        ConstraintViolation violation = constraintViolations.iterator().next();
        assertEquals(violation.getMessage(), "Estado invalido");
        logger.log(Level.INFO, "Cliente invalido invalidado com sucesso.", cliente);
    }


    @Test
    public void t04_atualizarCartaoMerge() {
        logger.info("Executando t04: Atualizar Cartao Merge");
        Cartao cartao = em.find(Cartao.class, new Long(3));
        assertNotNull(cartao);
        cartao.setDataExpiracao(getData(19, Calendar.NOVEMBER, 2021));
        cartao.setBandeira("GOOD CARD");
        cartao.setNumero("6161616161616155");
        em.merge(cartao);
        em.flush();
        em.clear();
        cartao = em.find(Cartao.class, new Long(3));
        assertEquals("GOOD CARD", cartao.getBandeira());
        assertEquals("6161616161616155", cartao.getNumero());
        logger.log(Level.INFO, "Cartao atualizado com sucesso", cartao);
    }

    @Test
    public void t05_removerCliente() {
        logger.info("Executando t05: Remover Cliente");
        
        Cliente cliente = em.find(Cliente.class, new Long(2));
        assertNotNull(cliente.getId());
        Cartao cartao = em.find(Cartao.class, cliente.getCartao().getId());
        assertNotNull(cartao.getId());
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < cliente.getPedidos().size(); i++) {
            pedidos.add(cliente.getPedidos().get(i));
        }
        assertEquals(2, pedidos.size());
        cliente.removerPedido(pedidos.get(0));
        cliente.removerPedido(pedidos.get(1));
        em.remove(cliente);
        em.flush();
        em.clear();
        cliente = em.find(Cliente.class, new Long(2));
        assertNull(cliente);
        cartao = em.find(Cartao.class, cartao.getId());
        assertNull(cartao);
        Pedido pedido = em.find(Pedido.class, pedidos.get(0).getId());
        assertNull(pedido);
        pedido = em.find(Pedido.class, pedidos.get(1).getId());
        assertNull(pedido);

        logger.log(Level.INFO, "Cliente removido com sucesso", cliente);
    }

    @Test
    public void t06_atualizarClienteMerge() {
        logger.log(Level.INFO, "Executando t06: Atualizar Cliente Merge");
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente.getId());
        cliente.setSenha("outraSenha54321");
        cliente.setLogin("outroLogin");
        em.merge(cliente);
        em.flush();
        em.clear();
        cliente = em.find(Cliente.class, cliente.getId());
        assertNotNull(cliente.getLogin());
        assertNotNull(cliente.getSenha());
        logger.log(Level.INFO, "Cliente atualizado com sucesso", cliente);
    }

    @Test
    public void t07_atualizarEnderecoMerge() {
        logger.log(Level.INFO, "Executando t07: Atualizar Endereco");
        Cliente cliente = em.find(Cliente.class, new Long(6));
        assertNotNull(cliente);
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Esquadrão");
        endereco.setComplemento("Proximo ao Colegio Militar");
        endereco.setNumero(74);
        cliente.getEndereco().setLogradouro(endereco.getLogradouro());
        cliente.getEndereco().setComplemento(endereco.getComplemento());
        cliente.getEndereco().setNumero(endereco.getNumero());
        em.merge(cliente);
        em.flush();
        em.clear();
        cliente = em.find(Cliente.class, cliente.getId());
        assertEquals(new Long(74), new Long(cliente.getEndereco().getNumero()));
        assertNotNull(cliente.getEndereco().getLogradouro());
        assertNotNull(cliente.getEndereco().getComplemento());
        logger.log(Level.INFO, "Endereco atualizado com sucesso", cliente.getEndereco());

    }

    @Test
    public void t08_removerCartao() {
        logger.log(Level.INFO, "Executando t08: Remover Cartao");
        Cliente cliente = em.find(Cliente.class, new Long(4));
        Cartao cartao = em.find(Cartao.class, cliente.getCartao().getId());
        assertNotNull(cartao);
        em.remove(cartao);
        cliente.setCartao(null);
        em.flush();
        em.clear();
        cliente = em.find(Cliente.class, cliente.getId());
        cartao = em.find(Cartao.class, cartao.getId());
        assertNull(cliente.getCartao());
        assertNull(cartao);
        logger.log(Level.INFO, "Cartao removido com sucesso", cartao);
    }

    @Test
    public void t09_persistirBebidaValida() {
        logger.log(Level.INFO, "Executando t09: Persistir Bebida Valida");
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
    public void t10_persistirCartaoValido() {
        logger.log(Level.INFO, "Executando t10: Adicionar Cartao");
        Cartao cartao = new Cartao();
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente);
        cartao.setBandeira("MASTERCARD");
        cartao.setNumero("7951301472583690");
        cartao.setDataExpiracao(new Date());
        cliente.setCartao(cartao);
        em.persist(cartao);
        em.flush();
        em.clear();
        cartao = em.find(Cartao.class, cliente.getCartao().getId());
        assertNotNull(cartao.getId());
        assertNotNull(cliente.getCartao().getId());
        assertEquals(cliente.getCartao().getId(), cartao.getId());
        logger.log(Level.INFO, "Cartao adicionado com sucesso", cartao);
    }

    @Test
    public void t11_cartoesExpirados() {
        logger.log(Level.INFO, "Executando t11: SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE");
        TypedQuery<Cartao> query = em.createQuery(
                "SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE",
                Cartao.class);
        List<Cartao> cartoesExpirados = query.getResultList();
        assertEquals(1, cartoesExpirados.size());
    }

    @Test
    public void t12_bandeirasDistintas() {
        logger.log(Level.INFO, "Executando t12: SELECT DISTINCT(c.bandeira) FROM CartaoCredito c ORDER BY c.bandeira");
        TypedQuery<String> query
                = em.createQuery("SELECT DISTINCT(c.bandeira) FROM Cartao c ORDER BY c.bandeira",
                        String.class);
        List<String> bandeiras = query.getResultList();
        assertEquals(4, bandeiras.size());
    }

    @Test
    public void t13_cartoesMastercard() {
        logger.info("Executando t13: SELECT c FROM Cartao c WHERE c.bandeira = MASTERCARD ORDER BY c.id");
        TypedQuery<Cartao> query;
        query = em.createQuery("SELECT c FROM Cartao c WHERE c.bandeira LIKE ?1", Cartao.class);
        query.setParameter(1, "MASTERCARD");
        List<Cartao> master = query.getResultList();
        assertEquals(2, master.size());

    }

    @Test
    public void t14_pedidosNegados() {
        logger.info("Executando t14: SELECT p FROM Pedido p WHERE p.statusCompra = NEGADO");
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
    public void t15_findAllClientsNames() {
        logger.log(Level.INFO, "Executando t015: Find All Clients Names");
        Query query = em.createNamedQuery("Nomes.Clientes");
        List<Object> resultados = query.getResultList();
        assertEquals(6, resultados.size());

    }

    @Test
    public void t16_findAllClientsNamesSQL() {
        logger.log(Level.INFO, "Executando t16: Nomes.ClientesSQL");
        Query query = em.createNamedQuery("Nomes.ClientesSQL");
        List<Cliente> clientes = query.getResultList();
        assertEquals(6, clientes.size());

    }

    @Test
    public void t17_maximoPreco() {
        logger.log(Level.INFO, "Executando t17: SELECT max(ba.preco) FROM BebidaAlcoolica ba");
        Query query;
        query = em.createQuery("SELECT max(ba.preco) FROM BebidaAlcoolica ba");
        Object resultado = (Object) query.getSingleResult();
        Double preco = (Double) resultado;
        assertTrue(preco.longValue() == 109);

    }

    @Test
    public void t18_minimoPreco() {
        logger.log(Level.INFO, "Executando t18: SELECT min(bc.preco) FROM BebidaComum bc");
        Query query;
        query = em.createQuery("SELECT min(bc.preco) FROM BebidaComum bc");
        Object resultado = (Object) query.getSingleResult();
        Double preco = (Double) resultado;
        assertTrue(preco.longValue() == 2);

    }

    @Test
    public void t19_clienteCartao() {
        logger.log(Level.INFO, "Executando t19: SELECT c.nome, cc.bandeira FROM Cliente c LEFT OUTER JOIN c.cartao cc");
        TypedQuery<Object[]> query;
        query = em.createQuery(
                "SELECT c.nome, cc.bandeira FROM Cliente c LEFT OUTER JOIN c.cartao cc",
                Object[].class);
        List<Object[]> resultados = query.getResultList();
        assertEquals(6, resultados.size());
        if (logger.isLoggable(Level.INFO)) {
            for (Object[] resultado : resultados) {
                logger.log(Level.INFO, "{0}: {1}", new Object[]{resultado[0], resultado[1]});
            }
        }
    }

    @Test
    public void t20_clientePedidos() {
        logger.log(Level.INFO, "Executando t20: SELECT c.nome, p.id FROM Cliente c JOIN FETCH c.pedidos p WHERE c.id = 4");
        TypedQuery<Object[]> query;
        query = em.createQuery(
                "SELECT c.nome, p.id FROM Cliente c JOIN FETCH c.pedidos p WHERE c.id = 4",
                Object[].class);
        List<Object[]> resultados = query.getResultList();
        assertEquals(2, resultados.size());
        if (logger.isLoggable(Level.INFO)) {
            for (Object[] resultado : resultados) {
                logger.log(Level.INFO, "{0}: {1}", new Object[]{resultado[0], resultado[1]});
            }
        }
    }

    @Test
    public void t21_persistirBebidaInvalida(){
        logger.log(Level.INFO, "Executando t21: Persistir Bebida Invalida");
        BebidaComum bebida = new BebidaComum();
        bebida.setNome("Lt");//nome invalido
        bebida.setPreco(-0.0);//preco invalido
        bebida.setAcucar(32);
        bebida.setEstoque(-32);//estoque invalido
        
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        
        Set<ConstraintViolation<BebidaComum>> constraintViolations;
        constraintViolations = validator.validate(bebida);
        
        if (logger.isLoggable(Level.INFO)) {
            for (ConstraintViolation violation : constraintViolations) {
                Logger.getGlobal().log(Level.INFO, "{0}.{1}: {2}", new Object[]{violation.getRootBeanClass(), violation.getPropertyPath(), violation.getMessage()});
            }
        }
        
        assertEquals(3, constraintViolations.size());
        logger.log(Level.INFO, "Bebida invalida invalidada com sucesso.", bebida);
        
    }
    
    
    
}