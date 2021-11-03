package escalonadores;

import kernel.PCB;
import operacoes.*;
import java.util.List;

public class SJF extends Escalonador{

    @Override
    public void executaCiclo(List<PCB> prontos) {
        if (super.getAtual().contadorDePrograma == super.getAtual().codigo.length) setProcessoTerminado(true);
        else {
            setProcessoTerminado(false);
            Operacao op = super.getAtual().codigo[super.getAtual().contadorDePrograma];
            setOpES(op instanceof OperacaoES);
        }
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        super.getAtual().mudaEstimativa();
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
