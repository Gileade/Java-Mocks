package br.com.gile.leilao.service;

import java.time.LocalDate;

import br.com.gile.leilao.model.Lance;
import br.com.gile.leilao.model.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gile.leilao.dao.PagamentoDao;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;

	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos) {
		this.pagamentos = pagamentos;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now().plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, vencimento);
		this.pagamentos.salvar(pagamento);
	}

}
