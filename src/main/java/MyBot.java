// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.

import hlt.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MyBot {
    public static void main(final String[] args) {
        final long rngSeed;
        if (args.length > 1) {
            rngSeed = Integer.parseInt(args[1]);
        } else {
            rngSeed = System.nanoTime();
        }
        final Random rng = new Random(rngSeed);

        Game game = new Game();
        // At this point "game" variable is populated with initial map data.
        // This is a good place to do computationally expensive start-up pre-processing.
        // As soon as you call "ready" function below, the 2 second per turn timer will start.
        game.ready("MyJavaBot");

        Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");

        for (;;) {
            game.updateFrame();
            final Player me = game.me;
            final GameMap gameMap = game.gameMap;

            final ArrayList<Command> commandQueue = new ArrayList<>();

            for (final Ship ship : me.ships.values()) {
//                if(ship.halite > 900){
//                    ship.setFlag(true);
//                }
//                else{
//
//                }
                if (ship.halite > 800) {
//                    final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
                    commandQueue.add(ship.move(gameMap.naiveNavigate(ship, me.shipyard.position)));
                    ship.setFlag(true);
                }else {
                    if(gameMap.at(ship).halite > Constants.MAX_HALITE / 10){
                        gameMap.at(ship.position).markUnsafe(ship);
                        commandQueue.add(ship.stayStill());
                    }
                    else{
                        if(gameMap.at(ship.position.directionalOffset(Direction.NORTH)).isOccupied()){
                            final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
                            commandQueue.add(ship.move(randomDirection));
                            gameMap.at(ship.position.directionalOffset(randomDirection)).markUnsafe(ship);
                        }
                        else {
                            commandQueue.add(ship.move(Direction.NORTH));
                            gameMap.at(ship.position.directionalOffset(Direction.NORTH)).markUnsafe(ship);
                        }
                    }
                }
            }
            if (
                //game.turnNumber <= 200 &&
                me.halite >= Constants.SHIP_COST &&
                !gameMap.at(me.shipyard).isOccupied()
                && me.ships.size() < 4)
            {
                commandQueue.add(me.shipyard.spawn());
            }
            game.endTurn(commandQueue);
        }
    }
    public static Zone getZone(Game game){
        if(game.players.size() == 2){
            return getZoneForTwo(game.me, game.gameMap);
        }
        else{
            return getZoneForFour(game.me, game.gameMap);
        }
    }

    public static Zone getZoneForFour(Player me, GameMap gameMap){
        if (me.shipyard.position.x > gameMap.width/2){
            if(me.shipyard.position.y < gameMap.height/2){
                return new Zone(gameMap.width/2, gameMap.height/2, gameMap.width, 0);
            }
            else{
                return new Zone(gameMap.width/2, gameMap.height, gameMap.width, gameMap.height/2);
            }
        }
        else{
            if(me.shipyard.position.y < gameMap.height/2){
                return new Zone(0, gameMap.height/2, gameMap.width/2, 0);
            }
            else{
                return new Zone(0, gameMap.height, gameMap.width/2, gameMap.height/2);
            }
        }
    }

    public static Zone getZoneForTwo(Player me, GameMap gameMap){
        if( me.shipyard.position.x > gameMap.width/2 ){
            return new Zone(gameMap.width/2, gameMap.height, gameMap.width, 0);
        }
        else{
            return new Zone(0, gameMap.height, gameMap.width/2, 0);
        }
    }
}
