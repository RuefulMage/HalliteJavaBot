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
        Zone [] zones = new Zone[4];
        game.ready("MyJavaBot");
        Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
        Zone home = getZone(game);
        /*Zone [] temp = divideZoneByX(home);
        Zone [] temp2 = divideZoneByY(temp[0]);
        Zone [] temp3 = divideZoneByY(temp[1]);
        zones[0] = temp2[0];
        zones[1] = temp2[1];
        zones[2] = temp3[0];
        zones[3] = temp3[1];*/
        for (;;) {
            game.updateFrame();
            final Player me = game.me;
            final GameMap gameMap = game.gameMap;

            final ArrayList<Command> commandQueue = new ArrayList<>();
            int counter = 0;
            for (final Ship ship : me.ships.values()) {
                if (ship.halite > 600) {
//                    final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
                    commandQueue.add(ship.move(gameMap.naiveNavigate(ship, me.shipyard.position)));
                    gameMap.at(ship.position.directionalOffset(gameMap.naiveNavigate(ship, me.shipyard.position))).markUnsafe(ship);

                } else {
                    if(gameMap.at(ship).halite > Constants.MAX_HALITE / 10){
                        commandQueue.add(ship.stayStill());
                        gameMap.at(ship.position).markUnsafe(ship);
                    }
                    else{
                        final int randomDirection = rng.nextInt(4);
                        if(ship.position.equals(me.shipyard.position)) {
                            commandQueue.add(ship.move(gameMap.naiveNavigate(ship, getMaxInZone(gameMap, home))));
                            gameMap.at(ship.position.directionalOffset(gameMap.naiveNavigate(ship, getMaxInZone(gameMap, home)))).markUnsafe(ship);
                        }
                        else{
                            commandQueue.add(ship.move(gameMap.naiveNavigate(ship, getMaxInZone(gameMap, home))));
                            gameMap.at(ship.position.directionalOffset(gameMap.naiveNavigate(ship, getMaxInZone(gameMap, home)))).markUnsafe(ship);
                        }

                    }
                }
                counter ++;
            }
            if (
                //game.turnNumber <= 200 &&
                me.halite >= Constants.SHIP_COST &&
                !gameMap.at(me.shipyard).isOccupied()
                && me.ships.size() < 4)
            {

                commandQueue.add(me.shipyard.spawn());
                home = getExpansedZone(home, game);
            }
            game.endTurn(commandQueue);
        }
    }
    public static Zone getZone(Game game){
//        if(game.players.size() == 2){
//            return getZoneForTwo(game.me, game.gameMap);
//        }
//        else{
//            return getZoneForFour(game.me, game.gameMap);
//        }
        int x1 = game.gameMap.width - 5;
        int y1 = game.gameMap.height - 5;
        int x2 = game.gameMap.width + 5;
        int y2 = game.gameMap.height + 5;
        return  new Zone(x1,y1,x2,y2);
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
    public static Position getMaxInZone(GameMap map, Zone zone){
        Position max = new Position(zone.leftX, zone.leftY);
        for (int i = zone.leftX; i < zone.rightX + 1; i++){
            for (int j = zone.rightY; j < zone.leftX + 1; j++){
                if ((map.at(new Position(i, j)).halite > map.at(max).halite)){
                    max = new Position(i, j);
                }
            }
        }
        return max;
    }
    public static ArrayList<Position> getListOfPos(GameMap map, Zone zone){
        ArrayList<Position> pos = new ArrayList<>();
        for (int i = zone.leftX; i < zone.rightX + 1; i++){
            for (int j = zone.rightY; j < zone.leftX + 1; j++){
                pos.add(new Position(i, j));
            }
        }
        return pos;
    }

    public static Zone[] divideZoneByX(Zone zone){
        int leftX1 = zone.leftX + (zone.rightX / 2);
        int rightX2 = leftX1;
        Zone zone1 = new Zone(zone.leftX, zone.leftY, leftX1, zone.rightY);
        Zone zone2 = new Zone(leftX1, zone.leftY, zone.rightX, zone.rightY);
        return new Zone[] {zone1, zone2};
    }

    public static Zone[] divideZoneByY(Zone zone){
        int newY = zone.leftY/2;
        Zone zone1 = new Zone(zone.leftX, newY, zone.rightX, zone.rightY);
        Zone zone2 = new Zone(zone.leftX, zone.leftY, zone.rightX, newY);
        return new Zone[] {zone1, zone2};
    }


    public static Zone getCurrentZone(Ship ship, Zone[] zones){
        for( Zone zone : zones){
            if (inZone(ship, zone)) {
                return zone;
            }
        }
        return null;
    }

    public static boolean inZone(Ship ship, Zone zone){
        if( (zone.leftX <= ship.position.x) && (ship.position.x <= zone.rightX)
                && (zone.leftY >=  ship.position.y) && (ship.position.y >= zone.rightY)){
            return true;
        }
        return false;
    }


    public static Zone getExpansedZone(Zone zone, Game game){
        if((zone.rightY - 2 < game.gameMap.height) || (zone.leftY + 2 > game.gameMap.height)
                || (zone.rightX + 2> game.gameMap.width) || (zone.leftX - 2< 0)){
            Log.log("zone is too large");
            return zone;
        }
        return  new Zone(zone.leftX - 2, zone.leftY + 2, zone.rightX + 2, zone.rightY - 2);
    }
}
