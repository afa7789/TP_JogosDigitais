/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.movement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author NataliaNatsumy
 */
public class Bullet {
    public Vector3 initialPosition;
    public Pose pose;
    private MovementAlgorithm comportamento;

    public Color cor;

    public Bullet(Vector3 posicao, Color cor) {
        this.initialPosition=posicao;
        this.pose = new Pose(posicao, 0);
        this.cor = cor;
    }

    public void atualiza(float delta) {
        if (comportamento != null) {
            // pergunta ao algoritmo de movimento (comportamento) 
            // para onde devemos ir
            Direction direcionamento = comportamento.guiar(this.pose);

            // faz a simulação física usando novo estado da entidade cinemática
            pose.atualiza(direcionamento, delta);
        }
    }

    /**
     * @param comportamento o novo comportamento de movimentação
     */
    public void defineComportamento(MovementAlgorithm comportamento) {
        this.comportamento = comportamento;
    }
    public char getNomeComportamento() {
        return comportamento != null ? comportamento.getNome() : '-';
    }
    
    public Vector3 getInitialPosition() {
        return this.initialPosition;
    }
}
