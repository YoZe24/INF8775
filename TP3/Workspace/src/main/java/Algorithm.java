import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {
    static List<Integer> positions;
    static HashMap<Integer, Integer> sizes;
    static int[][] graph;
    static HashMap<Integer, HashSet<Integer>> friendships;
    static Random rand = new Random();

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/instances/" + "118_2962.0";

        readInstance(path);

        long s = System.currentTimeMillis();
//        findHamiltonianCycle(graph);


//        greedy(sizes,friendships);

        Integer[] ham = HybridHAM(friendships);
        HashSet<Integer> testHam = new HashSet<>(Arrays.asList(ham));
        System.out.println("Size end "+testHam.size());
        System.out.println(conflicts(Arrays.asList(ham),friendships));
    }

    public static Integer[] HybridHAM(HashMap<Integer, HashSet<Integer>> friendships){
        HashMap<Integer,Integer> degrees = numberOfFriends(friendships);
        Integer[] degreesArr = degrees.values().toArray(new Integer[0]);
        Integer[] vMin = degrees.keySet().toArray(new Integer[0]);
        Integer[] vMax;


        Arrays.sort(vMin, (o1, o2) -> Float.compare(degreesArr[o1-1], degreesArr[o2-1]));
        vMax = vMin.clone();
        Collections.reverse(Arrays.asList(vMax));

        List<Integer> currPath = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        for(int m = 0 ; m < 1000 ; m++) {

            currPath = getLongestPathPossible(vMax, vMin, friendships);

            HashSet<Integer> visited = new HashSet(currPath);

            while (currPath.size() != friendships.size()) {
                int n = currPath.size();
                int end = currPath.get(n - 1);


                int candTail = getExtremity(end, false, visited, friendships);
                int candHead = getExtremity(currPath.get(0),true, visited, friendships);
                if (candTail != -1) {
                    currPath.add(candTail);
                    visited.add(candTail);
                }
                if (candHead != -1) {
                    currPath.add(0, candHead);
                    visited.add(candHead);
                }

                currPath = rotation(currPath, degrees, visited, vMin, friendships);
                if (currPath == null) break;

                //                int ngbMax = 0;
                //                int ngbNode = -1;
                //
                //                for (int ngb : friendships.get(end)) {
                //                    if (degrees.get(ngb) > ngbMax && currPath.indexOf(ngb) < n-2 && !rotationned.contains(ngb)) {
                //                        ngbMax = degrees.get(ngb);
                //                        ngbNode = ngb;
                //                    }
                //                    if(ngbNode != -1)
                //                        rotationned.add(ngbNode);
                //                }
                //                if(ngbNode == -1){
                //                    if(!rotationned.contains(currPath.get(0))){
                //                        rotationned.add(currPath.get(0));
                //                        Collections.reverse(currPath);
                //                        continue;
                //                    }
                //                    else
                //                        break;
                //                }

                //                System.out.println(rotationned);
                //                System.out.println("end : " + end + ", Ngb ind : " + ngbNode + " currsize : "+currPath.size());
                //                System.out.println("Before rotation : " + currPath);
                //
                //                List<Integer> toReverse;
                //                int posNgbInd;
                //
                //                if (ngbNode == currPath.get(0)) {
                //                    ngbMax = 0;
                //
                //                    int maxNextHead = 0;
                //                    int nextHead;
                //
                //                    for (int ni : notIn) {
                //                        if (degrees.get(ni) > ngbMax) {
                //                            nextHead = -1;
                //                            for(int ngNext : friendships.get(ni)){
                //                                if(setMax.contains(ngNext)){
                //                                    nextHead = ngNext;
                //                                    break;
                //                                }
                //                            }
                //                            if(nextHead != -1){
                //                                ngbMax = degrees.get(ni);
                //                                ngbNode = ni;
                //                                maxNextHead = nextHead;
                //                            }
                //                        }
                //                    }
                //                    posNgbInd = currPath.indexOf(maxNextHead);
                //                    List<Integer> newList = new ArrayList<>();
                //                    newList.add(ngbNode);
                //                    newList.addAll(currPath.subList(posNgbInd, n));
                //                    newList.addAll(currPath.subList(0, posNgbInd));
                //                    System.out.println("New List " + newList);
                //                    currPath = new ArrayList<>(newList);
                //
                //                    setMax.add(ngbNode);
                //                    notIn.remove(ngbNode);
                //                }
                //                else {
                //
                //                    System.out.println(ngbNode +", friends "+ friendships.get(ngbNode) + " end friend "+ friendships.get(end));
                //                    posNgbInd = currPath.indexOf(ngbNode);
                //                    toReverse = new ArrayList(currPath.subList(posNgbInd+1, n));
                //                    Collections.reverse(toReverse);
                //                    currPath = currPath.subList(0, posNgbInd+1);
                //                    currPath.addAll(toReverse);
                //
                //                    System.out.println("After rotation " + currPath + " new ngb " + friendships.get(currPath.get(n - 1)));
                //
                //
                //                }
                //
                //            }
                //
                //            while (true) {
                //                extend = greedyChoice(currPath.get(currPath.size() - 1), vMin, setMax, friendships);
                //                if (extend == -1)
                //                    break;
                //
                //                System.out.println("Added " + extend);
                //                setMax.add(extend);
                //                currPath.add(extend);
                //                notIn.remove(extend);
                //            }

            }

//            HashSet<Integer> test = new HashSet<>(currPath);
//            System.out.println(test.size() + " last solution " + currPath);
//            System.out.println(conflicts(currPath,friendships) + " val = " + unSatisfaction(currPath,sizes));

            min = Math.min(min,unSatisfaction(currPath,sizes));

            for (int i = 0; i < 100; i++) {

                currPath = rotation(currPath, degrees, visited, vMin, friendships);

                if(i % 10 == 0) Collections.reverse(currPath);
                min = Math.min(min,unSatisfaction(currPath,sizes));
                System.out.println(min);
            }

        }
        System.out.println(min);

        return currPath.toArray(new Integer[0]);
    }

    public static int getExtremity(int ext,boolean head, HashSet<Integer> visited, HashMap<Integer,HashSet<Integer>> friendships){
        Set<Integer> possibleHead = new HashSet(friendships.get(ext));
        List<Integer> cands = new ArrayList<>();
        for(int n : possibleHead){
            if(!visited.contains(n))
                cands.add(n);
        }
        if(cands.size() == 0) return -1;

        return cands.get(rand.nextInt(cands.size()));
    }

    public static List<Integer> rotation(List<Integer> path,HashMap<Integer, Integer> degree, HashSet<Integer> visited, Integer[] vMin, HashMap<Integer,HashSet<Integer>> friendships){
        int n = path.size();
        int end = path.get(n-1);
        if(degree.get(path.get(0)) > degree.get(end)
                || (degree.get(path.get(0)) == degree.get(end) && rand.nextBoolean())) {
            Collections.reverse(path);
            end = path.get(n-1);
        }

        List<Integer> candidates = friendships.get(end)
                                              .stream()
                                              .filter(ngb -> visited.contains(ngb))
                                              .collect(Collectors.toList());
        if(candidates.size() == 0) return null;
        int ngbNode = candidates.get(rand.nextInt(candidates.size()));
        int posNgbInd = path.indexOf(ngbNode);

        path = reverseFromIndex(path,posNgbInd+1);

        path = greedyPath(path, visited, vMin, friendships);

        return path;

    }

    public static List<Integer> reverseFromIndex(List<Integer> list, int index){
        List <Integer> toReverse = new ArrayList(list.subList(index, list.size()));
        Collections.reverse(toReverse);
        list= new ArrayList<>(list.subList(0, index));
        list.addAll(toReverse);
        return list;
    }

    public static List<Integer> getLongestPathPossible(Integer[] vMax,Integer[] vMin, HashMap<Integer,HashSet<Integer>> friendships){
        List<Integer> pathMax = new ArrayList<>();
        for(int i = 0 ; i < vMax.length ; i++) {
            if(vMax[i] == vMax[0]) {
                List<Integer> path = new ArrayList<>(Arrays.asList(vMax[i]));
                HashSet<Integer> visited = new HashSet<>(path);

                path = greedyPath(path, visited , vMin, friendships);

                if (path.size() > pathMax.size()) {
                    pathMax = new ArrayList(path);
                }
            }else{
                break;
            }
        }

        return pathMax;
    }

    public static boolean isUnreachable(HashSet<Integer> visited, int node, HashMap<Integer,HashSet<Integer>> friendships){
        for(int ngb: friendships.get(node)){

            if(!visited.contains(ngb)){

                int cptNgbOfNgb = 0;
                for(int ngbOfNgb: friendships.get(ngb)){
                    if(!visited.contains(ngbOfNgb)){
                        if(++cptNgbOfNgb == 2) break;
                    }
                }
                if(cptNgbOfNgb < 2) return true;
            }
        }
        return false;
    }

    public static List<Integer> greedyPath(List<Integer> path, HashSet<Integer> visited,Integer[] vMin, HashMap<Integer, HashSet<Integer>> friendships){
//        HashSet<Integer> visited = new HashSet<>();
//        List<Integer> path = new ArrayList<>();
//        path.add(start);
//        visited.add(start);

        while (true) {
            int extend = greedyChoice(path.get(path.size()-1),vMin,visited, friendships);
            if(extend == -1)
                break;

            path.add(extend);
            visited.add(extend);

        }
        return path;
    }

    public static int greedyChoice(int toExtend, Integer[] vMin, HashSet<Integer> visited, HashMap<Integer,HashSet<Integer>> friendships){

        HashSet<Integer> fCurr = friendships.get(toExtend);
//        int sizeMin = Integer.MAX_VALUE;
//        int nNode = -1;
//        for(int n : fCurr){
//            if(!visited.contains(n) && sizes.get(n) < sizeMin){
//                sizeMin = n;
//                nNode = n;
//            }
//        }
//        return nNode;
        for (int nMin : vMin) {
            if (!visited.contains(nMin) &&
                    fCurr.contains(nMin)) {

//                if (!isUnreachable(visited, nMin, friendships)
////                        && rand.nextInt(1000) != 1
//                ) {
                    return nMin;
//                }
            }
        }
        return -1;
    }

//    public static void greedy(HashMap<Integer, Integer> sizes, HashMap<Integer, HashSet<Integer>> friendships){
//        positions = new ArrayList<>();
//        HashSet<Integer> visited = new HashSet<>();
//
//        HashSet<Integer> candidats = new HashSet<>();
//        for(int i = 1 ; i <= sizes.size() ; i++){
//            candidats.add(i);
//        }
//
//        while(positions.size() < sizes.size()){
//            int node = greedyChoice(sizes,friendships,candidats);
//            visited.add(node);
//            candidats = friendships.get(node);
//            removeFriends(friendships,node);
//
//            System.out.println(candidats + " " + positions.size());
////            candidats.removeAll(visited);
//            positions.add(node);
//        }
////        HashMap<Integer, Integer> nbFriends = numberOfFriends(friendships);
////        System.out.println(nbFriends);
//        System.out.println(positions);
//    }
//
//
//    public static void removeFriends(HashMap<Integer,HashSet<Integer>> friendships, int node){
//        for(int f: friendships.get(node)){
//            friendships.get(f).remove(node);
//        }
//    }
//
//    public static int greedyChoice(HashMap<Integer, Integer> sizes, HashMap<Integer, HashSet<Integer>> friendships, HashSet<Integer> candidat){
//        int max = Integer.MIN_VALUE;
//        int maxInd = 0;
//        int minNeighboor = Integer.MAX_VALUE;
//        int size;
//        for(int c: candidat){
//
//            size = sizes.get(c);
//            if(size > max) {
//                max = size;
//                maxInd = c;
//
//            }else if(size == max && friendships.get(c).size() < minNeighboor){
//                minNeighboor = friendships.get(c).size();
//                maxInd = c;
//            }
//        }
//        return maxInd;
//
//    }

    static boolean hamiltonianPath(int adj[][], int N) {
        boolean dp[][] = new boolean[N][(1 << N)];

        // Set all dp[i][(1 << i)] to
        // true
        for(int i = 0; i < N; i++) {
            System.out.println((1 << i) + " , " + N + " , " + (1<<N)) ;
            dp[i][(1 << i)] = true;
        }

        // Iterate over each subset
        // of nodes
        for(int i = 0; i < (1 << N); i++)
        {
            for(int j = 0; j < N; j++)
            {

                // If the jth nodes is included
                // in the current subset
                if ((i & (1 << j)) != 0)
                {

                    // Find K, neighbour of j
                    // also present in the
                    // current subset
                    for(int k = 0; k < N; k++)
                    {

                        if ((i & (1 << k)) != 0 &&
                                adj[k][j] == 1 && j != k &&
                                dp[k][i ^ (1 << j)])
                        {

                            // Update dp[j][i]
                            // to true
                            dp[j][i] = true;
                            break;
                        }
                    }
                }
            }
        }

        // Traverse the vertices
        for(int i = 0; i < N; i++)
        {

            // Hamiltonian Path exists
            if (dp[i][(1 << N) - 1])
                return true;
        }

        // Otherwise, return false
        return false;
    }

    public static HashMap<Integer, Integer> numberOfFriends(HashMap<Integer, HashSet<Integer>> friendships){
        HashMap<Integer, Integer> numberOfFriends = new HashMap<>();

        for (Map.Entry<Integer, HashSet<Integer>> item : friendships.entrySet()) {
            numberOfFriends.put(item.getKey(), item.getValue().size());
        }

        return numberOfFriends;
    }

    public static void makeExample(){
        friendships = new HashMap<>();
        sizes = new HashMap<>();
        positions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            friendships.put(i, new HashSet<>());
        }

        for (int i = 0; i < 5; i++) {
            sizes.put(i,4 - i);
        }

        friendships.get(0).add(1);
        friendships.get(0).add(2);
        friendships.get(1).add(0);
        friendships.get(2).add(0);
        friendships.get(2).add(3);
        friendships.get(3).add(2);
        friendships.get(3).add(4);
        friendships.get(4).add(3);
    }

    public static void solveExample(){
        for (int i = 0; i < 5; i++) {
            positions.add(4 - i);
        }

        positions.set(3, 0);
        positions.set(4, 1);
    }

    public static void printResults(List<Integer> positions, HashMap<Integer, Integer> sizes, HashMap<Integer, HashSet<Integer>> friendships){
        printArrays(positions, sizes);
        System.out.println("Conflits amitié : " + conflicts(positions, friendships));
        System.out.println("Conflits taille : " + unSatisfaction(positions, sizes));
    }

    public static void printArrays(List<Integer> positions, HashMap<Integer, Integer> sizes){
        List<Integer> sizesList = new ArrayList<>();
        for (int i : positions) {
            sizesList.add(sizes.get(i));
        }

        System.out.println("Rangée : " + positions);
        System.out.println("Taille : " + sizesList);
    }

    public static int unSatisfaction(List<Integer> positions, HashMap<Integer, Integer> sizes){
        int counter = 0;
        for (int i = positions.size() - 1; i > 0; i--) {
            int e1 = positions.get(i);
            for (int j = i - 1; j > 0; j--) {
                int e2 = positions.get(j);
                if (sizes.get(e1) < sizes.get(e2)){
                    counter++;
                    break;
                }
            }
        }
        return counter;
    }

    public static int conflicts(List<Integer> positions, HashMap<Integer, HashSet<Integer>> friendships){
        int counter = 0;
        for (int i = 0; i < positions.size() - 1; i++) {
            int e1 = positions.get(i) ;
            int e2 = positions.get(i + 1) ;


            if (!friendships.get(e1).contains(e2)){
                counter++;
                System.out.println(friendships.get(e1) + " and " + friendships.get(e2));
                System.out.print(e1 + " - " + e2);
                System.out.println(" conflict");
            }else{
//                System.out.println(" miracle");
            }
        }
        return counter;
    }

    public static void readInstance(String path) throws FileNotFoundException {
        sizes = new HashMap<>();
        friendships = new HashMap<>();
        positions = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        int nbStudents = scanner.nextInt();

        graph = new int[nbStudents][nbStudents];

        int nbFriendships = scanner.nextInt();
        for (int i = 1; i <= nbStudents; i++) {
            int size = scanner.nextInt();
            sizes.put(i, size);
            friendships.put(i, new HashSet<>());
        }


        for (int i = 0; i < nbFriendships; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();

            friendships.get(a).add(b);
            friendships.get(b).add(a);

            graph[a-1][b-1] = 1;
            graph[b-1][a-1] = 1;
        }
    }

    private static int V, pathCount;
    private static int[] path;
    private static HashSet<Integer> vis;

    public static void findHamiltonianCycle(int[][] g)
    {
        vis = new HashSet<>();
        V = g.length;
        path = new int[V];

        Arrays.fill(path, -1);
        graph = g;
        try
        {
            path[0] = 0;
            vis.add(0);
            pathCount = 1;
            solve(0);
            System.out.println("No solution");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            display();
        }
    }
    /** function to find paths recursively **/
    public static void solve(int vertex) throws Exception
    {
        /** solution **/
//        if (graph[vertex][0] == 1 && pathCount == V)
//            throw new Exception("Solution found");
        /** all vertices selected but last vertex not linked to 0 **/
        if (pathCount == V)
            throw new Exception("Solution found");

//            return;

        for (int v = 0; v < V; v++)
        {
            /** if connected **/
            if (graph[vertex][v] == 1 && !vis.contains(v))
            {
                /** add to path **/
                path[pathCount++] = v;
                /** remove connection **/
                graph[vertex][v] = 0;
                graph[v][vertex] = 0;

                /** if vertex not already selected  solve recursively **/
                solve(v);

                /** restore connection **/
                graph[vertex][v] = 1;
                graph[v][vertex] = 1;
                /** remove path **/
                path[--pathCount] = -1;

                vis.remove(v);
            }
        }
    }
    /** function to check if path is already selected **/
    public static boolean isPresent(int v)
    {
        for (int i = 0; i < pathCount - 1; i++)
            if (path[i] == v)
                return true;
        return false;
    }
    /** display solution **/
    public static void display()
    {
        System.out.print("\nPath : ");
        for (int i = 0; i < V; i++)
            System.out.print(path[i % V] +" ");
        System.out.println();
    }

//    public static Integer[] HybridHAMWorking(HashMap<Integer, HashSet<Integer>> friendships){
//        HashMap<Integer,Integer> degrees = numberOfFriends(friendships);
//        Integer[] degreesArr = degrees.values().toArray(new Integer[0]);
//        Integer[] vMin = degrees.keySet().toArray(new Integer[0]);
//        Integer[] vMax;
//
//        Arrays.sort(vMin, (o1, o2) -> Float.compare(degreesArr[o1-1], degreesArr[o2-1]));
//        vMax = vMin.clone();
//        Collections.reverse(Arrays.asList(vMax));
//
//        List<Integer> currPath = getLongestPathPossible(vMax, vMin, friendships);
//
//        HashSet<Integer> setMax = new HashSet(currPath);
//        Set<Integer> notIn = new HashSet<>();
//        for (int i = 1; i <= friendships.size(); i++) {
//            if(!setMax.contains(i)){
//                notIn.add(i);
//            }
//        }
//
//        int extend;
//        HashSet<Integer> rotationned = new HashSet<>();
//
//        while(currPath.size() != friendships.size()) {
//            int n = currPath.size();
//            int end = currPath.get(n - 1);
//
//            Set<Integer> possibleHead = new HashSet(friendships.get(currPath.get(0)));
//            possibleHead.retainAll(notIn);
//            Set<Integer> possibleTail = new HashSet(friendships.get(end));
//            possibleTail.retainAll(notIn);
//            if(possibleHead.size() > 0){
//                extend = possibleHead.iterator().next();
//                currPath.add(0,extend);
//                notIn.remove(extend);
//                setMax.add(extend);
//            }else if(possibleTail.size() > 0){
//                extend = possibleTail.iterator().next();
//                currPath.add(extend);
//                notIn.remove(extend);
//                setMax.add(extend);
//
//            }else {
//
//                int ngbMax = 0;
//                int ngbNode = -1;
//
//                for (int ngb : friendships.get(end)) {
//                    if (degrees.get(ngb) > ngbMax && currPath.indexOf(ngb) < n-2 && !rotationned.contains(ngb)) {
//                        ngbMax = degrees.get(ngb);
//                        ngbNode = ngb;
//                    }
//                    if(ngbNode != -1)
//                        rotationned.add(ngbNode);
//                }
//                if(ngbNode == -1){
//                    if(!rotationned.contains(currPath.get(0))){
//                        rotationned.add(currPath.get(0));
//                        Collections.reverse(currPath);
//                        continue;
//                    }
//                    else
//                        break;
//                }
//
//                System.out.println(rotationned);
//                System.out.println("end : " + end + ", Ngb ind : " + ngbNode + " currsize : "+currPath.size());
//                System.out.println("Before rotation : " + currPath);
//
//                List<Integer> toReverse;
//                int posNgbInd;
//
//                if (ngbNode == currPath.get(0)) {
//                    ngbMax = 0;
//
//                    int maxNextHead = 0;
//                    int nextHead;
//
//                    for (int ni : notIn) {
//                        if (degrees.get(ni) > ngbMax) {
//                            nextHead = -1;
//                            for(int ngNext : friendships.get(ni)){
//                                if(setMax.contains(ngNext)){
//                                    nextHead = ngNext;
//                                    break;
//                                }
//                            }
//                            if(nextHead != -1){
//                                ngbMax = degrees.get(ni);
//                                ngbNode = ni;
//                                maxNextHead = nextHead;
//                            }
//                        }
//                    }
//                    posNgbInd = currPath.indexOf(maxNextHead);
//                    List<Integer> newList = new ArrayList<>();
//                    newList.add(ngbNode);
//                    newList.addAll(currPath.subList(posNgbInd, n));
//                    newList.addAll(currPath.subList(0, posNgbInd));
//                    System.out.println("New List " + newList);
//                    currPath = new ArrayList<>(newList);
//
//                    setMax.add(ngbNode);
//                    notIn.remove(ngbNode);
//                }
//                else {
//
//                    System.out.println(ngbNode +", friends "+ friendships.get(ngbNode) + " end friend "+ friendships.get(end));
//                    posNgbInd = currPath.indexOf(ngbNode);
//                    toReverse = new ArrayList(currPath.subList(posNgbInd+1, n));
//                    Collections.reverse(toReverse);
//                    currPath = currPath.subList(0, posNgbInd+1);
//                    currPath.addAll(toReverse);
//
//                    System.out.println("After rotation " + currPath + " new ngb " + friendships.get(currPath.get(n - 1)));
//
//                    while (true) {
//                        extend = greedyChoice(currPath.get(n - 1), vMin, setMax, friendships);
//                        if (extend == -1)
//                            break;
//
//                        System.out.println("Added " + extend);
//                        setMax.add(extend);
//                        currPath.add(extend);
//                        notIn.remove(extend);
//                    }
//                }
//            }
//        }
//
//        System.out.println(currPath.size()+ " last solution" + currPath);
//        System.out.println(notIn);
//
//        return currPath.toArray(new Integer[0]);
//    }
}