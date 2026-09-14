package Lec19

/*
 * [핵심 해결 아이디어]
 * - `takeIf`는 객체의 유효성을 평가하여 조건을 만족하면 자기 자신을, 만족하지 못하면 null을 반환합니다.
 *   이를 통해 복잡한 if 조건문 분기 대신 단일 표현식으로 깔끔하게 값을 필터링할 수 있습니다.
 * - `takeUnless`는 부정 조건(공백이거나 길이 초과 등)을 검사할 때 직관적으로 쓰이며,
 *   Safe call(`?.`)과 연계하여 유효한 값에 대해서만 후속 가공(`trim()`)을 수행할 수 있습니다.
 * - `typealias`는 고차 함수 매개변수나 복합 타입에 가독성 높은 별칭을 부여하여 함수 시그니처를 단순화합니다.
 */

// TextPredicate 타입 별칭은 Quiz01_Problem.kt에 선언된 것을 공유합니다.

// 1. 유효한 포트 번호 검증 모범 답안
private fun validatePortNumber(port: Int): Int? {
    // 1..65535 범위 내에 있을 때만 해당 포트 번호를 반환하고, 아니면 null 반환
    return port.takeIf { it in 1..65535 }
}

// 2. 닉네임 유효성 검사 및 정제 모범 답안
private fun sanitizeNickname(nickname: String): String? {
    // 공백이거나 10자를 초과하는 잘못된 닉네임이 아닐 때만 값을 유지하고, 유효한 경우 공백 제거(trim)
    return nickname.takeUnless { it.isBlank() || it.length > 10 }?.trim()
}

// 3. 커스텀 조건에 따른 텍스트 필터링 모범 답안
private fun filterText(text: String, predicate: TextPredicate): String? {
    // 주어진 술어 함수(predicate)를 만족하는 경우에만 텍스트 반환
    return text.takeIf(predicate)
}

fun main() {
    // 1. validatePortNumber 검증
    check(validatePortNumber(8080) == 8080) { "8080 포트는 유효하므로 8080이 반환되어야 합니다." }
    check(validatePortNumber(1) == 1) { "1 포트는 유효하므로 1이 반환되어야 합니다." }
    check(validatePortNumber(65535) == 65535) { "65535 포트는 유효하므로 65535가 반환되어야 합니다." }
    check(validatePortNumber(0) == null) { "0 포트는 범위를 벗어나므로 null이어야 합니다." }
    check(validatePortNumber(-1) == null) { "음수 포트는 null이어야 합니다." }
    check(validatePortNumber(70000) == null) { "65535 초과 포트는 null이어야 합니다." }

    // 2. sanitizeNickname 검증
    check(sanitizeNickname("  코틀린짱  ") == "코틀린짱") {
        "정상 닉네임은 앞뒤 공백이 제거된 '코틀린짱'이어야 합니다."
    }
    check(sanitizeNickname("코딩러") == "코딩러") { "공백 없는 정상 닉네임은 그대로 반환되어야 합니다." }
    check(sanitizeNickname("   ") == null) { "공백으로만 이루어진 닉네임은 null이어야 합니다." }
    check(sanitizeNickname("") == null) { "빈 닉네임은 null이어야 합니다." }
    check(sanitizeNickname("열글자가넘어가는아주긴닉네임입니다") == null) {
        "10자를 초과하는 닉네임은 null이어야 합니다."
    }

    // 3. filterText 검증
    val startsWithK: TextPredicate = { it.startsWith("K") }
    check(filterText("Kotlin", startsWithK) == "Kotlin") {
        "'Kotlin'은 'K'로 시작하므로 'Kotlin'이 반환되어야 합니다."
    }
    check(filterText("Java", startsWithK) == null) {
        "'Java'는 'K'로 시작하지 않으므로 null이어야 합니다."
    }

    val lengthAtLeast5: TextPredicate = { it.length >= 5 }
    check(filterText("Hello", lengthAtLeast5) == "Hello") {
        "길이가 5 이상인 문자열은 그대로 반환되어야 합니다."
    }
    check(filterText("Hi", lengthAtLeast5) == null) {
        "길이가 5 미만인 문자열은 null이어야 합니다."
    }

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 조건부 널 반환의 압도적 간결성: Java의 장황한 `if (port < 1 || port > 65535) return null; return port;` 구조를
 *    단 한 줄의 `port.takeIf { it in 1..65535 }` 표현식으로 깔끔하게 작성할 수 있습니다.
 * 2. 널 안정성 체이닝(Safe-call)과의 완벽한 조화: `takeUnless { ... }?.trim()`처럼 유효성 검사 통과 시에만
 *    이어서 메서드를 호출하는 안전한 파이프라인을 부가적인 if문 없이 구축할 수 있습니다.
 * 3. Type Alias의 유연성: Java에서는 매번 새로운 `@FunctionalInterface` 인터페이스를 선언하고 명명해야 하지만,
 *    Kotlin에서는 `typealias` 키워드로 기존 함수 타입을 간결하고 직관적인 도메인 용어로 즉시 치환할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. takeIf와 takeUnless의 참/거짓 반환 조건 혼동:
 *    - `takeIf { condition }`: 조건이 true일 때 수신 객체 반환, false일 때 null 반환.
 *    - `takeUnless { condition }`: 조건이 true일 때 null 반환, false일 때 수신 객체 반환.
 *    특히 부정 연산자(`!`)와 함께 사용할 때 두 함수의 의미를 거꾸로 적용하는 실수가 잦으므로 주의해야 합니다.
 * 2. 무리한 takeIf 남용: 복합적인 비즈니스 로직이나 부수 효과(Side-effect)가 있는 로직에 무리하게 `takeIf`를 적용하면
 *    오히려 가독성을 해칠 수 있습니다. 단순한 조건 검사나 파이프라인 흐름에서 주로 활용하는 것이 좋습니다.
 */
