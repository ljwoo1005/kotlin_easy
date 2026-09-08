package Lec03

/*
 * [학습 목표 & 복습 개념]
 * - 다형성 파라미터(Any?)를 전달받아 'when' 식과 결합된 스마트 캐스트(is)를 체화합니다.
 * - 정상 종료되지 않는 함수를 표현하는 코틀린의 특이한 타입 'Nothing'의 개념과 실무적 활용(fail 함수)을 학습합니다.
 * - 여러 줄 문자열("""...""".trimIndent())과 복합 문자열 템플릿(${})을 활용해 정형화된 비즈니스 문서를 생성합니다.
 *
 * [문제 설명]
 * 전자상거래 결제 시스템에서 다양한 형식으로 유입되는 원시 결제 데이터를 정규화하고 표준 영수증을 발행하는 3개 함수를 완성하세요.
 *
 * 1. fail(message: String): Nothing
 *    - 오류 메시지를 담아 IllegalArgumentException을 던지는 공통 예외 처리 함수입니다.
 *    - 코틀린의 'Nothing' 타입을 반환 타입으로 명시하여, 이 함수 호출 이후의 코드가 실행되지 않음을 컴파일러에게 알립니다.
 *
 * 2. normalizeAmount(rawAmount: Any?): Long
 *    - 외부 시스템에서 Long, Int, String 등 다양한 타입(Any?)으로 전달되는 결제 금액을 표준 Long 단위로 정규화합니다.
 *    - rawAmount가 Long인 경우: 그대로 반환합니다.
 *    - rawAmount가 Int인 경우: 명시적 변환(.toLong())을 통해 반환합니다.
 *    - rawAmount가 String인 경우: toLongOrNull()로 변환하되, 숫자로 변환되지 않거나 비어있으면 fail("유효하지 않은 금액 형식: $rawAmount")을 호출합니다.
 *    - 그 외의 타입이거나 null인 경우: fail("지원하지 않는 금액 타입입니다: $rawAmount")을 호출합니다.
 *    - 'when' 식과 'is' 스마트 캐스트를 사용하여 구현하세요.
 *
 * 3. generateReceipt(target: Any?): String
 *    - target이 Quiz03Payment 객체인지 확인합니다.
 *    - Quiz03Payment가 아니거나 null인 경우: fail("결제 정보가 올바르지 않습니다: $target")을 호출합니다.
 *    - target이 Quiz03Payment인 경우: 스마트 캐스트를 활용하여 여러 줄 문자열("""...""".trimIndent())로 영수증을 생성합니다.
 *      포맷:
 *      [결제 영수증]
 *      - 거래번호: {txId}
 *      - 고객명: {user의 첫글자}** ({user})
 *      - 결제금액: {amount}원
 *    - 고객명의 첫 글자는 문자열 인덱싱(target.user[0])을 사용하세요.
 *
 * [요구사항 & 제약조건]
 * - fail 함수의 반환 타입은 반드시 `Nothing`으로 선언하세요.
 * - normalizeAmount 함수는 `when (rawAmount)` 표현식을 사용하여 간결하게 작성하세요.
 * - generateReceipt 함수는 `"""...""".trimIndent()` 멀티라인 문자열 템플릿을 반드시 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - normalizeAmount(10000L)   -> 10000L
 * - normalizeAmount(5000)     -> 5000L
 * - normalizeAmount("3000")   -> 3000L
 * - normalizeAmount("invalid")-> IllegalArgumentException 발생
 * - normalizeAmount(null)     -> IllegalArgumentException 발생
 * - generateReceipt(Quiz03Payment("TX-101", "홍길동", 15000L)) ->
 *   "[결제 영수증]\n- 거래번호: TX-101\n- 고객명: 홍** (홍길동)\n- 결제금액: 15000원"
 *
 * [💡 HINT]
 * - `Nothing`은 코틀린의 모든 타입의 최하위 서브타입(Bottom type)이므로, when 식의 모든 분기에서 반환 타입 불일치 없이 자연스럽게 식의 일부로 동작합니다.
 * - 여러 줄 문자열에서 들여쓰기를 제거하려면 `"""...""".trimIndent()`를 호출합니다.
 * - 문자열의 첫 글자는 `target.user[0]`으로 접근할 수 있습니다.
 */

// 실습용 도메인 데이터 클래스
data class Quiz03Payment(
    val txId: String,
    val user: String,
    val amount: Long
)

// 1. 공통 예외 전담 함수 뼈대 (Nothing 타입 명시)
private fun fail(message: String): Nothing {
    TODO()
}

// 2. 다양한 타입의 원시 금액 정규화 함수 뼈대 (when 식 스마트 캐스트)
private fun normalizeAmount(rawAmount: Any?): Long {
    TODO()
}

// 3. 결제 영수증 생성 함수 뼈대 (멀티라인 문자열 및 스마트 캐스트)
private fun generateReceipt(target: Any?): String {
    TODO()
}

fun main() {
    // 1. normalizeAmount 검증 (학습자가 미구현 시 NotImplementedError 발생)
    check(normalizeAmount(10000L) == 10000L) { "Long 타입은 그대로 반환되어야 합니다." }
    check(normalizeAmount(5000) == 5000L) { "Int 타입은 Long으로 변환되어야 합니다." }
    check(normalizeAmount("3000") == 3000L) { "문자열 '3000'은 3000L로 변환되어야 합니다." }
    check(runCatching { normalizeAmount("invalid") }.isFailure) { "잘못된 문자열은 예외가 발생해야 합니다." }
    check(runCatching { normalizeAmount(null) }.isFailure) { "null은 예외가 발생해야 합니다." }
    check(runCatching { normalizeAmount(3.14) }.isFailure) { "지원하지 않는 타입은 예외가 발생해야 합니다." }

    // 2. generateReceipt 검증
    val payment = Quiz03Payment("TX-2026", "김코틀린", 50000L)
    val expectedReceipt = """
        [결제 영수증]
        - 거래번호: TX-2026
        - 고객명: 김** (김코틀린)
        - 결제금액: 50000원
    """.trimIndent()

    val actualReceipt = generateReceipt(payment)
    check(actualReceipt == expectedReceipt) {
        "영수증 출력이 기대값과 일치하지 않습니다.\n[기대값]:\n$expectedReceipt\n[실제값]:\n$actualReceipt"
    }
    check(runCatching { generateReceipt("잘못된객체") }.isFailure) { "Payment 객체가 아니면 예외가 발생해야 합니다." }
    check(runCatching { generateReceipt(null) }.isFailure) { "null이면 예외가 발생해야 합니다." }

    // 3. fail 함수 검증
    val failResult = runCatching { fail("테스트 실패") }
    check(failResult.isFailure) { "fail 함수는 예외를 던져야 합니다." }
    check(failResult.exceptionOrNull() is IllegalArgumentException) { "IllegalArgumentException이어야 합니다." }

    println("✅ Quiz03 테스트 통과! (Any 스마트 캐스트, Nothing, 멀티라인 문자열 정상 동작)")
}
