package kernel;
import java.util.*;

import operacoes.Carrega;
import operacoes.Operacao;
import operacoes.OperacaoES;
import operacoes.Soma;

public class SeuSO extends SO {
	private List<PCB> novos = new LinkedList<>();
	private List<PCB> prontos = new LinkedList<>();
	private List<PCB> esperando = new LinkedList<>();
	private List<PCB> terminados = new LinkedList<>();
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
		pcbAtual.estado = PCB.Estado.ESPERANDO;
		esperando.add(pcbAtual);
		atual = pcbProximo;
		atual.estado = PCB.Estado.EXECUTANDO;
		processador.registradores = atual.registradores;
	}

	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		return null;///////////procura a prox
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		for (int i = atual.contadorDePrograma; i < atual.codigo.length; i++) {
			Operacao op = atual.codigo[i];
			if (op instanceof Soma || op instanceof Carrega) return op;
		}
		return null;
	}

	@Override
	protected void executaCicloKernel() {
		novos.get(idProcessoNovo()).estado = PCB.Estado.PRONTO;/////acho q é um de cada vez(tvz n)
	}

	@Override
	protected boolean temTarefasPendentes() {
		return !prontos.isEmpty() || !novos.isEmpty();
	}

	@Override
	protected Integer idProcessoNovo() {
		return novos.get(novos.size()-1).idProcesso;
	}

	@Override
	protected List<Integer> idProcessosProntos() {
		List<Integer> ipp = new LinkedList<>();
		for (PCB p : prontos) ipp.add(p.idProcesso);
		return ipp;
	}

	@Override
	protected Integer idProcessoExecutando() {
		return atual.idProcesso;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		List<Integer> ie = new LinkedList<>();
		for (PCB pcb : esperando) ie.add(pcb.idProcesso);
		return ie;
	}

	@Override
	protected List<Integer> idProcessosTerminados() {
		List<Integer> it = new LinkedList<>();
		for (PCB pcb : terminados) it.add(pcb.idProcesso);
		return it;
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
