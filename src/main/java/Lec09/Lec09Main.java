package Lec09;

public class Lec09Main {



}

class JavaPerson {

    private final String name; // 개명이 불가능한 나라에 사는 Person 클래스
    private int age;

    public JavaPerson(int age, String name) {
        // 나이 검증 = 생성자 호출 시
        if (this.age <= 0) {
            throw new IllegalArgumentException(String.format("나이는 %s일 수 없습니다", age));
        }

        this.age = age;
        this.name = name;
    }

    // 새로 태어나는 아기는 무조건 1살이니, 아기를 위한 새로운 생성자를 만들어보자
    public JavaPerson(String name) {
        this(1, name);
    }

    // name은 상수이기 때문에 setter가 없다!

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 성인인지 확인하는 함수
    public boolean isAdult() {
        return this.age >= 20;
    }

}