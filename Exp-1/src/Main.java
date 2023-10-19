import java.util.Scanner;

/**
 * @author Lzl
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] students = new int[n];

        for (int i = 0; i < n; i++) {
            students[i] = i + 1;
        }

        for (int i = 0; i < m; i++) {
            int p = sc.nextInt();
            int q = sc.nextInt();

            int index = findIndex(students, p);
            int temp = students[index];
            if (q > 0) {
                for (int j = index; j < index + q; j++) {
                    students[j] = students[j + 1];
                }
            } else {
                for (int j = index; j > index + q; j--) {
                    students[j] = students[j - 1];
                }
            }
            students[index + q] = temp;
        }

        for (int i = 0; i < n; i++) {
            System.out.print(students[i] + " ");
        }

        sc.close();
    }
    public static int findIndex(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
