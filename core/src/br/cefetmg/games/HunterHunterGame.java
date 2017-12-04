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
import br.cefetmg.games.movement.Position;
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
    private MetricsRenderer metricsRenderer;
    private BulletRenderer bulletRenderer;
    private EnemyRenderer enemyRenderer;

    private ArrayList<Tower> torres = new ArrayList<Tower>();

    public Tower teste2;
    private final String windowTitle;

    private boolean debugMode = false;
    private boolean constructionMode = false;

    private boolean showingMetrics;

    public boolean booleanSpawn;
    
    private int quantidadeDeInimigosDisponiveis=3;
    
    private int quantidadeDeTorresDisponiveis=10;

    int counter = 0;
    int nivel =0 ;
    
    
    private Array<Bullet> bullets;
    private ArrayList<Attack> attacks;
    public Attack teste;
    private BulletTarget objetivo;
    private Follow buscar;
    private MovementAlgorithm algoritmoCorrente;
    private Array<MovementAlgorithm> algoritmos;

    private int colidiu = 0;

    ArrayList<Enemy> enemys = new ArrayList<Enemy>();
    Texture enemyspritesheet;
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

    @Override
    public void create() {

        booleanSpawn = false;
        
        quantidadeDeTorresDisponiveis = 10;
        quantidadeDeInimigosDisponiveis = 3;
        
        teste2 = new Tower();

        //init time 
        start = TimeUtils.millis();
        cont = 1;
        deadEnemy = 0;

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
        enemyRenderer = new EnemyRenderer(batch, camera, new Texture("gon.png")); //new AgentRenderer(batch, camera,enemyspritesheet);
        //enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2),Color.FIREBRICK));

        metricsRenderer = new MetricsRenderer(batch, shapeRenderer,
                new BitmapFont());

        batch = new SpriteBatch();
        bulletRenderer = new BulletRenderer(camera, batch);

        // define o objetivo (perseguição, fuga) inicialmente no centro do mundo
        //objetivo = new BulletTarget(new Vector3(0, 0, 0));

        // configura e registra os comportamentos disponíveis
        algoritmos = new Array<>();
        buscar = new Follow(80);
        buscar.alvo = objetivo;
        algoritmos.add(buscar);
        algoritmoCorrente = buscar;

        attacks = new ArrayList<Attack>();
        
        
        
        //bullets = new Array<>();
        //  for(int i=0;i<enemys.size();i++){
        //enemys.get(0).setGoal(LevelManager.totalPixelWidth - 1, LevelManager.totalPixelHeight / 2);
        // }
        
        //agent.setGoal(LevelManager.totalPixelWidth-1, LevelManager.totalPixelHeight/2);
        
        //teste2.setTorre(300, 300);
        //teste = new Attack(teste2, 40, new Position(new Vector2(500, 500)), enemys.get(0));
        
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
                if (keycode == Input.Keys.C) {
                    constructionMode = !constructionMode;
                }
                if (keycode == Input.Keys.A) {
                    booleanSpawn = !booleanSpawn;
                }
                return false;
            }

            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                Vector2 clique = new Vector2(x, y);
                viewport.unproject(clique);
                // Botão ESQUERDO: posiciona torre
                if (button == Input.Buttons.LEFT) {
                    if (quantidadeDeTorresDisponiveis > 0) {
                        construtorDeTorre(clique.x, clique.y);
                        quantidadeDeTorresDisponiveis--;
                    } else {
                        //seila tocar um som para mostrar que não pode construir.
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
    public void construtorDeTorre(float x, float y) {
        TileNode towerNode = LevelManager.graph.getNodeAtCoordinates((int) x, (int) y);
        boolean emptyPlace = true;
        for (Tower torre : torres) {
            if (torre.position.coords.x == towerNode.getPosition().x && torre.position.coords.y == towerNode.getPosition().y) {
                System.out.println("ja existe uma torre no lugar!");
                emptyPlace = false;
            }
        }
        if (emptyPlace) {
            //Aux.newBullet(new Vector3((int) clique.x, (int) clique.y, 0));
            //torres.add(Aux);
            //Random r = new Random();
            //int en = r.nextInt(enemys.size());
            //Aux.setComportamento(new Vector2(enemys.get(en).position.coords.x,enemys.get(e
            //System.out.println("vai por a torre");
            atualizaGrafo();
            Tower Aux = new Tower();
            Aux.setTorre((int) x, (int) y);
            torres.add(Aux);
        }
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    public void atualizaGrafo() {
        LevelManager.setGraph(GraphGenerator.generateGraphAgain(LevelManager.graph.getAllNodes(), LevelManager.tiledMap));
        graphRenderer = new GraphRenderer(batch, shapeRenderer);
        graphRenderer.renderGraphToTexture(LevelManager.graph);
        metricsRenderer = new MetricsRenderer(batch, shapeRenderer, new BitmapFont());
        for (Enemy enemy : enemys) {
            enemy.updatePathFinder(LevelManager.graph);
        }
    }

    public boolean InimigosPodemSpawnar() {
        if ( booleanSpawn && quantidadeDeInimigosDisponiveis > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void adicionaInimigos() {
        if (InimigosPodemSpawnar()) {
            if( TimeUtils.timeSinceMillis(start)%500 == 0){
                System.out.println("spawno");
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2),Color.FIREBRICK));
                enemys.get(enemys.size() - 1).setGoal(LevelManager.totalPixelWidth - 1, LevelManager.totalPixelHeight / 2);
                enemys.get(enemys.size() - 1).setGoal(LevelManager.totalPixelWidth - 1, LevelManager.totalPixelHeight / 2);
                enemys.get(enemys.size() - 1).update(Gdx.graphics.getDeltaTime());
                cont++;
                quantidadeDeInimigosDisponiveis--;
            }
        }
    }

    public void emissorDeAtaques() {
        for (Tower torre : torres) {
            if (torre.atacandoAlguem()) {
                if (torre.target.getLife() > 0) {
                    if (counter % torre.attackSpeed == 0) {
                        System.out.println("Adicionou ataque");
                        attacks.add(new Attack(torre, 100, torre.position, torre.target));
                    }
                } else {
                    System.out.println("Parou de Atacar");
                    torre.parouDeAtacar();
                }
            } else {
                torre.target = pegaInimigoMaisProximoDoAlcanceDaTorre(torre);
            }
        }
    }

    public Enemy pegaInimigoMaisProximoDoAlcanceDaTorre(Tower torre) {
        Float menorValor = torre.actionZone;
        Enemy inimigoMaisProximo = null;
        Float distancia;
        for (Enemy enemy : enemys) {
            distancia = enemy.enviaPosicionamento().dst2(torre.position.coords);
            if (distancia <= torre.actionZone && distancia < menorValor) {
                System.out.println("Agora a torre está a Atacar");
                torre.estáAtacando();
                inimigoMaisProximo = enemy;
                menorValor = distancia;
            }
        }
        return inimigoMaisProximo;
    }

    public void removerAtualizarInimigos(float delta) {
        for (Enemy enemy : enemys) {
            enemy.update(delta);
            if (enemy.getLife() > 0) {
                //enemyRenderer.render(enemy);
            } else {
                //Tem q somar os pontos aqui
                enemys.remove(enemy);
                cont--;
            }
        }
    }

    public void desenhoGeral() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (debugMode) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            graphRenderer.renderOffScreenedGraph();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Tower t : torres) {
                t.render(shapeRenderer);
            }
            shapeRenderer.end();
        }
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

    }
    
    public void controleDeFase(){
            boolean faseAcabou=false;
        if(quantidadeDeTorresDisponiveis == 0){
            booleanSpawn=true;
        }
        if( (quantidadeDeInimigosDisponiveis == 0 && cont==0) && quantidadeDeTorresDisponiveis == 0){
            faseAcabou=true;
            System.out.println("Fim da Fase");
        }
        if(faseAcabou){
            System.out.println("Nova Fase");
            nivel++;
            quantidadeDeTorresDisponiveis = 5;// tem que colocar de acordo com o numero de Inimigos q matou.
            quantidadeDeInimigosDisponiveis = 3 * nivel;
            faseAcabou=false;
        }
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        controleDeFase();
        //Adiciona Inimigos
        adicionaInimigos();
        //Remove o inimigo
        removerAtualizarInimigos(delta);
        //Atualiza as Torres quem elas atacam e etc
        emissorDeAtaques();
        //Atualiza Posição dos Ataques da Dano nos inimigos
        atualizaAtaques(delta);
        //desenho do Mapa e etc
        desenhoGeral();
        batch.setProjectionMatrix(camera.combined);
//        bulletRenderer.desenha(teste);
//        towerRenderer.render(teste2);
        enemyRenderer.renderAll(enemys);
        towerRenderer.renderAll(torres);
        bulletRenderer.renderAll(attacks);
        Gdx.graphics.setTitle(String.format(windowTitle, Gdx.graphics.getFramesPerSecond()));
        counter++;
    }

    private void atualizaAtaques(float delta) {
        for (Attack attack : attacks) {
            // atualiza lógica
            attack.update(delta);
            // contém os agentes dentro do mundo
            //revolveCoordenadas(agente);
        }
    }

    public static final boolean colideCom(Circle circulo, Vector3 ponto) {
        return circulo.contains(new Vector2(ponto.x, ponto.y));
    }

}
