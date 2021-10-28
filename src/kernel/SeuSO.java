package kernel;
import java.util.*;

import operacoes.*;
import escalonadores.*;

public class SeuSO extends SO {
	escalonadores.Escalonador escalonador;
	private List<PCB> novos = new LinkedList<>();
	private List<PCB> prontos = new LinkedList<>();
	private List<PCB> esperando = new LinkedList<>();
	private List<PCB> terminados = new LinkedList<>();
	private Map<Integer, LinkedList<OperacaoES>> opsDispES = new TreeMap<>();
	private PCB atual;
	private Escalonador e;
	private int pCount = 0;

	@Override
	// ATENCÃO: cria o processo mas o mesmo
	// só estará "pronto" no próximo ciclo
	protected void criaProcesso(Operacao[] codigo) {
		PCB pcb = new PCB();
		pcb.codigo = codigo;
		pcb.contadorDePrograma = 0;
		pcb.idProcesso = pCount;
		pCount++;
		novos.add(pcb);
	}

	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {//pq tem o pcbAtual se ja tem o atual? fizemo caquinha
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
		if (opsDispES.get(idDispositivo).isEmpty()) return null;
		return opsDispES.get(idDispositivo).get(0);
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		if(atual.estado == PCB.Estado.PRONTO) atual.estado = PCB.Estado.EXECUTANDO;
		int i = atual.contadorDePrograma;
		while (i < atual.codigo.length) {
			Operacao op = atual.codigo[i];
			atual.contadorDePrograma++;
			if (op instanceof Soma || op instanceof Carrega) return op;
			else addOperacaoES(op);
			i = atual.contadorDePrograma;
		}
		return null;
	}

	protected void addOperacaoES(Operacao op) {
		OperacaoES opES = (OperacaoES) op;
		if (opsDispES.isEmpty()) inicializaMap(opsDispES);
		opsDispES.get(opES.idDispositivo).add(opES);
	}

	protected void inicializaMap(Map<Integer, LinkedList<OperacaoES>> map) {
		for(int i = 0; i < 5; i++)
			map.put(i, new LinkedList<OperacaoES>());
	}
	@Override
	protected void executaCicloKernel() {
		escalonador.setAtual(atual);
		atual = escalonador.executaCiclo(novos, prontos, terminados);
	}

	@Override
	protected boolean temTarefasPendentes() {
		return !prontos.isEmpty() || !novos.isEmpty();//esperando
	}

	@Override
	protected Integer idProcessoNovo() {
		if (novos.isEmpty()) return null;
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
		if (atual == null) return null;
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
		criaEscalonador();
	}

	protected void criaEscalonador() {
		switch (e) {
			case FIRST_COME_FIRST_SERVED -> this.escalonador = new FCFS();
			default -> throw new RuntimeException("Escalonador inválido.");
		}
	}
}
