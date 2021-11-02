package escalonadores;

import kernel.PCB;
import operacoes.Carrega;
import operacoes.Soma;

import java.util.LinkedList;
import java.util.List;

public class RRQF extends Escalonador{
    int quantum = 0;

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
}
