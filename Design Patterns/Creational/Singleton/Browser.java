package Singleton;

public class Browser
{
    private static volatile Browser browser;
    private Browser(){}
        public static Browser getInstance(){
            if (browser==null){
               synchronized (Browser.class){
                  if(browser== null){
                      browser=new Browser();
                  }
               }
            }
            return browser;
        }
        public void display(){
            System.out.println("Info...");
        }

}