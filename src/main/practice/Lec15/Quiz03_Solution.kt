package Lec15

/*
 * [핵심 해결 아이디어]
 * - `List<Quiz03Item?>?`는 "리스트 자체가 null일 수 있고, 리스트 내부의 원소도 null일 수 있음"을 의미합니다.
 * - 리스트 자체가 null인 경우 빠른 반환(Early Return) 패턴 `if (items == null) return 0`을 적용하여 조기에 처리합니다.
 * - 리스트를 순회하며 개별 아이템이 null인지 검사(`if (item != null)`)하면, 코틀린 컴파일러가 해당 블록 내에서 `item`을 non-null 타입(`Quiz03Item`)으로 스마트 캐스트합니다.
 * - 이후 비즈니스 유효성 검사(가격 음수 체크)를 거친 후 누적 합산하여 안전하고 직관적인 코드를 완성합니다.
 */

// 실습용 상품 데이터 클래스(Quiz03Item)는 Quiz03_Problem.kt에 선언된 모델을 사용합니다.

// 유효 상품 총액 계산 모범 답안 함수
private fun calculateValidTotalPrice(items: List<Quiz03Item?>?): Int {
    // 1. 리스트 자체가 null인 경우 조기 반환 (List? 대응)
    if (items == null) {
        return 0
    }

    var total = 0

    // 2. 리스트 순회 (List<Quiz03Item?> 대응)
    for (item in items) {
        // null 요소는 건너뛰기
        if (item != null) {
            // 스마트 캐스트 적용: item이 Quiz03Item으로 안전하게 취급됨
            if (item.price < 0) {
                throw IllegalArgumentException("상품 가격은 음수일 수 없습니다.")
            }
            total += item.price
        }
    }

    return total
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

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 세밀한 Nullability 타입 표현: 자바에서는 `List<Quiz03Item>`이라고 적어도 리스트가 null인지, 원소가 null인지 타입 시그니처만으로는 알 수 없어 주석이나 런타임 검사에 의존해야 했습니다. 코틀린은 `List<T?>?`, `List<T>?`, `List<T?>` 등으로 null 가능성을 컴파일 시점에 명확히 표현합니다.
 * 2. 강력한 스마트 캐스트(Smart Cast): null 검사를 수행하고 나면 별도의 캐스팅이나 임시 변수 할당 없이 곧바로 non-null 프로퍼티(`item.price`)에 안전하게 접근할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 물음표 위치 혼동:
 *    - `List<Int?>`: 리스트 자체는 null이 될 수 없으나, 원소는 null 가능
 *    - `List<Int>?`: 리스트 자체는 null일 수 있으나, 원소는 non-null
 *    - `List<Int?>?`: 리스트와 원소 모두 null 가능
 *    실무에서 이 세 가지의 차이를 정확히 인지하지 못하면 불필요한 null 검사가 남발되거나 의도치 않은 NullPointerException을 유발할 수 있습니다.
 * 2. filterNotNull() 무분별 사용 주의: 단순 필터링 시에는 `items.filterNotNull()`이 편리하지만, 음수 가격과 같은 비즈니스 예외를 함께 검증해야 할 때는 순회 루프 내에서 처리하는 것이 불필요한 중간 컬렉션 생성을 방지할 수 있습니다.
 */
