import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {
    static List<Integer> positions;
    static HashMap<Integer, Integer> sizes;
    static HashMap<Integer, HashSet<Integer>> friendships;
    static Random rand = new Random();

    public static void main(String[] args) throws FileNotFoundException {
        String path = args[0];
        boolean p = args.length == 2;

        readInstance(path);

        localSearch(friendships, sizes, p);
    }

    public static List<Integer> localSearch(HashMap<Integer, HashSet<Integer>> friendships, HashMap<Integer, Integer> sizes, boolean p){
        Integer[] path = HybridHAM(friendships);
        List<Integer> bestSol = Arrays.asList(path);
        int unSat = unSatisfaction(bestSol, sizes);
        int conflicts = conflicts(bestSol, friendships);
        int min = unSat;
        if (conflicts == 0){
            if (p){
                printSolution(bestSol);
            }
            else{
                System.out.flush();
                System.out.println(min);
            }
        }
        while (unSat > 0){
            int minSat = Integer.MAX_VALUE;
            List<Integer> curBest = new ArrayList<>();
            List<List<Integer>> puzzles = puzzles(friendships, sizes, bestSol);
            if (puzzles.size() > 0){
                for (List<Integer> list : puzzles) {
                    int curSat = unSatisfaction(list, sizes);
                    if (curSat < minSat){
                        minSat = curSat;
                        curBest = list;
                    }
                }
                int rnd = rand.nextInt(4);
                if (rnd == 0 || minSat < unSat){
                    unSat = minSat;
                    bestSol = curBest;
                    if (min > unSat) {
                        min = unSat;
                        if (p){
                            // System.out.println(min);
                            printSolution(bestSol);
                        }
                        else{
                            System.out.flush();
                            System.out.println(min);
                        }
                    }
                }
            }

            List<Integer> candidate = localRotation(friendships, sizes, bestSol);
            int unSatCandidate = unSatisfaction(candidate, sizes);
            int rnd = rand.nextInt(25);
            if (rnd == 0 || unSatCandidate < unSat){
                unSat = unSatCandidate;
                bestSol = candidate;
                if (min > unSat) {
                    min = unSat;
                    if (p){
                        // System.out.println(min);
                        printSolution(bestSol);
                    }
                    else{
                        System.out.flush();
                        System.out.println(min);
                    }
                }
            }
        }
        return bestSol;
    }

    public static List<Integer> localRotation(HashMap<Integer, HashSet<Integer>> friendships, HashMap<Integer, Integer> sizes, List<Integer> positions){
        int last = positions.get(positions.size() - 1);
        List<Integer> neighbors = new ArrayList<>(friendships.get(last));

        Random rand = new Random();
        int neighbor = neighbors.get(rand.nextInt(neighbors.size()));

        List<Integer> newList = new ArrayList<>();
        for (int item : positions) {
            newList.add(item);
            if (neighbor == item) break;
        }

        for (int i = positions.size() - 1; i > 0; i--) {
            int item = positions.get(i);
            if (item == neighbor) break;
            newList.add(item);
        }

        return newList;
    }

    public static List<List<Integer>> puzzles(HashMap<Integer, HashSet<Integer>> friendships, HashMap<Integer, Integer> sizes, List<Integer> positions){
        List<List<Integer>> solutions = new ArrayList<>();
        int last = positions.get(positions.size() - 1);
        List<Integer> neighbors = new ArrayList<>(friendships.get(last));
        for (int neigh : neighbors) {
            int posNeigh = positions.indexOf(neigh);
            if (positions.size() - 1 - posNeigh > 1){
                int posDude1 = posNeigh + 1;
                int dude1 = positions.get(posDude1);
                List<Integer> neighborsDude1 = new ArrayList<>(friendships.get(dude1));

                for (int candidate : neighborsDude1) {
                    int posCandidate = positions.indexOf(candidate);
                    int posNextCandidate = posCandidate + 1;
                    if (posCandidate < posNeigh){
                        if (friendships.get(positions.get(posNextCandidate)).contains(last)){
                            List<Integer> newList = new ArrayList<>();
                            for (int i = 0; i <= posCandidate; i++) {
                                newList.add(positions.get(i));
                            }
                            for (int i = posDude1; i < positions.size(); i++) {
                                newList.add(positions.get(i));
                            }

                            newList.add(positions.get(posNeigh));

                            for (int i = posCandidate + 1; i < posNeigh; i++) {
                                newList.add(positions.get(i));
                            }

                            if (conflicts(newList, friendships) == 0){
                                solutions.add(newList);
                            }
                        }
                    }
                }
            }
        }
        return solutions;
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
            int e1 = positions.get(i);
            int e2 = positions.get(i + 1);
            if (!friendships.get(e1).contains(e2)){
                counter++;
            }
        }
        return counter;
    }

    public static HashMap<Integer, Integer> numberOfFriends(HashMap<Integer, HashSet<Integer>> friendships){
        HashMap<Integer, Integer> numberOfFriends = new HashMap<>();

        for (Map.Entry<Integer, HashSet<Integer>> item : friendships.entrySet()) {
            numberOfFriends.put(item.getKey(), item.getValue().size());
        }

        return numberOfFriends;
    }

    public static void readInstance(String path) throws FileNotFoundException {
        sizes = new HashMap<>();
        friendships = new HashMap<>();
        positions = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        int nbStudents = scanner.nextInt();
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
        }
    }

    public static void printSolution(List<Integer> positions){
        System.out.flush();
        for (int i : positions) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static Integer[] HybridHAM(HashMap<Integer, HashSet<Integer>> friendships){
        HashMap<Integer,Integer> degrees = numberOfFriends(friendships);
        Integer[] degreesArr = degrees.values().toArray(new Integer[0]);
        Integer[] vMin = degrees.keySet().toArray(new Integer[0]);
        Arrays.sort(vMin, (o1, o2) -> Float.compare(degreesArr[o1-1], degreesArr[o2-1]));

        Integer[] vMax = vMin.clone();
        Collections.reverse(Arrays.asList(vMax));

        List<Integer> currPath = new ArrayList<>();


        currPath = getLongestPathPossible(vMax, vMin, friendships);
        HashSet<Integer> visited = new HashSet(currPath);

        while (currPath.size() != friendships.size()) {
            int n = currPath.size();

            int candTail = getExtremity(currPath.get(n-1),  visited, friendships);
            int candHead = getExtremity(currPath.get(0), visited, friendships);
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


        }
        return currPath.toArray(new Integer[0]);
    }

    public static int getExtremity(int ext, HashSet<Integer> visited, HashMap<Integer,HashSet<Integer>> friendships){
        List<Integer> candidates = friendships.get(ext).stream()
                .filter(node -> !visited.contains(node))
                .collect(Collectors.toList());

        if(candidates.size() == 0) return -1;

        return candidates.get(rand.nextInt(candidates.size()));
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
        path = reverseFromIndex(path,path.indexOf(ngbNode)+1);
        greedyPath(path, visited, vMin, friendships);

        return path;
    }

    public static List<Integer> reverseFromIndex(List<Integer> list, int index){
        List <Integer> toReverse = new ArrayList(list.subList(index, list.size()));
        Collections.reverse(toReverse);
        list = new ArrayList<>(list.subList(0, index));
        list.addAll(toReverse);
        return list;
    }

    public static List<Integer> getLongestPathPossible(Integer[] vMax,Integer[] vMin, HashMap<Integer,HashSet<Integer>> friendships){
        List<Integer> pathMax = new ArrayList<>();
        for (Integer max : vMax) {
            if (max == vMax[0]) {
                List<Integer> path = new ArrayList<>(Arrays.asList(max));
                HashSet<Integer> visited = new HashSet<>(path);

                greedyPath(path, visited, vMin, friendships);
                if (path.size() > pathMax.size()) {
                    pathMax = new ArrayList(path);
                }
            } else {
                break;
            }
        }
        return pathMax;
    }

    public static void greedyPath(List<Integer> path, HashSet<Integer> visited,Integer[] vMin, HashMap<Integer, HashSet<Integer>> friendships){
        while (true) {
            int extend = greedyChoice(path.get(path.size()-1),vMin,visited, friendships);
            if(extend == -1)
                break;

            path.add(extend);
            visited.add(extend);
        }
    }

    public static int greedyChoice(int toExtend, Integer[] vMin, HashSet<Integer> visited, HashMap<Integer,HashSet<Integer>> friendships){
        HashSet<Integer> fCurr = friendships.get(toExtend);
        for (int nMin : vMin) {
            if (!visited.contains(nMin) && fCurr.contains(nMin)) {
//                if (!isDeadEnd(visited, nMin, friendships)) {
                return nMin;
//                }
            }
        }
        return -1;
    }

    public static boolean isDeadEnd(HashSet<Integer> visited, int node, HashMap<Integer,HashSet<Integer>> friendships){
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

    public static List<Integer> insertAtPosition(List<Integer> positions, int child, int index){
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            if (positions.get(i) != child){
                newList.add(positions.get(i));
            }
        }
        newList.add(child);
        for (int i = index; i < positions.size(); i++) {
            newList.add(positions.get(i));
        }
        return newList;
    }

    public static Integer countOneChildBlocks(List<Integer> positions, HashMap<Integer, Integer> sizes, int child){
        int i = 0;
        while (i < positions.size() && child != positions.get(i)){
            i++;
        }

        int count = 0;
        for (int j = i; j < positions.size(); j++) {
            if (sizes.get(positions.get(i)) > sizes.get(positions.get(j))) count ++;
        }

        return count;
    }
}
