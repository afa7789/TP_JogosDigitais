/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.movement;

/**
 *
 * @author NataliaNatsumy
 */
public abstract class MovementAlgorithm {
    protected float maxVelocidade;
    public BulletTarget alvo;
    private final char nome;

    public MovementAlgorithm(char nome) {
        this.nome = nome;
    }

    /**
     * Determina a movimentação do agente de acordo com o que este algoritmo
     * faz.
     *
     * @param agente agente que está sendo guiado.
     * @return um direcionamento (steering) para o agente.
     */
    public abstract Direction guiar(Pose agente);

    public char getNome() {
        return nome;
    }
}
