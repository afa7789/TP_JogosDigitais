package br.cefetmg.games.graphics;

import br.cefetmg.games.Agent;
import br.cefetmg.games.Enemy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Um renderizador de agentes que usa uma sprite animada com 8 direções.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class EnemyRenderer {

    private final SpriteBatch batch;
    private final Camera camera;
    private final OrientedCharacterSprite sprite;

    // tamanho dos quadros da animação (40x40)
    private static final int FRAME_WIDTH = 40;
    private static final int FRAME_HEIGHT = FRAME_WIDTH;

    // um deslocamento em Y para que o personagem fique alinhado à altura 
    // da ponte
    private static final int POSITION_OFFSET_Y = FRAME_HEIGHT / 2;        // 20

    /**
     * Cria um novo renderizador com uma textura 8x3 (8 direções, 3 quadros de
     * animação de "andando" em cada uma).
     *
     * @param batch
     * @param camera
     * @param character
     */
    public EnemyRenderer(SpriteBatch batch, Camera camera,
            Texture character) {
        this.batch = batch;
        this.camera = camera;
        this.sprite = new OrientedCharacterSprite(character,
                FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void render(Enemy agent) {
        sprite.update(Gdx.graphics.getDeltaTime());
        sprite.setCenter(
                agent.position.coords.x,
                agent.position.coords.y);
        sprite.setFacing(agent.getFacing());
        sprite.setMoving(agent.isMoving());
        sprite.translateY(POSITION_OFFSET_Y);
        batch.setProjectionMatrix(camera.combined);
            batch.begin();
            sprite.draw(batch);
            batch.end();
        }
    }
