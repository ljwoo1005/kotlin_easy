package Lec19

/*
 * [학습 목표 & 복습 개념]
 * - `typealias`를 활용하여 복잡하고 긴 함수 타입에 직관적인 도메인 별칭을 부여하는 방법을 체화합니다.
 * - `takeIf`와 `takeUnless`의 동작 원리를 이해하고, 장황한 if-null 분기문 대신 코틀린의 관용적인 단일 표현식으로 안전하게 값을 검증/필터링하는 기법을 학습합니다.
 * - Safe call(`?.`) 및 엘비스 연산자(`?:`)와 결합하여 불필요한 임시 변수 없이 유효성 검사 파이프라인을 구축하는 방법을 익힙니다.
 *
 * [문제 설명]
 * 서버 설정 및 사용자 입력값을 검증하는 3가지 기초 유효성 검증 함수를 완성하세요.
 *
 * 1. validatePortNumber(port: Int): Int?
 *    - 네트워크 포트 번호(`port`)가 정상 범위(1 이상 65535 이하)인 경우에만 해당 포트 번호를 그대로 반환하고,
 *      범위를 벗어나면 `null`을 반환합니다.
 *    - 반드시 `takeIf`를 활용하여 작성하세요.
 *
 * 2. sanitizeNickname(nickname: String): String?
 *    - 사용자가 입력한 닉네임(`nickname`)을 검증합니다.
 *    - 닉네임이 공백이거나(isBlank) 길이가 10자를 초과하는 비정상적인 값인 경우 `null`을 반환합니다.
 *    - 정상적인 닉네임인 경우 앞뒤 공백을 제거한(trim()) 깨끗한 문자열을 반환합니다.
 *    - 반드시 `takeUnless`를 활용하여 작성하세요.
 *
 * 3. filterText(text: String, predicate: TextPredicate): String?
 *    - 텍스트 검사 조건식인 `TextPredicate`를 매개변수로 받아, 주어진 `text`가 조건을 만족하면 `text`를 반환하고,
 *      만족하지 않으면 `null`을 반환합니다.
 *    - 파일 상단에 정의된 `typealias TextPredicate = (String) -> Boolean`을 매개변수 타입으로 활용하고,
 *      내부에서 `takeIf`를 사용하여 구현하세요.
 *
 * [요구사항 & 제약조건]
 * - `validatePortNumber`: `takeIf`를 활용하여 구현하세요.
 * - `sanitizeNickname`: `takeUnless`와 Safe call(`?.`)을 연계하여 작성하세요.
 * - `filterText`: `typealias TextPredicate = (String) -> Boolean`을 반드시 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - validatePortNumber(8080) -> 8080
 * - validatePortNumber(-1) -> null
 * - validatePortNumber(70000) -> null
 * - sanitizeNickname("  코틀린짱  ") -> "코틀린짱"
 * - sanitizeNickname("   ") -> null
 * - sanitizeNickname("열글자가넘어가는아주긴닉네임입니다") -> null
 * - filterText("Kotlin") { it.startsWith("K") } -> "Kotlin"
 * - filterText("Java") { it.startsWith("K") } -> null
 *
 * [💡 HINT]
 * - `takeIf`는 수신 객체가 주어진 람다 조건을 만족(`true`)할 때 객체 자신을 반환하고, 그렇지 않으면 `null`을 반환합니다.
 * - `takeUnless`는 반대로 주어진 람다 조건을 만족할 때 `null`을 반환하고, 만족하지 않을 때(`false`) 객체 자신을 반환합니다.
 * - `typealias 별칭 = 기존타입` 문법을 사용하면 긴 함수형 타입을 간결한 이름으로 치환할 수 있습니다.
 */

// 텍스트 조건 검사를 위한 함수 타입 별칭 정의
typealias TextPredicate = (String) -> Boolean

// 1. 유효한 포트 번호 검증 함수 뼈대
private fun validatePortNumber(port: Int): Int? {
    // TODO: 여기에 코드를 작성하세요 (takeIf 활용)
    TODO()
}

// 2. 닉네임 유효성 검사 및 정제 함수 뼈대
private fun sanitizeNickname(nickname: String): String? {
    // TODO: 여기에 코드를 작성하세요 (takeUnless 활용)
    TODO()
}

// 3. 커스텀 조건에 따른 텍스트 필터링 함수 뼈대
private fun filterText(text: String, predicate: TextPredicate): String? {
    // TODO: 여기에 코드를 작성하세요 (takeIf 활용)
    TODO()
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

    println("✅ Quiz01 테스트 통과! (Type Alias & takeIf/takeUnless 기초 완벽 체화)")
}
