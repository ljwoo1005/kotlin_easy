package Lec14;

import java.util.Objects;

/*
 * 자바에서 계층간의 데이터를 전달하기 위한 DTO (Data Transfer Object)
 * - 데이터(필드)
 * - 생성자와 getter
 * - equals, hashCode
 * - toString
 *
 * 해당 메서드들은 IDE를 활용하여 만들 수도 있고, lombok을 활용할 수도 있지만
 * 클래스가 장황해지거나, 클래스 생성 이후 추가적인 처리를 해줘야 하는 단점이 있다.
 */
public class JavaPersonDto {

    private final String name;
    private final int age;

    public JavaPersonDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JavaPersonDto that = (JavaPersonDto) o;
        return age == that.age && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "JavaPersonDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

}
