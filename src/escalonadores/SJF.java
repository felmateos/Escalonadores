package escalonadores;

import kernel.PCB;
import operacoes.*;
import java.util.List;

public class SJF extends Escalonador{

    @Override
    public void executaCiclo(List<PCB> prontos) {
        if (getAtual().contadorDePrograma == getAtual().codigo.length) setProcessoTerminado(true);
        else {
            setProcessoTerminado(false);
            Operacao op = getAtual().codigo[getAtual().contadorDePrograma];
            setOpES(op instanceof OperacaoES);
            if (!isOpES()) getAtual().burstAtual++;
        }
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        getAtual().atualizaEstimativa();
        PCB menorBurst = null;
        for (PCB pcb : prontos) {
            if (pcb.estado != PCB.Estado.EXECUTANDO) {
                if (menorBurst == null) menorBurst = pcb;
                if (pcb.estimativa < menorBurst.estimativa) menorBurst = pcb;
            }
        }
        return menorBurst;
    }
}
