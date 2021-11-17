import java.util.*;

public class BranchAndBound {
    public static HashMap<Integer, Integer> branchAndBound(HashMap<Integer, HashSet<Integer>> graph) {
        HashMap<Integer, Integer> CStar = Greedy.greedy(graph);
        int UB = Utils.nbColors(CStar);
        Stack<HashMap<Integer, Integer>> nodes_stack = new Stack<>();

        int n = graph.size();
        HashMap<Integer, Integer> C = Utils.newColoration(n);
        int v = Utils.getNodeMaxDegree(graph);
        C.put(v, 0);
        nodes_stack.push(C);

        while(!nodes_stack.isEmpty()){
            C = nodes_stack.pop();
            int KC = Utils.nbColors(C);
            int KCStar = Utils.nbColors(CStar);
            if (Utils.isColorationComplete(C)){
                if (KC < KCStar) {
                    CStar = C;
                    UB = KC;
                }
            }
            //TODO : Verify KC or KC-1
            else if (KC < UB){
                for (HashMap<Integer, Integer> CPrime : exploreNodes(graph, C)) {
                    nodes_stack.push(CPrime);
                }
            }
        }

        return CStar;
    }

    public static Iterable<HashMap<Integer, Integer>> exploreNodes(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> C){
        List<HashMap<Integer, Integer>> list = new ArrayList<>();
        Set<Integer> neighbors_colors = new HashSet<>();

        int v = Greedy.greedyChoice(graph,C);
        int KC = Utils.nbColors(C);

        for(int neighbor : graph.get(v))
            neighbors_colors.add(C.get(neighbor));

        for(int i = 0 ; i <= KC ; i++){
            if(!neighbors_colors.contains(i)){
                HashMap<Integer, Integer> CPrime = new HashMap<>(C);
                CPrime.put(v, i);
                list.add(CPrime);
            }
        }

        return list;
    }
}
