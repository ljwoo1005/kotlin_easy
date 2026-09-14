package Lec19

/*
 * [핵심 해결 아이디어]
 * - 코틀린의 구조분해(Destructuring Declaration)는 타입 기반이 아닌 메서드 이름 규약(`componentN`)에 의해 동작합니다.
 * - 일반 클래스라 하더라도 `operator fun component1()`, `operator fun component2()`를 구현해주면
 *   컴파일러가 이를 인식하여 `val (a, b) = instance` 구문으로 분해할 수 있도록 지원합니다.
 * - `data class`는 주 생성자의 프로퍼티들에 대해 `componentN()` 메서드를 자동으로 생성하므로,
 *   불필요한 프로퍼티는 언더스코어(`_`)로 무시하면서 원하는 필드만 깔끔하게 구조분해할 수 있습니다.
 */

// Quiz19Employee 데이터 클래스는 Quiz02_Problem.kt에 선언된 모델을 공유합니다.

// 1. 일반 클래스 2차원 좌표 모델 모범 구현
// (Quiz02_Problem.kt의 Quiz19Point 클래스와의 충돌을 방지하고 즉시 독립 실행을 검증할 수 있도록 Quiz19SolutionPoint로 명명)
class Quiz19SolutionPoint(
    val x: Int,
    val y: Int
) {
    // operator 키워드를 붙여 구조분해의 첫 번째 요소(component1)로 x 반환
    operator fun component1(): Int = this.x

    // operator 키워드를 붙여 구조분해의 두 번째 요소(component2)로 y 반환
    operator fun component2(): Int = this.y
}

// 2. 원점 또는 대각선 좌표 판별 모범 답안
private fun isOriginOrDiagonal(point: Quiz19SolutionPoint): Boolean {
    // operator fun component1, component2를 활용한 구조분해 선언
    val (x, y) = point

    // 원점((0, 0))이거나 대각선상(x == y)인지 여부 반환
    return (x == 0 && y == 0) || (x == y)
}

// 3. 특정 부서 최고 급여 직원 조회 모범 답안
private fun findTopEarnerInDept(employees: List<Quiz19Employee>, targetDept: String): Pair<String, Int>? {
    // 1) 해당 부서에 소속된 직원들만 필터링
    val deptEmployees = employees.filter { it.department == targetDept }

    // 2) 최고 급여를 받는 직원을 조회 (없으면 null)
    val topEarner = deptEmployees.maxByOrNull { it.salary } ?: return null

    // 3) data class의 componentN()을 활용한 구조분해 (필요한 name, salary만 추출)
    val (_, name, _, salary) = topEarner

    return Pair(name, salary)
}

fun main() {
    // 1. Quiz19SolutionPoint 구조분해 및 isOriginOrDiagonal 검증
    val p1 = Quiz19SolutionPoint(0, 0)
    val (x1, y1) = p1
    check(x1 == 0 && y1 == 0) { "Quiz19SolutionPoint의 구조분해가 올바르게 동작해야 합니다. (0, 0)" }
    check(isOriginOrDiagonal(p1)) { "원점 (0, 0)은 true를 반환해야 합니다." }

    val p2 = Quiz19SolutionPoint(7, 7)
    check(isOriginOrDiagonal(p2)) { "대각선 좌표 (7, 7)은 true를 반환해야 합니다." }

    val p3 = Quiz19SolutionPoint(3, 8)
    val (x3, y3) = p3
    check(x3 == 3 && y3 == 8) { "Quiz19SolutionPoint의 구조분해가 올바르게 동작해야 합니다. (3, 8)" }
    check(!isOriginOrDiagonal(p3)) { "(3, 8)은 대각선이나 원점이 아니므로 false여야 합니다." }

    // 2. findTopEarnerInDept 검증
    val sampleEmployees = listOf(
        Quiz19Employee(1L, "이영희", "개발팀", 4000),
        Quiz19Employee(2L, "김개발", "개발팀", 5500),
        Quiz19Employee(3L, "박디자인", "디자인팀", 4200),
        Quiz19Employee(4L, "최개발", "개발팀", 5000),
        Quiz19Employee(5L, "정디자인", "디자인팀", 4500)
    )

    val devTop = findTopEarnerInDept(sampleEmployees, "개발팀")
    check(devTop == Pair("김개발", 5500)) {
        "개발팀 최고 급여자는 Pair('김개발', 5500)이어야 합니다. 실제: $devTop"
    }

    val designTop = findTopEarnerInDept(sampleEmployees, "디자인팀")
    check(designTop == Pair("정디자인", 4500)) {
        "디자인팀 최고 급여자는 Pair('정디자인', 4500)이어야 합니다. 실제: $designTop"
    }

    val planTop = findTopEarnerInDept(sampleEmployees, "기획팀")
    check(planTop == null) {
        "존재하지 않는 부서 검색 시 null이어야 합니다. 실제: $planTop"
    }

    val emptyTop = findTopEarnerInDept(emptyList(), "개발팀")
    check(emptyTop == null) {
        "빈 직원 목록 전달 시 null이어야 합니다."
    }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 복합 반환값 및 매핑의 간결성: Java에서는 여러 값을 묶어서 다룰 때 별도의 불변 DTO를 만들고 getter를 일일이 호출해야 하지만,
 *    Kotlin에서는 구조분해 선언을 통해 `val (x, y) = point`처럼 단 한 줄로 필요한 필드를 언패킹할 수 있습니다.
 * 2. 언더스코어(_)를 통한 불필요한 필드 생략: `val (_, name, _, salary) = employee`와 같이 사용하지 않는 필드를
 *    컴파일 경고 없이 명시적으로 무시할 수 있어 코드의 의도가 매우 명확해집니다.
 * 3. 연산자 오버로딩의 개방성: `data class`뿐만 아니라 개발자가 정의한 일반 클래스, 심지어 서드파티 라이브러리의 클래스에도
 *    확장 함수(`operator fun ThirdParty.component1()`)를 통해 구조분해 기능을 유연하게 부여할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 프로퍼티 이름이 아닌 "순서"에 종속되는 구조분해:
 *    - 구조분해 선언은 변수 이름이 아니라 `componentN()` 함수의 순서(data class의 경우 주 생성자 프로퍼티 순서)에 따라 매핑됩니다.
 *    - 만약 data class 주 생성자의 프로퍼티 순서가 바뀌면(`data class Person(val age: Int, val name: String)`),
 *      기존에 `val (name, age) = person`으로 작성된 코드에서 타입 불일치 에러나 잘못된 값 할당 버그가 발생할 수 있습니다.
 * 2. operator 키워드 누락:
 *    - 일반 클래스에서 `component1()` 메서드를 작성할 때 `operator` 키워드를 빠뜨리면,
 *      일반 메서드로 인식되어 컴파일러가 구조분해 문법(`val (a, b) = obj`)을 허용하지 않습니다.
 */
