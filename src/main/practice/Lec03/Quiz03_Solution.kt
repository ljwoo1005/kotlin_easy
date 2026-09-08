package Lec03

/*
 * [핵심 해결 아이디어]
 * - 'Nothing' 타입은 함수가 정상적으로 값을 반환하지 않고 항상 예외를 던지거나 종료됨을 표현하는 코틀린 고유의 최하위 타입입니다.
 * - 'when' 식에서 'is Type' 검사를 수행하면 분기마다 해당 타입으로 자동 스마트 캐스트되어 안전하게 메서드를 호출할 수 있습니다.
 * - 여러 줄 문자열("""...""".trimIndent())을 사용하면 코드 가독성을 위한 들여쓰기는 유지하면서도 출력 시 불필요한 공백을 깔끔하게 제거할 수 있습니다.
 */

// 실습용 도메인 데이터 클래스(Quiz03Payment)는 Quiz03_Problem.kt에 정의된 모델을 공유합니다.

// 1. 공통 예외 전담 모범 답안 함수 (Nothing 반환 타입 명시)
private fun fail(message: String): Nothing {
    // 무조건 예외를 던지며 정상 반환되지 않음을 Nothing 타입으로 표현
    throw IllegalArgumentException(message)
}

// 2. 다양한 타입의 원시 금액 정규화 모범 답안 함수 (when 식 스마트 캐스트)
private fun normalizeAmount(rawAmount: Any?): Long {
    // when 식에서 'is' 검사를 통해 각 분기별로 자동 스마트 캐스트 적용
    return when (rawAmount) {
        is Long -> rawAmount
        is Int -> rawAmount.toLong()
        is String -> rawAmount.toLongOrNull() ?: fail("유효하지 않은 금액 형식: $rawAmount")
        else -> fail("지원하지 않는 금액 타입입니다: $rawAmount")
    }
}

// 3. 결제 영수증 생성 모범 답안 함수 (멀티라인 문자열 및 스마트 캐스트)
private fun generateReceipt(target: Any?): String {
    // target이 Quiz03Payment가 아니거나 null이면 Nothing 함수로 예외 발생
    if (target !is Quiz03Payment) {
        fail("결제 정보가 올바르지 않습니다: $target")
    }

    // 스마트 캐스트 적용: target을 Quiz03Payment로 바로 사용
    // trimIndent()로 공통 들여쓰기를 제거한 멀티라인 문자열 생성
    return """
        [결제 영수증]
        - 거래번호: ${target.txId}
        - 고객명: ${target.user[0]}** (${target.user})
        - 결제금액: ${target.amount}원
    """.trimIndent()
}

fun main() {
    // 1. normalizeAmount 검증
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

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. Nothing 타입의 강력함: 자바에는 정상 종료되지 않는 메서드를 표현하는 타입이 없어 컴파일러가 'return' 문이나 초기화 누락 경고를 낼 수 있지만, 코틀린의 Nothing은 모든 타입의 최하위 타입이므로 표현식(Expression) 어디서든 타입 불일치 없이 자연스럽게 사용 가능합니다.
 * 2. 표현식 기반 다형 분기(when + is): 자바의 긴 'if-else-instanceof' 체인과 강제 캐스팅 대비, 코틀린의 'when (x) { is Type -> ... }' 구조는 가독성이 훨씬 뛰어나고 실수를 방지합니다.
 * 3. 여러 줄 문자열(Multi-line String): 자바의 줄바꿈 문자('\n') 연결이나 복잡한 템플릿 엔진 없이도 '"""...""".trimIndent()'를 통해 소스 코드 들여쓰기와 최종 문자열 포맷을 완벽하게 일치시킬 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. trimIndent() 누락: 여러 줄 문자열에 """ 만 사용하고 .trimIndent()를 누락하면, 소스 코드 가독성을 위해 넣은 들여쓰기 공백이 출력 문자열에도 그대로 포함되는 실수가 자주 발생합니다.
 * 2. Nothing 반환 함수에서의 리턴 시도: Nothing 타입은 정상 리턴이 불가능하므로, 함수 내부에서 값을 반환하려고 하면 컴파일 에러가 발생합니다. 반드시 throw나 무한 루프 등으로 끝나야 합니다.
 */
