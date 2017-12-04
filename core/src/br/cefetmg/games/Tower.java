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
    public boolean isMaxPower;
    public boolean isFinalForm;
    public Strength towerLevel;
    public final float actionZone;
    public int tempoEntreAtaques;
    public boolean isAttacking=false;
    public int target;
    public static Vector2 worldDimensions;
    public static final float reajuste = 0.02f;

    //public final TextureRegion texture;
    public static Texture texture_teste = new Texture("torre_temporaria.png");
    public ArrayList<Bullet> bullets;
    public MovementAlgorithm comportamento;

    public Tower(float worldWidth, float worldHeight) {
        actionZone = 48f;
        bullets = new ArrayList<Bullet>();
        worldDimensions = new Vector2(worldWidth, worldHeight);
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
        //System.out.println(" "+towerNode.getPosition().x +" "+towerNode.getPosition().y);
        if (debugMode) System.out.println(" "+towerNode.getPosition().x +" "+towerNode.getPosition().y);
        this.position = new Position(towerNode.getPosition());
        towerNode.setIsObstacle(true);
        this.tempoEntreAtaques=100;
    }
    
    public Texture getTexture (){
        return texture_teste;
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
             case VIOLETA:
                 
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
     
    public Color getColor(){
        Color corReceber= new Color();
        switch(towerLevel){
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
     
     
    public void changeTowerType() {//era para ir para o próximo tipo do Enum.
        //tem q seta o Type IS Final para true se tentar no final.
        switch (type) {
            case LINE:
                type = TowerType.DOUBLE_LINE;
                break;
            case DOUBLE_LINE:
                type = TowerType.TRIANGLE;
                break;
            case TRIANGLE:
                type = TowerType.SQUARE;
                break;
            case SQUARE:
                type = TowerType.PENTAGON;
                break;
            case PENTAGON:
                type = TowerType.HEXAGON;
                break;
            case HEXAGON:
                type = TowerType.HEPTAGON;
                break;
            case HEPTAGON:
                type = TowerType.OCTAGON;
                break;
            case OCTAGON:
                type = TowerType.STAR;
                break;
            case STAR:
                type = TowerType.JEW_STAR;
                break;
            case JEW_STAR:
                type = TowerType.HOURGLASS;
                break;
            case HOURGLASS:
                type = TowerType.CIRCLE;
                break;
            case CIRCLE:
                type = TowerType.OVAL;
                break;
            case OVAL:
                type = TowerType.INFINITE;
                break;
        }
    }

    public Position getPosition() {
        return position;
    }
    
    

    public ShapeRenderer getForm(ShapeRenderer renderer) {
        Vector2 size;
        switch (type) {
            case LINE:
                size = new Vector2(0, worldDimensions.cpy().scl(reajuste).y);
                renderer.line(position.coords.cpy().sub(size), position.coords.cpy().add(size));
                break;
            case DOUBLE_LINE:
                renderer.x(position.coords, 10);
                break;
            case TRIANGLE:
                // O 1.02f é um reajuste para ficar mais agradável o triangulo
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.triangle(position.coords.cpy().sub(size).x, position.coords.cpy().sub(size).y,
                        position.coords.cpy().add(size).x, position.coords.cpy().sub(size).y,
                        position.coords.cpy().x, position.coords.cpy().add(size).scl(1.02f).y);
                break;
            case SQUARE:
                // 1.02f , 1.1f , 2 reajustes para ficar mais agradevel a tela
                // 0f variáveis de controle do eixo z
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.box(position.coords.cpy().sub(size).scl(1.02f).x, position.coords.cpy().sub(size).y, 0f, 1.1f * size.x, 2*size.y, 0f);
                break;
            case PENTAGON:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.circle(position.coords.cpy().x, position.coords.cpy().y, size.x,5);
                break;
            case HEXAGON:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.circle(position.coords.cpy().x, position.coords.cpy().y, size.x,6);
                break;
            case HEPTAGON:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.circle(position.coords.cpy().x, position.coords.cpy().y, size.x,7);
                break;
            case OCTAGON:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.circle(position.coords.cpy().x, position.coords.cpy().y, size.x,8);
                break;
            case STAR:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.polygon(new float[] {      
                    position.coords.cpy().scl(1.02f).sub(size).x, position.coords.cpy().sub(size).y,               // Vertex 0         3  
                    position.coords.cpy().x, position.coords.cpy().add(size).scl(1.03f).y,                         // Vertex 3      \     /
                    position.coords.cpy().scl(0.98f).add(size).x, position.coords.cpy().sub(size).y,               // Vertex 1      /     \
                    position.coords.cpy().scl(1.01f).sub(size).x, position.coords.cpy().scl(0.98f).add(size).y,     // Vertex 4       0---1 
                    position.coords.cpy().scl(0.99f).add(size).x, position.coords.cpy().scl(0.98f).add(size).y    // Vertex 2     4       2   
                });
                break;
            case JEW_STAR:
            size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.triangle(position.coords.cpy().sub(size).x, position.coords.cpy().sub(size).y,
                        position.coords.cpy().add(size).x, position.coords.cpy().sub(size).y,
                        position.coords.cpy().x, position.coords.cpy().add(size).scl(1.02f).y);                            // Vertex 0
                renderer.triangle(position.coords.cpy().sub(size).x, position.coords.cpy().add(size).y,
                        position.coords.cpy().add(size).x, position.coords.cpy().add(size).y,
                        position.coords.cpy().x, position.coords.cpy().sub(size).scl(0.98f).y);
                
                break;
            case HOURGLASS:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.triangle(position.coords.cpy().sub(size).x, position.coords.cpy().sub(size).y,
                        position.coords.cpy().add(size).x, position.coords.cpy().sub(size).y,
                        position.coords.cpy().x, position.coords.cpy().add(size).scl(0.96f).y);                            // Vertex 0
                renderer.triangle(position.coords.cpy().sub(size).x, position.coords.cpy().add(size).y,
                        position.coords.cpy().add(size).x, position.coords.cpy().add(size).y,
                        position.coords.cpy().x, position.coords.cpy().add(size).scl(0.96f).y);
                
                break;
            case CIRCLE:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.circle(position.coords.cpy().x, position.coords.cpy().y, size.x);
                break;
            case OVAL:
                size = new Vector2(worldDimensions.cpy().scl(reajuste));
                renderer.ellipse(position.coords.cpy().sub(size).x, position.coords.cpy().sub(size).y, (2 * size.x), (2 * size.y));
                break;
            case INFINITE:
                size = new Vector2(worldDimensions.cpy().scl(reajuste/2));
                renderer.circle(position.coords.cpy().add(size).x, position.coords.cpy().y, size.x);
                renderer.circle(position.coords.cpy().sub(size).x, position.coords.cpy().y, size.x);
                break;
        }
        return renderer;
    }
    
    
    public void render (ShapeRenderer renderer) {
        Circle circle = new Circle(this.position.coords, actionZone);
        renderer.identity();
        renderer.setColor(Color.RED);
        renderer.circle(circle.x, circle.y, circle.radius);
    }

}
