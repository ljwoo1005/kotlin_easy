package Lec19

/*
 * [학습 목표 & 복습 개념]
 * - 일반 클래스(`class`)에서 구조분해 선언(`val (a, b) = instance`)을 지원하기 위한 `operator fun componentN()` 연산자 오버로딩 규칙을 체화합니다.
 * - `data class`의 자동 `componentN()` 생성 메커니즘과 프로퍼티 선언 순서가 구조분해에 미치는 영향을 이해합니다.
 * - 컬렉션 순회 및 람다식 내부에서 구조분해를 활용하여 복합 데이터 객체의 특정 프로퍼티를 직관적으로 추출하고 처리하는 기법을 익힙니다.
 *
 * [문제 설명]
 * 2차원 좌표와 직원 데이터를 다루는 실습입니다. 다음 요구사항을 완성하세요.
 *
 * 1. 일반 클래스 Quiz19Point의 구조분해 지원
 *    - 아래에 정의된 일반 클래스 `Quiz19Point(val x: Int, val y: Int)`는 `data class`가 아닙니다.
 *    - 따라서 기본적으로 구조분해 문법(`val (x, y) = point`)을 사용할 수 없습니다.
 *    - 연산자 속성을 부여하는 `operator` 키워드를 사용하여 `component1()`과 `component2()` 메서드를 클래스 내부에 구현하세요.
 *
 * 2. isOriginOrDiagonal(point: Quiz19Point): Boolean
 *    - 전달받은 `point` 좌표가 원점(0, 0)이거나 x좌표와 y좌표가 같은 대각선상에 위치하는지 판별합니다.
 *    - 반드시 구조분해 구문 `val (x, y) = point`를 사용하여 좌표값을 꺼낸 후 검사하세요.
 *
 * 3. findTopEarnerInDept(employees: List<Quiz19Employee>, targetDept: String): Pair<String, Int>?
 *    - 직원 목록(`List<Quiz19Employee>`)에서 특정 부서(`targetDept`)에 소속된 직원들 중 가장 높은 급여를 받는 직원을 찾습니다.
 *    - 해당 부서의 최고 급여 직원이 존재하면 `Pair(name, salary)` 형태로 반환하고, 부서에 직원이 한 명도 없으면 `null`을 반환합니다.
 *    - `data class Quiz19Employee`의 구조분해 기능(`val (_, name, _, salary) = ...` 등)을 활용하여 구현하세요.
 *
 * [요구사항 & 제약조건]
 * - `Quiz19Point`: 반드시 `operator fun component1(): Int`와 `operator fun component2(): Int`를 구현하세요.
 * - `isOriginOrDiagonal`: 함수 내부에서 `val (x, y) = point` 구조분해 선언을 반드시 사용하세요.
 * - `findTopEarnerInDept`: `Quiz19Employee`가 없거나 해당 부서 직원이 없을 경우 안전하게 `null`을 반환하세요.
 *
 * [입출력 및 기대 결과]
 * - val (x, y) = Quiz19Point(3, 4) -> x = 3, y = 4
 * - isOriginOrDiagonal(Quiz19Point(0, 0)) -> true
 * - isOriginOrDiagonal(Quiz19Point(5, 5)) -> true
 * - isOriginOrDiagonal(Quiz19Point(3, 4)) -> false
 * - sampleEmployees에서 "개발팀" 최고 급여 직원 -> Pair("김개발", 5500)
 * - sampleEmployees에서 "기획팀" 최고 급여 직원 -> null
 *
 * [💡 HINT]
 * - 코틀린의 구조분해는 프로퍼티 이름이 아니라, `component1()`, `component2()` ... 함수의 반환 순서로 값을 매핑합니다.
 * - 일반 클래스에서 `componentN()` 메서드를 선언할 때는 반드시 함수명 앞에 `operator` 키워드를 붙여야 연산자 관례로 인정됩니다.
 * - `data class`는 주 생성자의 프로퍼티 순서대로 `componentN()` 함수를 컴파일러가 자동 생성합니다.
 */

// 1. 일반 클래스 2차원 좌표 모델
class Quiz19Point(
    val x: Int,
    val y: Int
) {
    // TODO: operator fun component1()을 구현하여 x를 반환하세요.
    operator fun component1(): Int {
        TODO()
    }

    // TODO: operator fun component2()를 구현하여 y를 반환하세요.
    operator fun component2(): Int {
        TODO()
    }
}

// 실습용 직원 데이터 클래스
data class Quiz19Employee(
    val id: Long,
    val name: String,
    val department: String,
    val salary: Int
)

// 2. 원점 또는 대각선 좌표 판별 함수 뼈대
private fun isOriginOrDiagonal(point: Quiz19Point): Boolean {
    // TODO: 여기에 코드를 작성하세요 (val (x, y) = point 구조분해 활용)
    TODO()
}

// 3. 특정 부서 최고 급여 직원 조회 함수 뼈대
private fun findTopEarnerInDept(employees: List<Quiz19Employee>, targetDept: String): Pair<String, Int>? {
    // TODO: 여기에 코드를 작성하세요 (구조분해 활용)
    TODO()
}

fun main() {
    // 1. Quiz19Point 구조분해 및 isOriginOrDiagonal 검증
    val p1 = Quiz19Point(0, 0)
    val (x1, y1) = p1
    check(x1 == 0 && y1 == 0) { "Quiz19Point의 구조분해가 올바르게 동작해야 합니다. (0, 0)" }
    check(isOriginOrDiagonal(p1)) { "원점 (0, 0)은 true를 반환해야 합니다." }

    val p2 = Quiz19Point(7, 7)
    check(isOriginOrDiagonal(p2)) { "대각선 좌표 (7, 7)은 true를 반환해야 합니다." }

    val p3 = Quiz19Point(3, 8)
    val (x3, y3) = p3
    check(x3 == 3 && y3 == 8) { "Quiz19Point의 구조분해가 올바르게 동작해야 합니다. (3, 8)" }
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

    println("✅ Quiz02 테스트 통과! (구조분해 및 커스텀 componentN 체화)")
}
