package Lec17

/*
 * [핵심 해결 아이디어]
 * - 코틀린에서는 함수가 1급 시민이므로 변수에 함수 타입을 지정하여 람다식을 직접 할당할 수 있습니다.
 * - 람다식의 매개변수가 하나뿐인 경우 매개변수 선언을 생략하고 암시적 파라미터인 `it`을 사용하여 코드를 간결하게 작성할 수 있습니다.
 * - 변수에 할당된 함수 객체는 일반 함수처럼 `fn()` 형태로 호출하거나, 함수 인터페이스의 내장 메서드인 `fn.invoke()`를 통해 명시적으로 실행할 수 있습니다.
 * - 고차 함수는 함수 타입 매개변수(`(A, B) -> C`)를 선언하여 호출자로부터 연산 로직을 동적으로 위임받아 유연성을 극대화합니다.
 */

// 실습용 과일 도메인 데이터 클래스(Quiz17Fruit)는 Quiz01_Problem.kt에 선언된 모델을 공유합니다.

// 1. 비싼 과일 판별 모범 답안 람다 변수
private val isExpensiveFruit: (Quiz17Fruit) -> Boolean = {
    // 단일 파라미터 it을 활용하여 과일 가격이 3000원 이상인지 평가 (마지막 줄의 결과가 반환값)
    it.price >= 3_000
}

// 2. 두 정수와 연산 함수를 받아 실행하는 모범 답안 고차 함수
private fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    // 전달받은 연산 람다 함수를 호출하여 결과를 즉시 반환
    return operation(a, b)
}

// 3. 람다를 소괄호 및 invoke 두 가지 방식으로 호출하는 모범 답안 함수
private fun executeTwice(message: String, action: (String) -> String): Pair<String, String> {
    // 첫 번째 호출 방식: 일반 함수 호출과 동일한 소괄호 방식
    val first = action(message)
    // 두 번째 호출 방식: 함수 객체의 invoke() 메서드를 통한 명시적 호출
    val second = action.invoke(message)

    // 두 실행 결과를 Pair로 묶어 반환
    return Pair(first, second)
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

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 1급 시민 지원: 자바에서는 람다를 사용하기 위해 반드시 `@FunctionalInterface` 인터페이스(Predicate, Function 등)가 필요했으나, 코틀린은 언어 레벨에서 `(T) -> R` 함수 타입을 기본 지원하므로 별도의 단일 메서드 인터페이스를 매번 정의할 필요가 없습니다.
 * 2. 간결한 `it` 키워드: 자바에서는 `fruit -> fruit.getPrice() >= 3000`처럼 파라미터 이름을 일일이 작성해야 하지만, 코틀린은 단일 파라미터에 대해 `it` 키워드를 제공하여 코드가 훨씬 간결해집니다.
 * 3. 후행 람다(Trailing Lambda): 마지막 매개변수가 함수인 경우 `calculate(10, 20) { a, b -> a + b }`처럼 소괄호 바깥으로 람다 블록을 뺄 수 있어 DSL(Domain Specific Language)과 같은 뛰어난 가독성을 제공합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 람다 내부에서 명시적 `return` 사용: 람다식 블록 내부에서 단순히 `return value`를 쓰면 람다를 빠져나가는 것이 아니라 람다를 감싸고 있는 외부 함수 전체가 리턴되는 비지역 반환(Non-local return)이 발생하거나 컴파일 에러가 발생할 수 있습니다. 람다의 반환값은 항상 마지막 라인의 표현식 평가 결과로 전달해야 합니다.
 * 2. `it`의 중첩 사용 혼란: 람다 내부에서 또 다른 람다를 중첩할 때 양쪽 모두 `it`을 쓰면 어떤 스코프의 `it`인지 모호해집니다. 중첩 람다에서는 명시적으로 파라미터 이름(예: `fruit ->`)을 지정하는 것이 권장됩니다.
 */
