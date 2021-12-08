//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.*;
//
//public class Algorithm {
//    static List<Integer> positions;
//    static HashMap<Integer, Integer> sizes;
//    static HashMap<Integer, HashSet<Integer>> friendships;
//
//    public static void main(String[] args) throws FileNotFoundException {
////        String path = "instances/" + args[0];
////        boolean p = args.length == 2;
//
//        String path = "src/main/resources/instances/" + "558_63109.0";
////        String path = "src/main/resources/instances/" + "118_2962.0";
//
//        readInstance(path);
//
//        // findHamiltonianPaths(friendships, 66);
//
////         makeExample();
////         fillExample();
//
////        System.out.println(positions);
////        System.out.println(sizes);
////        System.out.println(friendships);
////        System.out.println("-------------------------------------------------");
//
////        System.out.println(Arrays.toString(BranchAndBound.HybridHAM(friendships)));
////        positions = BranchAndBound.solve(friendships, sizes);
//        positions = LocalSearch.localSearch(friendships, sizes);
////        printSolution(positions);
////        System.out.println(positions);
////        System.out.println(conflicts(positions, friendships));
////        System.out.println(unSatisfaction(positions, sizes));
//        // findHamiltonianPaths(friendships, 5);
//        // algorithm(sizes, friendships);
//        // solveExample();
//
//
//        // printResults(positions, sizes, friendships);
//    }
//
//    // TODO :
//    // Considérer notre graphe de départ et le parcourir, dès qu'on rencontre un conflit on regarde parmis les voisins et si ça marche pas on teste un autre voisin au tout départ style dfs
//    public static void algorithm(HashMap<Integer, Integer> sizes, HashMap<Integer, HashSet<Integer>> friendships){
//        HashMap<Integer, Integer> nbFriends = numberOfFriends(friendships);
//        positions = orderSizes(sizes);
//
//        while(true){
//            int curr = positions.size() - 1;
//            while(curr > 1){
//                int neigh = curr - 1;
//                if (!friendships.get(curr).contains(neigh)){
//                    int nav = neigh - 1;
//                    while(nav >= 0 && !friendships.get(curr).contains(nav)){
//                        nav --;
//                    }
//
//                    if (nav != -1) {
//                        swapPositions(neigh, nav);
//                    }
//                }
//                curr--;
//            }
//
//            int conflicts = conflicts(positions, friendships);
//            if(conflicts == 0){
//                break;
//            }
//        }
//    }
//
//    public static void swapPositions(int a, int b){
//        int tmp = positions.get(a);
//        positions.set(a,b);
//        positions.set(b,tmp);
//    }
//
//    public static List<Integer> orderSizes(HashMap<Integer, Integer> sizes){
//        Comparator<Map.Entry<Integer, Integer>> comparator = new SizeComparator();
//        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>(comparator);
//
//        pq.addAll(sizes.entrySet());
//
//        List<Integer> positions = new ArrayList<>();
//        while (!pq.isEmpty()){
//            positions.add(pq.poll().getKey());
//        }
//
//        return positions;
//    }
//
//    public static void hamiltonianPaths(HashMap<Integer, HashSet<Integer>> graph, int v, HashSet<Integer> visited, List<Integer> path, int n)
//    {
//        if (path.size() == n)
//        {
//            System.out.println(path);
//            return;
//        }
//
//        for (int neighbor : graph.get(v))
//        {
//            if (!visited.contains(neighbor))
//            {
//                visited.add(neighbor);
//                path.add(neighbor);
//
//                System.out.println(path.size());
//
//                hamiltonianPaths(graph, neighbor, visited, path, n);
//
//                visited.remove(neighbor);
//                path.remove(path.size() - 1);
//            }
//        }
//    }
//
//    public static void findHamiltonianPaths(HashMap<Integer, HashSet<Integer>> graph, int n)
//    {
//        for (int start = 1; start <= n; start++)
//        {
//            List<Integer> path = new ArrayList<>();
//            path.add(start);
//            HashSet<Integer> visited = new HashSet<>();
//            visited.add(start);
//
//            hamiltonianPaths(graph, start, visited, path, n);
//        }
//    }
//
//    public static HashMap<Integer, Integer> numberOfFriends(HashMap<Integer, HashSet<Integer>> friendships){
//        HashMap<Integer, Integer> numberOfFriends = new HashMap<>();
//
//        for (Map.Entry<Integer, HashSet<Integer>> item : friendships.entrySet()) {
//            numberOfFriends.put(item.getKey(), item.getValue().size());
//        }
//
//        return numberOfFriends;
//    }
//
//    public static void makeExample(){
//        friendships = new HashMap<>();
//        sizes = new HashMap<>();
//        positions = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            friendships.put(i, new HashSet<>());
//        }
//
//        for (int i = 0; i < 5; i++) {
//            sizes.put(i,i);
//        }
//
//        friendships.get(0).add(1);
//        friendships.get(0).add(2);
//        friendships.get(1).add(0);
//        // friendships.get(1).add(2);
//        // friendships.get(2).add(1);
//        friendships.get(2).add(0);
//        friendships.get(2).add(3);
//        friendships.get(3).add(2);
//        friendships.get(3).add(4);
//        friendships.get(4).add(3);
//    }
//
//    public static void fillExample(){
//        for (int i = 0; i < 5; i++) {
//            positions.add(i);
//        }
//    }
//
//    public static void solveExample(){
//        for (int i = 0; i < 5; i++) {
//            positions.add(4 - i);
//        }
//
//        positions.set(3, 0);
//        positions.set(4, 1);
//    }
//
//    public static void printResults(List<Integer> positions, HashMap<Integer, Integer> sizes, HashMap<Integer, HashSet<Integer>> friendships){
//        printArrays(positions, sizes);
//        System.out.println("Conflits amitié : " + conflicts(positions, friendships));
//        System.out.println("Conflits taille : " + unSatisfaction(positions, sizes));
//    }
//
//    public static void printArrays(List<Integer> positions, HashMap<Integer, Integer> sizes){
//        List<Integer> sizesList = new ArrayList<>();
//        for (int i : positions) {
//            sizesList.add(sizes.get(i));
//        }
//
//        System.out.println("Rangée : " + positions);
//        System.out.println("Taille : " + sizesList);
//    }
//
//    public static int unSatisfaction(List<Integer> positions, HashMap<Integer, Integer> sizes){
//        int counter = 0;
//        for (int i = positions.size() - 1; i > 0; i--) {
//            int e1 = positions.get(i);
//            for (int j = i - 1; j > 0; j--) {
//                int e2 = positions.get(j);
//                if (sizes.get(e1) < sizes.get(e2)){
//                    counter++;
//                    break;
//                }
//            }
//        }
//        return counter;
//    }
//
//    public static int conflicts(List<Integer> positions, HashMap<Integer, HashSet<Integer>> friendships){
//        int counter = 0;
//        for (int i = 0; i < positions.size() - 1; i++) {
//            int e1 = positions.get(i);
//            int e2 = positions.get(i + 1);
//            if (!friendships.get(e1).contains(e2)){
//                counter++;
//            }
//        }
//        return counter;
//    }
//
//    public static void readInstance(String path) throws FileNotFoundException {
//        sizes = new HashMap<>();
//        friendships = new HashMap<>();
//        positions = new ArrayList<>();
//        Scanner scanner = new Scanner(new File(path));
//        int nbStudents = scanner.nextInt();
//        int nbFriendships = scanner.nextInt();
//        for (int i = 1; i <= nbStudents; i++) {
//            int size = scanner.nextInt();
//            sizes.put(i, size);
//            friendships.put(i, new HashSet<>());
//        }
//
//        for (int i = 0; i < nbFriendships; i++) {
//            int a = scanner.nextInt();
//            int b = scanner.nextInt();
//
//            friendships.get(a).add(b);
//            friendships.get(b).add(a);
//        }
//    }
//}