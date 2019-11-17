// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.

import hlt.*;

import java.util.ArrayList;
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
                if (ship.isFull()) {
//                    final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
                    commandQueue.add(ship.move(gameMap.naiveNavigate(ship, me.shipyard.position)));
                } else {
                    if(gameMap.at(ship).halite < Constants.MAX_HALITE / 10){
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
}
