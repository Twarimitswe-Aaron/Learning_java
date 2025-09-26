public class OddsAndEvens {
    public static void main(String[] args) {
        int[] arr={1,2,3,4,5,6,7,8,9,10};
        int evenSum=0, oddSum=0, oddCount=0,evenCount=0;
        for(int i=1;i<=arr.length-1;i++){
            if(arr[i]%2==0){
                evenSum+=arr[i];
                evenCount++;
            }else{
                oddSum+=arr[i];
                oddCount++;
            }
        }
        System.out.println("Sum of even numbers: "+evenSum+ " Count of even numbers: "+evenCount);
        System.out.println("Sum of odd numbers: "+oddSum + " Count of odd numbers: "+oddCount);
    }
}
