/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.graphics;

import br.cefetmg.games.Tower;
import br.cefetmg.games.TowerType;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

/**
 *
 * @author Dota
 */
public class TowerRenderer {
    private SpriteBatch batch;
    private final Camera camera;

    public TowerRenderer(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }
    public void render(Tower tower, ShapeRenderer renderer){
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        batch.draw( tower.getTexture(), tower.position.coords.x - tower.getTexture().getHeight()/2, tower.position.coords.y - tower.getTexture().getHeight()/2);
//        batch.end();
        renderer.setProjectionMatrix(camera.combined);
        renderer.identity();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(tower.getColor());
        tower.getForm(renderer);
        renderer.end();
        
    }
    
    public void renderAll(ArrayList<Tower> Towers, ShapeRenderer renderer){
        for (Tower Tower : Towers) {
            render(Tower, renderer);
        }
    }

}
