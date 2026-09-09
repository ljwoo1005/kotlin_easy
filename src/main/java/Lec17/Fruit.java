package Lec17;

public class Fruit {

    private final String name;
    private final int price;

    public Fruit(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public static boolean isApple(Fruit fruit) {
        return fruit.getName().equals("사과");
    }

}
