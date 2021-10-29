package escalonadores;

import kernel.*;
import java.util.*;

public abstract class Escalonador {
    private PCB atual;
    private boolean ultimaOpCPU = false;
    private boolean processoTerminado = false;

    public void executaCiclo(List<PCB> novos, List<PCB> prontos, List<PCB> terminados){}

    public void verificaOpCPU() {}

    public PCB escolheProximo(List<PCB> prontos) {
        return null;
    }

    public PCB getAtual() {
        return atual;
    }

    public void setAtual(PCB atual) {
        this.atual = atual;
    }

    public boolean isUltimaOpCPU() {
        return ultimaOpCPU;
    }

    public void setUltimaOpCPU(boolean ultimaOpCPU) {
        this.ultimaOpCPU = ultimaOpCPU;
    }

    public boolean isProcessoTerminado() {
        return processoTerminado;
    }

    public void setProcessoTerminado(boolean troca) {
        this.processoTerminado = troca;
    }
}
