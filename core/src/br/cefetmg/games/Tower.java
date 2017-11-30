/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.Position;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 *
 * @author Dota
 */
public class Tower {
    
    public Position position;
    public TowerType type;
    public Strength towerLevel;
    public final float ActionZone;
    //public final TextureRegion texture;
    public static Texture texture_teste = new Texture("torre_temporaria.png");

    public Tower() {
        ActionZone = 48f;
    }
    
    public void setTorre(int x, int y) {
        this.towerLevel = Strength.VERMELHO;
        this.type = TowerType.LINE;
        TileNode towerNode = LevelManager.graph.getNodeAtCoordinates(x, y);
        System.out.println(" "+towerNode.getPosition().x +" "+towerNode.getPosition().y);
        this.position = new Position(towerNode.getPosition());
        towerNode.setIsObstacle(true);
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
        Circle circle = new Circle(this.position.coords, ActionZone);
        renderer.identity();
        renderer.setColor(Color.CHARTREUSE);
        renderer.circle(circle.x, circle.y, circle.radius);
    }

}
