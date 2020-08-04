package masteringthreads.ch4_applied_threading_techniques.exercise_4_4;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class HouseDrawing extends StupidFramework {
    private static final ThreadLocal<String> tempColour = new ThreadLocal<>();
    private final String colour;

    public HouseDrawing(String title, String colour) {
        super(saveColour(title, colour));
        this.colour = colour;
        tempColour.remove();
    }

    private static String saveColour(String title, String colour) {
        tempColour.set(colour);
        return title;
    }

    public void draw() {
        System.out.println("Drawing house with colour " + getColour());
    }

    private String getColour() {
        if (colour == null) return tempColour.get();
        return colour;
    }
}
