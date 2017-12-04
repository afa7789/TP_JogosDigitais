package br.cefetmg.games;

import br.cefetmg.games.graphics.Facing;
import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.Steering;
import br.cefetmg.games.movement.Target;
import br.cefetmg.games.movement.behavior.Algorithm;
import br.cefetmg.games.movement.behavior.Seek;
import br.cefetmg.games.pathfinding.TileConnection;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder.Metrics;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class Enemy {

    public Position position;
    private final Algorithm seek;
    private static IndexedAStarPathFinder pathFinder;
    private final DefaultGraphPath<TileConnection> path;
    private Iterator<TileConnection> pathIterator;
    private final Target steeringTarget;
    private final float fullSpeed = 20;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    private Facing facing;
    private TileNode nextNode, currentNode;
    public Vector2 Goal;
    public Strength color;
    public boolean shouldMove;
    public boolean terminouOPercurso;
    public double life;
    public boolean desenhe =true;
    public boolean jaDeuPontos=false;
    public static Vector2 worldDimensions;
    public static final float reajuste = 0.02f;

    public Enemy(Vector2 position, Strength color,float worldWidth, float worldHeight) {
        this.position = new Position(position);
        this.color = color;
        this.steeringTarget = new Target(position);
        this.seek = new Seek(fullSpeed);
        this.seek.target = steeringTarget;
        this.pathFinder = new IndexedAStarPathFinder(LevelManager.graph, true);
        this.path = new DefaultGraphPath<>();
        this.pathIterator = this.path.iterator();
        this.facing = Facing.EAST;
        this.shouldMove = false;
        this.terminouOPercurso = false;
        this.life = 14 * getSustain();
        this.desenhe = true;
        this.worldDimensions = new Vector2(worldWidth, worldHeight);

    }
    private int getSustain() {
         switch (color){
             case VIOLETA:
                 return 7;
             case LARANJA:
                 return 2;
             case AMARELO:
                 return 3;
             case VERDE:
                 return 4;
             case CIANO:
                 return 5;
             case AZUL:
                 return 6;
             default:
                 return 1;
         }
    }
    public void naoDesenhar(){
        desenhe = false;
    }
    
    public void updatePathFinder (IndexedGraph g) {
        this.pathFinder = new IndexedAStarPathFinder(g, true);
    }
    /**
     * Atualiza a posição do agente de acordo com seu objetivo de alto nível
     * (pathfinding).
     *
     * @param delta tempo desde a última atualização.
     */
    public void update(float delta) {
        shouldMove = true;
        // verifica se atingimos nosso objetivo imediato
        if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED) {
            // procurar se temos outra conexão na nossa rota
            // e, caso afirmativo, definir o nó de chegada como novo target
            if ( shouldMove = pathIterator.hasNext() ) {
                TileConnection nextConnection = pathIterator.next();
                nextNode = nextConnection.getToNode();
                steeringTarget.coords = nextNode.getPosition();

                // atualiza a velocidade do "seek" de acordo com o terreno (a conexão)
                this.seek.maxSpeed = fullSpeed - (fullSpeed / 2.0f) * (nextConnection.getCost() - 1) / (LevelManager.maxCost - 1);
            }
        } else if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED * 6) {
            currentNode = nextNode;
        }
        // integra
        if (shouldMove) {
            Steering steering = seek.steer(this.position);
            position.integrate(steering, delta);
            // verifica o vetor velocidade para determinar a orientação
            float angle = steering.velocity.angle();
            int quadrant = (int) (((int) angle + (360 - 67.5f)) / 45) % 8;
            facing = Facing.values()[(8 - quadrant) % 8];
        } else {
            terminouOPercurso = chegouNoFinal();
        }
    }
    
    public Vector2 enviaPosicionamento(){
        return new Vector2(this.position.coords);
    }
    /**
     * Este método é chamado quando um clique no mapa é realizado.
     *
     * @param x coordenada x do ponteiro do mouse.
     * @param y coordenada y do ponteiro do mouse.
     */
    
    public void setGoal(int x, int y, boolean debugMode) {
        TileNode startNode = LevelManager.graph
                .getNodeAtCoordinates(
                        (int) this.position.coords.x,
                        (int) this.position.coords.y);
        TileNode targetNode = LevelManager.graph
                .getNodeAtCoordinates(x, y);
        Goal = new Vector2(x,y);

        path.clear();
        pathFinder.metrics.reset();
        // AQUI ESTAMOS CHAMANDO O ALGORITMO A* (instância pathFinder)
        pathFinder.searchConnectionPath(startNode, targetNode,
                new Heuristic<TileNode>() {
            @Override
            public float estimate(TileNode n, TileNode n1) {
                Vector2 v1,v2;
                v1 = new Vector2(n.getPosition().x/LevelManager.tileWidth,n.getPosition().x/LevelManager.tileHeight);
                v2 = new Vector2(n1.getPosition().x/LevelManager.tileWidth,n1.getPosition().x/LevelManager.tileHeight);
                float CustoEuclidiano=v1.dst(v2);
                Vector2 Diagonal = new Vector2(Math.abs(v1.x - v2.x), Math.abs(v1.y- v2.y));
                return (float) (CustoEuclidiano * (Diagonal.x + Diagonal.y) + ((Math.sqrt(2)-2)*(CustoEuclidiano)*Math.min(Diagonal.x,Diagonal.y)));
            }
        }, path);
        
        pathIterator = path.iterator();
       // System.out.println(path.nodes.size);
        if (debugMode) System.out.println(path.nodes.size);
    }

    /**
     * Retorna em que direção (das 8) o agente está olhando.
     *
     * @return a direção de orientação.
     */
    public Facing getFacing() {
        return facing;
    }
    public Vector2 getPosition(){
        return this.position.coords;
    }
    
    public boolean chegouNoFinal(){
        if( this.position.coords.dst2(Goal) > 0.5f)
            return true;
        return false;
    }
    
    /**
     * Retorna se o agente está se movimentando ou se está parado.
     *
     * @return
     */
    public boolean isMoving() {
        return shouldMove;
    }

    private float getPercentageOfNodeTraversalConcluded() {
        float totalDistance2 = currentNode.getPosition()
                .dst2(nextNode.getPosition());
        float remainingDistance2 = position.coords.dst2(nextNode.getPosition());
        return (totalDistance2 - remainingDistance2) / totalDistance2;
    }
    /**
     * Retorna as métricas da última execução do algoritmo de planejamento de
     * trajetórias.
     *
     * @return as métricas.
     */
    
    public Metrics getPathFindingMetrics() {
        return pathFinder.metrics;
    }
    
    public void setLife(double life){
        this.life=life;
    }
    
    public void looseLife(double percentage){
        this.life = this.life - 100*percentage;
    }
    
    public double getLife(){
        return this.life;
    }
    public Color getColor(){
        Color corReceber= new Color();
        switch(color){
            case VERMELHO:
                corReceber.set( 1, 0, 0, 0);
                break;
            case LARANJA:
                corReceber.set( 1, 0.64f, 0, 0);
                break;
            case AMARELO:
                corReceber.set( 1, 1, 0, 0);
                break;
            case VERDE:
                corReceber.set( 0, 1, 0, 0);
                break;
            case CIANO: 
                corReceber.set( 0, 1, 1, 0);
                break;
            case AZUL:
                corReceber.set( 0, 0, 1, 0);
                break;
            case VIOLETA:
                corReceber.set( 0.30f , 0.18f, 0.30f, 0);
                break;
            }
        return corReceber;
    }
    
    public ShapeRenderer getForm (ShapeRenderer renderer) {
        renderer.x(position.coords,10);
        renderer.circle(position.coords.x, position.coords.y, 6);
        Vector2 size = new Vector2(worldDimensions.cpy().scl(reajuste));
        renderer.polygon(new float[] {      
                    position.coords.cpy().scl(1.02f).sub(size).x, position.coords.cpy().sub(size).y,               // Vertex 0         3  
                    position.coords.cpy().scl(0.98f).add(size).x, position.coords.cpy().sub(size).y,               // Vertex 1      /     \        
                    position.coords.cpy().x, position.coords.cpy().add(size).scl(1.03f).y,                         // Vertex 3      \     /
                    position.coords.cpy().scl(1.01f).sub(size).x, position.coords.cpy().scl(0.98f).add(size).y,     // Vertex 4       0---1 
                    position.coords.cpy().scl(0.99f).add(size).x, position.coords.cpy().scl(0.98f).add(size).y    // Vertex 2     4       2   
                    
                });
        return renderer;
    }
    
}
