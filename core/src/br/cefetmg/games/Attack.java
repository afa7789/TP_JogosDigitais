/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.BulletTarget;
import br.cefetmg.games.movement.Direction;
import br.cefetmg.games.movement.MovementAlgorithm;
import br.cefetmg.games.movement.Pose;
import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.Target;
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
    
    public Vector3 initialPosition;
    public Pose pose;
    private MovementAlgorithm comportamento;
    public Color cor;
    
    Tower tower;
    Strength strengh;
    public TowerType towerType;
    public Enemy enemy;
    private final Algorithm seek = new Seek(100);;
    public Pose posicao;
    public int damage;
    //public TowerType towerType;
    boolean acertou;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    boolean draw;
    
    public Attack(Tower tower, Vector3 posicao, Color cor, MovementAlgorithm comportamento, Enemy enemy) {
        this.tower = tower;
        this.towerType = tower.type;
        this.strengh = tower.towerLevel;
        this.damage = defineDamage(tower.getPower());
        this.cor = tower.getColor();
        this.initialPosition=posicao;
        this.pose = new Pose(posicao, 0);
        this.comportamento = comportamento;
        this.enemy = enemy;
        this.acertou=false;
        this.draw=true;
    }
    
    
    public void atualiza(float delta, Vector3 alvo) {
        if (comportamento != null) {
            // pergunta ao algoritmo de movimento (comportamento) 
            // para onde devemos ir
            comportamento.alvo.setObjetivo(new Vector3(enemy.position.coords.x,
                                                        enemy.position.coords.y, 0));
            Direction direcionamento = comportamento.guiar(this.pose);

            // faz a simulação física usando novo estado da entidade cinemática
            pose.atualiza(direcionamento, delta);
            
            Vector2 novaPosição = new Vector2(alvo.x, alvo.y);
            //System.out.println("x1 = " + alvo.x + ", x2 = " + this.comportamento.alvo.getObjetivo().x);
            /*if( novaPosição.dst2(new Vector2(this.comportamento.alvo.getObjetivo().x, this.comportamento.alvo.getObjetivo().y)) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED){
            
                if(!acertou){
                    enemy.looseLife(this.damage);
                    //this.draw = false;
                    acertou = true;
                }   
        }else{*/
            pose.atualiza(direcionamento, delta);
            //pose.integrate(this.seek.steer(this.pose.posicao),delta);
        //}
                        
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
    
    
    
    public void setComportamento(Vector2 target){
        Follow buscar = new Follow(100);
        buscar.alvo = new BulletTarget(new Vector3(target.x, target.y, 0));
        this.comportamento = buscar;
        this.comportamento.alvo.setObjetivo(new Vector3(target.x, target.y, 0));
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
    
//    public void update(float delta){
//        //this.seek.target.coords = this.enemy.position.coords;
//        this.seek.target.coords.x = this.enemy.position.coords.x;
//        this.seek.target.coords.y = this.enemy.position.coords.y;
//        
//        /*Vector2 novaPosição = new Vector2(this.enemy.position.coords);
//        System.out.println("x: " + this.enemy.position.coords.x + ", y: " + this.position.coords.y);*/
//        this.comportamento.alvo = new BulletTarget(new Vector3(this.seek.target.coords.x, this.seek.target.coords.y,0));
//        
//        Direction direcionamento = comportamento.guiar(this.posicao);
//       posicao.atualiza(direcionamento, delta);
//        //Missil vai perseguir até acertar o inimigo.
//        /*if( novaPosição.dst2(this.seek.target.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED){
//            if(!acertou){
//                enemy.looseLife(this.damage);
//                acertou = true;
//            }   
//            this.posicao.atualiza(direcionamento, delta);
//        }else{
//            this.posicao.atualiza(direcionamento, delta);
//            this.position.integrate(this.seek.steer(this.position),delta);
//        }*/
//    }
    
    
}
