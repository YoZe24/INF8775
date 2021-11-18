import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Application {
    public static void main(String[] args) throws IOException {
        String algo = args[0];
        String path = args[1];
        boolean p = false;
        boolean t = false;

        if (args.length == 3) {
            if (args[2].equals("p"))
                p = true;
            else
                t = true;
        }
        else if (args.length == 4){
            p = true;
            t = true;
        }

        HashMap<Integer, HashSet<Integer>> graph = Utils.readGraphInFileGenerator(path);
        HashMap<Integer, Integer> coloration;

        long start = System.nanoTime();
        switch (algo){
            case "1":
                coloration = Greedy.greedy(graph);
                break;
            case "2":
                coloration = BranchAndBound.branchAndBound(graph);
                break;
            case "3":
                coloration = Tabou.tabou(graph);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + algo);
        }
        long end = System.nanoTime();

        if (p){
            Utils.printColoration(coloration);
        }

        if (t){
            Utils.printTime(start, end);
        }
    }
}