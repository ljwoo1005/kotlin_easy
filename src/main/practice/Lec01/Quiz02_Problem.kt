package Lec01

/*
 * [학습 목표 & 복습 개념]
 * - Java 코드와 Kotlin 코드의 선언 및 인스턴스화 문법 차이를 이해하고 리팩토링할 수 있습니다.
 * - 코틀린의 타입 추론(Type Inference)과 `val` 우선 원칙을 적용합니다.
 * - `new` 키워드 없는 객체 생성 방식을 체화합니다.
 * - Nullable 타입(`?`)을 활용한 안전한 널 표현 방식을 복습합니다.
 *
 * [문제 설명]
 * 아래는 Java 스타일로 작성된 상품 등록 코드입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaProductManager {
 *     public static Product registerProduct() {
 *         final long productId = 1001L;
 *         long currentStock = 50L;
 *         currentStock = 45L; // 판매로 인한 재고 차감 반영
 *         Long discountRate = null; // 신규 상품이라 할인율 없음(null 허용)
 *         Product product = new Product(productId, "Kotlin In Action", currentStock, discountRate);
 *         return product;
 *     }
 * }
 * ----------------------------------------------------
 * 위 Java 코드를 코틀린의 관용적(Idiomatic) 스타일로 리팩토링하는 createProduct 함수를 완성하세요.
 *
 * [요구사항 & 제약조건]
 * - 코틀린 문법에 맞게 `new` 키워드를 사용하지 않고 Quiz02Product 인스턴스를 생성하세요.
 * - 불변 값인 상품 ID는 `val`과 타입 추론을 활용하여 선언하세요.
 * - 값이 변경되는 재고는 `var`로 선언하고 50L에서 45L로 변경하세요.
 * - 할인율은 null을 허용하는 Nullable 타입(`Long?`)으로 명시하여 null을 할당하세요.
 * - 최종 생성된 Quiz02Product 객체를 반환하세요.
 *
 * [입출력 및 기대 결과]
 * - createProduct() 호출 시
 *   Quiz02Product(id=1001L, name="Kotlin In Action", stock=45L, discountRate=null) 객체 반환
 *
 * [💡 HINT]
 * - 코틀린에서는 객체를 인스턴스화할 때 `new` 키워드를 사용하지 않고 생성자를 함수처럼 호출합니다.
 * - 타입 추론이 가능한 곳에서는 불필요한 타입 명시를 생략하면 훨씬 코틀린스러운 코드가 됩니다.
 */

// 실습용 상품 데이터 클래스
data class Quiz02Product(
    val id: Long,
    val name: String,
    var stock: Long,
    val discountRate: Long?
)

// 학습자가 구현해야 할 함수 뼈대
private fun createProduct(): Quiz02Product {
    TODO()
}

fun main() {
    val product = createProduct()
    check(product.id == 1001L) { "상품 ID는 1001L이어야 합니다. (현재: ${product.id})" }
    check(product.name == "Kotlin In Action") { "상품명은 'Kotlin In Action'이어야 합니다. (현재: ${product.name})" }
    check(product.stock == 45L) { "최종 재고는 45L이어야 합니다. (현재: ${product.stock})" }
    check(product.discountRate == null) { "할인율은 null이어야 합니다. (현재: ${product.discountRate})" }

    println("✅ Quiz02 테스트 통과! (Java to Idiomatic Kotlin 리팩토링 완료)")
}
