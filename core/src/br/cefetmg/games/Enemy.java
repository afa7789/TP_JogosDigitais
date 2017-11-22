/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.movement.Position;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Dota
 */
public class Enemy {
        // private Texture texjogador;
 //   private Sprite jogador;
    private float posX,posY;
    TextureRegion[][] quadrosDaAnimacao;
    Animation andarcorrente;
    Animation andarParaFrente;
    Animation andarParaTras;
    Animation andarParaDireita;
    Animation andarParaEsquerda;
    float tempoDaAnimacao;
    private Vector2 position;
    
    Enemy(Texture spritesheet){
        quadrosDaAnimacao = TextureRegion.split(spritesheet, 21, 24);
       
        //  texjogador= new Texture("goomba.png");
        //   jogador = new Sprite(texjogador);
        //    jogador.setPosition(posX, posY);
        andarParaFrente = new Animation(0.1f,
         quadrosDaAnimacao[0][0], // 1ª linha, 1ª coluna
         quadrosDaAnimacao[0][1], // idem, 2ª coluna
         quadrosDaAnimacao[0][2],
         quadrosDaAnimacao[0][3],
         quadrosDaAnimacao[0][4]);
         andarParaFrente.setPlayMode(PlayMode.LOOP_PINGPONG);
       andarParaDireita = new Animation(0.1f,
         quadrosDaAnimacao[1][0], // 1ª linha, 1ª coluna
         quadrosDaAnimacao[1][1], // idem, 2ª coluna
         quadrosDaAnimacao[1][2],
         quadrosDaAnimacao[1][3],
         quadrosDaAnimacao[1][4]);
         andarParaDireita.setPlayMode(PlayMode.LOOP_PINGPONG);
        andarParaTras = new Animation(0.1f,
         quadrosDaAnimacao[2][0], // 1ª linha, 1ª coluna
         quadrosDaAnimacao[2][1], // idem, 2ª coluna
         quadrosDaAnimacao[2][2],
         quadrosDaAnimacao[2][3],
         quadrosDaAnimacao[2][4]);
         andarParaTras.setPlayMode(PlayMode.LOOP_PINGPONG);
        andarParaEsquerda = new Animation(0.1f,
         quadrosDaAnimacao[3][0], // 1ª linha, 1ª coluna
         quadrosDaAnimacao[3][1], // idem, 2ª coluna
         quadrosDaAnimacao[3][2],
         quadrosDaAnimacao[3][3],
         quadrosDaAnimacao[3][4]);
         andarParaEsquerda.setPlayMode(PlayMode.LOOP_PINGPONG);
         
         andarcorrente=andarParaDireita;
         tempoDaAnimacao=0;
         position = new Vector2(30, 10);
    }
    void update(){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(position.y<Gdx.graphics.getHeight()-25){
                position.y++;
             //   posY++;
             andarcorrente=andarParaTras;
            }
             
        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if(position.y>0){
                position.y--;
                andarcorrente=andarParaFrente;
          //      posY--;
            }
        }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(position.x>0){
                position.x--;
                andarcorrente=andarParaEsquerda;
             //   posX--;
            }
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(position.x<Gdx.graphics.getWidth()-20){
                position.x++;
                andarcorrente=andarParaDireita;
             //   posX++;
            }
        }
            
        tempoDaAnimacao += Gdx.graphics.getDeltaTime();
    //    jogador.setPosition(posX, posY);

    }
    void render(SpriteBatch batch){
  //      jogador.draw(batch);
        batch.draw((TextureRegion)
        andarcorrente.getKeyFrame(tempoDaAnimacao),  position.x, position.y);
}
        
}
