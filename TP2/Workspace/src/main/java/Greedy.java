import java.util.*;

public class Greedy {
    public static HashMap<Integer, Integer> greedy(HashMap<Integer, HashSet<Integer>> graph){
        int n = graph.size();
        HashMap<Integer, Integer> coloration = Utils.newColoration(n);

        int v = Utils.getNodeMaxDegree(graph);
        coloration.put(v, 0);
        n--;

        while(n > 0){
            int node = greedyChoice(graph, coloration);
            n--;
            coloration.put(node, getSmallestColor(graph,coloration,node));
        }

        return coloration;
    }

    public static Integer greedyChoice(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> coloration){
        int maxSat = -1;
        int maxSatDegree = -1;
        int maxNode = -1;
        for (Map.Entry<Integer, HashSet<Integer>> item : graph.entrySet()) {
            if (coloration.get(item.getKey()) == -1){
                int dsat = 0;
                for (int neighbor : item.getValue()) {
                    if (coloration.get(neighbor) != -1){
                        dsat ++;
                    }
                }

                if(dsat > maxSat){
                    maxSat = dsat;
                    maxNode = item.getKey();
                    maxSatDegree = item.getValue().size();
                }else if(dsat == maxSat && item.getValue().size() >= maxSatDegree){
                    maxNode = item.getKey();
                    maxSatDegree = item.getValue().size();
                }
            }
        }

        return maxNode;
    }

    public static int getSmallestColor(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> coloration, int node){
        int maxColor = -1;
        HashSet<Integer> used = new HashSet<>();

        for (int neighbor : graph.get(node)) {
            int color = coloration.get(neighbor);
            maxColor = Math.max(maxColor, color);
            used.add(color);
        }

        for (int i = 0; i < maxColor; i++) {
            if (!used.contains(i)) return i;
        }
        return maxColor + 1;
    }
}