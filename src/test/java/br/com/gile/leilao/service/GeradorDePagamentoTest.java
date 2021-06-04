package br.com.gile.leilao.service;

import br.com.gile.leilao.dao.LeilaoDao;
import br.com.gile.leilao.dao.PagamentoDao;
import br.com.gile.leilao.model.Lance;
import br.com.gile.leilao.model.Leilao;
import br.com.gile.leilao.model.Pagamento;
import br.com.gile.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class GeradorDePagamentoTest {

    private GeradorDePagamento gerador;

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    public void beforEach(){
        MockitoAnnotations.initMocks(this);
        this.gerador = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao(){
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        gerador.gerarPagamento(vencedor);

        //Captura um objeto de dentro do método testado
        Mockito.verify(pagamentoDao).salvar(captor.capture());
        Pagamento pagamento = captor.getValue();

        Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assert.assertEquals(vencedor.getValor(), pagamento.getValor());
        Assert.assertFalse(pagamento.getPago());
        Assert.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        Assert.assertEquals(leilao, pagamento.getLeilao());
    }

    private Leilao leilao(){
        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Gile"));
        Lance lance = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);
        return leilao;
    }
}