package br.cefetmg.games;

import static br.cefetmg.games.LevelManager.graph;
import br.cefetmg.games.graphics.GraphRenderer;
import br.cefetmg.games.graphics.AgentRenderer;
import br.cefetmg.games.graphics.MetricsRenderer;
import br.cefetmg.games.graphics.RenderizadorAgente;
import br.cefetmg.games.graphics.RenderizadorObjetivo;
import br.cefetmg.games.movement.Agente;
import br.cefetmg.games.movement.AlgoritmoMovimentacao;
import br.cefetmg.games.movement.Alvo;
import br.cefetmg.games.movement.behavior.Buscar;
import br.cefetmg.games.pathfinding.GraphGenerator;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

public class HunterHunterGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private TiledMap tiledMap;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TiledMapRenderer tiledMapRenderer;
    private GraphRenderer graphRenderer;

    private Agent agent;
    private AgentRenderer agentRenderer;
    private ArrayList<Torre> torres = new ArrayList<Torre>();
    private final String windowTitle;
    private boolean debugMode = false;
    private boolean constructionMode = false;
    private MetricsRenderer metricsRenderer;
    private boolean showingMetrics;
    
    
    
    private Array<Agente> agentes;
    private RenderizadorAgente renderizador;
    private RenderizadorObjetivo renderizadorObjetivo;
    private Alvo objetivo;
    private Buscar buscar;
    private AlgoritmoMovimentacao algoritmoCorrente;
    private Array<AlgoritmoMovimentacao> algoritmos;

    public HunterHunterGame() {
        this.windowTitle = "Hunter x Hunter (%d)";
        showingMetrics = true;
    }

    public GraphRenderer getGraphRenderer() {
        return graphRenderer;
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
        
        //renderizadores
        renderizador = new RenderizadorAgente(camera, batch);
        renderizadorObjetivo = new RenderizadorObjetivo(camera);
        

        // Carrega o mapa
        tiledMap = LevelManager.LoadLevel("tp-mapa-teste.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        graphRenderer = new GraphRenderer(batch, shapeRenderer);
        graphRenderer.renderGraphToTexture(LevelManager.graph);
        

        agentRenderer = new AgentRenderer(batch, camera, new Texture("gon.png"));
        agent = new Agent(
                new Vector2(
                        LevelManager.tileWidth / 2, LevelManager.totalPixelHeight/2),
                Color.FIREBRICK
        );
        
        //define objetivo
        Vector3 pos = new Vector3(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight/2, 0);
        objetivo = new Alvo(pos);
        algoritmos = new Array<>();
        buscar = new Buscar(80);
        buscar.alvo = objetivo;
        algoritmos.add(buscar);
        agentes = new Array<>();
        metricsRenderer = new MetricsRenderer(batch, shapeRenderer,
                new BitmapFont());

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
                Vector3 clique = new Vector3(x, y, 0);
                viewport.unproject(clique);
                
                // Botão ESQUERDO: posiciona objetivo
                //else if(upgradeMode)
                    //torre.upgradeTorre((int) clique.x , (int) clique.y);
                if (button == Input.Buttons.LEFT) {
                    if (constructionMode){
                    //Torre Aux = new Torre();
                    //Aux.setTorre((int) clique.x, (int) clique.y);
                    //torres.add(Aux);
                    
                    //newNode.setIsObstacle(isObstacle(map, j, i));
                        LevelManager.graph.getNodeAtCoordinates((int) clique.x, (int) clique.y).setIsObstacle(true);
                        
                        LevelManager.setGraph(GraphGenerator.generateGraphAgain(LevelManager.graph.getAllNodes(),LevelManager.tiledMap));
                        graphRenderer = new GraphRenderer(batch, shapeRenderer);
                        graphRenderer.renderGraphToTexture(LevelManager.graph);
                        constructionMode=!constructionMode;
                        
                        novoAgente(clique).defineComportamento(buscar);
                    }
                    else
                        agent.setGoal((int) clique.x, (int) clique.y);
                    System.out.println("x= " + (int)clique.x);
                    System.out.println("y= " + (int)clique.y);
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
    
    
    private void atualizaAgentes(float delta) {

        // percorre a lista de agentes e os atualiza (agente.atualiza)
        buscar.alvo=objetivo;
        for (Agente agente : agentes) {
            // atualiza lógica
            agente.atualiza(delta);
            System.out.println(agente.initialPosition.x + "  " + agente.initialPosition.y);
        }
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
        agentRenderer.render(agent);
        if (showingMetrics) {
            metricsRenderer.render(agent.getPathFindingMetrics(),
                    LevelManager.graph.getNodeCount());
        }
        
        Vector3 pos = new Vector3();
        pos.x = agent.position.coords.x;
        pos.y = agent.position.coords.y;
        pos.z = 0;
        objetivo = new Alvo(pos);
        

        if (debugMode) {
            batch.begin();
            graphRenderer.renderOffScreenedGraph();
            batch.end();
        }

        Gdx.graphics.setTitle(
                String.format(windowTitle, Gdx.graphics.getFramesPerSecond()));
        
        
        
        
        for (Agente agente : agentes) {
            renderizador.desenha(agente);
        }

        // desenha o objetivo
        renderizadorObjetivo.desenha(objetivo);

        // tempo desde a última atualização
        float delta = Gdx.graphics.getDeltaTime();
        // atualiza a lógica de movimento dos agentes
        atualizaAgentes(delta);

        renderizadorObjetivo.update(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.end();

    }
    
    
    
    public Agente novoAgente(Vector3 posicao) {
        Agente agente = new Agente(posicao,
                new Color(
                        (float) Math.random(),
                        (float) Math.random(),
                        (float) Math.random(), 1));
        agente.pose.orientacao = (float) (Math.random() * Math.PI * 2);
        agente.defineComportamento(buscar);

        agentes.add(agente);
        return agente;
    }

}
