/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.Bullet;
import br.cefetmg.games.movement.BulletTarget;
import br.cefetmg.games.movement.MovementAlgorithm;
import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.behavior.Follow;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;

/**
 *
 * @author Dota
 */
public class Tower {
    
    public Position position;
    public TowerType type;
    public Strength towerLevel;
    public final float actionZone;
    public int attackSpeed;
    public boolean isAttacking=false;
    public Enemy target;

    //public final TextureRegion texture;
    public static Texture texture_teste = new Texture("torre_temporaria.png");
    public ArrayList<Bullet> bullets;
    public MovementAlgorithm comportamento;

    public Tower() {
        actionZone = 48f;
        bullets = new ArrayList<Bullet>();
    }
    
    public void setComportamento(Vector2 target){
        Follow buscar = new Follow(80);
        //buscar.alvo.setObjetivo(Vector3.Zero);
        buscar.alvo = new BulletTarget(new Vector3(target.x, target.y, 0));
        comportamento = buscar;
        comportamento.alvo.setObjetivo(new Vector3(target.x, target.y, 0));
    }
    public void newBullet(Vector3 position){
        Bullet agente = new Bullet(position,
				new Color(0, 0, 1, 1), comportamento);
        agente.pose.orientacao = (float) (Math.random() * Math.PI * 2);
        agente.defineComportamento(comportamento);
        bullets.add(agente);
        
    }
    public void setTorre(int x, int y, boolean debugMode) {
        this.towerLevel = Strength.VERMELHO;
        this.type = TowerType.LINE;
        TileNode towerNode = LevelManager.graph.getNodeAtCoordinates(x, y);
        if (debugMode) System.out.println(" "+towerNode.getPosition().x +" "+towerNode.getPosition().y);
        this.position = new Position(towerNode.getPosition());
        towerNode.setIsObstacle(true);
        this.attackSpeed=100;
    }
    
    public boolean atacandoAlguem(){
        return isAttacking;
    }
    public void estáAtacando(){
        isAttacking = true;
    }
    public void parouDeAtacar(){
        isAttacking = false;
    }
    // Ao acionar o upgrade da Torre a forca dela altera
    // Para alterações na torre desviculadas a tipo dela
    // implementar nessa função de upgrade
      public void upgradeTower() {	 
         switch (towerLevel) {
             case VERMELHO:
                 towerLevel = Strength.LARANJA;
                 break;
             case LARANJA:
                 towerLevel = Strength.AMARELO;
                 break;
             case AMARELO:
                 towerLevel = Strength.VERDE;
                 break;
             case VERDE:
                 towerLevel = Strength.CIANO;
                 break;
             case CIANO:
                 towerLevel = Strength.AZUL;
                 break;
             case AZUL:
                 towerLevel = Strength.VIOLETA;
                 break;
             default:
                 break;
         }
         
     }
     // Associa a cor da torre a um poder bruto
     // A classe Attack, associara' esse poder 'a um dano
     public int getPower() {
         switch (towerLevel){
             case VIOLETA:
                 return 7;
             case LARANJA:
                 return 2;
             case AMARELO:
                 return 3;
             case VERDE:
                 return 4;
             case CIANO:
                 return 5;
             case AZUL:
                 return 6;
             default:
                 return 1;
         }
      }		      

    public void changeTowerType() {//era para ir para o próximo tipo do Enum.
        //type.;
    }

    public Position getPosition() {
        return position;
    }
    
    public void drawTower(ShapeRenderer renderer) {
        switch (type) {
            case LINE:
                
                break;
            case DOUBLE_LINE:
                break;
            case TRIANGLE:
                break;
            case SQUARE:
                break;
            case PENTAGON:
                break;
            case HEXAGON:
                break;
            case HEPTAGON:
                break;
            case OCTAGON:
                break;
            case STAR:
                break;
            case JEW_STAR:
                break;
            case HOURGLASS:
                break;
            case CIRCLE:
                break;
            case OVAL:
                break;
            case INFINITE:
                break;
        }
    }

    public Texture getTexture() {
        switch (type) {
            case LINE:
                return texture_teste;
            case DOUBLE_LINE:
                return texture_teste;
            case TRIANGLE:
                return texture_teste;
            case SQUARE:
                return texture_teste;
            case PENTAGON:
                return texture_teste;
            case HEXAGON:
                return texture_teste;
            case HEPTAGON:
                return texture_teste;
            case OCTAGON:
                return texture_teste;
            case STAR:
                return texture_teste;
            case JEW_STAR:
                return texture_teste;
            case HOURGLASS:
                return texture_teste;
            case CIRCLE:
                return texture_teste;
            case OVAL:
                return texture_teste;
            case INFINITE:
                return texture_teste;
        }
        return texture_teste;
    }
    
    public void render (ShapeRenderer renderer) {
        Circle circle = new Circle(this.position.coords, actionZone);
        renderer.identity();
        renderer.setColor(Color.RED);
        renderer.circle(circle.x, circle.y, circle.radius);
    }

}
