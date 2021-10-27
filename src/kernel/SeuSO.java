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
		if (opsDispES.get(idDispositivo).isEmpty()) return null;
		return opsDispES.get(idDispositivo).get(0);
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		if(atual.estado == PCB.Estado.PRONTO) atual.estado = PCB.Estado.EXECUTANDO;
		for (int i = atual.contadorDePrograma; i < atual.codigo.length; i++) {
			Operacao op = atual.codigo[i];
			if (op instanceof Soma || op instanceof Carrega) {
				atual.contadorDePrograma++;
				if (atual.contadorDePrograma == atual.codigo.length) atual = null;
				return op;
			} else addOperacaoES(op);
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
	    switch(e) {
            case FIRST_COME_FIRST_SERVED -> executaCicloFCFS();
            case SHORTEST_JOB_FIRST -> executaCicloSJF();
            case SHORTEST_REMANING_TIME_FIRST -> executaCicloSRTF();
            case ROUND_ROBIN_QUANTUM_5 -> executaCicloRRQF();
            default -> throw new RuntimeException("Escalonador Inválido.");
        }
	}

	protected void executaCicloFCFS() {
		if (!novos.isEmpty()) {
			PCB n = novos.get(0);
			n.estado = PCB.Estado.PRONTO;
			prontos.add(n);
			novos.remove(n);
			if (atual == null) atual = prontos.get(0);
		}
    }

    protected void executaCicloSJF() {

    }

    protected void executaCicloSRTF() {

    }

    protected void executaCicloRRQF() {

    }

	@Override
	protected boolean temTarefasPendentes() {
		return !prontos.isEmpty() || !novos.isEmpty();
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
