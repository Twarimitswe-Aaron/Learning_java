public class Print<T extends Number>
{
    T valueToPrint;
    public Print(T valueToPrint){
        this.valueToPrint=valueToPrint;
    }

    public void print(){
        System.out.println("Value: "+ valueToPrint);
    }

}