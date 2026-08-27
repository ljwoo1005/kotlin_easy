package Lec10;

public class Lec10Main {



}

abstract class JavaAnimal {

    protected final String species;
    protected final int legCount;

    public JavaAnimal(String species, int legCount) {
        this.species = species;
        this.legCount = legCount;
    }

    abstract public void move();

    public String getSpecies() {
        return species;
    }

    public int getLegCount() {
        return legCount;
    }

}

class JavaCat extends JavaAnimal {

    public JavaCat(String species) {
        super(species, 4);
    }

    @Override
    public void move() {
        System.out.println("고양이가 사뿐 사뿐 걸어가~");
    }

}

final class JavaPenguin extends JavaAnimal {

    private final int wingCount;

    public JavaPenguin(String species) {
        super(species, 2);
        this.wingCount = 2;
    }

    @Override
    public void move() {
        System.out.println("펭귄이 움직입니다! 꿱꿱");
    }

    @Override
    public int getLegCount() {
        return super.legCount + this.wingCount;
    }

}

interface JavaSwimable {

    default void act() {
        System.out.println("어푸 어푸");
    }

}

interface JavaFlyable {

    default void act() {
        System.out.println("파닥 파닥");
    }

    void fly();

}

final class JavaPenguin2 extends JavaAnimal implements JavaFlyable, JavaSwimable {

    private final int wingCount;

    public JavaPenguin2(String species) {
        super(species, 2);
        this.wingCount = 2;
    }

    /*
     * 여러 인터페이스의 동일한 이름의 default 메서드를 override할 때
     * default 메서드는 완성된 메서드이기에 컴파일러 입장에서 어느 인터페이스의 메서드를 호출해야할 지 판단할 수 없기에,
     * 구현체에서 호출 모호성을 해결해야 함
     * -> 구현체에서 default 메서드를 override함으로써 모호성 해결
     */
    @Override
    public void act() {
        JavaSwimable.super.act();
        JavaFlyable.super.act();
    }

    @Override
    public void fly() {

    }

    @Override
    public void move() {

    }

}