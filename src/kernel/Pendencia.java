package kernel;

import operacoes.OperacaoES;

public class Pendencia {
    private PCB pcb;
    private OperacaoES opES;

    public Pendencia(PCB pcb, OperacaoES opES) {
        this.pcb = pcb;
        this.opES = opES;
    }

    public PCB getPcb() {
        return pcb;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public OperacaoES getOpES() {
        return opES;
    }

    public void setOpES(OperacaoES opES) {
        this.opES = opES;
    }
}
