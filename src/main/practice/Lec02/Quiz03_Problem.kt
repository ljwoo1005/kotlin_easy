package Lec02

/*
 * [학습 목표 & 복습 개념]
 * - 다단계 참조 객체 모델에서 연쇄 Safe Call(?.)을 통한 안전한 프로퍼티 접근을 체화합니다.
 * - Elvis 연산자를 활용한 함수 조기 반환(Early Return, `?: return`) 패턴을 학습합니다.
 * - Not-null assertion(!!)의 위험성을 이해하고, 안전한 예외 처리(`?: throw`)로 대체하는 모범 사례를 복습합니다.
 *
 * [문제 설명]
 * 온라인 쇼핑몰 주문 시스템의 세 가지 비즈니스 로직 함수를 완성하세요.
 *
 * 1. getUserEmailDomain(order: Quiz03Order?): String
 *    - 주문(order) -> 고객(user) -> 이메일(email)을 연쇄 탐색하여 도메인(예: "gmail.com")을 추출합니다.
 *    - order가 null이거나, user가 null이거나, email이 null인 경우 "UNKNOWN"을 반환합니다.
 *    - email에 "@"가 포함되지 않은 경우에도 "UNKNOWN"을 반환합니다.
 *
 * 2. calculateTotalPayment(order: Quiz03Order?): Int
 *    - 주문(order)이 null인 경우 계산을 진행하지 않고 즉시 0을 반환합니다 (Elvis Early Return 활용).
 *    - 상품 금액(itemPrice)에서 쿠폰 할인액(couponDiscount)을 차감하여 최종 결제액을 계산합니다.
 *    - couponDiscount가 null인 경우 할인이 없는 것(0원 할인)으로 처리합니다.
 *    - 최종 결제액이 0원 미만인 경우 0원을 반환합니다.
 *
 * 3. getRequiredUserName(order: Quiz03Order?): String
 *    - 결제 영수증 발행을 위해 주문자 이름이 필수적으로 요구됩니다.
 *    - order가 null이거나 user가 null인 경우 `IllegalStateException("주문 고객 정보가 누락되었습니다.")` 예외를 던집니다.
 *    - 정상적인 경우 고객의 이름을 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - 널 아님 단언(!!)을 사용하지 마세요. (NPE 유발 금지)
 * - calculateTotalPayment 함수는 Elvis 연산자를 활용한 Early Return(`order ?: return 0`) 패턴을 반드시 적용하세요.
 * - getRequiredUserName 함수는 `?: throw IllegalStateException(...)`을 활용하여 작성하세요.
 *
 * [입출력 및 기대 결과]
 * - getUserEmailDomain(정상주문 with "user@kotlin.org") -> "kotlin.org"
 * - getUserEmailDomain(null)                           -> "UNKNOWN"
 * - calculateTotalPayment(Quiz03Order(..., 10000, 3000)) -> 7000
 * - calculateTotalPayment(Quiz03Order(..., 10000, null)) -> 10000
 * - calculateTotalPayment(null)                          -> 0
 * - getRequiredUserName(정상주문 with "홍길동")           -> "홍길동"
 * - getRequiredUserName(null)                            -> IllegalStateException 발생
 *
 * [💡 HINT]
 * - 연쇄 Safe Call: `order?.user?.email` 형태로 중간 단계가 null이면 전체가 안전하게 null이 됩니다.
 * - 문자열의 특정 구분자 뒷부분은 `substringAfter("@")` 메서드를 활용할 수 있습니다.
 * - `val currentOrder = order ?: return 0` 구문을 사용하면 그 아래 줄부터는 `currentOrder`가 Non-null 타입으로 스마트 캐스트됩니다.
 */

// 실습용 도메인 데이터 클래스
data class Quiz03User(
    val name: String,
    val email: String?
)

data class Quiz03Order(
    val orderId: Long,
    val user: Quiz03User?,
    val itemPrice: Int,
    val couponDiscount: Int?
)

// 1. 사용자 이메일 도메인 추출 함수 뼈대
private fun getUserEmailDomain(order: Quiz03Order?): String {
    TODO()
}

// 2. 최종 결제 금액 계산 함수 뼈대 (Elvis Early Return 활용)
private fun calculateTotalPayment(order: Quiz03Order?): Int {
    TODO()
}

// 3. 필수 주문자 이름 조회 함수 뼈대
private fun getRequiredUserName(order: Quiz03Order?): String {
    TODO()
}

fun main() {
    val normalUser = Quiz03User("김코틀린", "developer@kotlinlang.org")
    val noEmailUser = Quiz03User("이자바", null)
    val normalOrder = Quiz03Order(101L, normalUser, 30000, 5000)
    val noCouponOrder = Quiz03Order(102L, noEmailUser, 20000, null)
    val noUserOrder = Quiz03Order(103L, null, 15000, null)

    // 1. getUserEmailDomain 검증
    check(getUserEmailDomain(normalOrder) == "kotlinlang.org") { "도메인은 kotlinlang.org이어야 합니다. (현재: ${getUserEmailDomain(normalOrder)})" }
    check(getUserEmailDomain(noCouponOrder) == "UNKNOWN") { "이메일이 null이면 UNKNOWN이어야 합니다. (현재: ${getUserEmailDomain(noCouponOrder)})" }
    check(getUserEmailDomain(noUserOrder) == "UNKNOWN") { "유저가 null이면 UNKNOWN이어야 합니다. (현재: ${getUserEmailDomain(noUserOrder)})" }
    check(getUserEmailDomain(null) == "UNKNOWN") { "주문이 null이면 UNKNOWN이어야 합니다. (현재: ${getUserEmailDomain(null)})" }

    // 2. calculateTotalPayment 검증
    check(calculateTotalPayment(normalOrder) == 25000) { "30000원에서 5000원 할인된 25000원이어야 합니다. (현재: ${calculateTotalPayment(normalOrder)})" }
    check(calculateTotalPayment(noCouponOrder) == 20000) { "쿠폰이 null이면 20000원이어야 합니다. (현재: ${calculateTotalPayment(noCouponOrder)})" }
    check(calculateTotalPayment(null) == 0) { "주문이 null이면 0원이어야 합니다. (현재: ${calculateTotalPayment(null)})" }

    // 3. getRequiredUserName 검증
    check(getRequiredUserName(normalOrder) == "김코틀린") { "주문자 이름은 '김코틀린'이어야 합니다. (현재: ${getRequiredUserName(normalOrder)})" }
    val noUserExceptionThrown = runCatching { getRequiredUserName(noUserOrder) }.isFailure
    check(noUserExceptionThrown) { "유저가 null인 주문은 IllegalStateException을 발생시켜야 합니다." }
    val nullOrderExceptionThrown = runCatching { getRequiredUserName(null) }.isFailure
    check(nullOrderExceptionThrown) { "null 주문은 IllegalStateException을 발생시켜야 합니다." }

    println("✅ Quiz03 테스트 통과! (실전 비즈니스 시나리오 검증 성공)")
}
