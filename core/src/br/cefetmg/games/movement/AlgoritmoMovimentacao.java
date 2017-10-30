package br.cefetmg.games.movement;

/**
 * Um algoritmo de movimentação. O principal método é o "guiar" que, dada uma
 * pose de um agente, retorna qual o direcionamento (velocidade) ele deve
 * seguir.
 *
 * @author Flávio Coutinho <fegemo@cefetmg.br>
 * @see Direcionamento
 */
public abstract class AlgoritmoMovimentacao {

    protected float maxVelocidade;
    public Alvo alvo;
    private final char nome;

    public AlgoritmoMovimentacao(char nome) {
        this.nome = nome;
    }

    /**
     * Determina a movimentação do agente de acordo com o que este algoritmo
     * faz.
     *
     * @param agente agente que está sendo guiado.
     * @return um direcionamento (steering) para o agente.
     */
    public abstract Direcionamento guiar(Pose agente);

    public char getNome() {
        return nome;
    }
}
