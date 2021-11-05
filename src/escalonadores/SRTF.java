package escalonadores;

import kernel.PCB;
import operacoes.*;
import java.util.*;

public class SRTF extends Escalonador{

    @Override
    public void executaCiclo(List<PCB> prontos) {
        if (getAtual().contadorDePrograma == getAtual().codigo.length) setProcessoTerminado(true);
        else {
            setProcessoTerminado(false);
            Operacao op = getAtual().codigo[getAtual().contadorDePrograma];
            setOpES(op instanceof OperacaoES);
            if (!isOpES() || !isTrocaProcesso()) {
                getAtual().burstAtual++;
                if (getAtual().tempoRestEstimado > 0) getAtual().tempoRestEstimado--;
            }
            verificaTempoRestEstimado(prontos);
        }
    }

    private void verificaTempoRestEstimado(List<PCB> prontos) {
        for (PCB pcb : prontos)
            if (pcb.tempoRestEstimado < getAtual().tempoRestEstimado)
                setTrocaProcesso(true);
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        PCB menorBurst = null;
        if (isOpES() || isProcessoTerminado()) {
            getAtual().atualizaEstimativa();
            for (PCB pcb : prontos) {
                if (pcb.estado != PCB.Estado.EXECUTANDO) {
                    if (menorBurst == null) menorBurst = pcb;
                    if (pcb.tempoRestEstimado < menorBurst.tempoRestEstimado) menorBurst = pcb;
                }
            }
        } else {
            if (!prontos.isEmpty()) menorBurst = prontos.get(0);
            for (PCB pcb : prontos)
                if (pcb.tempoRestEstimado < menorBurst.tempoRestEstimado) menorBurst = pcb;
        }
        return menorBurst;
    }
}
