package br.com.gile.leilao.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import br.com.gile.leilao.model.Lance;
import br.com.gile.leilao.model.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gile.leilao.dao.PagamentoDao;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;
	private Clock clock;

	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos, Clock clock) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}

	private LocalDate proximoDiaUtil(LocalDate dataBase) {
		DayOfWeek diaDaSemana = dataBase.getDayOfWeek();
		if (diaDaSemana == DayOfWeek.SATURDAY){
			return dataBase.plusDays(2);
		}else if (diaDaSemana == DayOfWeek.SUNDAY){
			return dataBase.plusDays(1);
		}

		return dataBase;
	}

}
