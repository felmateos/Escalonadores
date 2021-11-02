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
	private Map<Integer, LinkedList<Pendencia>> listaES = new TreeMap<>();
	private int pCount = 0;

	@Override
	protected void criaProcesso(Operacao[] codigo) {
		PCB pcb = new PCB();
		pcb.codigo = codigo;
		pcb.idProcesso = pCount;
		novos.add(pcb);
		pCount++;
	}

	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		pcbAtual.registradores = processador.registradores;
		Arrays.fill(processador.registradores, 0);
		prontos.remove(pcbAtual);
		if (escalonador.isProcessoTerminado() && escalonador.getAtual().idProcesso != -1) {
			terminados.add(pcbAtual);
			pcbAtual.estado = PCB.Estado.TERMINADO;
			escalonador.setProcessoTerminado(false);
		} else if (!escalonador.isOpCPU() && escalonador.getAtual().idProcesso != -1) {
			esperando.add(pcbAtual);
			pcbAtual.estado = PCB.Estado.ESPERANDO;
			adicionaOperacaoES(pcbAtual);
		}
		escalonador.setAtual(processoNulo());
		if (pcbProximo != null) escalonador.setAtual(pcbProximo);
	}

	@Override
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		if (listaES.isEmpty()) return null;
		if (listaES.get(idDispositivo).isEmpty()) return null;
		Pendencia pendencia = listaES.get(idDispositivo).get(0);
		if (pendencia.getOpES().ciclos == 0) eliminaPendencia(idDispositivo, pendencia);
		if (!listaES.get(idDispositivo).isEmpty()) return listaES.get(idDispositivo).get(0).getOpES();
		return null;
	}

	protected void eliminaPendencia(int idDispositivo, Pendencia pendencia) {
		PCB pcb = pendencia.getPcb();
		listaES.get(idDispositivo).remove(pendencia);
		esperando.remove(pcb);
		pcb.contadorDePrograma++;
		if (pcb.contadorDePrograma == pcb.codigo.length) {
			pcb.estado = PCB.Estado.TERMINADO;
			terminados.add(pendencia.getPcb());
		} else if (pcb.codigo[pcb.contadorDePrograma] instanceof OperacaoES) {
				adicionaOperacaoES(pcb);
				} else {
					pcb.estado = PCB.Estado.PRONTO;
					prontos.add(pcb);
				}
	}

	protected void adicionaOperacaoES(PCB pcb) {
		OperacaoES opES = (OperacaoES) pcb.codigo[pcb.contadorDePrograma];
		if (listaES.isEmpty()) inicializaMap(listaES);
		listaES.get(opES.idDispositivo).add(new Pendencia(pcb, opES));
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		if (escalonador.getAtual() == null) return operacaoNula();
		Operacao op = escalonador.getAtual().codigo[escalonador.getAtual().contadorDePrograma];
		escalonador.getAtual().contadorDePrograma++;
		return op;
	}

	protected void inicializaMap(Map<Integer, LinkedList<Pendencia>> map) {
		for(int i = 0; i < 5; i++) map.put(i, new LinkedList<Pendencia>());
	}

	@Override
	protected void executaCicloKernel() {
		if (prontos.size() == 0 && esperando.size() == 0 && terminados.size() == 0)
			escalonador.setAtual(processoNulo());
		escalonador.executaCiclo();
        if (pCount > 1) atualizaEstado();
		if (escalonador.isProcessoTerminado() || !escalonador.isOpCPU()) {
			prontos.remove(escalonador.getAtual());
			trocaContexto(escalonador.getAtual(), escalonador.escolheProximo(prontos));
		}
	}

	public void atualizaEstado() {
		if (novos.isEmpty()) return;
		if (novos.get(0).codigo[0] instanceof Soma || novos.get(0).codigo[0] instanceof Carrega) {
			novos.get(0).estado = PCB.Estado.PRONTO;
			prontos.add(novos.get(0));
		} else {
			novos.get(0).estado = PCB.Estado.ESPERANDO;
			esperando.add(novos.get(0));
			adicionaOperacaoES(novos.get(0));
		}
		novos.remove(novos.get(0));
	}

	public PCB processoNulo() {
		PCB nulo = new PCB();
		nulo.idProcesso = -1;
		nulo.codigo = new Operacao[1];
		nulo.codigo[0] = operacaoNula();
		return nulo;
	}

	protected Operacao operacaoNula() {
		return new Carrega(0, processador.registradores[0]);
	}

	@Override
	protected boolean temTarefasPendentes() {
		return !novos.isEmpty() || !prontos.isEmpty() || !esperando.isEmpty();
	}

	@Override
	protected Integer idProcessoNovo() {
		if (novos.isEmpty()) return null;
		return novos.get(0).idProcesso;
	}

	@Override
	protected List<Integer> idProcessosProntos() {
		List<Integer> ip = new LinkedList<>();
		for (PCB p : prontos) ip.add(p.idProcesso);
		return ip;
	}

	@Override
	protected Integer idProcessoExecutando() {
		if (escalonador.getAtual() == null || escalonador.getAtual().idProcesso == -1) return null;
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
	protected int tempoEsperaMedio() { //Tempo que ficou em espera
		return 0;
		/*int somaEspera = 0;
		for(int te : tEspera) {
			System.out.println(te);
			somaEspera += te;
		}
		return somaEspera/tEspera.size();///metodo de media de listas em geral!!!*/
	}

	@Override
	protected int tempoRespostaMedio() { //Tempo para a 1a execucao
		return 0;
	}

	@Override
	protected int tempoRetornoMedio() { //Termino - Inicio
		return 0;
	}

	@Override
	protected int trocasContexto() { //Trocas de contexto
		return 0;
	}

	@Override
	public void defineEscalonador(Escalonador e) {
		switch (e) {
			case FIRST_COME_FIRST_SERVED -> this.escalonador = new FCFS();//////////
			case SHORTEST_JOB_FIRST -> this.escalonador = new FCFS();//////////
			case SHORTEST_REMANING_TIME_FIRST -> this.escalonador = new FCFS();//////////
			case ROUND_ROBIN_QUANTUM_5 -> this.escalonador = new RRQF();//////////
			default -> throw new RuntimeException("Escalonador inválido.");
		}
	}
}