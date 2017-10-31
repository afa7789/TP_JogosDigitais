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
public class Direction {
    public Vector3 velocidade;  // velocidade linear
    public double rotacao;      // velocidade angular
    
    public Direction() {
        velocidade = new Vector3();
        rotacao = 0;
    }
}
