/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.behavior.Algorithm;
import br.cefetmg.games.movement.behavior.Seek;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Dota
 */
public class Attack {
    
    public int damage;
    public TowerType towerType;
    boolean acertou;
    
     public Color cor;
    
    public Position position;
    private final Algorithm seek;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    public Enemy enemy;
    
    public Attack(Tower a, int speed, Position position,Enemy enemy) {
        this.towerType = a.type;
        this.position = position;
        this.seek = new Seek(speed);
        this.damage = defineDamage(a.getPower());
        this.enemy =enemy;
        this.acertou=false;
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
    
    public void Update(float delta){
        this.seek.target.coords = this.enemy.position.coords;
        
        Vector2 novaPosição = new Vector2(this.position.coords);
        
        //Missil vai perseguir até acertar o inimigo.
        if( novaPosição.dst2(this.seek.target.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED){
            if(!acertou){
                enemy.looseLife(this.damage);
                acertou = true;
            }    
        }else{
            this.position.integrate(this.seek.steer(this.position),delta);
        }
    }
    
    
}
