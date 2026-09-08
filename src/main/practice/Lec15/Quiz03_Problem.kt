package Lec15

/*
 * [학습 목표 & 복습 개념]
 * - Lec15의 핵심 주제인 컬렉션의 null 가능성(`List<T?>?`)을 실무 비즈니스 시나리오에서 체화합니다.
 * - 컬렉션 자체가 null인 경우(`List?`)와 컬렉션 내부 요소가 null인 경우(`List<T?>`)의 차이를 명확히 구분하고 처리합니다.
 * - Safe Call(?.)과 null 체크, 예외 처리를 결합하여 안정적인 비즈니스 계산 로직을 작성합니다.
 *
 * [문제 설명]
 * 온라인 쇼핑몰 주문 시스템에서 장바구니에 담긴 상품들의 최종 결제 금액을 계산하는 함수를 작성해야 합니다.
 * 외부 결제 모듈이나 레거시 API로부터 넘어오는 상품 목록 데이터는 네트워크 오류나 미입력으로 인해
 * 리스트 자체가 null일 수도 있고, 리스트 내부의 개별 상품이 null일 수도 있습니다(`List<Quiz03Item?>?`).
 *
 * 상품 정보를 담는 `Quiz03Item` 데이터 클래스를 기반으로 `calculateValidTotalPrice` 함수를 완성하세요.
 *
 * [요구사항 & 제약조건]
 * 1. 전달받은 `items` 리스트 자체가 `null`인 경우 총액 `0`을 반환하세요.
 * 2. 리스트 내부 요소 중 `null`인 항목은 건너뛰고 합산하지 않습니다.
 * 3. 유효한(non-null) 상품 중 가격(`price`)이 음수(`price < 0`)인 항목이 발견되면
 *    즉시 `IllegalArgumentException("상품 가격은 음수일 수 없습니다.")` 예외를 던지세요.
 * 4. 모든 유효한 상품의 가격을 합산한 최종 금액을 정수(`Int`)로 반환하세요.
 *
 * [입출력 및 기대 결과]
 * - calculateValidTotalPrice(null)                                                    -> 0
 * - calculateValidTotalPrice(emptyList())                                             -> 0
 * - calculateValidTotalPrice(listOf(null, null))                                      -> 0
 * - calculateValidTotalPrice(listOf(Quiz03Item("A", 1000), Quiz03Item("B", 2000)))   -> 3000
 * - calculateValidTotalPrice(listOf(Quiz03Item("A", 1000), null, Quiz03Item("B", 2000))) -> 3000
 * - calculateValidTotalPrice(listOf(Quiz03Item("Bad", -500)))                         -> IllegalArgumentException 발생
 *
 * [💡 HINT]
 * - `items`가 null인지 먼저 확인(`if (items == null) return 0`)하거나 Safe Call 등을 활용할 수 있습니다.
 * - 리스트를 순회할 때 `for (item in items)` 내에서 `item != null` 여부를 검사하면 스마트 캐스트(Smart Cast)가 적용되어 `item.price`에 바로 안전하게 접근할 수 있습니다.
 */

// 실습용 상품 데이터 클래스
data class Quiz03Item(
    val name: String,
    val price: Int
)

// 유효 상품 총액 계산 함수 뼈대
private fun calculateValidTotalPrice(items: List<Quiz03Item?>?): Int {
    TODO()
}

fun main() {
    // 1. null 리스트 및 빈 리스트 검증
    check(calculateValidTotalPrice(null) == 0) { "items가 null이면 0을 반환해야 합니다." }
    check(calculateValidTotalPrice(emptyList()) == 0) { "items가 빈 리스트이면 0을 반환해야 합니다." }

    // 2. null 요소만 포함된 리스트 검증
    check(calculateValidTotalPrice(listOf(null, null)) == 0) { "null 요소만 존재할 경우 0을 반환해야 합니다." }

    // 3. 정상 상품 목록 합산 검증
    val normalItems = listOf(
        Quiz03Item("키보드", 50000),
        Quiz03Item("마우스", 30000)
    )
    check(calculateValidTotalPrice(normalItems) == 80000) { "정상 상품 합산액은 80000이어야 합니다." }

    // 4. 중간에 null 요소가 포함된 리스트 검증
    val mixedItems = listOf(
        Quiz03Item("키보드", 50000),
        null,
        Quiz03Item("모니터", 200000),
        null
    )
    check(calculateValidTotalPrice(mixedItems) == 250000) { "null 요소를 건너뛰고 정상 상품만 합산한 금액은 250000이어야 합니다." }

    // 5. 음수 가격 상품 포함 시 예외 발생 검증
    val invalidItems = listOf(
        Quiz03Item("정상상품", 10000),
        Quiz03Item("불량상품", -1000)
    )
    val exceptionThrown = runCatching { calculateValidTotalPrice(invalidItems) }.isFailure
    check(exceptionThrown) { "음수 가격 상품이 포함되면 IllegalArgumentException이 발생해야 합니다." }

    println("✅ Quiz03 테스트 통과! (컬렉션 null 가능성 다루기 및 유효성 검증 완료)")
}
