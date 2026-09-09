package Lec17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Lec17Main {

    /*
     * 1. 자바에서 람다를 다루기 위한 노력
     */

    // 과일가게 장사중
    List<Fruit> fruits = Arrays.asList(
            new Fruit("사과", 1_000),
            new Fruit("사과", 1_200),
            new Fruit("사과", 1_200),
            new Fruit("사과", 1_500),
            new Fruit("바나나", 3_000),
            new Fruit("바나나", 3_200),
            new Fruit("바나나", 2_500),
            new Fruit("수박", 10_000)
    );

    // 사장님 사과만 보여주세요~
    private List<Fruit> findApples(List<Fruit> fruits) {

        List<Fruit> apples = new ArrayList<>();

        for ( Fruit fruit : fruits ) {
            if ( fruit.getName().equals("사과") ) {
                apples.add( fruit );
            }
        }

        return apples;

    }

    // 사장님 바나나만 보여주세요~
    private List<Fruit> findBananas(List<Fruit> fruits) {

        List<Fruit> bananas = new ArrayList<>();

        for ( Fruit fruit : fruits ) {
            if ( fruit.getName().equals("바나나") ) {
                bananas.add( fruit );
            }
        }

        return bananas;

    }

    // 함수 기능이 중복되네? 둘이 합쳐야지
    private List<Fruit> findFruitsWithName(List<Fruit> fruits, String name) {

        List<Fruit> results = new ArrayList<>();

        for ( Fruit fruit : fruits ) {
            if ( fruit.getName().equals( name ) ) {
                results.add( fruit );
            }
        }

        return results;

    }

    // 사장님 사과랑 바나나 같이 보여주세요~
    // 사과인데 가격이 1200원을 넘지 않는 사과만 보여주세요~
    // 10000원 이하의 수박과 1000원 이상의 바나나 보여주세요~
    // 위 함수들로는 대응이 안되는 요구사항이다.
    // 그렇다고 파라미터나 메서드를 계속해서 추가하기에는 너무 비효율적이다.

    // 여기서 인터페이스와 익명클래스를 사용해보자!
    private List<Fruit> filterFruits(List<Fruit> fruits, FruitFilter fruitFilter) {

        List<Fruit> results = new ArrayList<>();

        for ( Fruit fruit : fruits ) {
            if ( fruitFilter.isSelected(fruit) ) {
                results.add(fruit);
            }
        }

        return results;

    }

    // 익명클래스를 사용하는 것으로 무수한 메서드 생성을 막아냈다!
    private void callFilterFruits() {

        List<Fruit> results = this.filterFruits(this.fruits, new FruitFilter() {
            @Override
            public boolean isSelected(Fruit fruit) {
                return Arrays.asList("사과, 바나나").contains(fruit.getName()) && fruit.getPrice() > 5_000; // 다양한 조건을 즉석에서 생성
            }
        });

    }

    /*
     * 그러나 자바에서 익명클래스를 사용하는 것은 너무 복잡하다.
     * 또한 다양한 Filter가 필요할 수도 있다.
     * 과일 간의 무게 비교, N개의 과일을 한 번에 비교 등등...
     *
     * 자바에서는 이런 어려움을 해소하기 위해 JDK8부터 람다(이름없는 함수)가 등장했다!
     * 또한, 위의 FruitFilter와 같은 인터페이스 Predicate, Consumer 등을 많이 만들어 두었다!
     */
    private List<Fruit> filterFruits2(List<Fruit> fruits, Predicate<Fruit> fruitFilter) {

        List<Fruit> results = new ArrayList<>();

        for ( Fruit fruit : fruits ) {
            if ( fruitFilter.test(fruit) ) {
                results.add(fruit);
            }
        }

        return results;

    }

    private void callFilterFruitLambda() {

        // 람다 사용
        List<Fruit> results = this.filterFruits2(this.fruits, fruit -> fruit.getName().equals("사과"));

    }

    /*
     * 자바에서 람다 사용 문법
     *  - 변수 -> 변수를 이용한 함수
     *  - (변수1, 변수2) -> 변수1과 변수2를 이용한 함수
     *
     * JDK8에서는 if문과 for문을 좀 더 간결하고 깔끔하게 사용하기 위해 스트림이 등장했다.
     */
    private List<Fruit> filterFruitsWithStream(List<Fruit> fruits, Predicate<Fruit> fruitFilter) {
        return fruits.stream()
                .filter(fruitFilter)
                .collect(Collectors.toList());
    }

    private void callFilterFruitsLambda2() {
        List<Fruit> results = this.filterFruitsWithStream(this.fruits, Fruit::isApple); // 메서드 레퍼런스 사용
    }

    /*
     * 포인트는 메서드 자체를 직접 넘겨주는 것 처럼 쓸 수 있다.
     * 하지만 실제로 받는 것은 Predicate 인터페이스 구현체를 받는다.
     *
     * 바꿔 말하면, 자바에서 함수는 변수에 할당되거나 파라미터로 전달할 수 없다.
     * 자바에서 함수는 2급 시민으로 간주한다.
     */


    private void callFilterFruits3() {

        String targetFruitName = "바나나";
        targetFruitName = "수박";
        filterFruits(fruits, (fruit) -> targetFruitName.equals(fruit.getName())); // 에러

        /*
         * Variable used in lambda expression should be final or effectively final
         * 자바에서는 람다를 쓸 때 사용할 수 있는 변수에 제약이 있다!
         * 상수거나, 실질적 상수인 변수만 람다에서 사용할 수 있다.
         */


    }

}
