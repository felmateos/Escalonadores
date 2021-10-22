import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import kernel.SO;
import kernel.SO.Escalonador;
import kernel.SeuSO;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		List<Escalonador> escalonadores = new LinkedList<>();	
		escalonadores.add(SO.Escalonador.FIRST_COME_FIRST_SERVED);
		escalonadores.add(SO.Escalonador.SHORTEST_JOB_FIRST);
		escalonadores.add(SO.Escalonador.SHORTEST_REMANING_TIME_FIRST);
		escalonadores.add(SO.Escalonador.ROUND_ROBIN_QUANTUM_5);
		
		for(Escalonador esc : escalonadores) {
			SO so = new SeuSO();
			so.defineEscalonador(esc);
			so.leArquivoEntrada(args[0]);
			so.simula();
		}
	}
}
