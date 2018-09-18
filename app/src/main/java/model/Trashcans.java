package model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Andreas on 01.05.2017.
 */

public class Trashcans {
    private static ArrayList<Trashcan> trashcans;

    public static ArrayList<Trashcan> getTrashcans() {
        return trashcans;
    }

    public static void setTrashcans(ArrayList<Trashcan> trashcans) {
        Trashcans.trashcans = trashcans;
    }
}
