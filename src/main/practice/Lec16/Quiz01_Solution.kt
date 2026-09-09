package Lec16

/*
 * [핵심 해결 아이디어]
 * - 확장 함수는 클래스 멤버를 수정하지 않고도 외부에서 `fun 수신객체타입.함수명()` 형태로 기능을 덧붙일 수 있습니다.
 * - 확장 함수 본문 내에서 `this`를 통해 수신 객체의 인스턴스에 접근할 수 있으며, 필요 시 `this`는 생략 가능합니다.
 * - 확장 프로퍼티는 백킹 필드(상태 저장소)가 없으므로 `custom getter`(`get() = ...`)를 통해 매번 계산된 값을 제공해야 합니다.
 * - `maskTail`은 `substring`으로 보존할 앞부분을 자르고, 코틀린 표준의 `repeat` 함수를 활용해 나머지 길이를 `'*'`로 간결하게 결합합니다.
 */

// 1. 문자열 첫 글자 반환 모범 답안 확장 함수
private fun String.firstChar(): Char {
    // 빈 문자열인 경우 요구사항에 따라 예외 발생
    if (this.isEmpty()) {
        throw IllegalArgumentException("문자열이 비어 있습니다.")
    }
    // 첫 번째 인덱스 문자 반환 (this[0] 또는 first() 활용)
    return this[0]
}

// 2. 짝수 여부 확인 모범 답안 확장 프로퍼티
private val Int.isEven: Boolean
    // 커스텀 게터를 통해 수신 객체(this)가 2로 나누어떨어지는지 검사
    get() = this % 2 == 0

// 3. 뒷자리 마스킹 모범 답안 확장 함수
private fun String.maskTail(visibleCount: Int): String {
    // visibleCount가 음수일 경우 예외 발생
    if (visibleCount < 0) {
        throw IllegalArgumentException("visibleCount는 0 이상이어야 합니다.")
    }

    // 마스킹할 필요가 없는 경우(문자열 길이 이상) 원본 그대로 반환
    if (visibleCount >= this.length) {
        return this
    }

    // 보존할 앞부분 추출
    val prefix = this.substring(0, visibleCount)
    // 마스킹할 나머지 글자 수만큼 '*' 반복 생성
    val maskedPart = "*".repeat(this.length - visibleCount)

    return prefix + maskedPart
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

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 직관적인 점 표기법 호출: 자바에서는 `StringUtils.firstChar(str)`처럼 유틸리티 클래스를 거쳐 호출해야 했지만, 코틀린은 `str.firstChar()`처럼 객체 지향적으로 직관적인 호출이 가능하며 IDE의 자동완성 지원을 극대화합니다.
 * 2. 간결한 프로퍼티 접근 문법: 메서드 호출 형태(`num.isEven()`) 대신 `num.isEven`처럼 필드에 접근하는 듯한 자연스러운 표현을 제공합니다.
 * 3. 기존 라이브러리 클래스 확장: JDK에 이미 내장된 `String`이나 `Int`와 같은 final 클래스에도 추가적인 수정 없이 손쉽게 프로젝트 맞춤 유틸 함수를 부착할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 캡슐화 무시: 확장 함수는 클래스 외부에 정의되므로, 대상 클래스의 `private` 또는 `protected` 멤버에는 절대로 접근할 수 없습니다.
 * 2. 멤버 함수와의 시그니처 중복: 동일한 시그니처(이름, 매개변수 타입)를 가진 멤버 함수가 클래스 내부에 이미 존재한다면, 코틀린 컴파일러는 항상 멤버 함수를 최우선으로 호출합니다.
 * 3. 확장 프로퍼티 필드 선언 시도: 확장 프로퍼티는 실제 메모리 공간(backing field)을 갖지 않으므로 `= ""` 형태로 초기화할 수 없으며, 반드시 `get()` 게터를 작성해야 합니다.
 */
