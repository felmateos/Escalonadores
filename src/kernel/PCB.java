package kernel;
import operacoes.Operacao;

public class PCB{
	public enum Estado {NOVO, PRONTO, EXECUTANDO, ESPERANDO, TERMINADO};
	public int idProcesso; // primeiro processo criado deve ter id = 0
	public Estado estado = Estado.NOVO;
	public int[] registradores = new int[5];
	public int contadorDePrograma = 0;
	public Operacao[] codigo;
	int pendencias = 0;
}