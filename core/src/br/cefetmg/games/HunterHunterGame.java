package br.cefetmg.games;

import static br.cefetmg.games.LevelManager.graph;
import br.cefetmg.games.graphics.GraphRenderer;
import br.cefetmg.games.graphics.AgentRenderer;
import br.cefetmg.games.graphics.BulletRenderer;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Iterator;

public class HunterHunterGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private TiledMap tiledMap;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TiledMapRenderer tiledMapRenderer;
    private GraphRenderer graphRenderer;
    private TowerRenderer towerRenderer;
    
    private Agent agent;
    private AgentRenderer agentRenderer;
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
    
    
    
    public HunterHunterGame() {
        this.windowTitle = "Hunter x Hunter (%d)";
        showingMetrics = true;
    }

    public GraphRenderer getGraphRenderer() {
        return graphRenderer;
    }
    
    
    public Bullet novoAgente(Vector3 posicao) {
		Bullet agente = new Bullet(posicao,
				new Color(0, 0, 1, 1));
		agente.pose.orientacao = (float) (Math.random() * Math.PI * 2);
		agente.defineComportamento(algoritmoCorrente);

		bullets.add(agente);
		return agente;
	}
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.translate(w / 2, h / 2);
        camera.update();
        viewport = new ScreenViewport(camera);

        // Carrega o mapa
        tiledMap = LevelManager.LoadLevel("tp-mapa-teste.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        graphRenderer = new GraphRenderer(batch, shapeRenderer);
        graphRenderer.renderGraphToTexture(LevelManager.graph);
        towerRenderer = new TowerRenderer(batch);
        
        
        agentRenderer = new AgentRenderer(batch, camera, new Texture("gon.png"));
        agent = new Agent(
                new Vector2(
                        LevelManager.tileWidth / 2, LevelManager.totalPixelHeight/2),
                Color.FIREBRICK
        );

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
                
                //else if(upgradeMode)
                    //torre.upgradeTorre((int) clique.x , (int) clique.y);
                if (button == Input.Buttons.LEFT) {
                    if (constructionMode){
                        Tower Aux = new Tower();
                        Aux.setTorre((int) clique.x, (int) clique.y);
                        torres.add(Aux);
                        atualizaGrafo();
                        novoAgente(new Vector3((int) clique.x, (int) clique.y,0));
                        objetivo.setObjetivo(new Vector3(agent.position.coords.x,agent.position.coords.y,0));
                        
                        constructionMode=!constructionMode;
                    }
                    else
                        agent.setGoal((int) clique.x, (int) clique.y);
                }
                if (button == Input.Buttons.RIGHT) {
                    for (Tower t : torres) {
                        System.out.println(t.getPosition().coords.x +" " + (int) clique.x);
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
        agent.updatePathFinder(LevelManager.graph);
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        agent.update(Gdx.graphics.getDeltaTime());

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        towerRenderer.renderAll(torres);
        if(colidiu<5)
            agentRenderer.render(agent); 
        if (showingMetrics) {
            metricsRenderer.render(agent.getPathFindingMetrics(),
                    LevelManager.graph.getNodeCount());
        }

        if (debugMode) {
            batch.begin();
            graphRenderer.renderOffScreenedGraph();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Tower t : torres) {
                t.render(shapeRenderer);
            }
            shapeRenderer.end();
            batch.end();
        }
        
        
        counter++;
        //varios tiros para uma mesma torre
        if(counter%100==0){
            for(int i=0; i<torres.size();i++){
                objetivo.setObjetivo(new Vector3(agent.position.coords.x, agent.position.coords.y, 0));
                novoAgente(new Vector3(torres.get(i).position.coords.x,torres.get(i).position.coords.y,0)).defineComportamento(buscar);
                
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
		//fonte.draw(batch, stringAlgoritmoCorrente, -viewport.getWorldWidth() / 2, -viewport.getWorldHeight() / 2 + 15);
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
