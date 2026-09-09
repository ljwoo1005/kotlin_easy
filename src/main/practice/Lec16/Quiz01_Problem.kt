package Lec16

/*
 * [학습 목표 & 복습 개념]
 * - 클래스 외부에서 정의되지만 마치 멤버 메서드처럼 호출되는 코틀린 확장 함수(Extension Function)의 기본 문법을 체화합니다.
 * - 확장 함수 내부에서 수신 객체(`this`)를 다루는 방법과 빈 문자열에 대한 방어적 예외 처리를 학습합니다.
 * - 백킹 필드 없이 커스텀 게터(`get()`)를 통해 계산된 값을 반환하는 확장 프로퍼티(Extension Property)를 구현합니다.
 *
 * [문제 설명]
 * 확장 함수와 확장 프로퍼티를 활용하여 다음 3가지 기능을 구현하세요.
 *
 * 1. String.firstChar(): Char
 *    - 문자열의 첫 번째 문자를 반환하는 확장 함수입니다.
 *    - 만약 빈 문자열(`""`)인 경우 IllegalArgumentException("문자열이 비어 있습니다.")을 던집니다.
 *
 * 2. Int.isEven: Boolean
 *    - 정수가 짝수인지 여부를 반환하는 확장 프로퍼티입니다. (커스텀 게터 활용)
 *    - 0과 음수 짝수(-2, -4 등)도 짝수로 간주합니다.
 *
 * 3. String.maskTail(visibleCount: Int): String
 *    - 문자열의 앞 visibleCount 글자를 제외한 나머지 뒷부분을 전부 '*' 문자로 마스킹하여 반환하는 확장 함수입니다.
 *    - visibleCount가 음수인 경우 IllegalArgumentException("visibleCount는 0 이상이어야 합니다.")을 던집니다.
 *    - visibleCount가 문자열의 길이 이상인 경우, 마스킹할 글자가 없으므로 원본 문자열을 그대로 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - firstChar: fun String.firstChar(): Char 시그니처를 사용하고, 수신 객체의 길이나 isEmpty()를 확인하세요.
 * - isEven: val Int.isEven: Boolean 시그니처와 get() 커스텀 게터를 반드시 사용하세요.
 * - maskTail: fun String.maskTail(visibleCount: Int): String 시그니처를 사용하세요.
 * - maskTail: 문자열 슬라이싱(substring)과 반복 문자 생성을 활용하여 간결하게 작성하세요.
 *
 * [입출력 및 기대 결과]
 * - "Hello".firstChar()           -> 'H'
 * - "".firstChar()                -> IllegalArgumentException 발생
 * - 4.isEven                      -> true
 * - 7.isEven                      -> false
 * - 0.isEven                      -> true
 * - "kotlin".maskTail(2)          -> "ko****"
 * - "secret".maskTail(0)          -> "******"
 * - "abc".maskTail(5)             -> "abc"
 * - "abc".maskTail(-1)            -> IllegalArgumentException 발생
 *
 * [💡 HINT]
 * - 확장 함수 내부에서 확장 대상 인스턴스는 `this` 키워드로 참조할 수 있습니다.
 * - 확장 프로퍼티는 값을 저장할 수 있는 필드(field)가 없으므로 대입 연산자(=) 대신 `get() = ...` 문법을 사용해야 합니다.
 * - 문자열의 특정 문자를 반복할 때는 `"*".repeat(count)` 함수를 유용하게 활용할 수 있습니다.
 */

// 1. 문자열 첫 글자 반환 확장 함수 뼈대
private fun String.firstChar(): Char {
    TODO()
}

// 2. 짝수 여부 확인 확장 프로퍼티 뼈대
private val Int.isEven: Boolean
    get() = TODO()

// 3. 뒷자리 마스킹 확장 함수 뼈대
private fun String.maskTail(visibleCount: Int): String {
    TODO()
}


fun main() {
    // 1. firstChar 검증
    check("Kotlin".firstChar() == 'K') { "첫 글자는 'K'여야 합니다." }
    check("A".firstChar() == 'A') { "단일 문자열 첫 글자는 'A'여야 합니다." }

    val emptyStringThrown = runCatching { "".firstChar() }.isFailure
    check(emptyStringThrown) { "빈 문자열에 firstChar 호출 시 IllegalArgumentException이 발생해야 합니다." }

    // 2. isEven 검증
    check(2.isEven) { "2는 짝수여야 합니다." }
    check(0.isEven) { "0은 짝수여야 합니다." }
    check((-4).isEven) { "-4는 짝수여야 합니다." }
    check(!3.isEven) { "3은 홀수여야 합니다." }
    check(!(-5).isEven) { "-5는 홀수여야 합니다." }

    // 3. maskTail 검증
    check("kotlin".maskTail(2) == "ko****") { "앞 2자리 유지 마스킹 결과는 'ko****'이어야 합니다." }
    check("secret".maskTail(0) == "******") { "앞 0자리 유지 마스킹 결과는 '******'이어야 합니다." }
    check("abc".maskTail(3) == "abc") { "visibleCount가 길이와 같으면 원본 그대로여야 합니다." }
    check("abc".maskTail(10) == "abc") { "visibleCount가 길이보다 커도 원본 그대로여야 합니다." }

    val negativeCountThrown = runCatching { "test".maskTail(-1) }.isFailure
    check(negativeCountThrown) { "visibleCount가 음수이면 IllegalArgumentException이 발생해야 합니다." }

    println("✅ Quiz01 테스트 통과! (확장 함수 및 확장 프로퍼티 체화 완료)")
}
