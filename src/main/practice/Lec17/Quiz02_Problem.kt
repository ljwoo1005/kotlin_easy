package Lec17

/*
 * [학습 목표 & 복습 개념]
 * - Java의 단일 메서드 인터페이스(SAM) 및 Predicate 기반 필터링 코드를 코틀린의 관용적인 고차 함수(Higher-Order Function)로 리팩토링합니다.
 * - 함수의 마지막 인자가 람다식일 때 소괄호 바깥으로 람다 블록을 분리하는 후행 람다(Trailing Lambda) 문법을 체화합니다.
 * - 자바에서는 불가능했던 외부 가변 변수(`var`)의 참조 및 수정을 가능하게 하는 코틀린의 클로저(Closure) 메커니즘을 학습합니다.
 *
 * [문제 설명]
 * 아래는 Java에서 특정 조건의 과일을 필터링하기 위해 작성된 전형적인 코드입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public interface FruitFilter {
 *     boolean isSelected(Fruit fruit);
 * }
 *
 * public class JavaFruitHelper {
 *     // 1. 과일 필터링 메서드
 *     public static List<Fruit> filterFruits(List<Fruit> fruits, FruitFilter filter) {
 *         List<Fruit> results = new ArrayList<>();
 *         for (Fruit fruit : fruits) {
 *             if (filter.isSelected(fruit)) {
 *                 results.add(fruit);
 *             }
 *         }
 *         return results;
 *     }
 *
 *     // 2. 가변 변수를 활용한 조건 필터링 시도
 *     public static List<Fruit> tryFilterWithBudget(List<Fruit> fruits, int budget) {
 *         int totalSpent = 0;
 *         // 컴파일 에러 발생!
 *         // "Variable used in lambda expression should be final or effectively final"
 *         // return filterFruits(fruits, fruit -> {
 *         //     if (totalSpent + fruit.getPrice() <= budget) {
 *         //         totalSpent += fruit.getPrice(); // 자바 람다에서는 외부 변수 수정 불가!
 *         //         return true;
 *         //     }
 *         //     return false;
 *         // });
 *         return new ArrayList<>();
 *     }
 * }
 * ----------------------------------------------------
 * 위 Java 코드를 코틀린의 관용적인 함수 타입 및 클로저를 활용한 코드로 리팩토링하세요.
 *
 * 1. filterList<T>(items: List<T>, predicate: (T) -> Boolean): List<T>
 *    - 특정 도메인에 종속되지 않고 임의의 타입 `T` 리스트와 조건 검사 람다 `predicate`를 받아 조건을 만족하는 요소만 담은 새 리스트를 반환하는 고차 함수입니다.
 *    - 내부에서 루프를 순회하며 `predicate(item)`이 true인 요소만 수집합니다.
 *
 * 2. filterAndAccumulatePrice(fruits: List<Quiz17Fruit>, budget: Int): Pair<List<Quiz17Fruit>, Int>
 *    - 예산 `budget` 한도 내에서 과일을 앞에서부터 순서대로 담아 구매 가능한 과일 리스트와 총 사용 금액을 `Pair`로 반환합니다.
 *    - 함수 내부에 가변 변수 `var totalSpent = 0`을 선언하고, `filterList`의 람다 내부에서 `totalSpent`를 직접 갱신하는 코틀린의 클로저(Closure)를 활용하여 구현하세요.
 *
 * [요구사항 & 제약조건]
 * - filterList: 제네릭 함수 `fun <T> filterList(items: List<T>, predicate: (T) -> Boolean): List<T>` 시그니처를 사용하세요.
 * - filterAndAccumulatePrice: 내부에서 `filterList` 함수와 후행 람다(소괄호 밖 `{ }`) 문법을 반드시 활용하세요.
 * - filterAndAccumulatePrice: 람다 블록 안에서 외부 가변 변수(`var totalSpent`)를 포획(Capture)하여 누적 금액을 갱신하세요.
 *
 * [입출력 및 기대 결과]
 * - filterList(listOf(1, 2, 3, 4, 5)) { it % 2 == 0 } -> [2, 4]
 * - val fruits = listOf(Quiz17Fruit("사과", 1000), Quiz17Fruit("바나나", 3000), Quiz17Fruit("수박", 5000))
 * - filterAndAccumulatePrice(fruits, 4000) -> Pair([Quiz17Fruit("사과", 1000), Quiz17Fruit("바나나", 3000)], 4000)
 * - filterAndAccumulatePrice(fruits, 500)  -> Pair([], 0)
 *
 * [💡 HINT]
 * - 코틀린은 람다가 시작되는 지점에 참조하는 변수들을 포획하여 보관하므로, 자바와 달리 람다 내부에서 외부 `var` 변수를 자유롭게 수정할 수 있습니다.
 * - `filterList` 호출 시 마지막 인자가 람다이므로 `filterList(fruits) { fruit -> ... }` 형태로 호출할 수 있습니다.
 */

// 1. 범용 리스트 필터링 고차 함수 뼈대
private fun <T> filterList(items: List<T>, predicate: (T) -> Boolean): List<T> {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 2. 클로저를 활용한 예산 내 과일 선택 및 금액 누적 함수 뼈대
private fun filterAndAccumulatePrice(fruits: List<Quiz17Fruit>, budget: Int): Pair<List<Quiz17Fruit>, Int> {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

fun main() {
    // 1. filterList 제네릭 고차 함수 검증
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    val evenNumbers = filterList(numbers) { it % 2 == 0 }
    check(evenNumbers == listOf(2, 4, 6)) { "짝수 필터링 결과는 [2, 4, 6]이어야 합니다." }

    val words = listOf("kotlin", "java", "swift", "c")
    val longWords = filterList(words) { it.length >= 5 }
    check(longWords == listOf("kotlin", "swift")) { "길이 5 이상 단어는 [kotlin, swift]여야 합니다." }

    // 2. filterAndAccumulatePrice 및 클로저(Closure) 검증
    val marketFruits = listOf(
        Quiz17Fruit("사과", 1_000),
        Quiz17Fruit("바나나", 2_000),
        Quiz17Fruit("오렌지", 1_500),
        Quiz17Fruit("수박", 10_000)
    )

    // 예산 3,000원: 사과(1000) + 바나나(2000) = 3000원 (오렌지는 예산 초과로 제외)
    val result1 = filterAndAccumulatePrice(marketFruits, 3_000)
    check(result1.first.map { it.name } == listOf("사과", "바나나")) { "선택된 과일은 사과, 바나나여야 합니다." }
    check(result1.second == 3_000) { "총 사용 금액은 3,000원이어야 합니다." }

    // 예산 4,500원: 사과(1000) + 바나나(2000) + 오렌지(1500) = 4500원
    val result2 = filterAndAccumulatePrice(marketFruits, 4_500)
    check(result2.first.size == 3) { "선택된 과일은 총 3개여야 합니다." }
    check(result2.second == 4_500) { "총 사용 금액은 4,500원이어야 합니다." }

    // 예산 500원: 아무것도 구매 불가
    val result3 = filterAndAccumulatePrice(marketFruits, 500)
    check(result3.first.isEmpty()) { "구매 가능한 과일이 없어야 합니다." }
    check(result3.second == 0) { "총 사용 금액은 0원이어야 합니다." }

    println("✅ Quiz02 테스트 통과! (고차 함수 리팩토링 및 클로저 체화 완료)")
}
