package Lec04

/*
 * [핵심 해결 아이디어]
 * - 'operator' 제어자를 붙인 plus와 minus 메서드를 통해 객체 간 직접적인 산술 연산('+', '-')을 수행합니다.
 * - Comparable 인터페이스가 구현된 객체는 별도 정의 없이도 '..' 연산자를 통해 ClosedRange를 생성할 수 있으며, 'in' 연산자로 범위 포함 여부를 판정합니다.
 * - 'operator fun get(index: Int)'를 구현함으로써 리스트 래퍼 객체나 커스텀 컬렉션에 자바 스타일(get()) 대신 배열 인덱싱 구문('[index]')으로 접근할 수 있습니다.
 */

// 실습용 가격(Quiz03Price) 및 장바구니(Quiz03Cart) 클래스는 Quiz03_Problem.kt에 정의된 모델을 사용합니다.

// 1. 오버로딩된 '-', '+' 연산자를 활용한 최종 결제 금액 계산 모범 답안
private fun calculateFinalPrice(
    original: Quiz03Price,
    discount: Quiz03Price,
    deliveryFee: Quiz03Price
): Quiz03Price {
    // 도메인 수식 그대로 '-'와 '+' 연산자를 사용하여 순차 연산
    return original - discount + deliveryFee
}

// 2. '..' 범위 연산자와 'in' 포함 연산자를 결합한 단일 표현식 모범 답안
private fun isInBudget(
    price: Quiz03Price,
    minBudget: Quiz03Price,
    maxBudget: Quiz03Price
): Boolean = price in minBudget..maxBudget

// 3. '[]' 인덱싱 연산자를 활용한 단일 표현식 모범 답안
private fun getItemPrice(cart: Quiz03Cart, index: Int): Quiz03Price = cart[index]

fun main() {
    // 1. calculateFinalPrice 검증
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

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 도메인 표현력의 극대화: 자바에서는 금액이나 좌표 같은 VO(Value Object)를 다룰 때 'original.minus(discount).plus(deliveryFee)'처럼 체이닝 메서드로 표현해야 해서 가독성이 떨어졌습니다. 코틀린은 연산자 오버로딩을 통해 비즈니스 수식 'original - discount + deliveryFee'를 그대로 코드로 옮길 수 있습니다.
 * 2. 간결하고 안전한 범위 검사: 자바에서 'price.compareTo(min) >= 0 && price.compareTo(max) <= 0'로 길게 작성해야 했던 범위를 'price in min..max'로 직관적이고 실수 없이 판정할 수 있습니다.
 * 3. 일관된 컬렉션 접근: 'operator fun get'을 통해 커스텀 래퍼 객체도 표준 배열처럼 'cart[index]'로 접근 가능하므로 API 사용성이 크게 향상됩니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 범위 연산자(..) 사용 시 순서 뒤바뀜: 'a..b'에서 a가 b보다 크면 빈 범위(Empty Range)가 생성되어 'in' 검사 시 항상 false가 반환될 수 있으므로, 최소값과 최대값의 순서에 유의해야 합니다.
 * 2. 연산자 오버로딩의 무분별한 남용: 연산자의 본래 의미(덧셈, 뺄셈, 조회)와 전혀 무관한 로직에 연산자를 오버로딩하면 코드의 가독성을 심각하게 해칠 수 있으므로, 상식적인 도메인 연산에만 제한적으로 사용해야 합니다.
 */
