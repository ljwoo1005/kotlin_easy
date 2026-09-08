package Lec04

/*
 * [학습 목표 & 복습 개념]
 * - 비즈니스 도메인 객체에 산술 연산자 오버로딩(`operator fun plus`, `operator fun minus`)을 정의하고 `+`, `-` 기호로 계산합니다.
 * - 코틀린의 특이 연산자인 범위 연산자(`..`)와 포함 연산자(`in`)를 활용하여 특정 금액이 예산 범위에 속하는지 판정합니다.
 * - 인덱싱 연산자 오버로딩(`operator fun get`)을 정의하여 장바구니 품목을 대괄호(`[]`)로 직관적으로 조회합니다.
 *
 * [문제 설명]
 * 이커머스 장바구니 및 결제 정산 시스템에서 상품 가격을 계산하고 예산을 검증하는 3개 함수를 완성하세요.
 *
 * 1. calculateFinalPrice(original: Quiz03Price, discount: Quiz03Price, deliveryFee: Quiz03Price): Quiz03Price
 *    - 상품 원가(original)에서 할인 금액(discount)을 차감하고, 배송비(deliveryFee)를 가산한 최종 결제 금액을 반환합니다.
 *    - Quiz03Price에 오버로딩된 `-` 및 `+` 연산자를 활용하여 간결하게 계산하세요.
 *
 * 2. isInBudget(price: Quiz03Price, minBudget: Quiz03Price, maxBudget: Quiz03Price): Boolean
 *    - 결제 금액(price)이 최소 예산(minBudget) 이상 최대 예산(maxBudget) 이하의 범위에 속하는지 판정합니다.
 *    - 코틀린의 범위 연산자(`..`)와 포함 연산자(`in`)를 사용하여 단일 표현식으로 구현하세요.
 *
 * 3. getItemPrice(cart: Quiz03Cart, index: Int): Quiz03Price
 *    - 장바구니(cart)에서 지정된 순번(index)의 상품 가격을 조회하여 반환합니다.
 *    - Quiz03Cart에 오버로딩된 인덱싱 연산자(`cart[index]`)를 활용하세요.
 *
 * [요구사항 & 제약조건]
 * - calculateFinalPrice 함수는 `plus`, `minus` 메서드를 직접 호출하지 말고 반드시 `-`, `+` 연산자를 사용하세요.
 * - isInBudget 함수는 if 조건문 대신 `in`과 `..` 연산자를 사용하세요.
 * - getItemPrice 함수는 `cart.items.get(index)` 대신 인덱싱 연산자 `cart[index]`를 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - calculateFinalPrice(Quiz03Price(10000L), Quiz03Price(3000L), Quiz03Price(2500L)) -> Quiz03Price(9500L)
 * - calculateFinalPrice(Quiz03Price(2000L), Quiz03Price(5000L), Quiz03Price(1000L))  -> Quiz03Price(1000L) (차감 시 0원 이하 방지)
 * - isInBudget(Quiz03Price(5000L), Quiz03Price(1000L), Quiz03Price(10000L))         -> true
 * - isInBudget(Quiz03Price(15000L), Quiz03Price(1000L), Quiz03Price(10000L))        -> false
 * - getItemPrice(Quiz03Cart(listOf(Quiz03Price(1000L), Quiz03Price(2000L))), 0)      -> Quiz03Price(1000L)
 *
 * [💡 HINT]
 * - `Comparable` 인터페이스가 구현된 클래스는 `a..b` 형태로 `ClosedRange`를 생성할 수 있으며, `x in a..b`로 범위 포함 여부를 검사할 수 있습니다.
 * - 클래스 내부에 `operator fun get(index: Int)`를 선언하면 인스턴스에 `인스턴스[index]` 형태로 접근할 수 있습니다.
 */

// 실습용 가격 도메인 클래스 (Comparable 및 +,- 연산자 오버로딩)
data class Quiz03Price(
    val value: Long
) : Comparable<Quiz03Price> {
    override fun compareTo(other: Quiz03Price): Int = this.value.compareTo(other.value)

    // '+' 연산자 오버로딩: 두 금액 합산
    operator fun plus(other: Quiz03Price): Quiz03Price = Quiz03Price(this.value + other.value)

    // '-' 연산자 오버로딩: 차감 후 금액이 음수가 되지 않도록 maxOf(0L, ...) 처리
    operator fun minus(other: Quiz03Price): Quiz03Price = Quiz03Price(maxOf(0L, this.value - other.value))
}

// 실습용 장바구니 클래스 ([] 인덱싱 연산자 오버로딩)
data class Quiz03Cart(
    val items: List<Quiz03Price>
) {
    // 대괄호 '[]' 인덱싱 연산자 오버로딩
    operator fun get(index: Int): Quiz03Price = items[index]
}

// 1. 최종 결제 금액 계산 함수 뼈대
private fun calculateFinalPrice(
    original: Quiz03Price,
    discount: Quiz03Price,
    deliveryFee: Quiz03Price
): Quiz03Price {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 2. 예산 범위 판정 함수 뼈대
private fun isInBudget(
    price: Quiz03Price,
    minBudget: Quiz03Price,
    maxBudget: Quiz03Price
): Boolean {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 3. 장바구니 품목 조회 함수 뼈대
private fun getItemPrice(cart: Quiz03Cart, index: Int): Quiz03Price {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

fun main() {
    // 1. calculateFinalPrice 검증 (학습자 미구현 시 NotImplementedError 발생)
    val price1 = calculateFinalPrice(Quiz03Price(10000L), Quiz03Price(3000L), Quiz03Price(2500L))
    check(price1 == Quiz03Price(9500L)) { "10000원에서 3000원 할인 후 배송비 2500원이면 9500원이어야 합니다. (현재: $price1)" }

    val price2 = calculateFinalPrice(Quiz03Price(2000L), Quiz03Price(5000L), Quiz03Price(1000L))
    check(price2 == Quiz03Price(1000L)) { "할인이 원가보다 커도 차감 후 0원이 되고 배송비 1000원이 가산되어 1000원이어야 합니다. (현재: $price2)" }

    // 2. isInBudget 검증
    check(isInBudget(Quiz03Price(5000L), Quiz03Price(1000L), Quiz03Price(10000L))) { "5000원은 1000원~10000원 예산 내에 포함되어야 합니다." }
    check(isInBudget(Quiz03Price(1000L), Quiz03Price(1000L), Quiz03Price(10000L))) { "경계값 1000원은 예산 내에 포함되어야 합니다." }
    check(isInBudget(Quiz03Price(10000L), Quiz03Price(1000L), Quiz03Price(10000L))) { "경계값 10000원은 예산 내에 포함되어야 합니다." }
    check(!isInBudget(Quiz03Price(500L), Quiz03Price(1000L), Quiz03Price(10000L))) { "500원은 최소 예산 미만이므로 제외되어야 합니다." }
    check(!isInBudget(Quiz03Price(15000L), Quiz03Price(1000L), Quiz03Price(10000L))) { "15000원은 최대 예산 초과이므로 제외되어야 합니다." }

    // 3. getItemPrice 검증
    val cart = Quiz03Cart(listOf(Quiz03Price(1000L), Quiz03Price(2000L), Quiz03Price(3000L)))
    check(getItemPrice(cart, 0) == Quiz03Price(1000L)) { "0번 인덱스는 1000원이어야 합니다." }
    check(getItemPrice(cart, 1) == Quiz03Price(2000L)) { "1번 인덱스는 2000원이어야 합니다." }
    check(getItemPrice(cart, 2) == Quiz03Price(3000L)) { "2번 인덱스는 3000원이어야 합니다." }

    println("✅ Quiz03 테스트 통과! (연산자 오버로딩, in/.. 범위 연산자, get 인덱싱 정상 동작)")
}
