package escalonadores;

import kernel.*;
import operacoes.*;
import java.util.*;

public abstract class Escalonador {
    private PCB atual;
    private boolean processoTerminado = false;
    private boolean opES = false;
    private boolean trocaProcesso = false;

    public void executaCiclo(List<PCB> prontos) {
        if (atual.contadorDePrograma == atual.codigo.length) processoTerminado = true;
        else {
            processoTerminado = false;
            Operacao op = atual.codigo[atual.contadorDePrograma];
            opES = op instanceof OperacaoES;
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
