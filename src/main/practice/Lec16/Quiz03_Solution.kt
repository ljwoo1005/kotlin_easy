package Lec16

/*
 * [핵심 해결 아이디어]
 * - 지역 함수(Local Function)는 함수 내부에서만 사용되는 유틸리티성 로직(예: 파라미터 유효성 검증)을 캡슐화하기에 최적입니다.
 * - 지역 함수를 사용하면 동일한 검증 로직(`isBlank()` 체크, 음수 체크 등)의 코드 중복을 말끔히 제거하면서도 외부 클래스로 불필요하게 스코프를 넓히지 않습니다.
 * - 데이터 클래스에 비즈니스 표시 로직을 확장 함수(`formattedSummary`)로 분리하여 데이터 모델의 순수성을 유지하고, 불변 갱신을 돕는 `copy`와 중위 함수(`addBonus`)를 조합하여 안전하고 명확한 비즈니스 코드를 작성합니다.
 */

// 실습용 사용자 도메인 데이터 클래스(Quiz16User)는 Quiz03_Problem.kt에 선언된 모델을 공유합니다.

// 1. 지역 함수를 활용한 회원 등록 모범 답안 함수
private fun registerUser(username: String, email: String, initialPoint: Int): Quiz16User {
    // 1-1. 문자열 공백 검증을 위한 지역 함수 선언
    fun validateNotBlank(value: String, fieldName: String) {
        if (value.isBlank()) {
            throw IllegalArgumentException("${fieldName}은 비어있을 수 없습니다.")
        }
    }

    // 1-2. 정수 음수 검증을 위한 지역 함수 선언
    fun validateNotNegative(value: Int, fieldName: String) {
        if (value < 0) {
            throw IllegalArgumentException("${fieldName}은 0 이상이어야 합니다.")
        }
    }

    // 지역 함수를 호출하여 각 필드의 유효성 검증 수행
    validateNotBlank(username, "username")
    validateNotBlank(email, "email")
    validateNotNegative(initialPoint, "initialPoint")

    // 검증을 무사히 통과하면 인스턴스 생성 및 반환
    return Quiz16User(username, email, initialPoint)
}

// 2. 사용자 요약 정보 모범 답안 확장 함수
private fun Quiz16User.formattedSummary(): String {
    // 문자열 템플릿을 활용하여 수신 객체(this)의 속성들을 깔끔하게 포맷팅
    return "[${this.username}] 이메일: ${this.email}, 포인트: ${this.point}P"
}

// 3. 보너스 포인트 지급 모범 답안 중위 함수
private infix fun Quiz16User.addBonus(bonus: Int): Quiz16User {
    // 보너스 금액에 대한 비즈니스 제약조건 검증
    if (bonus <= 0) {
        throw IllegalArgumentException("보너스 포인트는 1 이상이어야 합니다.")
    }
    // 데이터 클래스의 copy 기능을 활용하여 기존 상태는 보존하고 포인트만 갱신된 새 객체 반환
    return this.copy(point = this.point + bonus)
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

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 안전한 스코프 격리: 자바에서는 함수 내부에서만 쓰는 유효성 검증 로직도 클래스 레벨의 `private static` 메서드로 분리해야 해서 클래스가 장황해지기 쉽습니다. 코틀린은 함수 내에 지역 함수(Local Function)를 둘 수 있어 스코프를 완벽히 격리하고 응집도를 높입니다.
 * 2. 외부 스코프 접근성: 지역 함수는 자신이 선언된 바깥 함수의 파라미터나 로컬 변수에 직접 접근할 수 있어, 자바처럼 많은 인자를 일일이 넘기지 않아도 편리하게 작업할 수 있습니다.
 * 3. 데이터 클래스의 불변 복사: 자바에서는 일부 필드만 변경된 새 객체를 만들 때 복잡한 빌더 패턴이나 수동 생성자 호출이 필요하지만, 코틀린은 `copy()`를 통해 단 한 줄로 불변 객체를 생성할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 지역 함수의 과도한 중첩: 지역 함수를 너무 많이 선언하거나 depth가 깊어지면 오히려 가독성이 저하될 수 있습니다. 만약 해당 유효성 검사 로직이 여러 함수에 걸쳐 공통으로 쓰인다면 도메인 객체 내부의 `init` 블록이나 별도의 검증 클래스로 승격시키는 것이 바람직합니다.
 * 2. 원본 객체 변경 시도(가변성 함정): 함수형 스타일과 불변성을 권장하는 코틀린에서는 객체의 상태를 직접 변경(`user.point += bonus`)하기보다는 `copy()`를 통해 새로운 상태를 가진 인스턴스를 반환하는 방식을 지향해야 부수 효과(side-effect)를 방지할 수 있습니다.
 */
