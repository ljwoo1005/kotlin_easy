package Lec02

/*
 * [핵심 해결 아이디어]
 * - 중첩 객체의 깊은 참조(order -> user -> email) 시, 중간에 어느 하나라도 null일 수 있다면
 *   'order?.user?.email'처럼 연쇄 Safe Call을 연결하여 단 한 번에 안전하게 null을 방어합니다.
 * - Elvis 연산자를 Early Return(`val currentOrder = order ?: return 0`)으로 활용하면, 이후 스코프에서
 *   'currentOrder'가 컴파일러에 의해 Non-null 타입으로 스마트 캐스트되어 안전하고 직관적인 코드를 작성할 수 있습니다.
 * - 필수 값이 null일 때 무분별하게 '!!'를 쓰면 NPE가 발생하여 원인 파악이 어려우므로,
 *   'order?.user?.name ?: throw IllegalStateException(...)' 형태로 명확한 예외 메시지를 지정하는 것이 모범 사례입니다.
 */

// 실습용 도메인 데이터 클래스(Quiz03User, Quiz03Order)는 Quiz03_Problem.kt에 정의된 모델을 사용합니다.

// 1. 사용자 이메일 도메인 추출 모범 답안 (연쇄 Safe Call 활용)
private fun getUserEmailDomain(order: Quiz03Order?): String {
    // 연쇄 Safe Call로 이메일을 안전하게 가져오며, null이면 UNKNOWN 반환
    val email = order?.user?.email ?: return "UNKNOWN"
    // email에 '@' 기호가 존재하는지 확인 후 도메인 반환
    return if (email.contains("@")) {
        email.substringAfter("@")
    } else {
        "UNKNOWN"
    }
}

// 2. 최종 결제 금액 계산 모범 답안 (Elvis Early Return 패턴 적용)
private fun calculateTotalPayment(order: Quiz03Order?): Int {
    // 1. 주문이 null인 경우 조기 반환 (이후 currentOrder는 스마트 캐스트됨)
    val currentOrder = order ?: return 0

    // 2. 쿠폰 할인이 null이면 0원으로 처리
    val discount = currentOrder.couponDiscount ?: 0

    // 3. 할인 차감 후 최소 0원 보장
    val finalPrice = currentOrder.itemPrice - discount
    return maxOf(0, finalPrice)
}

// 3. 필수 주문자 이름 조회 모범 답안 (!! 대신 안전한 예외 발생)
private fun getRequiredUserName(order: Quiz03Order?): String {
    // 연쇄 Safe Call과 Elvis 연산자로 안전하게 주문 고객 이름 획득 또는 예외 발생
    return order?.user?.name ?: throw IllegalStateException("주문 고객 정보가 누락되었습니다.")
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

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 깊은 참조의 간결성: 자바에서는 'order != null && order.getUser() != null && order.getUser().getEmail() != null'
 *    처럼 다단계 null 체크 피라미드가 형성되지만, 코틀린은 'order?.user?.email' 한 줄로 깔끔하게 해결됩니다.
 * 2. Early Return과 스마트 캐스트: 'val currentOrder = order ?: return 0'을 통과한 변수는 컴파일러가
 *    Non-null 타입으로 자동 인지하므로, 이후 안전하게 프로퍼티를 직접 참조할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. '!!' (Not-null assertion)의 무분별한 사용: 'order!!.user!!.name'처럼 작성하면 데이터가 단 하나라도
 *    누락되었을 때 설명 없는 NullPointerException이 런타임에 터지게 됩니다. 비즈니스 예외 메시지를 담은
 *    '?: throw IllegalStateException("...")'을 사용하는 것이 안전합니다.
 * 2. 플랫폼 타입 과신: 자바 라이브러리/코드를 연동할 때 null 관련 애노테이션이 없다면 컴파일러가 null 검사를 강제하지 않으므로,
 *    코틀린 단에서 방어적으로 Nullable 취급하거나 Safe Call을 적용해야 런타임 장애를 막을 수 있습니다.
 */
