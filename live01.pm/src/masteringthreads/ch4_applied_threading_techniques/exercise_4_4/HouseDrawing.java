package masteringthreads.ch4_applied_threading_techniques.exercise_4_4;

import java.util.*;

public class HouseDrawing extends StupidFramework {
    private static String tempColour;
    private final String colour;

    public HouseDrawing(String title, String colour) {
        super(saveColour(title, colour));
        this.colour = colour;
        tempColour = null;
    }

    private static String saveColour(String title, String colour) {
        tempColour = colour;
        return title;
    }

    public void draw() {
        System.out.println("Drawing house with colour " + getColour());
    }

    private String getColour() {
        return Objects.requireNonNullElse(colour, tempColour);
    }
}
