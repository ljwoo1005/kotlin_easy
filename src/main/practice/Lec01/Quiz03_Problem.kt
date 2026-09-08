package Lec01

/*
 * [학습 목표 & 복습 개념]
 * - 실무 비즈니스 모델링에서 불변 프로퍼티(val)와 가변 프로퍼티(var)의 역할을 체화합니다.
 * - 선택적 입력 필드를 Nullable 타입(String?)으로 안전하게 다루는 방법을 학습합니다.
 * - new 키워드 없는 인스턴스화와 프로퍼티 값 수정 흐름을 실전 시나리오로 복습합니다.
 *
 * [문제 설명]
 * 서비스의 신규 회원 프로필 생성 및 정보 갱신을 담당하는 두 함수를 완성하세요.
 *
 * 1. createUserProfile(userId: Long, nickname: String): Quiz03UserProfile
 *    - 주어진 회원 ID와 닉네임으로 새로운 Quiz03UserProfile 인스턴스를 생성합니다.
 *    - 초기 가입 시 자기소개(bio)는 null, 로그인 횟수(loginCount)는 1L로 설정합니다.
 *
 * 2. updateUserProfile(profile: Quiz03UserProfile, newNickname: String, newBio: String?): Quiz03UserProfile
 *    - 전달받은 profile 객체의 닉네임을 newNickname으로 갱신합니다.
 *    - profile의 bio를 newBio(문자열 또는 null)로 갱신합니다.
 *    - 로그인 횟수(loginCount)를 1 증가시킵니다.
 *    - 갱신된 profile 객체를 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - Quiz03UserProfile 객체를 생성할 때 `new` 키워드를 사용하지 마세요.
 * - 불변 식별자인 `userId`는 수정하지 마세요.
 * - 가변 프로퍼티인 `nickname`, `bio`, `loginCount`를 알맞게 갱신하세요.
 * - `bio`는 null이 될 수 있으므로 Nullable 타입(String?)을 적절히 처리하세요.
 *
 * [입출력 및 기대 결과]
 * - createUserProfile(1L, "코린이")
 *   -> Quiz03UserProfile(userId=1L, nickname="코린이", bio=null, loginCount=1L)
 * - updateUserProfile(user, "코틀린고수", "열공 중입니다!")
 *   -> Quiz03UserProfile(userId=1L, nickname="코틀린고수", bio="열공 중입니다!", loginCount=2L)
 * - updateUserProfile(user, "코틀린마스터", null)
 *   -> Quiz03UserProfile(userId=1L, nickname="코틀린마스터", bio=null, loginCount=3L)
 *
 * [💡 HINT]
 * - 코틀린의 data class 인스턴스는 new 없이 `클래스명(...)` 형태로 생성합니다.
 * - `var`로 선언된 프로퍼티는 `.프로퍼티명 = 새값` 형태로 값을 직접 변경할 수 있습니다.
 */

// 실습용 사용자 프로필 데이터 클래스
data class Quiz03UserProfile(
    val userId: Long,
    var nickname: String,
    var bio: String?,
    var loginCount: Long
)

// 1. 신규 프로필 생성 함수 뼈대
private fun createUserProfile(userId: Long, nickname: String): Quiz03UserProfile {
    TODO()
}

// 2. 프로필 정보 갱신 함수 뼈대
private fun updateUserProfile(
    profile: Quiz03UserProfile,
    newNickname: String,
    newBio: String?
): Quiz03UserProfile {
    TODO()
}

fun main() {
    // 1. 신규 회원 프로필 생성 검증
    val user = createUserProfile(1L, "코린이")
    check(user.userId == 1L) { "회원 ID는 1L이어야 합니다. (현재: ${user.userId})" }
    check(user.nickname == "코린이") { "닉네임은 '코린이'이어야 합니다. (현재: ${user.nickname})" }
    check(user.bio == null) { "초기 자기소개(bio)는 null이어야 합니다. (현재: ${user.bio})" }
    check(user.loginCount == 1L) { "초기 로그인 횟수는 1L이어야 합니다. (현재: ${user.loginCount})" }

    // 2. 프로필 갱신 검증 (자기소개 작성)
    val updatedUser = updateUserProfile(user, "코틀린고수", "열공 중입니다!")
    check(updatedUser.userId == 1L) { "회원 ID는 불변이어야 합니다." }
    check(updatedUser.nickname == "코틀린고수") { "닉네임이 '코틀린고수'로 갱신되어야 합니다. (현재: ${updatedUser.nickname})" }
    check(updatedUser.bio == "열공 중입니다!") { "자기소개가 '열공 중입니다!'로 갱신되어야 합니다. (현재: ${updatedUser.bio})" }
    check(updatedUser.loginCount == 2L) { "로그인 횟수가 2L로 증가해야 합니다. (현재: ${updatedUser.loginCount})" }

    // 3. 프로필 갱신 검증 (자기소개 삭제: null 입력)
    val clearedBioUser = updateUserProfile(updatedUser, "코틀린마스터", null)
    check(clearedBioUser.nickname == "코틀린마스터") { "닉네임이 '코틀린마스터'로 갱신되어야 합니다. (현재: ${clearedBioUser.nickname})" }
    check(clearedBioUser.bio == null) { "자기소개가 null로 초기화되어야 합니다. (현재: ${clearedBioUser.bio})" }
    check(clearedBioUser.loginCount == 3L) { "로그인 횟수가 3L로 증가해야 합니다. (현재: ${clearedBioUser.loginCount})" }

    println("✅ Quiz03 테스트 통과! (미니 시나리오 검증 성공)")
}
