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
//      HashSet<HashMap<Integer, Integer>> visited = new HashSet<>();

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
            else{
                if (KC - 1 < UB){
                    for (HashMap<Integer, Integer> CPrime : exploreNodes(graph, C)) {
                        nodes_stack.push(CPrime);
//                      if (!visited.contains(CPrime)){
//                          nodes_stack.push(CPrime);
//                          visited.add(CPrime);
//                      }
                    }
                }
            }
        }

        return CStar;
    }

    public static Iterable<HashMap<Integer, Integer>> exploreNodes(HashMap<Integer, HashSet<Integer>> graph, HashMap<Integer, Integer> C){
        List<HashMap<Integer, Integer>> list = new ArrayList<>();
        int v = Greedy.greedyChoice(graph,C);
        int KC = Utils.nbColors(C);
        for (int i = 0; i <= KC; i++) {
            boolean colorValid = true;
            for (int neighbor : graph.get(v)) {
                if (i == C.get(neighbor)) {
                    colorValid = false;
                }
            }
            if (colorValid){
                HashMap<Integer, Integer> CPrime = new HashMap<>(C);
                CPrime.put(v, i);
                list.add(CPrime);
            }
        }

        return list;
    }
}
