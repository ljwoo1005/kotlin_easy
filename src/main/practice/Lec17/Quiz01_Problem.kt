package Lec17

/*
 * [학습 목표 & 복습 개념]
 * - 코틀린에서 함수는 변수에 할당되거나 파라미터로 전달될 수 있는 '1급 시민(First-Class Citizen)'임을 체화합니다.
 * - 함수 타입 선언 문법 `(파라미터타입) -> 반환타입`을 이해하고, 중괄호(`{ }`) 기반의 람다식을 작성합니다.
 * - 람다식의 매개변수가 단 하나일 때 사용할 수 있는 `it` 키워드의 용법을 익힙니다.
 * - 변수에 할당된 함수를 호출하는 두 가지 방식인 소괄호 직접 호출(`fn()`)과 `invoke()` 메서드 호출(`fn.invoke()`)을 실습합니다.
 * - 함수를 파라미터로 넘겨받는 기초적인 고차 함수(Higher-Order Function)를 구현합니다.
 *
 * [문제 설명]
 * 과일 정보와 정수 연산을 다루는 3가지 람다 및 고차 함수 기능을 완성하세요.
 *
 * 1. isExpensiveFruit: (Quiz17Fruit) -> Boolean
 *    - 과일 객체(Quiz17Fruit)를 받아 가격이 3,000원 이상이면 true, 미만이면 false를 반환하는 람다식 변수입니다.
 *    - 단일 매개변수를 참조할 때 `it` 키워드를 활용하세요.
 *
 * 2. calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int
 *    - 두 정수 `a`, `b`와 두 정수를 받아 정수를 반환하는 함수 `operation`을 전달받아, 연산 결과를 반환하는 고차 함수입니다.
 *
 * 3. executeTwice(message: String, action: (String) -> String): Pair<String, String>
 *    - 문자열 `message`와 문자열 변환 함수 `action`을 전달받습니다.
 *    - 동일한 `action` 함수를 두 번 실행하되, 첫 번째는 소괄호 직접 호출(`action(message)`), 두 번째는 `action.invoke(message)` 방식으로 호출하여 두 결과를 `Pair`로 묶어 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - isExpensiveFruit: val 변수에 `(Quiz17Fruit) -> Boolean` 함수 타입으로 선언하세요.
 * - calculate: fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int 시그니처를 사용하세요.
 * - executeTwice: fun executeTwice(message: String, action: (String) -> String): Pair<String, String> 시그니처를 사용하세요.
 * - executeTwice: 내부에서 반드시 소괄호 호출(`action(...)`)과 invoke 호출(`action.invoke(...)`)을 각각 1회씩 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - isExpensiveFruit(Quiz17Fruit("사과", 1000))       -> false
 * - isExpensiveFruit(Quiz17Fruit("수박", 10000))      -> true
 * - calculate(10, 20) { a, b -> a + b }             -> 30
 * - calculate(10, 20) { a, b -> a * b }             -> 200
 * - executeTwice("Kotlin") { "Hello, $it" }          -> Pair("Hello, Kotlin", "Hello, Kotlin")
 *
 * [💡 HINT]
 * - 코틀린에서 매개변수가 하나뿐인 람다는 파라미터 이름을 명시하지 않고 `it`으로 접근할 수 있습니다.
 * - 코틀린에서 함수는 일반 객체처럼 취급되므로, 변수명 뒤에 `.invoke()`를 붙여 명시적으로 호출할 수도 있습니다.
 * - 고차 함수의 마지막 파라미터가 함수 타입인 경우, 호출부에서 소괄호 바깥으로 중괄호 람다를 뺄 수 있습니다(후행 람다 문법).
 */

// 실습용 과일 도메인 데이터 클래스
data class Quiz17Fruit(
    val name: String,
    val price: Int
)

// 1. 비싼 과일 판별 람다 변수 뼈대
private val isExpensiveFruit: (Quiz17Fruit) -> Boolean = {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 2. 두 정수와 연산 함수를 받아 실행하는 고차 함수 뼈대
private fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 3. 람다를 소괄호 및 invoke 두 가지 방식으로 호출하는 함수 뼈대
private fun executeTwice(message: String, action: (String) -> String): Pair<String, String> {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

fun main() {
    // 1. isExpensiveFruit 검증
    val apple = Quiz17Fruit("사과", 1_000)
    val banana = Quiz17Fruit("바나나", 3_000)
    val watermelon = Quiz17Fruit("수박", 10_000)

    check(!isExpensiveFruit(apple)) { "1,000원 사과는 비싼 과일이 아니어야 합니다." }
    check(isExpensiveFruit(banana)) { "3,000원 바나나는 비싼 과일이어야 합니다." }
    check(isExpensiveFruit(watermelon)) { "10,000원 수박은 비싼 과일이어야 합니다." }

    // 2. calculate 검증
    val sumResult = calculate(10, 20) { x, y -> x + y }
    check(sumResult == 30) { "10 + 20의 결과는 30이어야 합니다." }

    val multiplyResult = calculate(5, 6) { x, y -> x * y }
    check(multiplyResult == 30) { "5 * 6의 결과는 30이어야 합니다." }

    val minusResult = calculate(100, 40) { x, y -> x - y }
    check(minusResult == 60) { "100 - 40의 결과는 60이어야 합니다." }

    // 3. executeTwice 검증
    val greetingPair = executeTwice("Kotlin") { "Hello, $it!" }
    check(greetingPair.first == "Hello, Kotlin!") { "첫 번째 호출 결과는 'Hello, Kotlin!'이어야 합니다." }
    check(greetingPair.second == "Hello, Kotlin!") { "두 번째(invoke) 호출 결과는 'Hello, Kotlin!'이어야 합니다." }

    val upperPair = executeTwice("quiz") { it.uppercase() }
    check(upperPair.first == "QUIZ" && upperPair.second == "QUIZ") { "대문자 변환 결과가 일치해야 합니다." }

    println("✅ Quiz01 테스트 통과! (함수 1급 시민 및 람다 기본기 체화 완료)")
}
