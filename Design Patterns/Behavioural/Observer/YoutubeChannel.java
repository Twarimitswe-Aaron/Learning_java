package Observer;

import java.util.ArrayList;
import java.util.List;

public class YoutubeChannel implements Subject {
    private List<Observer> observers=new ArrayList<>();
    private String video;

    @Override
    public  void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public  void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public  void notifyObservers() {
        for(Observer observer:observers){
            observer.update(video);
        }
    }

    public void upload(String title){
        this.video=title;
        notifyObservers();
    }
    public YoutubeChannel() {}
}
