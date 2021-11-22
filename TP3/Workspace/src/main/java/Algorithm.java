import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Algorithm {
    static HashMap<Integer, Integer> sizes;
    static HashMap<Integer, HashSet<Integer>> friendships;

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/main/resources/instances/" + "66_99.0";
        List<Integer> positions = new ArrayList<>();
        readInstance(path);
    }

    public static int unSatisfaction(List<Integer> positions){
        int counter = 0;
        for (int i = 0; i < positions.size() - 1; i++) {
            if (!friendships.get(i).contains(i + 1)){
                counter++;
            }
        }
        return counter;
    }

    public static void readInstance(String path) throws FileNotFoundException {
        sizes = new HashMap<>();
        friendships = new HashMap<>();
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