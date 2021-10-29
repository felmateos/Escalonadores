package escalonadores;

import kernel.PCB;

import java.util.List;

public class RRQF extends Escalonador{
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
        if (super.getUltimaOp()) {
            super.getAtual().estado = PCB.Estado.TERMINADO;
            prontos.remove(super.getAtual());
            terminados.add(super.getAtual());
            super.setUltimaOp(false);
            return escolheProximo(prontos, terminados);
        }
        return super.getAtual();
    }

    @Override
    public PCB escolheProximo(List<PCB> prontos, List<PCB> terminados) {
        if (!prontos.isEmpty()) {
            PCB atual = prontos.get(0);
            atual.estado = PCB.Estado.EXECUTANDO;
            super.setAtual(atual);
            return atual;
        }
        return null;
    }
}