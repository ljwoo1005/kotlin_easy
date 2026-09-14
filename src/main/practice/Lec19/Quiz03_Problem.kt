package Lec19

/*
 * [학습 목표 & 복습 개념]
 * - `forEach` 람다 블록 내부에서 `continue`와 유사한 효과를 내는 `return@forEach` 패턴을 체화합니다.
 * - `forEach` 람다 블록을 조기 탈출(`break` 효과)하기 위해 `run { ... return@run }` 구조를 활용하는 방법을 익힙니다.
 * - 중첩 루프(다중 for문)에서 명시적이고 안전한 탈출을 지원하는 라벨(`label@`) 문법의 사용법과 주의점을 학습합니다.
 *
 * [문제 설명]
 * 실무 로그 스트림 처리 및 2차원 행렬 탐색을 수행하는 2가지 함수를 완성하세요.
 *
 * 1. processLogsUntilCritical(logs: List<Quiz19Log>): List<String>
 *    - 시스템 로그 목록(`List<Quiz19Log>`)을 순서대로 순회하며 처리합니다.
 *    - 로그 레벨(`level`)이 `"DEBUG"`인 항목은 무시하고 다음 로그로 건너뜁니다 (`return@forEach` 활용).
 *    - 로그 레벨이 `"CRITICAL"`인 치명적 장애 항목을 만나면 즉시 전체 순회를 중단하고 그때까지 수집된 로그 리스트를 반환합니다 (`run { ... return@run }` 활용).
 *    - 그 외 정상 로그(`INFO`, `WARN` 등)는 `"[${log.level}] ${log.message}"` 형식의 문자열로 변환하여 수집합니다.
 *
 * 2. findTargetMatrixCell(matrix: List<List<Int>>, target: Int): Pair<Int, Int>?
 *    - 2차원 정수 행렬(`matrix`)에서 `target` 값의 위치를 탐색합니다.
 *    - 행(Row)과 열(Col)을 순회하는 중첩 루프를 구성하고, `target` 값을 찾는 즉시 모든 루프를 중단하여
 *      해당 위치 좌표 `Pair(rowIndex, colIndex)`를 반환합니다.
 *    - 바깥쪽 루프에 라벨(`searchLoop@`)을 지정하고, 조건 일치 시 `break@searchLoop`를 통해 즉시 탈출하도록 구현하세요.
 *    - 행렬 내에 `target` 값이 존재하지 않으면 `null`을 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - `processLogsUntilCritical`:
 *   - 반드시 `forEach` 람다를 사용하세요.
 *   - "DEBUG" 건너뛰기에는 반드시 `return@forEach`를 사용하세요.
 *   - "CRITICAL" 중단에는 반드시 `run { ... return@run }` 구조를 사용하세요.
 * - `findTargetMatrixCell`:
 *   - 중첩 for 루프를 사용하고, 바깥 루프에 라벨을 지정하여 `break@라벨`로 탈출하세요.
 *
 * [입출력 및 기대 결과]
 * - logs:
 *   [Quiz19Log("INFO", "서버 시작"), Quiz19Log("DEBUG", "캐시 적재"), Quiz19Log("WARN", "메모리 경고"), Quiz19Log("CRITICAL", "DB 다운"), Quiz19Log("INFO", "완료")]
 *   -> listOf("[INFO] 서버 시작", "[WARN] 메모리 경고")
 * - matrix:
 *   [[10, 20], [30, 40], [50, 60]], target = 40
 *   -> Pair(1, 1)
 * - matrix:
 *   [[1, 2], [3, 4]], target = 99
 *   -> null
 *
 * [💡 HINT]
 * - `forEach`는 일반적인 루프 키워드가 아니라 인라인 고차 함수이므로 일반 `break`, `continue` 키워드를 쓸 수 없습니다.
 * - `return@forEach`는 람다의 현재 실행을 종료하고 다음 요소의 람다 호출로 넘어가므로 `continue` 역할을 합니다.
 * - `run { ... }` 블록 안에서 `return@run`을 호출하면 `run` 표현식 전체가 즉시 반환되므로 `break` 역할을 합니다.
 * - 루프 앞에 `라벨이름@ for (...)` 형태로 라벨을 선언하면, 안쪽 루프에서 `break@라벨이름`으로 외부 루프까지 한 번에 빠져나올 수 있습니다.
 */

// 실습용 로그 도메인 데이터 클래스
data class Quiz19Log(
    val level: String,
    val message: String
)

// 1. 치명적 로그 발생 전까지 정상 로그를 수집하는 함수 뼈대
private fun processLogsUntilCritical(logs: List<Quiz19Log>): List<String> {
    // TODO: 여기에 코드를 작성하세요 (run 블록, forEach, return@run, return@forEach 활용)
    TODO()
}

// 2. 2차원 행렬에서 목표값의 첫 좌표를 찾는 함수 뼈대
private fun findTargetMatrixCell(matrix: List<List<Int>>, target: Int): Pair<Int, Int>? {
    // TODO: 여기에 코드를 작성하세요 (중첩 for문, 라벨 break 활용)
    TODO()
}

fun main() {
    // 1. processLogsUntilCritical 검증
    val sampleLogs = listOf(
        Quiz19Log("INFO", "서버 시작"),
        Quiz19Log("DEBUG", "내부 캐시 적재"),
        Quiz19Log("WARN", "메모리 점유율 상승"),
        Quiz19Log("CRITICAL", "DB 커넥션 고갈"),
        Quiz19Log("INFO", "후속 작업")
    )

    val processed = processLogsUntilCritical(sampleLogs)
    check(processed == listOf("[INFO] 서버 시작", "[WARN] 메모리 점유율 상승")) {
        "DEBUG는 스킵되고 CRITICAL 전까지의 로그만 수집되어야 합니다. 실제: $processed"
    }

    val normalLogs = listOf(
        Quiz19Log("INFO", "요청 수신"),
        Quiz19Log("DEBUG", "쿼리 실행"),
        Quiz19Log("INFO", "응답 완료")
    )
    check(processLogsUntilCritical(normalLogs) == listOf("[INFO] 요청 수신", "[INFO] 응답 완료")) {
        "CRITICAL이 없는 경우 모든 정상 로그가 수집되어야 합니다."
    }

    check(processLogsUntilCritical(emptyList()).isEmpty()) {
        "빈 로그 리스트 전달 시 빈 리스트가 반환되어야 합니다."
    }

    // 2. findTargetMatrixCell 검증
    val matrix = listOf(
        listOf(10, 20, 30),
        listOf(40, 50, 60),
        listOf(70, 80, 90)
    )

    val found1 = findTargetMatrixCell(matrix, 50)
    check(found1 == Pair(1, 1)) {
        "50의 위치는 행 1, 열 1이어야 합니다. 실제: $found1"
    }

    val found2 = findTargetMatrixCell(matrix, 90)
    check(found2 == Pair(2, 2)) {
        "90의 위치는 행 2, 열 2여야 합니다. 실제: $found2"
    }

    val found3 = findTargetMatrixCell(matrix, 999)
    check(found3 == null) {
        "존재하지 않는 원소는 null이어야 합니다. 실제: $found3"
    }

    println("✅ Quiz03 테스트 통과! (Jump와 Label 완벽 체화)")
}
