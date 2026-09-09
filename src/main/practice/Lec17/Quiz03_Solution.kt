package Lec17

/*
 * [핵심 해결 아이디어]
 * - `Closeable.use`는 코틀린 표준 라이브러리에서 try-with-resources를 대체하기 위해 제공되는 인라인 고차 확장 함수입니다.
 * - 본 솔루션에서는 `try-finally` 블록 내에서 람다(`block(session)`)를 실행함으로써 예외 발생 유무에 상관없이 리소스 해제(`close()`)를 보장하는 '자원 관리 고차 함수' 패턴을 직접 구현했습니다.
 * - `calculateFinalAmount`는 전략 패턴(Strategy Pattern)을 별도의 전략 클래스 없이 `discountPolicy: (Quiz17CartItem) -> Int` 람다 함수 타입 하나만으로 깔끔하게 대체하여 높은 유연성을 확보했습니다.
 */

// 실습용 도메인 클래스(Quiz17OrderSession, Quiz17CartItem)는 Quiz03_Problem.kt에 선언된 모델을 공유합니다.

// 1. 주문 세션 안전 관리 모범 답안 고차 함수
private fun <R> withOrderSession(sessionId: String, block: (Quiz17OrderSession) -> R): R {
    // 세션 인스턴스 생성
    val session = Quiz17OrderSession(sessionId)
    return try {
        // 전달받은 비즈니스 로직 람다를 실행하고 그 결과를 반환
        block(session)
    } finally {
        // 정상 완료되거나 예외가 발생하더라도 반드시 자원을 안전하게 해제
        session.close()
    }
}

// 2. 할인 정책 람다를 적용한 최종 결제 금액 계산 모범 답안 함수
private fun calculateFinalAmount(
    items: List<Quiz17CartItem>,
    discountPolicy: (Quiz17CartItem) -> Int
): Int {
    var totalAmount = 0

    // 각 장바구니 상품을 순회하며 할인 정책 람다 적용
    for (item in items) {
        // 음수 할인액에 대한 방어 처리 (할인액이 음수이면 0으로 처리)
        val discount = maxOf(0, discountPolicy(item))
        // 할인액이 원가보다 클 경우 결제 금액은 0원(음수 방지)
        val discountedPrice = maxOf(0, item.price - discount)
        totalAmount += discountedPrice
    }

    return totalAmount
}

fun main() {
    // 1. withOrderSession 정상 동작 및 자동 세션 종료 검증
    var capturedSession: Quiz17OrderSession? = null
    val result = withOrderSession("SESSION_A") { session ->
        capturedSession = session
        check(session.isOpen) { "블록 실행 중에는 세션이 열려 있어야 합니다." }
        session.processOrder("ORDER_123")
    }

    check(result == "ORDER_ORDER_123_PROCESSED") { "정상 주문 처리 결과가 올바르지 않습니다." }
    check(capturedSession != null && !capturedSession!!.isOpen) { "withOrderSession 블록 종료 후 세션이 반드시 닫혀 있어야 합니다." }

    // 2. withOrderSession 예외 발생 시 안전 종료 검증
    var errorSession: Quiz17OrderSession? = null
    val exceptionThrown = runCatching {
        withOrderSession("SESSION_FAIL") { session ->
            errorSession = session
            throw RuntimeException("주문 결제 중 네트워크 장애 발생!")
        }
    }.isFailure

    check(exceptionThrown) { "블록 내부에서 발생한 예외가 외부로 전파되어야 합니다." }
    check(errorSession != null && !errorSession!!.isOpen) { "예외가 발생하더라도 세션은 반드시 닫혀 있어야 합니다." }

    // 3. calculateFinalAmount 및 후행 람다 할인 정책 검증
    val cart = listOf(
        Quiz17CartItem("노트북", 1_000_000, "ELECTRONICS"),
        Quiz17CartItem("키보드", 100_000, "ELECTRONICS"),
        Quiz17CartItem("사과 한 박스", 30_000, "FOOD")
    )

    // 고정 10,000원 할인 정책
    val fixedDiscountTotal = calculateFinalAmount(cart) { 10_000 }
    check(fixedDiscountTotal == 1_100_000) { "고정 할인 적용 총액은 1,100,000원이어야 합니다. (실제: $fixedDiscountTotal)" }

    // 전자제품만 20% 할인 정책
    val categoryDiscountTotal = calculateFinalAmount(cart) { item ->
        if (item.category == "ELECTRONICS") item.price * 20 / 100 else 0
    }
    check(categoryDiscountTotal == 910_000) { "카테고리 할인 적용 총액은 910,000원이어야 합니다. (실제: $categoryDiscountTotal)" }

    // 가격보다 큰 과도한 할인(1,500,000원) 시 음수 방지 검증
    val overDiscountTotal = calculateFinalAmount(listOf(Quiz17CartItem("지우개", 500, "OFFICE"))) { 1_000 }
    check(overDiscountTotal == 0) { "할인액이 가격보다 클 경우 0원이어야 합니다." }

    // 빈 장바구니 검증
    val emptyTotal = calculateFinalAmount(emptyList()) { 5_000 }
    check(emptyTotal == 0) { "빈 장바구니의 결제 금액은 0원이어야 합니다." }

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 세련된 리소스 라이프사이클 관리: 자바에서는 `try (OrderSession s = new OrderSession()) { ... }`처럼 문법적 구문에 묶여 있었지만, 코틀린에서는 `withOrderSession { session -> ... }`처럼 고차 함수를 활용하여 세션 획득, 트랜잭션 시작/커밋, 롤백 및 자원 해제 전 과정을 캡슐화한 커스텀 블록 함수를 손쉽게 제작할 수 있습니다.
 * 2. 전략 패턴(Strategy Pattern)의 극적인 단순화: 다양한 할인 정책을 지원하기 위해 `DiscountPolicy` 인터페이스와 무수한 구현 클래스를 만드는 대신, 람다 파라미터(`(Item) -> Int`) 하나로 호출 시점에 인라인으로 다양한 비즈니스 정책을 전달할 수 있습니다.
 * 3. DSL 스타일 가독성: 후행 람다를 활용하면 비즈니스 로직이 설정 코드와 분리되어 마치 자연어 명령처럼 직관적으로 읽힙니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 자원 해제 누락: `try-finally`를 설계할 때 `block(session)` 호출 전후에 예외가 발생하거나 `finally` 블록을 누락하면 자원 누수가 발생할 수 있으므로, 검증된 `Closeable.use` 확장 함수나 완벽한 `try-finally` 구조를 준수해야 합니다.
 * 2. 람다의 예외 은폐(Exception Swallowing): 고차 함수 내부에서 `runCatching`이나 빈 `catch` 블록으로 람다가 던진 비즈니스 예외를 삼켜버리면 상위 호출자가 문제를 인지할 수 없으므로, 자원 해제 후 예외가 정상적으로 전파되도록 설계해야 합니다.
 */
