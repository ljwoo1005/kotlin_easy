package Lec01

/*
 * [핵심 해결 아이디어]
 * - Java의 장황한 선언 문법을 코틀린의 관용적(Idiomatic) 문법으로 리팩토링합니다.
 * - final 변수는 'val'로 선언하고 타입 추론을 활용하여 'final long'을 'val'로 단순화합니다.
 * - 가변 변수는 'var'로 선언하여 상태 변경을 명시합니다.
 * - null 가능성이 있는 필드는 타입 뒤에 '?'를 명시하여 컴파일 타임에 안전성을 보장합니다.
 * - 객체 생성 시 불필요한 'new' 키워드를 제거하고 클래스 생성자를 직접 호출합니다.
 */

// 실습용 상품 데이터 클래스(Quiz02Product)는 Quiz02_Problem.kt에 정의된 모델을 사용합니다.

// Java 코드를 리팩토링한 코틀린 관용 모범 답안 함수
private fun createProduct(): Quiz02Product {
    // 1. 불변 상품 ID 선언 (자바의 final long 대치, 타입 추론 활용)
    val productId = 1001L

    // 2. 가변 재고 변수 선언 및 재고 차감 반영 (자바의 long currentStock 대치)
    var currentStock = 50L
    currentStock = 45L

    // 3. Nullable 타입 명시 및 null 대입 (자바의 Long discountRate 대치)
    val discountRate: Long? = null

    // 4. new 키워드 없이 코틀린 생성자 호출을 통해 객체 인스턴스화
    return Quiz02Product(
        id = productId,
        name = "Kotlin In Action",
        stock = currentStock,
        discountRate = discountRate
    )
}

fun main() {
    val product = createProduct()
    check(product.id == 1001L) { "상품 ID는 1001L이어야 합니다. (현재: ${product.id})" }
    check(product.name == "Kotlin In Action") { "상품명은 'Kotlin In Action'이어야 합니다. (현재: ${product.name})" }
    check(product.stock == 45L) { "최종 재고는 45L이어야 합니다. (현재: ${product.stock})" }
    check(product.discountRate == null) { "할인율은 null이어야 합니다. (현재: ${product.discountRate})" }

    println("✅ Quiz02 해답 검증 통과! (리팩토링 모범 답안 정상 동작)")
}

/*
 * [자바 대비 장점]
 * 1. 'new' 키워드 생략: 생성자를 일반 함수처럼 호출하므로 문법이 간결해지고 가독성이 향상됩니다.
 * 2. 타입 추론에 의한 보일러플레이트 제거: 'final long productId = 1001L;' 대신 'val productId = 1001L'로 타입 중복을 제거할 수 있습니다.
 * 3. Primitive vs Wrapper 통합: 자바에서는 'long'과 'Long'의 박싱/언박싱을 신경 써야 하지만, 코틀린은 컴파일러가 최적의 타입(기본형 또는 래퍼형)을 자동으로 선택하므로 개발자가 신경 쓸 필요가 없습니다.
 * 4. 명시적인 Null Safety: 'Long discountRate = null;'은 자바에서 언제든 NPE를 유발할 수 있지만, 코틀린은 'Long?'으로 명시하여 컴파일러의 null 검사를 지원받을 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 자바 습관으로 'new Product(...)' 작성: 코틀린에서는 'new' 키워드가 금지되어 있어 컴파일 에러가 발생합니다.
 * 2. Nullable 변수 선언 시 타입 생략: 'val discountRate = null'로 쓰면 컴파일러가 Nothing? 타입으로 추론하므로, 'val discountRate: Long? = null'처럼 타입을 명시해야 합니다.
 */
