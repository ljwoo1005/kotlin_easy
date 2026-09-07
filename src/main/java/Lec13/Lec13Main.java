package Lec13;

public class Lec13Main {

}

/*
 * 자바 중첩 클래스 종류
 * - static을 사용하는 중첩 클래스 : 밖의 클래스를 직접적으로 참조할 수 없는 클래스
 * - static을 사용하지 않는 중첩 클래스
 *      1. 내부 클래스(Inner Class) : 밖의 클래스를 직접 참조 가능한 클래스
 *      2. 지역 클래스(Local Class) : 메서드 내부에 클래스를 정의 (사용 케이스 거의 없음)
 *      3. 익명 클래스(Anonymous Class) : 일회성 클래스
 *
 * 보통 중첩 클래스라 하면 static 클래스 or 내부 클래스를 말한다.
 */
class JavaHouse {

    private String address;
    private LivingRoom livingRoom;

    public JavaHouse(String address) {
        this.address = address;
        this.livingRoom = new LivingRoom(10);
    }

    public LivingRoom getLivingRoom() {
        return livingRoom;
    }

    /*
     * 내부 클래스
     */
    public class LivingRoom {

        private double area;

        public LivingRoom(double area) {
            this.area = area;
        }

        public String getAddress() {
            return JavaHouse.this.address; // 바깥 클래스와 연결되어 있다.
        }

    }

    /*
     * static 클래스
     */
    public static class StaticLivingRoom {

        private double area;

        public StaticLivingRoom(double area) {
            this.area = area;
        }

        public String getAddress() {
            return JavaHouse.this.address; // static클래스는 바깥 클래스를 바로 불러올 수 없다.
        }

    }

}

/*
 * Effective Java 3rd Edition - Item24, Item86
 *
 * 1. 내부 클래스는 숨겨진 외부 클래스 정보를 가지고 있어,
 * 참조를 해지하지 못하는 경우 메모리 누수가 생길 수 있고,
 * 이를 디버깅 하기 어렵다.
 *
 * 2. 내부 클래스의 직렬화 형태가 명확하게 정의되지 않아
 * 직렬화에 있어 제한이 있다.
 *
 * 즉, 내부 클래스가 외부 클래스를 참조함으로 인에 생기는 몇 가지 문제점이 존재한다.
 *
 * 그래서 Effective Java에서는 "클래스 안에 클래스를 만들 때는 static 클래스를 사용하라"고 가이드하고 있다.
 */