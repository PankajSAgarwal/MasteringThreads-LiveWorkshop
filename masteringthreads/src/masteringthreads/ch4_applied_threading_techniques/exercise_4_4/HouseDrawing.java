package masteringthreads.ch4_applied_threading_techniques.exercise_4_4;

public class HouseDrawing extends StupidFramework {
    private final String colour;

    public HouseDrawing(String title, String colour) {
        super(title);
        this.colour = colour;
    }

    public void draw() {
        System.out.println("Drawing house with colour " + colour);
    }
}
