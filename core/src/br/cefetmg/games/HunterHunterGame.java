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
    private boolean GameOver = false;    
    private boolean showingMetrics;
    
    public boolean booleanSpawn;

    private int quantidadeDeInimigosDisponiveis;
    private int maximoDeInimigos;

    private int quantidadeDeTorresDisponiveis = 1;
    public int valorParaGanharVidaExtra = 100;

    int counter;
    int nivel;
    int numeroDeVidasMaximo;
    int numeroDeVidas;
    int pontos;
    int somatorioDePontos ;
    int valorDePontosQueGanhaSeForGanharVidaEVidaJaTiverNoMaximo;
    int posicaoy;
    
    
    private Array<Bullet> bullets;
    private ArrayList<Attack> attacks;
    public Attack teste;
    private BulletTarget objetivo;
    private Follow buscar;
    private MovementAlgorithm algoritmoCorrente;
    private Array<MovementAlgorithm> algoritmos;

    HUD hud;
        
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
    
    void restart(){
        counter = 0;
        nivel = 0;
        numeroDeVidasMaximo = 10;
        numeroDeVidas = 5;
        pontos = 1;
        somatorioDePontos = 0;
        valorDePontosQueGanhaSeForGanharVidaEVidaJaTiverNoMaximo = 10;
        booleanSpawn = false;
        constructionMode=false;
        quantidadeDeTorresDisponiveis = 5;
        quantidadeDeInimigosDisponiveis = 5;
        maximoDeInimigos = quantidadeDeInimigosDisponiveis;
        
        start = TimeUtils.millis();
        cont = 0;
        deadEnemy = 0;
        posicaoy=0;
        enemys = new ArrayList<Enemy>();
        torres = new ArrayList<Tower>();
        attacks = new ArrayList<Attack>();
    }
    
    @Override
    public void create() {

        hud = new HUD();
        
        restart();
        //init time 
        hud.setFulllif(numeroDeVidas);
       
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
        towerRenderer = new TowerRenderer(batch, camera);

        //Enemy 
        //enemyspritesheet=new Texture("goomba-spritesheet.png");
        enemyRenderer = new EnemyRenderer(batch, camera);//, new Texture("gon.png")); //new AgentRenderer(batch, camera,enemyspritesheet);
       /* enemys.add(new Enemy(new Vector2(
                LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2),
                Color.FIREBRICK));*/

        metricsRenderer = new MetricsRenderer(batch, shapeRenderer,
                new BitmapFont());

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
        //teste2 = new Tower(viewport.getWorldWidth(), viewport.getWorldHeight());
        //teste2.setTorre(300,300, debugMode);
        //bullets = new Array<>();
        //  for(int i=0;i<enemys.size();i++){
        //enemys.get(0).setGoal(LevelManager.totalPixelWidth - 1, LevelManager.totalPixelHeight / 2, debugMode);
        // }
        //agent.setGoal(LevelManager.totalPixelWidth-1, LevelManager.totalPixelHeight/2);
        //teste = new Attack(teste2,40,new Position(new Vector2(500,500)),enemys.get(0));
        
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
                    System.out.println(booleanSpawn + "  " + quantidadeDeInimigosDisponiveis + "  " + (booleanSpawn && quantidadeDeInimigosDisponiveis > 0));
                }
                if (keycode == Input.Keys.E) {
                    System.out.println(quantidadeDeTorresDisponiveis + 1);
                    quantidadeDeTorresDisponiveis++;
                }
                return false;
            }

            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                Vector2 clique = new Vector2(x, y);
                viewport.unproject(clique);
                
                hud.listeningbutton(button, clique);
                
                if (clique.y < 640) {
                    posicaoy = (int) clique.y;
                }
                if (button == Input.Buttons.LEFT) {
                    if (quantidadeDeTorresDisponiveis > 0) {
                        if (!constructionMode) {
                            construtorDeTorre(clique.x, posicaoy);
                        } else {
                            //seila tocar um som para mostrar que não pode construir.
                        }
                        if (constructionMode) {
                            rebuildTower(clique.x, posicaoy);
                            constructionMode = !constructionMode;
                        }
                    }
                }
                if (button == Input.Buttons.RIGHT) {
                    if (quantidadeDeTorresDisponiveis > 0) {
                        for (Tower t : torres) {
                            //System.out.println(t.getPosition().coords.x +" " + (int) clique.x);
                            if (Math.abs(t.getPosition().coords.x - (int) clique.x) < 16 && Math.abs(t.getPosition().coords.y - posicaoy) < 16) {
                                t.upgradeTower();
                                if (t.isMaxPower) {
                                    if (debugMode) {
                                        System.out.println("já está com poder no maximo não da para aumentar");
                                    }
                                } else {
                                    quantidadeDeTorresDisponiveis--;
                                }
                                if (debugMode) {
                                    System.out.println("OK");
                                }
                            }
                        }
                    }
                }
                // Botão ESQUERDO: posiciona objetivo
                /*if (button == Input.Buttons.LEFT) {
                    if (constructionMode) {
                        TileNode towerNode = LevelManager.graph.getNodeAtCoordinates((int) clique.x, (int) clique.y);
                        boolean emptyPlace = true;
                        for (int i = 0; i < torres.size(); i++) {
                            if (torres.get(i).getPosition().coords.x == towerNode.getPosition().x && torres.get(i).getPosition().coords.y == towerNode.getPosition().y) {
                                if (debugMode) System.out.println("ja existe uma torre no lugar!");
                                torres.get(i).changeTowerType();
                                emptyPlace = false;
                            }
                        }
                        if (emptyPlace) {
                            Tower Aux = new Tower(viewport.getWorldWidth(), viewport.getWorldHeight());
                            Aux.setTorre((int) clique.x, (int) clique.y, debugMode);
                            Random r = new Random();
                            int en = r.nextInt(enemys.size());
                            
                            //Aux.setComportamento(new Vector2(enemys.get(en).position.coords.x,enemys.get(en).position.coords.y));
                            //Aux.setTarget(enemys.get(en));
                            //Aux.newBullet(new Vector3((int) clique.x, (int) clique.y, 0));
                            //Aux.estáAtacando();
                            torres.add(Aux);
                            atualizaGrafo();
                        }
                        constructionMode = !constructionMode;
                    }
                }
                if (button == Input.Buttons.RIGHT) {
                    if (quantidadeDeTorresDisponiveis > 0) {
                        for (Tower t : torres) {
                            //System.out.println(t.getPosition().coords.x +" " + (int) clique.x);
                            if (Math.abs(t.getPosition().coords.x - (int) clique.x) < 16 && Math.abs(t.getPosition().coords.y - posicaoy) < 16) {
                                t.upgradeTower();
                                if (t.isMaxPower) {
                                    if (debugMode) {
                                        System.out.println("já está com poder no maximo não da para aumentar");
                                    }
                                } else {
                                    quantidadeDeTorresDisponiveis--;
                                }
                                if (debugMode) {
                                    System.out.println("OK");
                                }
                            }
                        }
                    }
                }*/
                return true;
            }
        });
    }

    
    public boolean rebuildTower(float x, float y) {
        TileNode towerNode = LevelManager.graph.getNodeAtCoordinates((int) x, (int) y);
        for (Tower torre : torres) {
            if (torre.position.coords.x == towerNode.getPosition().x && torre.position.coords.y == towerNode.getPosition().y) {
                if (debugMode) {
                    System.out.println("ja existe uma torre no lugar!");
                }
                if (constructionMode) {
                    if (torre.isFinalForm) {
                        if (debugMode) {
                            System.out.println("Já ta na FormaFinal");
                        }
                    } else {
                        quantidadeDeTorresDisponiveis--;
                        torre.changeTowerType();
                    }
                }
                return false;
            }
        }
        return true;
    }

    public void construtorDeTorre(float x, float y) {
        boolean emptyPlace = rebuildTower(x, y);
        if (emptyPlace) {
//            Random r = new Random();
//            int en = r.nextInt(enemys.size());
//            Aux.setComportamento(new Vector2(enemys.get(en).position.coords.x,enemys.get(en).position.coords.y));
//            Aux.newBullet(new Vector3((int) clique.x, (int) clique.y, 0));
            Tower Aux = new Tower(viewport.getWorldWidth(), viewport.getWorldHeight());
            Aux.setTorre((int) x, (int) y, debugMode);
            torres.add(Aux);
            quantidadeDeTorresDisponiveis--;
            atualizaGrafo();
            
        }

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

    public void atualizaGrafo() {
        LevelManager.setGraph(GraphGenerator.generateGraphAgain(LevelManager.graph.getAllNodes(), LevelManager.tiledMap),debugMode);
        graphRenderer = new GraphRenderer(batch, shapeRenderer);
        graphRenderer.renderGraphToTexture(LevelManager.graph);
        metricsRenderer = new MetricsRenderer(batch, shapeRenderer, new BitmapFont());
        for (Enemy enemy : enemys) {
            enemy.updatePathFinder(LevelManager.graph);
        }
    }

    
    public boolean InimigosPodemSpawnar() {
        if (booleanSpawn && quantidadeDeInimigosDisponiveis > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void criaInimigo() {
        if (nivel < 3) {
            enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VERMELHO, viewport.getWorldWidth(), viewport.getWorldHeight()));
        } else if (nivel < 5) {
            if (quantidadeDeInimigosDisponiveis >= maximoDeInimigos / 2) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VERMELHO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.LARANJA, viewport.getWorldWidth(), viewport.getWorldHeight()));
            }
        } else if (nivel < 7) {
            if (quantidadeDeInimigosDisponiveis >= 3 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VERMELHO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else if (quantidadeDeInimigosDisponiveis >= 1 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.LARANJA, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.AMARELO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            }
        } else if (nivel < 10) {
            if (quantidadeDeInimigosDisponiveis >= 3 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.LARANJA, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else if (quantidadeDeInimigosDisponiveis >= 1 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.AMARELO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VERDE, viewport.getWorldWidth(), viewport.getWorldHeight()));
            }
        } else if (nivel < 11) {
            if (quantidadeDeInimigosDisponiveis >= 3 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.AMARELO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else if (quantidadeDeInimigosDisponiveis >= 1 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VERDE, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.CIANO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            }
        } else if (nivel < 12) {
            if (quantidadeDeInimigosDisponiveis >= 3 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VERDE, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else if (quantidadeDeInimigosDisponiveis >= 2 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.CIANO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else if (quantidadeDeInimigosDisponiveis >= maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.AZUL, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VIOLETA, viewport.getWorldWidth(), viewport.getWorldHeight()));
            }
        } else {
            if (quantidadeDeInimigosDisponiveis >= 3 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.CIANO, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else if (quantidadeDeInimigosDisponiveis >= 2 * maximoDeInimigos / 4) {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.AZUL, viewport.getWorldWidth(), viewport.getWorldHeight()));
            } else {
                enemys.add(new Enemy(new Vector2(LevelManager.tileWidth / 2, LevelManager.totalPixelHeight / 2), Strength.VIOLETA, viewport.getWorldWidth(), viewport.getWorldHeight()));
            }
        }

    }
    
    
    public void adicionaInimigos() {
        if (InimigosPodemSpawnar()) {
            //System.out.println(counter);
            if (counter % (200 / (nivel + 1)) == 0) {//aranjar jeito melhor de fazer isso.
                System.out.println("era para spawnar");
                if (debugMode) {
                    System.out.println("spawno");
                }
                criaInimigo();
                enemys.get(enemys.size() - 1).setGoal(LevelManager.totalPixelWidth - 1, LevelManager.totalPixelHeight / 2, debugMode);
                //enemys.get(enemys.size() - 1).setGoal(LevelManager.totalPixelWidth - 1, LevelManager.totalPixelHeight / 2, debugMode);
                enemys.get(enemys.size() - 1).update(Gdx.graphics.getDeltaTime());
                cont++;
                quantidadeDeInimigosDisponiveis--;
            }
        }
    }
    
    public void emissorDeAtaques() {
        for (Tower torre : torres) {
            if (torre.atacandoAlguem()) {
                if(torre.target.getLife()>0){
                    if (counter % torre.tempoEntreAtaques == 0) {
                        if (debugMode) System.out.println("Adicionou ataque");
                        torre.newAttack(new Vector3(torre.position.coords.x, torre.position.coords.y,0),torre.target);
                    }
                }else{
                    if (debugMode) System.out.println("Parou de Atacar");    
                    torre.parouDeAtacar();
                }
            }else{
                torre.target = pegaInimigoMaisProximoDoAlcanceDaTorre(torre);
            }
        }
    }
    
    public Enemy pegaInimigoMaisProximoDoAlcanceDaTorre(Tower torre){
        Float menorValor=torre.actionZone;
        Enemy inimigoMaisProximo=null;
        Float distancia;
        for (Enemy enemy : enemys) {
            distancia = enemy.enviaPosicionamento().dst(torre.position.coords);
            if(distancia<=torre.actionZone){
                if (debugMode) System.out.println("Agora a torre está a Atacar");
                torre.estáAtacando();
                inimigoMaisProximo=enemy;
                menorValor=distancia;
                Position p = new Position(new Vector2(torre.position.coords.x, torre.position.coords.y));
                //Attack attack = new Attack(torre, 80, p, enemy);
                torre.setComportamento(new Vector2(enemy.position.coords.x,enemy.position.coords.y));
                torre.newAttack(new Vector3(torre.position.coords.x, torre.position.coords.y,0),enemy);
                //attack.setComportamento(new Vector2(enemy.position.coords.x, enemy.position.coords.y));
                //attacks.add(attack);
            }                    
        }
            return inimigoMaisProximo;
    }
 public void removendoOInimigo(Enemy enemy) {
        enemy.naoDesenhar();
        //enemys.remove(enemy);
        cont--;
    }

    public void adicionarPontos() {
        pontos++;
    }

    public void perdeVida() {
        numeroDeVidas--;
        hud.contdead();
    }

    public void removendoUltimaTorre() {//Pego o Tamanho do ArrayList e remove a ultima torre posta
        torres.remove(torres.get(torres.size() - 1));
        //para corrigir isso vou usar adicionar Pontos para compensar então na proxima Wave ele pode por mais torre.
        adicionarPontos();
    }

    public void removerAtualizarInimigos(float delta) {
        for (Enemy enemy : enemys) {
            enemy.update(delta);
            if (enemy.getLife() > 0) {
                if (enemy.desenhe) {
                    if (!enemy.shouldMove && !enemy.terminouOPercurso) {
                        removendoUltimaTorre();
                        quantidadeDeTorresDisponiveis++;
                        enemy.terminouOPercurso = !enemy.terminouOPercurso;
                        //Acho que tem q atualizar o Path após remover a torre.
                        atualizaGrafo();
                    }
                    if (!enemy.shouldMove && enemy.terminouOPercurso) {
                        //chegouNoFim
                        removendoOInimigo(enemy);
                        //cont--; //tem q tirar isso quando voltar com a de cima.
                        perdeVida();
                    }
                }
            } else {
                //Tem q somar os pontos aqui
                adicionarPontos();
                //removendoOInimigo(enemy);
                enemys.remove(enemy);
            }
        }
    }

    public void desenhoGeral() {
        /* Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);*/

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (debugMode) {
            batch.begin();
            graphRenderer.renderOffScreenedGraph();
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Tower t : torres) {
                t.render(shapeRenderer);
            }
            shapeRenderer.end();
        }
        if (showingMetrics) {
            metricsRenderer.render(numeroDeVidas, pontos, nivel,quantidadeDeTorresDisponiveis);
        }

    }

    public void controleDeFase() {
        boolean faseAcabou = false;

        if (quantidadeDeTorresDisponiveis == 0) {
            booleanSpawn = true;
        }
        if (debugMode) {
            System.out.println("aass " + quantidadeDeInimigosDisponiveis + " " + cont + " " + (quantidadeDeInimigosDisponiveis == 0 && cont == 0) + " " + quantidadeDeTorresDisponiveis);
        }
        if ((quantidadeDeInimigosDisponiveis == 0 && cont == 0) && quantidadeDeTorresDisponiveis == 0) {
            faseAcabou = true;
            if (debugMode) {
                System.out.println("Fim da Fase");
            }
            booleanSpawn = false;
        }
        if (faseAcabou) {
            if (debugMode) {
                System.out.println("Nova Fase " + nivel);
            }
            nivel++;
            enemys.removeAll(enemys);//deve tirar todos os enemys.
            attacks.removeAll(attacks);//deve tirar todos os ataques
            quantidadeDeTorresDisponiveis = pontos * nivel;// tem que colocar de acordo com o numero de Inimigos q matou.
            quantidadeDeInimigosDisponiveis = 4 + (numeroDeVidasMaximo - numeroDeVidas) * nivel;
            maximoDeInimigos = quantidadeDeInimigosDisponiveis;

            somatorioDePontos += pontos;
            pontos = 0;
            if (somatorioDePontos % valorParaGanharVidaExtra == 0) {
                if (numeroDeVidas < numeroDeVidasMaximo && numeroDeVidas > 0) {
                    numeroDeVidas++;
                } else {
                    for (int i = 0; i < valorDePontosQueGanhaSeForGanharVidaEVidaJaTiverNoMaximo; i++) {
                        adicionarPontos();
                    }
                }
            }
            faseAcabou = false;
        }
    }

    public void desenhaHUD() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        hud.render(batch);
        batch.end();
        hud.button_render();
    }


    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        desenhaHUD();
        //hud.update();
        if(hud.getState()==0){
            restart();
            hud.setState(1);

        }else if(hud.getState()==1)
            if(numeroDeVidas>0){

               if(numeroDeVidas>=0 && !GameOver){
                   //Adiciona Inimigos
                   adicionaInimigos();
                   //Atualiza as Torres quem elas atacam e etc
                   emissorDeAtaques();
                   //controla as waves, pontos ,vidas etc
                   controleDeFase();
                   //desenho do Mapa e etc
                   desenhoGeral();
                   if(hud.state==1)
                    play(delta);
                   towerRenderer.renderAll(torres, shapeRenderer);
                    enemyRenderer.renderAll(enemys, shapeRenderer);
                    bulletRenderer.renderAll(attacks);
                    atualizaAtaques(delta);
                    for (int i=0; i<torres.size();i++) {
                        for(int j=0; j<torres.get(i).attacks.size();j++){
                            bulletRenderer.desenha(torres.get(i).attacks.get(j));
                        }
			
                        //System.out.println("target: " + bullets.get(i).bt.getObjetivo().x + ", " + bullets.get(i).bt.getObjetivo().y);
                    }
                    Gdx.graphics.setTitle(String.format(windowTitle, Gdx.graphics.getFramesPerSecond()));


               }else{
                   System.out.println("Game Over sua pontuação: "+ somatorioDePontos);
                   GameOver=true;
                   numeroDeVidas=-1;
               }
         }
    }
    void play(float delta) {
        //Remove o inimigo e atualiza posição dele
        //Atualiza Posição dos Ataques da Dano nos inimigos
        atualizaAtaques(delta);
        removerAtualizarInimigos(delta);
        counter++;

    }
    
    private void atualizaAtaques(float delta) {

       for (int i=0;i<torres.size();i++) {
            for(int j=0; j<torres.get(i).attacks.size();j++){
                // atualiza lógica
			torres.get(i).attacks.get(j).atualiza(delta, torres.get(i).comportamento.alvo.getObjetivo());
                         
            }
			
        }
        
    }
    
    
    /*private void atualizaAtaques(float delta) {
        for (Attack attack : attacks) {
            // atualiza lógica
            attack.update(delta);
            //attack.atualiza(delta);
            // contém os agentes dentro do mundo
            //revolveCoordenadas(agente);
        }
    }*/

    public static final boolean colideCom(Circle circulo, Vector3 ponto) {
        return circulo.contains(new Vector2(ponto.x, ponto.y));
    }

}
