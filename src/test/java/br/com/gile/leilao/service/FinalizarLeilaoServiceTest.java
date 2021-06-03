package br.com.gile.leilao.service;

import br.com.gile.leilao.dao.LeilaoDao;
import br.com.gile.leilao.model.Lance;
import br.com.gile.leilao.model.Leilao;
import br.com.gile.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class FinalizarLeilaoServiceTest {

    private FinalizarLeilaoService service;

    @Mock
    private LeilaoDao leilaoDao;
    @Mock
    private EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    public void beforEach(){
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
    }

    @Test
    void deveriaFinalizarUmLeilao(){
        List<Leilao> leiloes = leiloes();

        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

        service.finalizarLeiloesExpirados();

        Leilao leilao = leiloes.get(0);

        Assert.assertTrue(leilao.isFechado());
        Assert.assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());

        //Verifica se o método salvar foi chamado
        Mockito.verify(leilaoDao).salvar(leilao);
    }

    @Test
    void deveriaEnviarUmEmailParaOVencedorDoLeilao(){
        List<Leilao> leiloes = leiloes();

        Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);

        service.finalizarLeiloesExpirados();

        Leilao leilao = leiloes.get(0);
        Lance lanceVendedor = leilao.getLanceVencedor();

        //Verifica se o método enviarEmailVencedorLeilaos foi chamado
        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVendedor);
    }

    private List<Leilao> leiloes(){
        List<Leilao> lista = new ArrayList<>();

        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Gile"));

        Lance primeiro = new Lance(new Usuario("Fulano"), new BigDecimal("600"));
        Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        lista.add(leilao);

        return lista;
    }
}