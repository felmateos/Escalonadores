package escalonadores;

import kernel.PCB;
import operacoes.*;
import java.util.*;

public class RRQF extends Escalonador{
    int quantum = 0;

    @Override
    public void executaCiclo(List<PCB> prontos) {
        if (getAtual().contadorDePrograma == getAtual().codigo.length) setProcessoTerminado(true);
        else {
            quantum++;
            setTrocaProcesso(quantum == 5);
            setProcessoTerminado(false);
            Operacao op = getAtual().codigo[getAtual().contadorDePrograma];
            setOpES(op instanceof OperacaoES);
        }
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        quantum = 0;
        for (PCB pcb : prontos) if (pcb.estado != PCB.Estado.EXECUTANDO) return pcb;
        return null;
    }
}