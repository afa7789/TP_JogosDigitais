/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.graphics;

import br.cefetmg.games.Attack;
import br.cefetmg.games.Enemy;
import br.cefetmg.games.movement.Bullet;
import br.cefetmg.games.movement.Pose;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author NataliaNatsumy
 */
public class BulletRenderer {
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Camera camera;

    private final HashMap<Character, GlyphLayout> cacheNomes = new HashMap<>();

    public static final float RAIO = 5;

    public BulletRenderer(Camera camera, SpriteBatch batch) {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        this.batch = batch;
        this.camera = camera;
    }
    
    public void renderAll(ArrayList<Attack> attacks  ){
        for (Attack attack : attacks) {
            desenha(attack);
        }
    }
    
    public void desenha(Attack agente) {       
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(agente.cor);
        shapeRenderer.translate(
                agente.position.coords.x,
                agente.position.coords.y, 0);
        shapeRenderer.rotate(0, 0, 1,
                agente.posicao.orientacao * ((float) (180.0f / Math.PI)));
        shapeRenderer.circle(0, 0, RAIO);
        shapeRenderer.identity();
        shapeRenderer.end();

        batch.begin();
        batch.setTransformMatrix(new Matrix4()
                .setToTranslation(agente.posicao.posicao));
        //GlyphLayout layout = getTextoNome(agente.getNomeComportamento());
        //font.draw(batch, layout, -layout.width / 2.0f, layout.height / 2.0f);
        batch.end();
        batch.setTransformMatrix(new Matrix4().idt());
    }

    /**
     * Retorna o desenho do texto com o "nome" (a letra indicativa) deste tipo
     * de agente. Usa um cache para não ficar gerando esse texto o tempo todo.
     *
     * @param agente
     * @return
     */
    /*private GlyphLayout getTextoNome(char nomeComportamento) {
        GlyphLayout layout = cacheNomes.get(nomeComportamento);
        if (layout == null) {
            layout = new GlyphLayout(font, "" + nomeComportamento);
            cacheNomes.put(nomeComportamento, layout);
        }
        return layout;
    }*/

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
