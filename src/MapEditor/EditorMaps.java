package MapEditor;

import Level.Map;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.ToadsMap;
import Maps.Map2;
import Maps.PracticeRangeMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("ToadsMap");
            add("Map2");
            add("PracticeRangeMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "ToadsMap": 
                return new ToadsMap();
            case "Map2":
                return new Map2();
            case "PracticeRangeMap":
                return new PracticeRangeMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
