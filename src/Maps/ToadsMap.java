package Maps;

import Enemies.BugEnemy;
import Enemies.DinosaurEnemy;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
import Level.*;
import NPCs.Walrus;
import Tilesets.CommonTileset;
import Utils.Direction;

import java.util.ArrayList;

// Represents the ToadsMap to be used in a level
public class ToadsMap extends Map {

    public ToadsMap() {
        super("toadsmap.txt", new CommonTileset());
        // Set player start position, make sure these coordinates are valid for the map
        this.playerStartPosition = getMapTile(2, 11).getLocation();
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Adding a BugEnemy at a specific location, similar to Map2
        BugEnemy bugEnemy = new BugEnemy(getMapTile(16, 10).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy);

        // Adding a DinosaurEnemy with a patrol route
        DinosaurEnemy dinosaurEnemy = new DinosaurEnemy(
            getMapTile(19, 1).getLocation().addY(2),
            getMapTile(22, 1).getLocation().addY(2),
            Direction.RIGHT
        );
        enemies.add(dinosaurEnemy);

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        // Adding a horizontal moving platform similar to Map2
        HorizontalMovingPlatform hmp = new HorizontalMovingPlatform(
            ImageLoader.load("GreenPlatform.png"),
            getMapTile(24, 6).getLocation(),
            getMapTile(27, 6).getLocation(),
            TileType.JUMP_THROUGH_PLATFORM,
            3,
            new Rectangle(0, 6, 16, 4),
            Direction.RIGHT
        );
        enhancedMapTiles.add(hmp);

        // Adding an end level box similar to Map2
        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(32, 7).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }

}