//import java.util.*;
//
//public class BranchAndBound {
//    public static List<Integer> solve(HashMap<Integer, HashSet<Integer>> friendships, HashMap<Integer, Integer> sizes){
//        Integer[] path = LocalSearch.HybridHAM(friendships, sizes);
//        List<Integer> bestSol = Arrays.asList(path);
//
////        System.out.println(Algorithm.conflicts(bestSol, friendships));
////        List<Integer> bestSol = Algorithm.orderSizes(sizes);
////        List<Integer> bestSol = new ArrayList<>(friendships.keySet());
////        Collections.shuffle(bestSol);
//
//        int UB = Algorithm.conflicts(bestSol, friendships);
//        int UB_sizes = Algorithm.unSatisfaction(bestSol, sizes);
//
//        Stack<List<Integer>> nodes_stack = new Stack<>();
//
//        int n = friendships.size();
//        List<Integer> tmpSol;
//
//        for (int i = 0; i < n; i++) {
//            List<Integer> start = new ArrayList<>(n);
//            start.add(i);
//            nodes_stack.push(start);
//        }
//
//        while(!nodes_stack.isEmpty()){
//            tmpSol = nodes_stack.pop();
//            int costTmpSol = Algorithm.conflicts(tmpSol, friendships);
//            int costBestSol = Algorithm.conflicts(bestSol, friendships);
//
//            int unSatTmpSol = Algorithm.unSatisfaction(tmpSol, sizes);
//            int unSatBestSol = Algorithm.unSatisfaction(bestSol, sizes);
//
//            if (tmpSol.size() == n){
//                if (costTmpSol < costBestSol){
//                    bestSol = tmpSol;
//                    UB = costTmpSol;
//                    UB_sizes = unSatTmpSol;
//                    System.out.println(UB + " - " + UB_sizes + " - " + bestSol);
//                }
//            }
//            else if (costTmpSol < UB){
//                System.out.println(UB + " " + UB_sizes);
//                for(List<Integer> solNeigh : explore(friendships, tmpSol, sizes)){
//                    nodes_stack.push(solNeigh);
//                }
//            }
//        }
//
//        return bestSol;
//    }
//
//    public static Iterable<List<Integer>> explore(HashMap<Integer, HashSet<Integer>> friendships, List<Integer> positions, HashMap<Integer, Integer> sizes){
//        HashSet<Integer> used = new HashSet<>(positions);
//        int lastChild = positions.get(positions.size() - 1);
//        List<List<Integer>> nodeList = new ArrayList<>();
//
//        HashMap<Integer, Integer> sizesNeighbors = new HashMap<>();
//        for (int neigh : friendships.get(lastChild)) {
//            sizesNeighbors.put(neigh, sizes.get(neigh));
//        }
//
//        Comparator<Map.Entry<Integer, Integer>> comparator = new SizeComparator();
//        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>(comparator);
//        pq.addAll(sizesNeighbors.entrySet());
//
//        List<Integer> sorted = new ArrayList<>();
//        while (!pq.isEmpty()){
//            sorted.add(pq.poll().getKey());
//        }
//
//        for (int neigh : sorted) {
//            if (!used.contains(neigh) && sizes.get(lastChild) < sizes.get(neigh)) {
//                List<Integer> neighList = new ArrayList<>(positions);
//                neighList.add(neigh);
//                nodeList.add(neighList);
//            }
//        }
//        return nodeList;
//    }
//
//    public static int getChildMinFriends(HashMap<Integer, HashSet<Integer>> friendships){
//        int min_child = -1;
//        int min_friends = Integer.MAX_VALUE;
//        for (Map.Entry<Integer, HashSet<Integer>> child : friendships.entrySet()) {
//            if (child.getValue().size() < min_friends){
//                min_child = child.getKey();
//                min_friends = child.getValue().size();
//            }
//        }
//        return min_child;
//    }
//}