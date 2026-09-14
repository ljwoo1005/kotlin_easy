package Lec19

/*
 * [핵심 해결 아이디어]
 * - `forEach`는 루프 제어문이 아니라 고차 함수이므로 `break`나 `continue` 키워드를 직접 쓸 수 없습니다.
 * - `return@forEach`는 람다 본문에서 즉시 복귀하여 다음 요소에 대한 람다를 계속 실행하므로 `continue` 효과를 냅니다.
 * - `run { ... return@run }` 구조는 `forEach`를 포함하는 `run` 블록 자체를 조기 탈출하므로 `break` 효과를 냅니다.
 * - 중첩 `for` 루프에서 특정 조건 달성 시 모든 루프를 일거에 탈출하기 위해 바깥쪽 루프에 라벨(`searchLoop@`)을 지정하고
 *   `break@searchLoop`를 사용합니다.
 */

// Quiz19Log 데이터 클래스는 Quiz03_Problem.kt에 선언된 모델을 공유합니다.

// 1. 치명적 로그 발생 전까지 정상 로그를 수집하는 모범 답안
private fun processLogsUntilCritical(logs: List<Quiz19Log>): List<String> {
    val result = mutableListOf<String>()

    // forEach 전체를 run 블록으로 감싸 return@run을 통한 break 효과 구현
    run {
        logs.forEach { log ->
            // CRITICAL 발생 시 run 블록을 조기 반환하여 전체 루프 즉시 중단 (break)
            if (log.level == "CRITICAL") {
                return@run
            }

            // DEBUG 로그는 무시하고 다음 로그 순회로 진행 (continue)
            if (log.level == "DEBUG") {
                return@forEach
            }

            // 정상 로그 수집
            result.add("[${log.level}] ${log.message}")
        }
    }

    return result
}

// 2. 2차원 행렬에서 목표값의 첫 좌표를 찾는 모범 답안
private fun findTargetMatrixCell(matrix: List<List<Int>>, target: Int): Pair<Int, Int>? {
    var foundCoordinate: Pair<Int, Int>? = null

    // 바깥쪽 for 루프에 라벨(searchLoop@)을 선언
    searchLoop@ for (rowIndex in matrix.indices) {
        val row = matrix[rowIndex]
        for (colIndex in row.indices) {
            // 목표값을 찾은 경우 좌표를 기록하고 라벨을 통해 외부 루프까지 즉시 탈출
            if (row[colIndex] == target) {
                foundCoordinate = Pair(rowIndex, colIndex)
                break@searchLoop
            }
        }
    }

    return foundCoordinate
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

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 람다 순회 중단/건너뛰기의 유연성: Java Stream API는 순회 도중 `continue`나 `break`를 걸기 어렵고
 *    별도의 커스텀 Spliterator나 예외 던지기 등의 번거로운 우회책을 써야 하지만, 코틀린은 인라인 함수의 특성을 활용하여
 *    `return@forEach`와 `return@run`으로 깔끔하게 제어할 수 있습니다.
 * 2. 명확한 레이블 점프: Java와 동일하게 레이블 탈출을 지원하지만, 코틀린의 경우 프로그래밍 언어 차원에서
 *    람다 식의 레이블 리턴과 루프 레이블 탈출을 통일된 `@` 문법으로 일관성 있게 다룹니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. forEach 람다 내부에서 단순 return 사용:
 *    - `forEach` 블록 안에서 `@forEach` 레이블 없이 그냥 `return`을 작성하면,
 *      해당 람다가 속해 있는 바깥쪽 함수 전체가 반환(non-local return)되어 버립니다!
 *    - 따라서 다음 반복으로 넘어가고자 할 때는 반드시 `return@forEach` 명시적 레이블 리턴을 사용해야 합니다.
 * 2. 무분별한 Label 점프의 남용:
 *    - 레이블을 이용한 점프(`break@loop`, `continue@loop`)는 복잡한 제어 흐름을 만들어 코드의 가독성과 유지보수성을 해칩니다.
 *    - 중첩 루프 탈출이 필요할 때는 가능하면 함수로 분리하여 조기 `return`을 하거나,
 *      `firstOrNull`, `takeWhile` 같은 표준 라이브러리 함수를 사용하는 것이 훨씬 권장됩니다.
 */
