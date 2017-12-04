    package br.cefetmg.games;   

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 *
 * @author Isaac
 */
public class HUD {
   // private Texture texjogador;
 //   private Sprite jogador;
    private float posX,posY;
    Texture space;
    Texture word;
    Texture life_bar;
    Texture button_newgame;
    Texture button_play;
    Texture button_pause;
    float tempoDaAnimacao;
    private Vector2 position;
    int fulllife,life;
    Sprite lifebarsprite;
    private TextureRegion NGTextureRegion;
    private TextureRegionDrawable NGTexRegionDrawable;
    private ImageButton NGbutton;
    private TextureRegion PTextureRegion;
    private TextureRegionDrawable PTexRegionDrawable;
    private ImageButton Pbutton;
    private Stage stage;
    
    int state;
    
    HUD(){
        state=1;
        space=new Texture(Gdx.files.local("hud/life-bar.png"));
        word=new Texture("hud/word.png");
        life_bar=new Texture("hud/life.png");
        button_newgame=new Texture("hud/btn_new.png");
        button_play=new Texture("hud/btn_play.png");
        button_pause=new Texture("hud/btn_pause.png");
        lifebarsprite=new Sprite(life_bar);
        position = new Vector2(30, 10);
        fulllife=20; 
        life=20;
        
        NGTextureRegion = new TextureRegion(button_newgame);
        NGTexRegionDrawable = new TextureRegionDrawable(NGTextureRegion);
        NGbutton = new ImageButton(NGTexRegionDrawable); 
        NGbutton.setPosition(10,640);
        
        PTextureRegion = new TextureRegion(button_pause);
        PTexRegionDrawable = new TextureRegionDrawable(PTextureRegion);
        Pbutton = new ImageButton(PTexRegionDrawable); 
        Pbutton.setPosition(894,640);
        
        stage = new Stage(new ScreenViewport()); 
        stage.addActor(NGbutton); 
        stage.addActor(Pbutton); 

        Gdx.input.setInputProcessor(stage);
    }
    void update(){
          
    }

    void setFulllif(int num){
        fulllife=num;
        life=num;
    }
    void contdead(){
         life--;
    }
    void render(SpriteBatch batch){
       
     
       batch.draw(word, 362, 680);
       batch.draw(space, 362, 640);
       batch.draw(lifebarsprite, 362, 640,300*life/fulllife,40);

    }
    void button_render(){
       stage.act(Gdx.graphics.getDeltaTime());
       stage.draw(); //Draw the ui
        
    }
    int getState(){
        return state;
    }
    void setState(int state){
        this.state=state;
    }
    void listeningbutton(int button, Vector2 clique) {
        if (button == Input.Buttons.LEFT) {
                   if((int) clique.y>650&&(int) clique.y<700 &&(int) clique.x>10 &&(int) clique.x<130 ){ 
                       
                        state=0;
                   }
                   if((int) clique.y>650&&(int) clique.y<700 &&(int) clique.x>894 &&(int) clique.x<1014 ){
                    
                   }
           }         
    }
}