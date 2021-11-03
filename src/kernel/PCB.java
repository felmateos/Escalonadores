package kernel;
import operacoes.Operacao;

public class PCB implements Comparable<PCB>{
	public enum Estado {NOVO, PRONTO, EXECUTANDO, ESPERANDO, TERMINADO};
	public int idProcesso = 0;
	public Estado estado = Estado.NOVO;
	public int[] registradores = new int[5];
	public int contadorDePrograma = 0;
	public Operacao[] codigo;
	public boolean respondeu = false;
	public int estimativa = 5;
	public int burstAtual = 0;

	@Override
	public int compareTo(PCB outro) {
		return Integer.compare(this.idProcesso, outro.idProcesso);
	}

	public void mudaEstimativa() {
		estimativa = (estimativa + burstAtual) / 2;
		burstAtual = 0;
	}

}