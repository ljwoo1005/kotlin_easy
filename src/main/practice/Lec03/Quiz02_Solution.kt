package Lec03

/*
 * [핵심 해결 아이디어]
 * - 코틀린의 'is' 연산자로 타입을 검사하면 컴파일러가 해당 스코프 안에서 대상을 자동으로 스마트 캐스트(Smart Cast)하므로 번거로운 명시적 형변환을 제거합니다.
 * - 'as?' 연산자는 실패 시 ClassCastException을 발생시키는 대신 null을 안전하게 반환하며, Safe Call(?.)과 결합하여 단 한 줄로 프로퍼티에 접근할 수 있습니다.
 * - 문자열 템플릿(${})을 활용하여 객체의 프로퍼티를 가독성 높고 깔끔하게 표현식 내에 삽입합니다.
 */

// 실습용 도메인 데이터 클래스(Quiz02Person)는 Quiz02_Problem.kt에 정의된 모델을 공유합니다.

// 1. 사람 정보 포맷팅 모범 답안 함수 (스마트 캐스트 활용)
private fun formatPerson(obj: Any?): String {
    // is 검사를 통과하면 obj는 블록 안에서 자동으로 Quiz02Person으로 스마트 캐스트됨
    return if (obj is Quiz02Person) {
        "${obj.name}(${obj.age}세)"
    } else {
        "알 수 없는 대상"
    }
}

// 2. 안전한 나이 조회 모범 답안 함수 (as? 연산자 활용 단일 표현식)
private fun getAgeOrNull(obj: Any?): Int? {
    // as?로 안전하게 캐스팅하고, Safe Call(?.)로 age 프로퍼티를 추출
    return (obj as? Quiz02Person)?.age
}

fun main() {
    val person1 = Quiz02Person("홍길동", 25)
    val person2 = Quiz02Person("이순신", 40)

    // 1. formatPerson 검증
    check(formatPerson(person1) == "홍길동(25세)") { "정상 객체는 '홍길동(25세)'여야 합니다. (현재: ${formatPerson(person1)})" }
    check(formatPerson("임의의문자열") == "알 수 없는 대상") { "다른 타입은 '알 수 없는 대상'이어야 합니다. (현재: ${formatPerson("임의의문자열")})" }
    check(formatPerson(123) == "알 수 없는 대상") { "숫자 타입은 '알 수 없는 대상'이어야 합니다. (현재: ${formatPerson(123)})" }
    check(formatPerson(null) == "알 수 없는 대상") { "null은 '알 수 없는 대상'이어야 합니다. (현재: ${formatPerson(null)})" }

    // 2. getAgeOrNull 검증
    check(getAgeOrNull(person2) == 40) { "person2의 나이는 40이어야 합니다. (현재: ${getAgeOrNull(person2)})" }
    check(getAgeOrNull("문자열") == null) { "문자열 입력 시 null이어야 합니다. (현재: ${getAgeOrNull("문자열")})" }
    check(getAgeOrNull(null) == null) { "null 입력 시 null이어야 합니다. (현재: ${getAgeOrNull(null)})" }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 스마트 캐스트(Smart Cast): 자바에서는 'if (obj instanceof Person) { Person p = (Person) obj; ... }'와 같이 타입 확인 후 반드시 수동 캐스팅을 해야 하지만, 코틀린은 컴파일러가 타입 검증 여부를 추적하여 별도 캐스팅 없이 즉시 멤버에 접근할 수 있습니다.
 * 2. 안전한 형변환(as?): 자바에서 잘못된 타입 캐스팅 시 ClassCastException 예외를 피하려면 사전에 항상 if-instanceof 검사를 거쳐야 합니다. 반면 코틀린은 'as?' 연산자 하나로 실패 시 null을 반환하도록 깔끔하게 안전 처리가 가능합니다.
 * 3. 직관적인 문자열 템플릿: String.format("%s(%d세)", ...)나 번거로운 문자열 '+' 연결 대신 "${obj.name}(${obj.age}세)"로 직관적이고 깔끔한 코드를 작성할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 불필요한 'as' 강제 캐스팅: 스마트 캐스트가 이미 적용되는 'if (obj is Type)' 내부에서 습관적으로 'val x = obj as Type'을 다시 작성하는 것은 중복 코드입니다.
 * 2. 'as' vs 'as?'의 혼동: 'obj as Type'은 obj가 null이거나 Type이 아닐 때 즉시 예외(ClassCastException 또는 NPE)를 던집니다. null 가능성이 있거나 타입 불일치가 정상적인 비즈니스 흐름이라면 반드시 'as?'를 사용해야 합니다.
 */
