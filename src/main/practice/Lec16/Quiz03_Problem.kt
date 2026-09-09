package Lec16

/*
 * [학습 목표 & 복습 개념]
 * - 함수 내부에서 선언되어 특정 함수 스코프 내에서만 재사용되는 코틀린 지역 함수(Local Function)의 문법과 실무 활용 패턴을 체화합니다.
 * - 반복되는 파라미터 유효성 검사 로직을 지역 함수로 추출하여 코드 중복을 제거하고 캡슐화하는 방법을 학습합니다.
 * - 도메인 데이터 클래스에 확장 함수(Extension Function)와 중위 함수(`infix`)를 결합하여 풍부한 비즈니스 표현력을 구현합니다.
 *
 * [문제 설명]
 * 회원 가입 및 포인트 적립을 처리하는 비즈니스 시스템의 일부 기능을 완성하세요.
 *
 * 1. registerUser(username: String, email: String, initialPoint: Int): Quiz16User
 *    - 회원 가입 정보를 받아 유효성을 검증한 후 `Quiz16User` 인스턴스를 반환합니다.
 *    - 검증 로직의 중복을 방지하기 위해 함수 내부에 반드시 다음 2개의 **지역 함수(Local Function)**를 선언하여 활용하세요:
 *      - `fun validateNotBlank(value: String, fieldName: String)`: 공백이거나 비어있으면 IllegalArgumentException("${fieldName}은 비어있을 수 없습니다.") 발생
 *      - `fun validateNotNegative(value: Int, fieldName: String)`: 0 미만인 음수이면 IllegalArgumentException("${fieldName}은 0 이상이어야 합니다.") 발생
 *    - `username`과 `email`은 `validateNotBlank`로 검증하고, `initialPoint`는 `validateNotNegative`로 검증합니다.
 *
 * 2. Quiz16User.formattedSummary(): String
 *    - 사용자의 기본 정보를 `"[${username}] 이메일: ${email}, 포인트: ${point}P"` 형식의 문자열로 반환하는 확장 함수입니다.
 *
 * 3. infix fun Quiz16User.addBonus(bonus: Int): Quiz16User
 *    - 기존 사용자의 포인트에 보너스를 합산한 새로운 `Quiz16User` 객체를 반환하는 중위 함수입니다.
 *    - `bonus <= 0`인 경우 IllegalArgumentException("보너스 포인트는 1 이상이어야 합니다.")을 던집니다.
 *    - 원본 객체를 직접 변경하지 않고, 데이터 클래스의 `copy` 기능을 활용하여 불변 객체를 생성해 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - registerUser: 내부에 2개의 지역 함수(`validateNotBlank`, `validateNotNegative`)를 반드시 작성하세요.
 * - formattedSummary: fun Quiz16User.formattedSummary(): String 확장 함수로 선언하세요.
 * - addBonus: infix fun Quiz16User.addBonus(bonus: Int): Quiz16User 중위 함수로 선언하고 `copy`를 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - registerUser("홍길동", "hong@test.com", 1000) -> Quiz16User("홍길동", "hong@test.com", 1000)
 * - registerUser("  ", "hong@test.com", 1000)     -> IllegalArgumentException 발생
 * - registerUser("홍길동", "", 1000)             -> IllegalArgumentException 발생
 * - registerUser("홍길동", "hong@test.com", -10)  -> IllegalArgumentException 발생
 * - user.formattedSummary()                      -> "[홍길동] 이메일: hong@test.com, 포인트: 1000P"
 * - user addBonus 500                            -> Quiz16User("홍길동", "hong@test.com", 1500)
 * - user addBonus 0                              -> IllegalArgumentException 발생
 *
 * [💡 HINT]
 * - 지역 함수는 자신이 정의된 외부 함수의 매개변수나 변수를 자유롭게 공유할 수 있습니다.
 * - 문자열 공백 여부는 `value.isBlank()`를 활용하면 빈 문자열(`""`)과 공백 문자열(`"   "`)을 모두 손쉽게 검증할 수 있습니다.
 * - 데이터 클래스의 `copy(point = this.point + bonus)`를 사용하면 나머지 필드는 그대로 유지한 채 특정 필드만 갱신된 새 인스턴스를 얻을 수 있습니다.
 */

// 실습용 사용자 도메인 데이터 클래스
data class Quiz16User(
    val username: String,
    val email: String,
    val point: Int
)

// 1. 지역 함수를 활용한 회원 등록 함수 뼈대
private fun registerUser(username: String, email: String, initialPoint: Int): Quiz16User {
    TODO()
}

// 2. 사용자 요약 정보 확장 함수 뼈대
private fun Quiz16User.formattedSummary(): String {
    TODO()
}

// 3. 보너스 포인트 지급 중위 함수 뼈대
private infix fun Quiz16User.addBonus(bonus: Int): Quiz16User {
    TODO()
}

fun main() {
    // 1. 정상 회원 등록 및 요약 확장 함수 검증
    val user = registerUser("홍길동", "hong@example.com", 1000)
    check(user.username == "홍길동") { "사용자 이름이 올바르지 않습니다." }
    check(user.email == "hong@example.com") { "사용자 이메일이 올바르지 않습니다." }
    check(user.point == 1000) { "초기 포인트는 1000이어야 합니다." }
    check(user.formattedSummary() == "[홍길동] 이메일: hong@example.com, 포인트: 1000P") {
        "formattedSummary 결과가 기대와 다릅니다: ${user.formattedSummary()}"
    }

    // 2. 지역 함수를 통한 유효성 검증 예외 발생 확인
    val blankNameThrown = runCatching { registerUser("   ", "hong@example.com", 1000) }.isFailure
    check(blankNameThrown) { "공백 이름에 대해 IllegalArgumentException이 발생해야 합니다." }

    val emptyEmailThrown = runCatching { registerUser("홍길동", "", 1000) }.isFailure
    check(emptyEmailThrown) { "빈 이메일에 대해 IllegalArgumentException이 발생해야 합니다." }

    val negativePointThrown = runCatching { registerUser("홍길동", "hong@example.com", -100) }.isFailure
    check(negativePointThrown) { "음수 포인트에 대해 IllegalArgumentException이 발생해야 합니다." }

    // 3. addBonus 중위 함수 검증 (일반 호출 및 중위 호출)
    val updatedUser = user addBonus 500
    check(updatedUser.point == 1500) { "보너스 적립 후 포인트는 1500이어야 합니다." }
    check(user.point == 1000) { "원본 객체는 불변이어야 하므로 1000P를 유지해야 합니다." }

    val methodCallUser = user.addBonus(200)
    check(methodCallUser.point == 1200) { "일반 호출로도 정상 적립되어야 합니다." }

    val zeroBonusThrown = runCatching { user addBonus 0 }.isFailure
    check(zeroBonusThrown) { "0 이하의 보너스 포인트는 예외가 발생해야 합니다." }

    val negativeBonusThrown = runCatching { user addBonus -50 }.isFailure
    check(negativeBonusThrown) { "음수 보너스 포인트는 예외가 발생해야 합니다." }

    println("✅ Quiz03 테스트 통과! (지역 함수 유효성 검증 및 확장/중위 비즈니스 로직 완료)")
}
