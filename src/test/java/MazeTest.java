import java.util.*;

public class MazeTest {

    public static void main(String[] args) {

        int row = 10;
        int col = 10;

        Stack<Integer> pos = new Stack<>();
        Stack<Integer> visited = new Stack<>();

        pos.add(0);
        visited.add(0);

        while (true) {
            int cP = pos.peek();
            int n = getNeighbour(row, col, cP, visited);
            System.out.println("Current position: " + cP);
            System.out.println("Neighbour: " + n);
            if (visited.size() == (col * row)) {
                System.out.println("Ends");
                break;
            } else {
                if (n == -1) {
                    pos.pop();
                    System.out.println("Going back to: " + pos.peek());
                } else {
                    pos.add(n);
                    System.out.println("New position: " + n);
                    visited.add(n);
                    System.out.println("Visited: " + n);
                }
            }
            System.out.println();
        }
        System.out.println(visited.toString());
    }

    static int getNeighbour(int row, int col, int index, Stack<Integer> visited) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        int rowStart = (index / col) * col;
        int rowEnd = rowStart + col - 1;

        int tn = index - col;
        int ln = index + 1;
        int bn = index + col;
        int rn = index - 1;

        if (tn >= 0 && !visited.contains(tn)) {
            neighbours.add(tn);
        }
        if (ln <= rowEnd && ln % col != 0 && !visited.contains(ln)) {
            neighbours.add(ln);
        }
        if (bn < row * col && !visited.contains(bn)) {
            neighbours.add(bn);
        }
        if (rn >= rowStart && rn % col != col - 1 && !visited.contains(rn)) {
            neighbours.add(rn);
        }

        if (neighbours.isEmpty()) {
            return -1;
        }

        int randomIndex = new Random().nextInt(neighbours.size());
        return neighbours.get(randomIndex);
    }
}
