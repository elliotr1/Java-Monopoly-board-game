

public class Main {

    public static void main(String[] args){

        Model model = new Model();
        //CLI cli = new CLI(model);
        View view = new View();
        new Controller(model, view);
    }

}
