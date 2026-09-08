package Lec01

/*
 * [핵심 해결 아이디어]
 * - 불변 데이터(식별자 userId 등)는 'val'로 선언하여 무결성을 유지하고, 변경 가능한 상태(nickname, bio, loginCount)는 'var'로 관리합니다.
 * - 선택적 입력이나 미정 값은 Nullable 타입('String?')으로 표현하여 컴파일 타임에 안전하게 null을 다룹니다.
 * - 코틀린에서는 'new' 키워드 없이 클래스 생성자를 직접 호출하여 인스턴스를 간결하게 생성합니다.
 */

// 실습용 사용자 프로필 데이터 클래스(Quiz03UserProfile)는 Quiz03_Problem.kt에 정의된 모델을 사용합니다.

// 1. 신규 프로필 생성 모범 답안 함수
private fun createUserProfile(userId: Long, nickname: String): Quiz03UserProfile {
    // new 키워드 없이 인스턴스를 생성하며, 미입력 bio는 null, 초기 loginCount는 1L로 설정
    return Quiz03UserProfile(
        userId = userId,
        nickname = nickname,
        bio = null,
        loginCount = 1L
    )
}

// 2. 프로필 정보 갱신 모범 답안 함수
private fun updateUserProfile(
    profile: Quiz03UserProfile,
    newNickname: String,
    newBio: String?
): Quiz03UserProfile {
    // var 프로퍼티에 새로운 값 할당 (가변성 활용)
    profile.nickname = newNickname
    profile.bio = newBio
    // 로그인 카운트 1 증가
    profile.loginCount += 1L
    return profile
}

fun main() {
    // 1. 신규 회원 프로필 생성 검증
    val user = createUserProfile(1L, "코린이")
    check(user.userId == 1L) { "회원 ID는 1L이어야 합니다. (현재: ${user.userId})" }
    check(user.nickname == "코린이") { "닉네임은 '코린이'이어야 합니다." }
    check(user.bio == null) { "초기 자기소개(bio)는 null이어야 합니다." }
    check(user.loginCount == 1L) { "초기 로그인 횟수는 1L이어야 합니다." }

    // 2. 프로필 갱신 검증 (자기소개 작성)
    val updatedUser = updateUserProfile(user, "코틀린고수", "열공 중입니다!")
    check(updatedUser.userId == 1L) { "회원 ID는 불변이어야 합니다." }
    check(updatedUser.nickname == "코틀린고수") { "닉네임이 '코틀린고수'로 갱신되어야 합니다. (현재: ${updatedUser.nickname})" }
    check(updatedUser.bio == "열공 중입니다!") { "자기소개가 '열공 중입니다!'로 갱신되어야 합니다. (현재: ${updatedUser.bio})" }
    check(updatedUser.loginCount == 2L) { "로그인 횟수가 2L로 증가해야 합니다. (현재: ${updatedUser.loginCount})" }

    // 3. 프로필 갱신 검증 (자기소개 삭제: null 입력)
    val clearedBioUser = updateUserProfile(updatedUser, "코틀린마스터", null)
    check(clearedBioUser.nickname == "코틀린마스터") { "닉네임이 '코틀린마스터'로 갱신되어야 합니다. (현재: ${clearedBioUser.nickname})" }
    check(clearedBioUser.bio == null) { "자기소개가 null로 초기화되어야 합니다." }
    check(clearedBioUser.loginCount == 3L) { "로그인 횟수가 3L로 증가해야 합니다. (현재: ${clearedBioUser.loginCount})" }

    println("✅ Quiz03 해답 검증 통과! (미니 시나리오 모범 답안 정상 동작)")
}

/*
 * [자바 대비 장점]
 * 1. 간결한 생성자 호출: 'new UserProfile(...)' 대신 함수처럼 호출하여 문법적 잡음(Noise)을 줄입니다.
 * 2. 명확한 프로퍼티 가변성 제어: 클래스 선언 단계에서부터 'val'(읽기 전용)과 'var'(가변)을 명시하여, 불변성을 직관적으로 제어할 수 있습니다.
 * 3. 널 안전성(Null Safety): 'String?' 타입을 통해 자기소개가 비어있을 수 있음을 타입 시스템에서 명확히 보장하므로 예기치 않은 NPE를 예방합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 불변이어야 할 식별자를 'var'로 선언: 데이터 모델링 시 모든 필드를 관성적으로 'var'로 선언하면 데이터 오염 위험이 커집니다. 변경되면 안 되는 식별자(ID) 등은 항상 'val'로 선언해야 합니다.
 * 2. Nullable 매개변수 타입 처리 시 기본 타입으로 선언: null이 들어올 수 있는 자리에 'String'으로 선언하면 null을 넘길 때 컴파일 에러가 발생하므로 'String?'으로 선언해야 합니다.
 */
