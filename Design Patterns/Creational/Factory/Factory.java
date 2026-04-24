package Factory;

public class Factory {
    public static void main(String args[]){
        Operating factory = new Operating();
        OS os =factory.getInstance("closed");
        os.spec();
    }
}
