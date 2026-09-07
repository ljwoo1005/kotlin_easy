package Lec12;

class JavaPerson {

    private static final int MIN_AGE = 1;

    // 정적 팩토리 메서드
    public static JavaPerson newBaby(String name) {
        return new JavaPerson(name, MIN_AGE);
    }

    private String name;
    private int age;

    private JavaPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

}

class JavaSingleton {

    private static final JavaSingleton INSTANCE = new JavaSingleton();

    private JavaSingleton() { }

    public static JavaSingleton getInstance() {
        return INSTANCE;
    }

}

interface Moveable {
    void move();
    void fly();
}

public class Lec12Main {

    private static void moveSomething(Moveable moveable) {
        moveable.move();
        moveable.fly();
    }

    public static void main(String[] args) {

        Person2.Companion.newBaby("ABC"); // 코틀린 클래스의 companion object에 접근(이름이 없는 companion object)
        Person2.Companion.getA(); // 코틀린 클래스의 companion object에서 static 변수에 접근(상수는 접근 불가능)
        Person2.setA(1); // @JvmStatic이 붙은 setter에 companion object 이름을 생략하고 실제 static 함수처럼 접근

        Person.Factory.newBaby("ABC"); // 코틀린 클래스의 companion object에 접근(이름이 있는 companion object)
        Person.log(); // @JvmStatic이 붙은 함수의 경우 companion object 이름을 생략하고 실제 static 함수처럼 접근 가능

        // 익명 클래스 사용
        moveSomething(new Moveable() {
            @Override
            public void move() {
                System.out.println("움직인다~~");
            }

            @Override
            public void fly() {
                System.out.println("난다~~~");
            }
        });

    }

}