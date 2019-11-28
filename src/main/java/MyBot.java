// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.

import hlt.*;

import java.lang.reflect.Array;
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
        Zone [] zones = new Zone[4];
        game.ready("MyJavaBot");
        Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
//        Zone home = getZone(game);
        System.out.println(makeSuperMerge());
        /*Zone [] temp = divideZoneByX(home);
        Zone [] temp2 = divideZoneByY(temp[0]);
        Zone [] temp3 = divideZoneByY(temp[1]);
        zones[0] = temp2[0];
        zones[1] = temp2[1];
        zones[2] = temp3[0];
        zones[3] = temp3[1];*/
//        for (;;) {
//            game.updateFrame();
//            final Player me = game.me;
//            final GameMap gameMap = game.gameMap;
//
//            final ArrayList<Command> commandQueue = new ArrayList<>();
//            int counter = 0;
//            ArrayList<MapCell> cells = getListOfCells(gameMap, home);
//            cells.sort(new PositionHalComparator());
//            for (final Ship ship : me.ships.values()) {
//                if (ship.halite > 400) {
////                    final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
//                    commandQueue.add(ship.move(gameMap.naiveNavigate(ship, me.shipyard.position)));
//                    gameMap.at(ship.position.directionalOffset(gameMap.naiveNavigate(ship, me.shipyard.position))).markUnsafe(ship);
//
//                } else {
//                    if(gameMap.at(ship).halite > Constants.MAX_HALITE / 10 && ship.halite < 100){
//                        commandQueue.add(ship.stayStill());
//                        gameMap.at(ship.position).markUnsafe(ship);
//                    }
//                    else{
//                        final int randomDirection = rng.nextInt(4);
//                        Position pos = cells.get(cells.size() - 1 - counter).position;
//                        commandQueue.add(ship.move(gameMap.naiveNavigate(ship, pos)));
//                        gameMap.at(ship.position.directionalOffset(gameMap.naiveNavigate(ship, pos))).markUnsafe(ship);
//                    }
//                }
//                counter ++;
//            }
//            if (
//                //game.turnNumber <= 200 &&
//                me.halite >= Constants.SHIP_COST &&
//                !gameMap.at(me.shipyard).isOccupied()
//                && me.ships.size() < home.area / 8)
//            {
//
//                commandQueue.add(me.shipyard.spawn());
//                home = getExpansedZone(home, game);
//            }
//            game.endTurn(commandQueue);
//        }
    }
    public static String makeSuperMerge(){
        String greatString = "SUPERMERGE";
        String epicEndOfLine = "!!!!!!!!!!!!!!!!!!!!!";
        return greatString.concat(epicEndOfLine);
    }

}
