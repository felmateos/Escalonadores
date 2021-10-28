package escalonadores;

import kernel.*;
import java.util.*;

public abstract class Escalonador {
    private PCB atual;

    public PCB executaCiclo(List<PCB> novos, List<PCB> prontos, List<PCB> terminados){
        return null;
    }

    public PCB verificaProcesso(List<PCB> prontos, List<PCB> terminados){
        return null;
    }

    public PCB escolheProximo(List<PCB> prontos, List<PCB> terminados) {
        return null;
    }

    public PCB getAtual() {
        return atual;
    }

    public void setAtual(PCB atual) {
        this.atual = atual;
    }
}
