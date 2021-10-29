package escalonadores;

import kernel.*;
import operacoes.Carrega;
import operacoes.Soma;

import java.util.*;

public class FCFS extends Escalonador {
    public FCFS(){}

    @Override
    public void executaCiclo(List<PCB> novos, List<PCB> prontos, List<PCB> terminados){
        if (!novos.isEmpty()) {
            PCB n = novos.get(0);
            n.estado = PCB.Estado.PRONTO;
            prontos.add(n);
            novos.remove(n);
        }
        if (super.getAtual() == null && !prontos.isEmpty()) super.setAtual(prontos.get(0));
    }

    @Override
    public void verificaOpCPU() {
        for(int i = super.getAtual().contadorDePrograma; i < super.getAtual().codigo.length; i++) {
            if (super.getAtual().codigo[i] instanceof Soma || super.getAtual().codigo[i] instanceof Carrega) {
                super.setUltimaOpCPU(false);
                return;
            }
        }
        super.setUltimaOpCPU(true);
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        if (!prontos.isEmpty()) return prontos.get(0);
        return null;
    }
}