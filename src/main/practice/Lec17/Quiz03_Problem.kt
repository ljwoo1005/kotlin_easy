package Lec17

/*
 * [학습 목표 & 복습 개념]
 * - 강의 4절에서 살펴본 코틀린 표준 라이브러리의 `Closeable.use` 고차 함수 원리를 직접 구현해 봄으로써 자원 관리 패턴을 체화합니다.
 * - `try-finally`와 람다 매개변수(`block: (T) -> R`)를 결합하여 예외 발생 여부와 상관없이 자원을 100% 안전하게 해제하는 세션 래퍼 고차 함수를 작성합니다.
 * - 비즈니스 할인 정책을 람다 함수 파라미터(`(Quiz17CartItem) -> Int`)로 주입받아 유연하게 계산하는 장바구니 결제 로직을 구현합니다.
 * - 후행 람다(Trailing Lambda)를 통해 마치 맞춤형 제어문처럼 읽히는 코틀린의 관용적(Idiomatic) 스타일을 체화합니다.
 *
 * [문제 설명]
 * 전자상거래 시스템의 주문 세션 안전 관리 및 장바구니 할인 계산 기능을 구현하세요.
 *
 * 1. withOrderSession<R>(sessionId: String, block: (Quiz17OrderSession) -> R): R
 *    - 주어진 `sessionId`로 `Quiz17OrderSession`을 생성한 뒤, `block` 람다를 실행하고 그 결과를 반환하는 고차 함수입니다.
 *    - `block` 람다가 정상적으로 종료되든, 내부에서 예외를 던지든 관계없이 **반드시 세션이 종료(`session.close()`)**되도록 보장해야 합니다.
 *    - 람다 실행 도중 예외가 발생하면 세션을 닫은 후 해당 예외를 외부로 다시 던져야(rethrow) 합니다.
 *
 * 2. calculateFinalAmount(items: List<Quiz17CartItem>, discountPolicy: (Quiz17CartItem) -> Int): Int
 *    - 장바구니에 담긴 상품 리스트와 상품별 할인 금액을 계산하는 람다 함수 `discountPolicy`를 전달받습니다.
 *    - 각 상품마다 `discountPolicy(item)`을 호출하여 할인액을 구한 후, `상품 원가 - 할인액`을 최종 결제 금액에 합산합니다.
 *    - 할인액이 원가보다 큰 경우 해당 상품의 결제 금액은 0원이며(음수 방지: `maxOf(0, item.price - discount)`), 할인액이 음수인 경우 0원으로 간주합니다.
 *
 * [요구사항 & 제약조건]
 * - withOrderSession: 제네릭 함수 `fun <R> withOrderSession(sessionId: String, block: (Quiz17OrderSession) -> R): R` 시그니처를 사용하세요.
 * - withOrderSession: `try-finally` 블록을 사용하거나 세션의 `use` 함수를 활용하여 세션의 안전한 닫힘을 100% 보장하세요.
 * - calculateFinalAmount: `fun calculateFinalAmount(items: List<Quiz17CartItem>, discountPolicy: (Quiz17CartItem) -> Int): Int` 시그니처를 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - withOrderSession("SES-001") { session -> session.processOrder("ORD-100") } -> "ORDER_ORD-100_PROCESSED" (실행 후 session.isOpen == false)
 * - val items = listOf(Quiz17CartItem("노트북", 1_000_000, "전자기기"), Quiz17CartItem("마우스", 50_000, "전자기기"))
 * - calculateFinalAmount(items) { 10_000 } -> 1,030,000 (각각 1만원 할인)
 * - calculateFinalAmount(items) { if (it.category == "전자기기") it.price * 10 / 100 else 0 } -> 945,000 (10% 할인)
 *
 * [💡 HINT]
 * - `AutoCloseable`을 구현한 클래스는 `try { block(session) } finally { session.close() }` 패턴 또는 코틀린 내장 `use`를 통해 안전하게 닫을 수 있습니다.
 * - 상품별 결제액 계산 시 `maxOf(0, item.price - maxOf(0, discount))`를 활용하면 음수 할인을 방지할 수 있습니다.
 */

// 실습용 주문 세션 클래스 (AutoCloseable 구현)
class Quiz17OrderSession(val sessionId: String) : AutoCloseable {
    var isOpen: Boolean = true
        private set

    fun processOrder(orderId: String): String {
        if (!isOpen) {
            throw IllegalStateException("세션($sessionId)이 닫혀 있어 주문을 처리할 수 없습니다.")
        }
        return "ORDER_${orderId}_PROCESSED"
    }

    override fun close() {
        isOpen = false
    }
}

// 실습용 장바구니 상품 데이터 클래스
data class Quiz17CartItem(
    val name: String,
    val price: Int,
    val category: String
)

// 1. 주문 세션 안전 관리 고차 함수 뼈대
private fun <R> withOrderSession(sessionId: String, block: (Quiz17OrderSession) -> R): R {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 2. 할인 정책 람다를 적용한 최종 결제 금액 계산 함수 뼈대
private fun calculateFinalAmount(
    items: List<Quiz17CartItem>,
    discountPolicy: (Quiz17CartItem) -> Int
): Int {
    // TODO: 여기에 코드를 작성하세요
    TODO()
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
    // 노트북: 990,000 / 키보드: 90,000 / 사과: 20,000 => 합계 1,100,000원
    check(fixedDiscountTotal == 1_100_000) { "고정 할인 적용 총액은 1,100,000원이어야 합니다. (실제: $fixedDiscountTotal)" }

    // 전자제품만 20% 할인 정책
    val categoryDiscountTotal = calculateFinalAmount(cart) { item ->
        if (item.category == "ELECTRONICS") item.price * 20 / 100 else 0
    }
    // 노트북: 800,000 / 키보드: 80,000 / 사과: 30,000 => 합계 910,000원
    check(categoryDiscountTotal == 910_000) { "카테고리 할인 적용 총액은 910,000원이어야 합니다. (실제: $categoryDiscountTotal)" }

    // 가격보다 큰 과도한 할인(1,500,000원) 시 음수 방지 검증
    val overDiscountTotal = calculateFinalAmount(listOf(Quiz17CartItem("지우개", 500, "OFFICE"))) { 1_000 }
    check(overDiscountTotal == 0) { "할인액이 가격보다 클 경우 0원이어야 합니다." }

    // 빈 장바구니 검증
    val emptyTotal = calculateFinalAmount(emptyList()) { 5_000 }
    check(emptyTotal == 0) { "빈 장바구니의 결제 금액은 0원이어야 합니다." }

    println("✅ Quiz03 테스트 통과! (안전 자원 관리 고차 함수 및 비즈니스 람다 체화 완료)")
}
