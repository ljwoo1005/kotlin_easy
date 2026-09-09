package Lec17

/*
 * [핵심 해결 아이디어]
 * - 자바에서는 필터링 로직을 추상화하기 위해 인터페이스를 별도로 선언해야 했지만, 코틀린은 `(T) -> Boolean` 함수 타입을 통해 제네릭 고차 함수로 깔끔하게 통합할 수 있습니다.
 * - 함수의 마지막 인자가 람다식일 때 소괄호 밖으로 중괄호를 빼내는 후행 람다(Trailing Lambda)를 적용하여 마치 언어 내장 키워드처럼 자연스러운 문법을 구현합니다.
 * - 코틀린은 람다식이 참조하는 외부의 변수를 모두 포획(Capture)하여 클로저(Closure) 객체로 관리합니다.
 *   덕분에 자바의 `effectively final` 제약에서 벗어나, 람다 내부에서 외부 `var` 변수의 값을 읽고 직접 수정(Mutation)할 수 있습니다.
 */

// 1. 범용 리스트 필터링 모범 답안 고차 함수
private fun <T> filterList(items: List<T>, predicate: (T) -> Boolean): List<T> {
    val results = mutableListOf<T>()
    // 컬렉션을 순회하며 조건 람다식을 만족하는 요소만 수집
    for (item in items) {
        if (predicate(item)) {
            results.add(item)
        }
    }
    return results
}

// 2. 클로저를 활용한 예산 내 과일 선택 및 금액 누적 모범 답안 함수
private fun filterAndAccumulatePrice(fruits: List<Quiz17Fruit>, budget: Int): Pair<List<Quiz17Fruit>, Int> {
    // 람다 외부에서 가변 변수 선언
    var totalSpent = 0

    // 후행 람다 문법을 사용하여 filterList 호출
    // 람다 내부에서 외부의 가변 변수(totalSpent)를 포획(Capture)하고 값을 직접 변경
    val selectedFruits = filterList(fruits) { fruit ->
        if (totalSpent + fruit.price <= budget) {
            totalSpent += fruit.price // 외부 var 변수 갱신 (코틀린 클로저의 강력한 특징)
            true // 조건을 만족하므로 필터링 목록에 포함
        } else {
            false // 예산 초과 시 제외
        }
    }

    return Pair(selectedFruits, totalSpent)
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

    // 예산 3,000원: 사과(1000) + 바나나(2000) = 3000원
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

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 클로저(Closure)의 완전한 지원: 자바 람다는 `effectively final`(사실상 불변) 변수만 캡처할 수 있어 외부 가변 변수를 수정하려면 배열이나 `AtomicInteger` 같은 래퍼 객체로 감싸는 번거로움이 필요했습니다. 반면 코틀린은 컴파일러가 `Ref` 객체로 자동 감싸주어 `var` 변수도 자연스럽게 읽고 쓸 수 있습니다.
 * 2. 인터페이스 보일러플레이트 제거: 자바에서는 새로운 형태의 필터나 콜백이 생길 때마다 인터페이스를 선언하거나 복잡한 제네릭 람다를 선언해야 했지만, 코틀린은 `(T) -> Boolean`과 같은 직관적인 인라인 함수 타입으로 모든 것을 해결합니다.
 * 3. 자연스러운 후행 람다 문법: 마지막 매개변수를 람다로 받으면 메서드 호출 시 중괄호를 밖으로 빼낼 수 있어 빌더 패턴이나 제어 구조를 커스텀으로 확장하기에 매우 유리합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 람다 내부 상태 변경(Side Effect)의 남용: 람다 안에서 외부 `var`를 자유롭게 바꿀 수 있지만, 과도한 사이드 이펙트는 순수 함수형 프로그래밍의 장점(불변성, 예측 가능성)을 해치고 멀티스레드 환경에서 동시성 문제를 유발할 수 있으므로 꼭 필요한 경우에만 신중히 사용해야 합니다.
 * 2. 타입 추론 실패 시 매개변수 타입 누락: 람다의 파라미터 타입을 컴파일러가 추론하지 못하는 복잡한 상황에서는 `fruit: Quiz17Fruit ->`처럼 명시적으로 타입을 적어주어야 합니다.
 */
