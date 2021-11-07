package escalonadores;

import kernel.*;
import java.util.*;

public abstract class Escalonador {
    private PCB atual;
    private boolean processoTerminado = false;
    private boolean opES = false;
    private boolean trocaProcesso = false;

    public void executaCiclo(List<PCB> prontos) {}

    public PCB escolheProximo(List<PCB> prontos) {
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

    public boolean isOpES() {
        return opES;
    }

    public void setOpES(boolean opES) {
        this.opES = opES;
    }

    public boolean isTrocaProcesso() {
        return trocaProcesso;
    }

    public void setTrocaProcesso(boolean trocaProcesso) {
        this.trocaProcesso = trocaProcesso;
    }
}