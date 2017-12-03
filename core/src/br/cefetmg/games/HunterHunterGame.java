package br.cefetmg.games;

import static br.cefetmg.games.LevelManager.graph;
import br.cefetmg.games.graphics.GraphRenderer;
import br.cefetmg.games.graphics.AgentRenderer;
import br.cefetmg.games.graphics.BulletRenderer;
import br.cefetmg.games.graphics.EnemyRenderer;
import br.cefetmg.games.graphics.MetricsRenderer;
import br.cefetmg.games.graphics.TowerRenderer;
import br.cefetmg.games.movement.Bullet;
import br.cefetmg.games.movement.BulletTarget;
import br.cefetmg.games.movement.MovementAlgorithm;
import br.cefetmg.games.movement.behavior.Follow;
import br.cefetmg.games.pathfinding.GraphGenerator;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class HunterHunterGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private TiledMap tiledMap;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TiledMapRenderer tiledMapRenderer;
    private GraphRenderer graphRenderer;
    private TowerRenderer towerRenderer;
    
   // private Agent agent;
   // private AgentRenderer agentRenderer;
    private ArrayList<Tower> torres = new ArrayList<Tower>();
    private final String windowTitle;
    private boolean debugMode = false;
    private boolean constructionMode = false;
    private MetricsRenderer metricsRenderer;
    private boolean showingMetrics;
    
    int counter=0;

    
    private Array<Bullet> bullets;
    private BulletRenderer bulletRender;
    private BulletTarget objetivo;
    private Follow buscar;
    private MovementAlgorithm algoritmoCorrente;
    private Array<MovementAlgorithm> algoritmos;
    
    private int colidiu =0;

    ArrayList<Enemy> enemys = new ArrayList<Enemy>();
    Texture enemyspritesheet ; 
    private EnemyRenderer enemyrender;
    long start;
    int cont; //Conta quantos inimigos a no mapa
    int deadEnemy;
    
    public HunterHunterGame() {
        this.windowTitle = "Hunter x Hunter (%d)";
        showingMetrics = true;
    }

    public GraphRenderer getGraphRenderer() {
        return graphRenderer;
    }
      
    public Bullet novoBullet(Vector3 posicao) {
		Bullet agente = new Bullet(posicao,new Color(0, 0, 1, 1));
		agente.pose.orientacao = (float) (Math.random() * Math.PI * 2);
		agente.defineComportamento(algoritmoCorrente);
		bullets.add(agente);
		return agente;
    }
    
    @Override
    public void create() {
        //init time 
        start = TimeUtils.millis();
        cont = 1;
        deadEnemy =0;
        
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.translate(w / 2, h / 2);
        camera.update();
        viewport = new ScreenViewport(camera);

        // Carrega o mapa
        tiledMap = LevelManager.LoadLevel("map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

        graphRenderer = new GraphRenderer(batch, shapeRenderer);
        graphRenderer.renderGraphToTexture(LevelManager.graph);
        towerRenderer = new TowerRenderer(batch);
        
        //Enemy 
        //enemyspritesheet=new Texture("goomba-spritesheet.png");
        enemyrender = new EnemyRenderer(batch, camera, new Texture("gon.png")); //new AgentRenderer(batch, camera,enemyspritesheet);
        enemys.add(new Enemy(new Vector2(
                        LevelManager.tileWidth / 2, LevelManager.totalPixelHeight/2),
                Color.FIREBRICK));
   
        metricsRenderer = new MetricsRenderer(batch, shapeRenderer,
                new BitmapFont());
        
        
		batch = new SpriteBatch();
		bulletRender = new BulletRenderer(camera, batch);

		// define o objetivo (perseguição, fuga) inicialmente no centro do mundo
		objetivo = new BulletTarget(new Vector3(0, 0, 0));

		// configura e registra os comportamentos disponíveis
		algoritmos = new Array<>();
		buscar = new Follow(80);
		buscar.alvo = objetivo;
		algoritmos.add(buscar);
		algoritmoCorrente = buscar;

		bullets = new Array<>();
      //  for(int i=0;i<enemys.size();i++){
            enemys.get(0).setGoal(LevelManager.totalPixelWidth-1, LevelManager.totalPixelHeight/2);          
       // }
        //agent.setGoal(LevelManager.totalPixelWidth-1, LevelManager.totalPixelHeight/2);
		
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    camera.translate(-32, 0);
                }
                if (keycode == Input.Keys.RIGHT) {
                    camera.translate(32, 0);
                }
                if (keycode == Input.Keys.UP) {
                    camera.translate(0, -32);
                }
                if (keycode == Input.Keys.DOWN) {
                    camera.translate(0, 32);
                }
                if (keycode == Input.Keys.NUM_1) {
                    tiledMap.getLayers().get(0).setVisible(
                            !tiledMap.getLayers().get(0).isVisible());
                }
                if (keycode == Input.Keys.NUM_2) {
                    tiledMap.getLayers().get(1).setVisible(
                            !tiledMap.getLayers().get(1).isVisible());
                }
                if (keycode == Input.Keys.M) {
                    showingMetrics = !showingMetrics;
                }
                if (keycode == Input.Keys.G) {
                    graphRenderer = new GraphRenderer(batch, shapeRenderer);
                    graphRenderer.renderGraphToTexture(LevelManager.graph);
                }
                if (keycode == Input.Keys.D) {
                    debugMode = !debugMode;
                }
                if (keycode == Input.Keys.C){
                    constructionMode = !constructionMode;
                }
                return false;
            }
          
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                Vector2 clique = new Vector2(x, y);
                viewport.unproject(clique);
                // Botão ESQUERDO: posiciona objetivo
                if (button == Input.Buttons.LEFT) {
                    if (constructionMode) {
                        TileNode towerNode = LevelManager.graph.getNodeAtCoordinates((int) clique.x, (int) clique.y);
                        boolean emptyPlace = true;
                        for (int i = 0; i < torres.size(); i++) {
                            if (torres.get(i).getPosition().coords.x == towerNode.getPosition().x && torres.get(i).getPosition().coords.y == towerNode.getPosition().y) {
                                System.out.println("ja existe uma torre no lugar!");
                                emptyPlace = false;
                            }
                        }
                        if (emptyPlace) {
                            Tower Aux = new Tower();
                            Aux.setTorre((int) clique.x, (int) clique.y);
                            torres.add(Aux);
                            atualizaGrafo();
                        }
                        constructionMode = !constructionMode;
                    }
                }
                if (button == Input.Buttons.RIGHT) {
                    for (Tower t : torres) {
                        //System.out.println(t.getPosition().coords.x +" " + (int) clique.x);
                        if (Math.abs(t.getPosition().coords.x - (int) clique.x) < 16 && Math.abs(t.getPosition().coords.y - (int) clique.y) < 16) {
                            t.upgradeTower();
                            System.out.println("OK");
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * Atualiza o mundo virtual para ter as mesmas proporções que a janela.
     *
     * @param w Largura da janela.
     * @param h Altura da janela.
     */
    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
    }
    
    public void atualizaGrafo(){
        LevelManager.setGraph( GraphGenerator.generateGraphAgain(LevelManager.graph.getAllNodes(),LevelManager.tiledMap));
        graphRenderer = new GraphRenderer(batch, shapeRenderer);
        graphRenderer.renderGraphToTexture(LevelManager.graph);
        metricsRenderer = new MetricsRenderer(batch, shapeRenderer, new BitmapFont());
        for(int i=0;i<enemys.size();i++)
            enemys.get(i).updatePathFinder(LevelManager.graph);
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        
        
        if (debugMode) {
            batch.begin();
            graphRenderer.renderOffScreenedGraph();
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            batch.begin();
            for (Tower t : torres) {
                t.render(shapeRenderer);
            }
            batch.end();
            shapeRenderer.end();
        }
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        towerRenderer.renderAll(torres);
        for(int i=0;i<enemys.size();i++) 
            enemys.get(i).update(Gdx.graphics.getDeltaTime());

        //Adiciona Inimigos
         if((TimeUtils.timeSinceMillis(start)/2000)+1>cont){     
            enemys.add(new Enemy(new Vector2(
                      LevelManager.tileWidth / 2, LevelManager.totalPixelHeight/2),
                   Color.FIREBRICK));
            enemys.get(enemys.size()-1).setGoal(LevelManager.totalPixelWidth-1, LevelManager.totalPixelHeight/2);  
            enemys.get(enemys.size()-1).setGoal(LevelManager.totalPixelWidth-1, LevelManager.totalPixelHeight/2);          
            enemys.get(enemys.size()-1).update(Gdx.graphics.getDeltaTime());
            cont++;     
        }
        
         
        //Remove o inimigo
        if (colidiu < 5) {
            //enemyRender.render(enemy);
            for (int i = 0; i < enemys.size(); i++) {
                enemyrender.render(enemys.get(i));
                if (enemys.get(i).isMoving() == false) {
                    enemys.remove(i);
                    deadEnemy++;
                }
            }
        }
        counter++;
        //varios tiros para uma mesma torre
        if(counter%100==0){
            for(int i=0; i<torres.size();i++){
                Random r = new Random();
                int en = r.nextInt(enemys.size());
                //objetivo.setObjetivo(new Vector3(agent.position.coords.x, agent.position.coords.y, 0));
                objetivo.setObjetivo(new Vector3(enemys.get(en).position.coords.x,enemys.get(en).position.coords.y, 0));
                novoBullet(new Vector3(torres.get(i).position.coords.x,torres.get(i).position.coords.y,0)).defineComportamento(buscar);   
            }
        }
        
        //verifica colisao com o alvo
        boolean definiuObjetivo = false;
        Iterator<Bullet> it = bullets.iterator();
                    Bullet atual = null;
                    while (it.hasNext() && !definiuObjetivo) {
                        atual = it.next();
                        definiuObjetivo = colideCom(
                                new Circle(
                                        new Vector2(
                                                atual.pose.posicao.x,
                                                atual.pose.posicao.y),
                                        BulletRenderer.RAIO),
                                new Vector3(objetivo.getObjetivo().x, objetivo.getObjetivo().y,0));
                        if(definiuObjetivo==true){
                            bullets.removeValue(atual, debugMode);
                            colidiu++;
                        }
                        
                    }
                    
                    
        batch.setProjectionMatrix(camera.combined);
		for (Bullet bullet : bullets) {
			bulletRender.desenha(bullet);
		}
		float delta = Gdx.graphics.getDeltaTime();
		// atualiza a lógica de movimento dos agentes
		atualizaAgentes(delta);

		//renderizadorObjetivo.update(Gdx.graphics.getDeltaTime());

		batch.begin();
              //  enemy.render(batch);
		batch.end();
        
                

        Gdx.graphics.setTitle(
                String.format(windowTitle, Gdx.graphics.getFramesPerSecond()));
    }
    
    private void atualizaAgentes(float delta) {
       for (Bullet bullet : bullets) {
			// atualiza lógica
			bullet.atualiza(delta);
			// contém os agentes dentro do mundo
			//revolveCoordenadas(agente);
		}
        
    }
    
    public static final boolean colideCom(Circle circulo, Vector3 ponto) {
        return circulo.contains(new Vector2(ponto.x, ponto.y));
    }
    

}
