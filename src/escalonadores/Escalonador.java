package escalonadores;

import kernel.*;
import operacoes.Carrega;
import operacoes.Operacao;
import operacoes.Soma;

import java.util.*;

public abstract class Escalonador {
    private PCB atual;
    private boolean processoTerminado = false;
    private boolean opCPU = true;

    public void executaCiclo() {
        if (atual.contadorDePrograma == atual.codigo.length) processoTerminado = true;
        else {
            processoTerminado = false;
            Operacao op = atual.codigo[atual.contadorDePrograma];
            opCPU = op instanceof Soma || op instanceof Carrega;
        }
    }

    public PCB escolheProximo(List<PCB> prontos) {
        if (!prontos.isEmpty()) return prontos.get(0);
        return null;
    }

    public PCB getAtual() {
        return atual;
    }

    public void setAtual(PCB atual) {
        this.atual = atual;
    }

    public boolean isProcessoTerminado() {
        return processoTerminado;
    }

    public void setProcessoTerminado(boolean processoTerminado) {
        this.processoTerminado = processoTerminado;
    }

    public boolean isOpCPU() {
        return opCPU;
    }

    public void setOpCPU(boolean opCPU) {
        this.opCPU = opCPU;
    }
}
