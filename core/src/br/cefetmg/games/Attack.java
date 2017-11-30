/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.behavior.Algorithm;
import br.cefetmg.games.movement.behavior.Seek;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Dota
 */
public class Attack {
    public int Damage;
    public TowerType towerType;
    public int speed;
    public Position position;
    private final Algorithm seek;
    //public Enemy enemy;
    
    public Attack(Tower a, int speed, Position position/*,Enemy enemy*/) {
        this.towerType = a.type;
        this.speed = speed;
        this.position = position;
        this.seek = new Seek(speed);
        this.Damage = defineDamage(a.getPower());
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
    
    public void moveTowards(/*Enemy enemy */){
        //this.seek.target.coords = enemy.position;
    }
    
    
}
