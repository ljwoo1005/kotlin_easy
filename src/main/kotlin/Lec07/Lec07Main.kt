package Lec07

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/*
 * 코틀린에서 예외를 다루는 방법
 *
 * 1. try catch finally 구문
 * 2. checked exception, unchecked exception
 * 3. try with resources 구문
 */

/*
 * 1. try catch finally 구문
 * 기본적으로 자바와 코틀린이 동일하다.
 */

/*
 * 주어진 문자열을 정수로 변경하는 예제
 */
fun parseIntOrThrow(str: String): Int {
    try {
        return str.toInt() // 코틀린에선 기본타입 간의 형변환에 toType() 메서드가 존재한다. toInt() 내부적으로 Integer.parseInt()를 호출한다.
    } catch(e: NumberFormatException) {
        throw IllegalArgumentException("주어진 ${str}는 숫자가 아닙니다")
    }
}

/*
 * 주어진 문자열을 정수로 변경하는 예제
 * 실패하면 null을 반환!
 *
 * 코틀린에서는 if-else 구문처럼 try catch 구문도 하나의 Expression으로 간주된다.
 */
fun parseIntOrNull(str: String): Int? {
    return try {
        str.toInt()
    } catch(e: NumberFormatException) {
        null
    }
}

class FilePrinter {

    /*
     * 2. checked exception, unchecked exception
     */

    /*
     * 자바때와 다르게 reader.readLine()이나, reader.close() 메서드는 checked exception이 존재하는데(IOException),
     * 코틀린에서는 throws 구문을 사용하지 않아도 컴파일 에러가 발생하지 않는다.
     *
     * 코틀린에서는 checked exception과 unchecked exception을 구분하지 않는다.
     * 모두 unchecked exception이다.
     */
    fun readFile() {
        val currentFile = File(".")
        val file = File(currentFile.absolutePath + "/a.txt")

        val reader = BufferedReader(FileReader(file))

        println(reader.readLine())
        reader.close()
    }

    /*
     * 3. try with resources
     * 코틀린은 try with resources 구문이 없다!
     * 대신 코틀린의 확장함수인 use 함수를 사용한다.
     * 자세한 내용은 섹션4의 함수 파트에서...
     */
    fun readFile2(path: String) {
        BufferedReader(FileReader(path)).use { reader ->
            println(reader.readLine())
        }
    }

}

/*
 * 번외. 코틀린에서 checked exception이 없는 이유
 *
 * - 불필요한 보일러플레이트 코드를 줄이고 개발자 생산성을 높이기 위한 실용주의적 설계 선택
 * - 의미 없는 예외 처리 반복
 *      자바에서 IOException 같은 체크 예외를 다룰 때,
 *      개발자는 실제 복구할 방법이 없음에도 컴파일 에러를 피하기 위해 빈 catch 블록이나 단순 로그 출력 코드를 반복 작성하게 됩니다.
 * - 아키텍처 레이어 오염
 *      메서드 선언부에 throws를 선언하면 하위 레벨의 예외 세부 정보가
 *      상위 인터페이스와 모든 호출부로 전파되어 코드가 장황해지고 모듈화가 깨집니다.
 * - 생산성과 실효성 저하
 *      실제 대다수의 현대 프로그래밍 언어들(C#, C++, Python, Scala, Kotlin 등)은
 *      체크 예외가 개발 품질 향상보다 방해 요소가 된다고 판단하여 채택하지 않고 있습니다.
 */