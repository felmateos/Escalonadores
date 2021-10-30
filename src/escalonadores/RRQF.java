package escalonadores;

import kernel.PCB;
import operacoes.Carrega;
import operacoes.Soma;

import java.util.LinkedList;
import java.util.List;

public class RRQF extends Escalonador{
    int quantum = 0;

    @Override
    public void executaCiclo(List<PCB> novos, List<PCB> prontos, List<PCB> terminados){
        if (!novos.isEmpty()) {
            PCB n = novos.get(0);
            n.estado = PCB.Estado.PRONTO;
            novos.remove(n);
            prontos.add(n);
        }
        if (super.getAtual() == null && !prontos.isEmpty()) super.setAtual(prontos.get(0));
        if (quantum == 5) super.setAtual(escolheProximo(prontos));
        quantum++;
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        if (!prontos.isEmpty() && prontos instanceof LinkedList) {
            if (quantum != 5 || prontos.size() == 1 || super.getAtual() == ((LinkedList<PCB>) prontos).getLast()) {
                quantum = 1;
                return prontos.get(0);
            }
            quantum = 0;
            return prontos.get(prontos.indexOf(super.getAtual()) + 1);
        }
        return null;
    }
}
