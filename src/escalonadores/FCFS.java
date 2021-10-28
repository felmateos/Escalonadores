package escalonadores;

import kernel.*;

import java.util.*;

public class FCFS extends Escalonador {
    public FCFS(){}

    @Override
    public PCB executaCiclo(List<PCB> novos, List<PCB> prontos, List<PCB> terminados){
        if (!novos.isEmpty()) {
            PCB n = novos.get(0);
            n.estado = PCB.Estado.PRONTO;
            prontos.add(n);
            novos.remove(n);
            if (super.getAtual() == null) super.setAtual(prontos.get(0));
        }
        return verificaProcesso(prontos, terminados);
    }

    @Override
    public PCB verificaProcesso (List<PCB> prontos, List<PCB> terminados){
        if (super.getAtual().contadorDePrograma == super.getAtual().codigo.length-1) {
            prontos.remove(super.getAtual());
            terminados.add(super.getAtual());
            System.out.println("NEW POS: " + (terminados.size()));
            return escolheProximo(prontos, terminados);
        }
        return super.getAtual();
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos, List<PCB> terminados) {
        if (!prontos.isEmpty()) return prontos.get(0);
        return null;
    }
}

