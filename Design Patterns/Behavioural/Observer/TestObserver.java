package Observer;

public class TestObserver {
    public static void main(String[] args) {
        YoutubeChannel channel = new YoutubeChannel();
        Subscriber sub1=new Subscriber("sub1");
        Subscriber sub2=new Subscriber("sub2");
        Subscriber sub3=new Subscriber("sub3");

        channel.addObserver(sub1);
        channel.addObserver(sub2);
        channel.addObserver(sub3);

        channel.upload("Desing pattern tutorial");
    }
}
