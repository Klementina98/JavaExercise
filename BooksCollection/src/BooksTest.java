import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class Book{
    String title;
    String category;
    Float price;

    public static Comparator<Book> comparatorByTitleAndPrice = Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice);
    public static Comparator<Book> comparatorbyPrice = Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle);

    public Book(String title, String category, Float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public Float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f",title,category,price);
    }
}
class BookCollection{
    List<Book> books;

    public BookCollection() {
        this.books =new ArrayList<>();
    }

    public void addBook(Book book){
       this.books.add(book);
    }
    public void printByCategory(String category){
        books.stream()
                .filter(book -> book.category.equals(category))
                .sorted(Book.comparatorByTitleAndPrice).forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        if (books.size()<n){
            return books.stream().sorted(Book.comparatorbyPrice).collect(Collectors.toList());
        }else{
            return books.stream().sorted(Book.comparatorbyPrice).limit(n).collect(Collectors.toList());
        }
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде