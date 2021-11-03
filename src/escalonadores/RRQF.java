package escalonadores;

import kernel.PCB;
import operacoes.*;
import java.util.List;

public class RRQF extends Escalonador{
    int quantum = 0;

    @Override
    public void executaCiclo(List<PCB> prontos) {
        if (super.getAtual().contadorDePrograma == super.getAtual().codigo.length) setProcessoTerminado(true);
        else {
            quantum++;
            super.setTrocaProcesso(quantum == 5);
            setProcessoTerminado(false);
            Operacao op = super.getAtual().codigo[super.getAtual().contadorDePrograma];
            setOpES(op instanceof OperacaoES);
        }
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos) {
        quantum = 0;
        if (!prontos.isEmpty()) return prontos.get(0);
        return null;
    }
}