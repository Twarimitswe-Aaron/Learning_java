public class Sort {
    public static void main(String[] args) {
        int[] arr={5,3,8,1,2};
        int n=arr.length-1;
        int minIndex=0;
        for(int i=0;i<=n;i++){
            minIndex=i;
            for(int j=i+1;j<=n;j++){
                if(arr[j]<arr[minIndex]){
                    minIndex=j;
                }
            }
            if(minIndex != i){
                int temp=arr[i];
                arr[i]=arr[minIndex];
                arr[minIndex]=temp;
            }

        }
        System.out.print("Sorted array: [");
        for(int i=0;i<=n;i++){
            System.out.print(arr[i]+",");
        }
        System.out.print("]");
    }
}
