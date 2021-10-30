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
}