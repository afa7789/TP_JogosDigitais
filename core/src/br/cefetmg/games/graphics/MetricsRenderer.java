package br.cefetmg.games.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder.Metrics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class MetricsRenderer {

    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;

    public MetricsRenderer(SpriteBatch batch, ShapeRenderer shapes,
            BitmapFont font) {
        this.batch = batch;
        this.shapes = shapes;
        this.font = font;
        font.setColor(Color.WHITE);
    }

    public void render(int vidas, int vidasMaximo, int nivel) {
        float verticalSpacing = 5;
        float fontHeight = font.getLineHeight() + verticalSpacing;
        float initialY = fontHeight + verticalSpacing;
        float initialX = 10;

        // desenha o fundo semi-transparente
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.setColor(0, 0, 0, 0.25f);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.rect(0, 0,
                Gdx.graphics.getWidth(),
                initialY + (2* fontHeight) + verticalSpacing);
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // desenha o texto com as métricas
        batch.begin();
        font.draw(batch, String.format("VIDAS: %d", vidas), initialX,
                initialY + 2*fontHeight);
        font.draw(batch, String.format("PONTOS: %d", vidasMaximo), initialX,
                initialY + fontHeight);
        font.draw(batch, String.format("Nivel: %d",
                nivel),
                initialX, initialY);
        batch.end();
    }
}
