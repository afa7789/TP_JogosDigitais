/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.Direction;
import br.cefetmg.games.movement.MovementAlgorithm;
import br.cefetmg.games.movement.Pose;
import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.behavior.Algorithm;
import br.cefetmg.games.movement.behavior.Follow;
import br.cefetmg.games.movement.behavior.Seek;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Dota
 */
public class Attack {
    
    public int damage;
    public TowerType towerType;
    boolean acertou;
    
    public Color cor;
    Strength strengh;
    public Pose posicao;
    public Position position;
    public MovementAlgorithm teste;
    private final Algorithm seek;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    public Enemy enemy;
    
    public Attack(Tower a, int speed, Position position,Enemy enemy) {
        this.towerType = a.type;
        this.strengh = a.towerLevel;
        this.position = position;
        this.posicao = new Pose(new Vector3(this.position.coords.x,this.position.coords.y,0),0);
        this.seek = new Seek(speed);
        this.damage = defineDamage(a.getPower());
        this.enemy =enemy;
        this.acertou=false;
        this.teste=new Follow(speed);
        this.cor = defineColor();
    }
    
    private Color defineColor(){
        Color corReceber= new Color();
        switch(strengh){
            case VERMELHO:
                corReceber.set( 1, 0, 0, 0);
                break;
            case LARANJA:
                corReceber.set( 1, 0.64f, 0, 0);
                break;
            case AMARELO:
                corReceber.set( 1, 1, 0, 0);
                break;
            case VERDE:
                corReceber.set( 0, 1, 0, 0);
                break;
            case CIANO: 
                corReceber.set( 0, 1, 1, 0);
                break;
            case AZUL:
                corReceber.set( 0, 0, 1, 0);
                break;
            case VIOLETA:
                corReceber.set( 0.30f , 0.18f, 0.30f, 0);
                break;
            }
        return corReceber;
    }
    
    // A função defineDamage
    // recebe o multiplicador de poder da torre (associado a cor dela)
    // e retorna o Dano causado da forma
    // Damage = DanoBaseDoTipoDaTorre * MultiplicadorDePoder
    // Ao aplicar status do tipo de torre 
    // pode-se defini-lo nessa função posteriormente
    
    private int defineDamage (int Power) {
        int Damage = 0;
        switch (towerType) {
            case DOUBLE_LINE:
                Damage = 2 * Power;
                break;
            case TRIANGLE:
                Damage = 3 * Power;
                break;
            case SQUARE:
                Damage = 4 * Power;
                break;
            case PENTAGON:
                Damage = 5 * Power;
                break;
            case HEXAGON:
                Damage = 6 * Power;
                break;
            case HEPTAGON:
                Damage = 7 * Power;
                break;
            case OCTAGON:
                Damage = 8 * Power;
                break;
            case STAR:
                Damage = 9 * Power;
                break;
            case JEW_STAR:
                Damage = 10 * Power;
                break;
            case HOURGLASS:
                Damage = 11 * Power;
                break;
            case CIRCLE:
                Damage = 12 * Power;
                break;
            case OVAL:
                Damage = 13 * Power;
                break;
            case INFINITE:
                Damage = 14 * Power;
                break;
                default:
                    Damage = Power;
                    break;
                            
        }
        return Damage;
    } 
    
    public void update(float delta){
        this.seek.target.coords = this.enemy.getPosition();
        
        Vector2 novaPosição = new Vector2(this.position.coords);
        Direction direcionamento = teste.guiar(this.posicao);
        //Missil vai perseguir até acertar o inimigo.
        if( novaPosição.dst2(this.seek.target.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED){
            if(!acertou){
                enemy.looseLife(this.damage);
                acertou = true;
            }    
        }else{
            this.posicao.atualiza(direcionamento, delta);
            this.position.integrate(this.seek.steer(this.position),delta);
        }
    }
    
    
}
