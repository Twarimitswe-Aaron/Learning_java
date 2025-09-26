public class SearchInArray {
    public static  void main(String args[]){
        int[] arr={1,2,3,4,5,6,6,7,777};
        int target=6;
        boolean found=false;
        for(int i=0;i<arr.length;i++){
            if(arr[i]==target){
                found=true;
                System.out.println("Element found at index: "+i);
                break;
            }
        }
        if(!found){
            System.out.println("Element not found in the array");
        }
    }
}
