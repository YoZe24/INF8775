import javafx.css.CssMetaData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Tabou {
    static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> tabouList;

    public static HashMap<Integer, Integer> tabou(HashMap<Integer, HashSet<Integer>> graph) {
        int n = graph.size();
        Random random = new Random();

        HashMap<Integer, Integer> CStar = Greedy.greedy(graph);
        int K = Utils.nbColors(CStar);
        HashMap<Integer, Integer> colors = new HashMap<>(CStar);

        while(true){
            HashMap<Integer, Integer> reduced = reduceColors(graph,colors,K);
            HashMap<Integer, Integer> current = new HashMap<>(reduced);

            K = K - 1;

            int currentConflicts = Utils.nbConflicts(graph,current);
            tabouList = initializeTabouList(n);

            int nStop = 0;
            while(Utils.nbConflicts(graph, current) > 0){
                updateTabouList();
                HashMap<Integer, Integer> bestNeighbor = Utils.newColoration(n);
                int bestNeighborConflicts = Integer.MAX_VALUE;
                int changedNode = -1;
                int changedColor = -1;
                for (Map.Entry<Integer, Integer> item : current.entrySet()) {
                    for (int i = 0; i < K ; i++) {
                        if (i != item.getValue() && !tabouList.get(item.getKey()).containsKey(i)){
                            HashMap<Integer, Integer> newColoration = new HashMap<>(current);
                            newColoration.put(item.getKey(), i);
                            int neighborConflicts = Utils.nbConflicts(graph,newColoration);

                            if (neighborConflicts < bestNeighborConflicts) {

                                bestNeighbor = newColoration;
                                bestNeighborConflicts = neighborConflicts;
                                changedNode = item.getKey();
                                changedColor = item.getValue();
                            }

                        }
                    }
                }

                int a = 2;
                int g = random.nextInt(10) + 1;
                int c = bestNeighborConflicts;
                if(changedNode != -1)
                    tabouList.get(changedNode).put(changedColor, (a*c) + g);

                current = bestNeighbor;
//                printTabou();

                if (bestNeighborConflicts < currentConflicts){
                    currentConflicts = bestNeighborConflicts;
                    nStop = 0;
                }
                else {
                    nStop++;
                }

                if (nStop > 5000){
                    System.out.println("Nb conflict : "+Utils.nbConflicts(graph,CStar));
                    return CStar;
                }
            }

            CStar = new HashMap<>(current);

            colors = new HashMap<>(CStar);
        }
    }

    public static HashMap<Integer,Integer> reduceColors(HashMap<Integer, HashSet<Integer>> graph , HashMap<Integer,Integer> C,int K){
        HashMap<Integer,Integer> C1 = new HashMap<>(C);
        int nbConflictMin;
        int nbConflict;
        int color_curr = -1;

        for (Map.Entry<Integer, Integer> item : C.entrySet()) {
            if (item.getValue() == K - 1){
                nbConflictMin = Integer.MAX_VALUE;

                for(int i = 0 ; i <= K-2 ; i++){
                    C1.put(item.getKey(),i);
                    nbConflict = Utils.nbConflicts(graph,C1);
                    if(nbConflict < nbConflictMin){
                        color_curr = i;
                        nbConflictMin = nbConflict;
                    }
                }
                C1.put(item.getKey(),color_curr);
            }
        }
        return C1;
    }

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> initializeTabouList(int n){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> list = new ConcurrentHashMap<>();
        for (int i = 0; i < n; i++) {
            list.put(i, new ConcurrentHashMap<>());
        }
        return list;
    }

    public static void updateTabouList(){
        for (Map.Entry<Integer, ConcurrentHashMap<Integer, Integer>> item : tabouList.entrySet()) {
            int key = item.getKey();

            for (Map.Entry<Integer, Integer> val : item.getValue().entrySet()) {
                int valKey = val.getKey();
                int newValue = val.getValue() - 1;
                if (newValue == 0){
                    tabouList.get(key).remove(valKey);
                }
                else{
                    tabouList.get(key).put(valKey, newValue);
                }
            }
        }
    }

    public static void printTabou(){
        for(int i = 0 ; i < tabouList.size() ; i++){
            System.out.println(i+ " - " + tabouList.get(i));
        }
    }

    public static Iterable<HashMap<Integer, Integer>> generateNeighbors(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> current, int greatestColor){
        List<HashMap<Integer, Integer>> neighbors = new ArrayList<>();

        for (Map.Entry<Integer, Integer> item : current.entrySet()) {
            for (int i = 0; i < greatestColor; i++) {
                if (i != item.getValue() && !tabouList.get(item.getKey()).containsKey(i)){
                    HashMap<Integer, Integer> newColoration = new HashMap<>(current);
                    newColoration.put(item.getKey(), i);
                    neighbors.add(newColoration);

//                    Random random = new Random();
//                    int a = 2;
//                    int g = random.nextInt(10) + 1;
//                    int c = Utils.nbConflicts(graph, newColoration);
//
//                    int l = (a * c) + g;
//                    tabouList.get(item.getKey()).put(i, l);
                }
            }
        }
        return neighbors;
    }

//    public static int nbConflicts(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> coloration){
//        int n = 0;
//        for (Map.Entry<Integer, HashSet<Integer>> item : graph.entrySet()) {
//            int colorItem = coloration.get(item.getKey());
//            for (int neighbor : item.getValue()) {
//                int colorNeighbor = coloration.get(neighbor);
//                if (colorItem == colorNeighbor) n++;
//            }
//        }
//
//        return n/2;
//    }
}