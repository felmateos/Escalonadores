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
	private Map<Integer, LinkedList<Pendencia>> opsDispES = new TreeMap<>();
	private Escalonador e;
	private int pCount = 0;

	@Override
	// ATENCÃO: cria o processo mas o mesmo
	// só estará "pronto" no próximo ciclo
	protected void criaProcesso(Operacao[] codigo) {
		PCB pcb = new PCB();
		pcb.codigo = codigo;
		pcb.idProcesso = pCount;
		pCount++;
		novos.add(pcb);
	}

	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		pcbAtual.registradores = processador.registradores;
		if (escalonador.isProcessoTerminado()) {
			pcbAtual.estado = PCB.Estado.TERMINADO;
			terminados.add(pcbAtual);
		} else if (escalonador.isUltimaOpCPU()) {
			pcbAtual.estado = PCB.Estado.ESPERANDO;
			esperando.add(pcbAtual);
		}
		Arrays.fill(processador.registradores, 0);
		escalonador.setAtual(pcbProximo);
		if (escalonador.getAtual() != null) escalonador.getAtual().estado = PCB.Estado.EXECUTANDO;
		escalonador.setProcessoTerminado(false);
		escalonador.setUltimaOpCPU(false);
	}
	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		if (opsDispES.isEmpty()) inicializaMap(opsDispES);
		while (true) {
			if (opsDispES.get(idDispositivo).isEmpty()) return null;
			OperacaoES op = opsDispES.get(idDispositivo).get(0).getOpES();
			if (op.ciclos == 0) eliminaPendencia(idDispositivo);
			else return opsDispES.get(idDispositivo).get(0).getOpES();
		}
	}

	protected void eliminaPendencia(int idDispositivo) {
		PCB pcbAtual = opsDispES.get(idDispositivo).get(0).getPcb();
		opsDispES.get(idDispositivo).remove(0);
		pcbAtual.pendencias--;
		if (pcbAtual.pendencias == 0 && pcbAtual.estado == PCB.Estado.ESPERANDO && pcbAtual.contadorDePrograma == pcbAtual.codigo.length) {
			pcbAtual.estado = PCB.Estado.TERMINADO;
			esperando.remove(pcbAtual);
			if (!terminados.contains(pcbAtual)) terminados.add(pcbAtual);
		}
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		if (escalonador.getAtual() == null) return operacaoNula();
		while (escalonador.getAtual().contadorDePrograma < escalonador.getAtual().codigo.length) {
			Operacao op = escalonador.getAtual().codigo[escalonador.getAtual().contadorDePrograma];
			escalonador.getAtual().contadorDePrograma++;
			if (op instanceof Soma || op instanceof Carrega) return op;
			else addOperacaoES(op);
		}
		return operacaoNula();
	}

	protected Operacao operacaoNula() {
		return new Carrega(0,processador.registradores[0]);
	}

	protected void addOperacaoES(Operacao op) {
		OperacaoES opES = (OperacaoES) op;
		if (opsDispES.isEmpty()) inicializaMap(opsDispES);
		escalonador.getAtual().pendencias++;
		opsDispES.get(opES.idDispositivo).add(new Pendencia(escalonador.getAtual(), opES));
	}

	protected void inicializaMap(Map<Integer, LinkedList<Pendencia>> map) {
		for(int i = 0; i < 5; i++) map.put(i, new LinkedList<Pendencia>());
	}

	@Override
	protected void executaCicloKernel() {
		escalonador.executaCiclo(novos, prontos, terminados);
		if (escalonador.getAtual() != null) {
			if (escalonador.getAtual().contadorDePrograma == escalonador.getAtual().codigo.length && escalonador.getAtual().pendencias == 0)
				escalonador.setProcessoTerminado(true);
			escalonador.verificaOpCPU();
			if (escalonador.getAtual().contadorDePrograma == escalonador.getAtual().codigo.length) {
				prontos.remove(escalonador.getAtual());
				trocaContexto(escalonador.getAtual(), escalonador.escolheProximo(prontos));
			}
		}
	}

	@Override
	protected boolean temTarefasPendentes() {
		return !prontos.isEmpty() || !novos.isEmpty() || !esperando.isEmpty();
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
		if (escalonador.getAtual() == null) return null;
		return escalonador.getAtual().idProcesso;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		List<Integer> ie = new LinkedList<>();
		for (PCB pcb : esperando) ie.add(pcb.idProcesso);
		Collections.sort(ie);
		return ie;
	}

	@Override
	protected List<Integer> idProcessosTerminados() {
		List<Integer> it = new LinkedList<>();
		for (PCB pcb : terminados) it.add(pcb.idProcesso);
		Collections.sort(it);
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
			case FIRST_COME_FIRST_SERVED -> this.escalonador = new FCFS();//////////
			case SHORTEST_JOB_FIRST -> this.escalonador = new FCFS();//////////
			case SHORTEST_REMANING_TIME_FIRST -> this.escalonador = new FCFS();//////////
			case ROUND_ROBIN_QUANTUM_5 -> this.escalonador = new FCFS();//////////
			default -> throw new RuntimeException("Escalonador inválido.");
		}
	}
}