public class MaximumElement {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 6, 7, 777};
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        System.out.println("The maximum element in the array is " + max);   
    }
}
