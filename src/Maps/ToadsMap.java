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
import Tilesets.ToadsTileset;
import Utils.Direction;

import java.util.ArrayList;

// Represents the ToadsMap to be used in a level
public class ToadsMap extends Map {

    public ToadsMap() {
        super("toadsmap.txt", new ToadsTileset());
        // Set player start position, make sure these coordinates are valid for the map
        this.playerStartPosition = getMapTile(2, 11).getLocation();
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Adding a BugEnemy at a specific location, similar to Map2
        BugEnemy bugEnemy = new BugEnemy(getMapTile(16, 10).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy);


        BugEnemy bugEnemy1 = new BugEnemy(getMapTile(2, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy1);

        BugEnemy bugEnemy2 = new BugEnemy(getMapTile(3, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy2);

        BugEnemy bugEnemy3 = new BugEnemy(getMapTile(4, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy3);

        BugEnemy bugEnemy4 = new BugEnemy(getMapTile(5, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy4);

        BugEnemy bugEnemy5 = new BugEnemy(getMapTile(6, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy5);

        BugEnemy bugEnemy6 = new BugEnemy(getMapTile(7, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy6);

        BugEnemy bugEnemy7 = new BugEnemy(getMapTile(8, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy7);

        BugEnemy bugEnemy8 = new BugEnemy(getMapTile(9, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy8);

        BugEnemy bugEnemy9 = new BugEnemy(getMapTile(10, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy9);

        BugEnemy bugEnemy10 = new BugEnemy(getMapTile(11, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy10);

        BugEnemy bugEnemy11 = new BugEnemy(getMapTile(12, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy11);

        BugEnemy bugEnemy12 = new BugEnemy(getMapTile(13, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy12);

        BugEnemy bugEnemy13 = new BugEnemy(getMapTile(14, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy13);

        BugEnemy bugEnemy14 = new BugEnemy(getMapTile(15, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy14);

        BugEnemy bugEnemy15 = new BugEnemy(getMapTile(0, 10).getLocation(), Direction.LEFT);
        enemies.add(bugEnemy15);

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