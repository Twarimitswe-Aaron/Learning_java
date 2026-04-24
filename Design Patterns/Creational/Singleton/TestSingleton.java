package Singleton;

public class TestSingleton {
    public static void main(String[] args) {
        Browser.getInstance().display();
        EnumLevel browser= EnumLevel.INSTANCE;
        browser.display();
    }
}
