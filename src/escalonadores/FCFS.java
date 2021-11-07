package escalonadores;

import kernel.*;
import operacoes.*;
import java.util.*;

public class FCFS extends Escalonador {
    @Override
    public void executaCiclo(List<PCB> prontos) {
        if (getAtual().contadorDePrograma == getAtual().codigo.length) setProcessoTerminado(true);
        else {
            setProcessoTerminado(false);
            Operacao op = getAtual().codigo[getAtual().contadorDePrograma];
            setOpES(op instanceof OperacaoES);
        }
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        for (PCB pcb : prontos) if (pcb.estado != PCB.Estado.EXECUTANDO) return pcb;
        return null;
    }
}