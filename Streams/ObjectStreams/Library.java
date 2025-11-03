
import java.io.*;
import java.util.*;

class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    String title;
    String author;
    double price;

    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book {title=}" + title + ", author=" + author + ", price=" + price + "}";
    }

}

public class Library {
    public static void main(String[] args) {
        Book b1 = new Book("Amazon", "Aart", 1000);
        Book b2 = new Book("Java", "Jules", 1500);
        Book b3 = new Book("Python", "Chouch", 1200);

        List<Book> books = new ArrayList<>();
        books.add(b1);
        books.add(b2);
        books.add(b3);

        try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.ser"));) {
            oos.writeObject(books);

        } catch (Exception e) {
            System.out.println("Serialization Error: " + e.getMessage());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("books.ser"));) {
            List<Book> deserializedBooks = (List<Book>) ois.readObject();

            for (Book b : deserializedBooks) {
                System.out.println(b);
            }
        } catch (Exception e) {
            System.out.println("Deserialization Error: " + e.getMessage());
        }

    }

}
