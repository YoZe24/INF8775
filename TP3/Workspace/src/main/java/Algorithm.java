import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Algorithm {
    static List<Integer> positions;
    static HashMap<Integer, Integer> sizes;
    static HashMap<Integer, HashSet<Integer>> friendships;

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/instances/" + "66_99.0";

        // readInstance(path);

        makeExample();
        algorithm(sizes, friendships);
        // solveExample();
        // printResults(positions, sizes, friendships);
    }

    public static void algorithm(HashMap<Integer, Integer> sizes, HashMap<Integer, HashSet<Integer>> friendships){
        positions = new ArrayList<>();
        HashMap<Integer, Integer> nbFriends = numberOfFriends(friendships);
        System.out.println(nbFriends);
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
            for (int j = i; j > 0; j--) {
                int e2 = positions.get(i - 1);
                if (sizes.get(e1) < sizes.get(e2)){
                    counter++;
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
}