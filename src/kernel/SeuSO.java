package kernel;
import java.util.*;

import operacoes.Carrega;
import operacoes.Operacao;
import operacoes.OperacaoES;
import operacoes.Soma;

public class SeuSO extends SO {
	private List<PCB> novos = new LinkedList<>();
	private List<PCB> prontos = new LinkedList<>();
	private PCB atual;
	private Escalonador e;

	@Override
	// ATENCÃO: cria o processo mas o mesmo
	// só estará "pronto" no próximo ciclo
	protected void criaProcesso(Operacao[] codigo) {
		PCB pcb = new PCB();
		pcb.codigo = codigo;
		pcb.contadorDePrograma = 0;
		pcb.idProcesso = novos.size();
		novos.add(pcb);

	}

	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		pcbAtual.registradores = processador.registradores;
		atual = pcbProximo;
		processador.registradores = atual.registradores;
	}

	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		Operacao op = atual.codigo[atual.contadorDePrograma];
		if (op instanceof OperacaoES)
			return (OperacaoES) op;
		return null;///////////procura a prox
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		Operacao op = atual.codigo[atual.contadorDePrograma];
		if (op instanceof Soma || op instanceof Carrega)
			return op;
		return null;///////////procura a prox
	}

	@Override
	protected void executaCicloKernel() {
		for(PCB p : novos) {
			p.estado = PCB.Estado.PRONTO;
			prontos.add(p);
			novos.remove(p);
		}//////////n sei se sao todos de uma vez (acho q nao)
	}

	@Override
	protected boolean temTarefasPendentes() {
		return !prontos.isEmpty();///////////
	}

	@Override
	protected Integer idProcessoNovo() {
		return novos.get(novos.size()-1).idProcesso;
	}

	@Override
	protected List<Integer> idProcessosProntos() {
		List<Integer> ipp = new LinkedList<>();
		for (PCB p : prontos)
			ipp.add(p.idProcesso);
		return ipp;
	}

	@Override
	protected Integer idProcessoExecutando() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Integer> idProcessosTerminados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int tempoEsperaMedio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int tempoRespostaMedio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int tempoRetornoMedio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int trocasContexto() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void defineEscalonador(Escalonador e) {
		this.e = e;
	}
}
