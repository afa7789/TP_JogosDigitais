/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.movement;

import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author NataliaNatsumy
 */
public class BulletTarget {
    private Vector3 objetivoEstatico;

    /**
     * Um agente que é o objetivo dos outros agentes.
     */
    private Bullet agenteObjetivo;

    public BulletTarget(Vector3 posicao) {
        this.objetivoEstatico = posicao;
    }

    public void setObjetivo(Vector3 posicao) {
        this.objetivoEstatico = posicao;
        this.agenteObjetivo = null;
    }

    public void setObjetivo(Bullet agente) {
        this.agenteObjetivo = agente;
        this.objetivoEstatico = null;
    }

    public boolean isSeguindoObjetivo() {
        return agenteObjetivo != null;
    }

    public Vector3 getObjetivo() {
        return isSeguindoObjetivo()
                ? agenteObjetivo.pose.posicao : objetivoEstatico;
    }
}
