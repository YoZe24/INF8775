import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Tabou {
    static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> tabouList;

    public static HashMap<Integer, Integer> tabou(HashMap<Integer, HashSet<Integer>> graph) {
        int n = graph.size();
        HashMap<Integer, Integer> CStar = Greedy.greedy(graph);

        int K = Utils.nbColors(CStar);

        HashMap<Integer, Integer> C1 = new HashMap<>(CStar);
        HashMap<Integer, Integer> C2 = new HashMap<>(CStar);
        HashMap<Integer, Integer> C3 = new HashMap<>(CStar);

        while(true){
            for (Map.Entry<Integer, Integer> item : C3.entrySet()) {
                if (item.getValue() == K - 1){
                    C1.put(item.getKey(),0);
                    C2.put(item.getKey(),0);
                    for (int i = 1; i < K - 2; i++) {
                        C1.put(item.getKey(), i);
                        if (nbConflicts(graph,C2) < nbConflicts(graph,C1)){
                            C1.put(item.getKey(), C2.get(item.getKey()));
                        }
                        else{
                            C2.put(item.getKey(), C1.get(item.getKey()));
                        }
                    }
                }
            }

            K = K - 1;

            HashMap<Integer, Integer> current = new HashMap<>(C1);
            int currentConflicts = nbConflicts(graph, current);

            tabouList = initializeTabouList(n);

            int nStop = 0;
            while(nbConflicts(graph, current) > 0){
                updateTabouList();
                HashMap<Integer, Integer> bestNeighbor = Utils.newColoration(n);
                int bestNeighborConflicts = nbConflicts(graph, bestNeighbor);
                for (HashMap<Integer, Integer> neighbor : generateNeighbors(graph, current, K - 1)) {
                    int neighborConflicts = nbConflicts(graph, neighbor);

                    if (neighborConflicts < bestNeighborConflicts) {
                        bestNeighbor = neighbor;
                        bestNeighborConflicts = neighborConflicts;
                    }
                }
                if (bestNeighborConflicts < currentConflicts){
                    currentConflicts = bestNeighborConflicts;
                    current = bestNeighbor;
                    nStop = 0;
                }
                else{
                    nStop ++;
                }

                if (nStop > 5000){
                    return CStar;
                }
            }

            CStar = current;

            C1 = CStar;
            C2 = CStar;
            C3 = CStar;
        }
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

    public static Iterable<HashMap<Integer, Integer>> generateNeighbors(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> current, int greatestColor){
        List<HashMap<Integer, Integer>> neighbors = new ArrayList<>();

        for (Map.Entry<Integer, Integer> item : current.entrySet()) {
            for (int i = 0; i < greatestColor; i++) {
                if (!tabouList.get(item.getKey()).containsKey(i)){
                    HashMap<Integer, Integer> newColoration = new HashMap<>(current);
                    newColoration.put(item.getKey(), i);
                    neighbors.add(newColoration);

                    Random random = new Random();
                    int a = 2;
                    int g = random.nextInt(10) + 1;
                    int c = nbConflicts(graph, newColoration);

                    int l = (a * c) + g;
                    tabouList.get(item.getKey()).put(i, l);
                }
            }
        }
        return neighbors;
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