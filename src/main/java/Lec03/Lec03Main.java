package Lec03;

public class Lec03Main {

    public static void printAgeIfPerson( Object obj ) {

        if( obj instanceof Person) {
            Person person = (Person) obj; // Object 타입인 obj를 instanceof 연산자로 Person 객체임을 검증 후 강제 형변환
            System.out.println( person.getAge() );
        }

    }

}
