import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Utils {
    public static HashMap<Integer, HashSet<Integer>> readGraphInFile(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        int size = scanner.nextInt();

        HashMap<Integer, HashSet<Integer>> graph = new HashMap<>();

        for (int i = 0; i < size; i++) {
            graph.put(i, new HashSet<>());
            for (int j = 0; j < size; j++) {
                int val = scanner.nextInt();
                if (val == 1){
                    graph.get(i).add(j);
                }
            }
        }

        return graph;
    }

    public static HashMap<Integer, HashSet<Integer>> readGraphInFileGenerator(String file) throws  FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));

        int nodes = 0;
        int edges = 0;

        while (true) {
            String nextLine = scanner.nextLine();
            char flag = nextLine.charAt(0);

            if (flag == 'p'){
                String[] split = nextLine.split(" ");
                nodes = Integer.parseInt(split[2]);
                edges = Integer.parseInt(split[3]);
                break;
            }
        }

        scanner.nextLine();

        HashMap<Integer, HashSet<Integer>> graph = new HashMap<>();

        for (int i = 1; i <= nodes; i++) {
            graph.put(i, new HashSet<>());
        }

        for (int i = 0; i < edges; i++) {
            String[] nextLineSplit = scanner.nextLine().split(" ");
            int nodeA = Integer.parseInt(nextLineSplit[1]);
            int nodeB = Integer.parseInt(nextLineSplit[2]);
            graph.get(nodeA).add(nodeB);
            graph.get(nodeB).add(nodeA);
        }

        return graph;
    }

    public static int getNodeMaxDegree(HashMap<Integer, HashSet<Integer>> graph){
        int max = -1;
        int index = -1;
        for (Map.Entry<Integer, HashSet<Integer>> item : graph.entrySet()) {
            int degree = item.getValue().size();
            if (degree > max) {
                index = item.getKey();
                max = degree;
            }
        }
        return index;
    }

    public static int getNodeMaxDegreeInList(HashMap<Integer, HashSet<Integer>> graph, Set<Integer> nodes){
        int max = -1;
        int index = -1;
        for (int node : nodes) {
            int degree = graph.get(node).size();
            if (degree > max) {
                index = node;
                max = degree;
            }
        }
        return index;
    }

    public static int nbColors(HashMap<Integer, Integer> coloration){
        int max = -1;
        for(int color : coloration.values()){
            max = Math.max(max, color);
        }

        return max + 1;
    }

    public static HashMap<Integer, Integer> newColoration(int graphSize){
        HashMap<Integer, Integer> coloration = new HashMap<>();
        for (int i = 1; i <= graphSize; i++) {
            coloration.put(i, -1);
        }

        return coloration;
    }

    public static HashMap<Integer, Integer> badColoration(int graphSize){
        HashMap<Integer, Integer> coloration = new HashMap<>();
        for (int i = 1; i <= graphSize; i++) {
            coloration.put(i, i);
        }

        return coloration;
    }

    public static boolean isColorationComplete(HashMap<Integer, Integer> coloration){
        for (int color : coloration.values()) {
            if (color == -1) return false;
        }

        return true;
    }

    public static void printColoration(HashMap<Integer, Integer> coloration){
        System.out.println(Utils.nbColors(coloration));
        for (int col : coloration.values()) {
            System.out.print(col + " ");
        }
        System.out.println();
    }

    public static void printTime(long start, long end){
        float time = (float) ((end - start) / 1000000.);
        System.out.println(time);
    }

    public static int nbConflicts(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> coloration){
        int n = 0;
        for (Map.Entry<Integer, HashSet<Integer>> item : graph.entrySet()) {
            int colorItem = coloration.get(item.getKey());
            for (int neighbor : item.getValue()) {
                int colorNeighbor = coloration.get(neighbor);
                if (colorItem == colorNeighbor) n++;
            }
        }

        return n/2;
    }
}
