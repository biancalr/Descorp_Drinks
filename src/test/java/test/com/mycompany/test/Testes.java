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
            if (et.isActive()) {
                et.rollback();
            }
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

    public Endereco criarEnderecoInvalido(Cliente cliente) {
        Endereco endereco = new Endereco();
        endereco.setCep("50690-2220");//cep invalido
        endereco.setEstado("Pernambuco1");//Estado Inválido
        endereco.setCidade("Recife");
        endereco.setBairro("Iputinga");
        endereco.setLogradouro("Rua Iolanda Rodrigues Sobral");
        endereco.setNumero(-550);//numero invalido
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

    public Date getData(Integer dia, Integer mes, Integer ano) {
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
        cliente.setNome("xuxa");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa_gmail.com");//email invalido
        cliente.setSenha("x666"); //senha invalida
        criarEnderecoInvalido(cliente);
        criarCartao(cliente);
        try {
            em.persist(cliente);
            assertTrue(false);
        } catch (ConstraintViolationException ex) {
            Logger.getGlobal().info(ex.getMessage());
            Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
            if (logger.isLoggable(Level.INFO)) {
                for (ConstraintViolation violation : constraintViolations) {
                    Logger.getGlobal().log(Level.INFO, "{0}.{1}: {2}", new Object[]{violation.getRootBeanClass(), violation.getPropertyPath(), violation.getMessage()});
                }
            }
            assertEquals(5, constraintViolations.size());
            assertNull(cliente.getId());
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
        if (logger.isLoggable(Level.INFO)) {
            for (ConstraintViolation violation : constraintViolations) {
                Logger.getGlobal().log(Level.INFO, "{0}.{1}: {2}", new Object[]{violation.getRootBeanClass(), violation.getPropertyPath(), violation.getMessage()});
            }
        }

        assertEquals(3, constraintViolations.size());
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
        cliente.setSenha("outraSenha23");
        cliente.setLogin("outroLogin");
        em.merge(cliente);
        em.flush();
        em.clear();
        cliente = em.find(Cliente.class, cliente.getId());
        assertTrue("outroLogin".equals(cliente.getLogin()));
        assertTrue("outraSenha23".equals(cliente.getSenha()));
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
    public void t10_persistirCartaoInValido() {
        logger.log(Level.INFO, "Executando t10: Persisir Cartao Invalido");
        Cartao cartao = new Cartao();
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente);
        cartao.setBandeira("GREEN CARD");//bandeira invalida
        cartao.setDataExpiracao(getData(19, Calendar.APRIL, 2017));
        cartao.setNumero("8051 30147 2583-622");//numero invalido
        cliente.setCartao(cartao);
        try {
            em.persist(cartao);
            assertTrue(false);
        } catch (ConstraintViolationException e) {
            Logger.getGlobal().info(e.getMessage());
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            if (logger.isLoggable(Level.INFO)) {
                constraintViolations.forEach((violation) -> {
                    Logger.getGlobal().log(Level.INFO, "{0}.{1}: {2}", new Object[]{violation.getRootBeanClass(), violation.getPropertyPath(), violation.getMessage()});
                });
            }
            assertEquals(2, constraintViolations.size());
            assertNull(cartao.getId());

        } catch (Exception e) {
            fail(e.getMessage());
        }

        logger.log(Level.INFO, "Cartao invalidado com sucesso", cartao);
    }

    @Test
    public void t11_persistirCartaoValido() {
        logger.log(Level.INFO, "Executando t11: Persistir Cartao Valido");
        Cartao cartao = new Cartao();
        Cliente cliente = em.find(Cliente.class, new Long(4));
        assertNotNull(cliente);
        cartao.setBandeira("MASTERCARD");
        cartao.setNumero("7951301472583690");
        cartao.setDataExpiracao(getData(19, Calendar.APRIL, 2020));
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
    public void t12_cartoesExpirados() {
        logger.log(Level.INFO, "Executando t12: SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE");
        TypedQuery<Cartao> query = em.createQuery(
                "SELECT c FROM Cartao c WHERE c.dataExpiracao < CURRENT_DATE",
                Cartao.class);
        List<Cartao> cartoesExpirados = query.getResultList();
        assertEquals(1, cartoesExpirados.size());
    }

    @Test
    public void t13_bandeirasDistintas() {
        logger.log(Level.INFO, "Executando t13: SELECT DISTINCT(c.bandeira) FROM CartaoCredito c ORDER BY c.bandeira");
        TypedQuery<String> query
                = em.createQuery("SELECT DISTINCT(c.bandeira) FROM Cartao c ORDER BY c.bandeira",
                        String.class);
        List<String> bandeiras = query.getResultList();
        assertEquals(4, bandeiras.size());
    }

    @Test
    public void t14_cartoesMastercard() {
        logger.info("Executando t14: SELECT c FROM Cartao c WHERE c.bandeira = MASTERCARD ORDER BY c.id");
        TypedQuery<Cartao> query;
        query = em.createQuery("SELECT c FROM Cartao c WHERE c.bandeira LIKE ?1", Cartao.class);
        query.setParameter(1, "MASTERCARD");
        List<Cartao> master = query.getResultList();
        assertEquals(2, master.size());

    }

    @Test
    public void t15_pedidosNegados() {
        logger.info("Executando t15: SELECT p FROM Pedido p WHERE p.statusCompra = NEGADO");
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
    public void t16_findAllClientsNames() {
        logger.log(Level.INFO, "Executando t016: Find All Clients Names");
        Query query = em.createNamedQuery("Nomes.Clientes");
        List<Object> resultados = query.getResultList();
        assertEquals(6, resultados.size());

    }

    @Test
    public void t17_findAllClientsNamesSQL() {
        logger.log(Level.INFO, "Executando t17: Nomes.ClientesSQL");
        Query query = em.createNamedQuery("Nomes.ClientesSQL");
        List<Cliente> clientes = query.getResultList();
        assertEquals(6, clientes.size());

    }

    @Test
    public void t18_bebidaAlcoolicaMaisCara() {
        logger.log(Level.INFO, "Executando t18: SELECT max(ba.preco) FROM BebidaAlcoolica ba");
        Query query;
        query = em.createQuery("SELECT max(ba.preco) FROM BebidaAlcoolica ba");
        Object resultado = (Object) query.getSingleResult();
        Double preco = (Double) resultado;
        assertTrue(preco.longValue() == 109);

    }

    @Test
    public void t19_bebidaComumMaisBarata() {
        logger.log(Level.INFO, "Executando t19: SELECT min(bc.preco) FROM BebidaComum bc");
        Query query;
        query = em.createQuery("SELECT min(bc.preco) FROM BebidaComum bc");
        Object resultado = (Object) query.getSingleResult();
        Double preco = (Double) resultado;
        assertTrue(preco.longValue() == 2);

    }

    @Test
    public void t20_clienteCartao() {
        logger.log(Level.INFO, "Executando t20: SELECT c.nome, cc.bandeira FROM Cliente c LEFT OUTER JOIN c.cartao cc");
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
    public void t21_clientePedidos() {
        logger.log(Level.INFO, "Executando t21: SELECT c.nome, p.id FROM Cliente c JOIN FETCH c.pedidos p WHERE c.id = 4");
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
    public void t22_persistirBebidaInvalida() {
        logger.log(Level.INFO, "Executando t22: Persistir Bebida Invalida");
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

    @Test
    public void t23_pedidoMaisRecente() {
        logger.info("Executando t23: SELECT o FROM Oferta o WHERE o.data >= ALL (SELECT o1.data FROM Oferta o1))");
        TypedQuery<Pedido> query;
        query = em.createQuery(
                "SELECT p FROM Pedido p WHERE p.dataPedido >= ALL (SELECT p1.dataPedido FROM Pedido p1)",
                Pedido.class);
        List<Pedido> pedidos = query.getResultList();
        assertEquals(1, pedidos.size());
        if (logger.isLoggable(Level.INFO)) {
            Pedido pedido = pedidos.get(0);
            logger.log(Level.INFO, "{0}: {1}", new Object[]{pedido.getDataPedido().toString(), pedido.getId()});
        }

    }

    @Test
    public void t24_clienteQuantidadePedido() {
        logger.info("Executando t24: Cliente.QuantidadePedidosSQL");
        Query query;
        query = em.createNamedQuery("Cliente.QuantidadePedidosSQL");
        query.setParameter(1, "Bianca Leopoldo");
        List<Object[]> resultados = query.getResultList();
        assertEquals(1, resultados.size());

        if (logger.isLoggable(Level.INFO)) {
            for (Object[] resultado : resultados) {
                logger.log(Level.INFO, "{0}: {1}", resultado);
            }
        }

    }

//    @Test
//    public void t25_delete() {
//        logger.info("Executando t25: DELETE FROM Pedido p WHERE p.dataPedido <= ?1");
//        Query query = em.createQuery("DELETE FROM Pedido p WHERE p.dataPedido <= ?1");
//        query.setParameter(1, getData(30, Calendar.MARCH, 2018));
//        List<Pedido> resultado = query.getResultList();
//        for (int i = 0; i < resultado.size(); i++) {
//            for (int j = 0; j < resultado.get(i).getItensSelecionados().size(); j++) {
//                Item item = em.find(Item.class, resultado.get(i).getItensSelecionados().get(j));
//                resultado.remove(item);
//            }
//        }
//        query.executeUpdate();
//        Pedido pedido = em.find(Pedido.class, new Long(8));
//        assertNull(pedido);
//        pedido = em.find(Pedido.class, new Long(11));
//        assertNull(pedido);
//        pedido = em.find(Pedido.class, new Long(12));
//        assertNull(pedido);
//        logger.log(Level.INFO, "Pedidos removidos com sucesso.");
//    }

//    @Test
//    public void t23_persistirPedidoValido(){
//        logger.log(Level.INFO, "Executando t23: Persistir Pedido Valido");
//        Bebida bebida = em.find(Bebida.class, new Long(7));
//        Item item1 = new Item();
//        item1.adicionarBebida(bebida, 2);
//        bebida = em.find(Bebida.class, new Long(5));
//        Item item2 = new Item();
//        item2.adicionarBebida(bebida, 1);
//        Pedido pedido = new Pedido();
//        pedido.setStatusCompra(StatusCompra.APROVADO);
//        item1.setPedido(pedido);
//        pedido.addItem(item1);
//        item2.setPedido(pedido);
//        pedido.addItem(item2);
//        Cliente cliente = em.find(Cliente.class, new Long(3));
//        pedido.setCliente(cliente);
//        cliente.addPedidos(pedido);
//        em.persist(pedido);
//        em.flush();
//        em.clear();
//        pedido = em.find(Pedido.class, pedido.getId());
//        assertNotNull(pedido.getId());
//        assertEquals(2, pedido.getItensSelecionados().size());
//        assertNotNull(pedido.getItensSelecionados().get(0).getId());
//        assertNotNull(pedido.getItensSelecionados().get(1).getId());
//        logger.log(Level.INFO, "Pedido incluído com sucesso.", pedido);
//    }
}
