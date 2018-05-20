package clock;

/**
 *
 * @author LUCIE
 */
public class Clock {
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);
    }
}
