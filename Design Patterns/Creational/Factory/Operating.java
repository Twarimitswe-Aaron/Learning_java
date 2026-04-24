package Factory;

public class Operating {
    public OS getInstance(String message){
        if(message.equals("closed")){
            return new IOS();
        }
        return new Android();
    }
}
