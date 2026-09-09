package Lec16;

public class Lec16Main {

    private String firstName;
    private String lastName;
    private int age;

    public Lec16Main(int age) {
        this.age = age;
    }

    public Lec16Main(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int nextYearAge() {
        System.out.println("멤버 함수");
        return this.age + 1;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public static void main(String[] args) {
        System.out.println(Lec16MainKt.lastChar("ABC"));
    }

}
