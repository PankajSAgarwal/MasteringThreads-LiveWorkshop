package masteringthreads.ch4_applied_threading_techniques.solution_4_4;

public abstract class StupidFramework {

    public StupidFramework(String title) {
        draw();
        System.out.println("You are using StupidFramework: " + title);
    }

    public abstract void draw();
}
