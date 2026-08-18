package Lec05

/*
 * 코틀린에서 조건문을 다루는 방법
 *
 * 1. if문
 * 2. Expression과 Statement
 * 3. switch와 when
 */

/*
 * if문 : 자바와 크게 다를 것이 없다.
 */
fun validateScoreIsNotNegative(score: Int) {
    if( score < 0 ) {
        throw IllegalArgumentException("${score}는 0보다 작을 수 없습니다.")
    }
}

/*
 * if-else문 : 자바와의 차이점이 하나 있다.
 * 자바에서 if-else는 Statement이고,
 * 코틀린에서 if-else는 Expression이다.
 *
 * Statement : 행동을 지시하는 실행 단위(완결된 하나의 명령). 값을 만들어내지 않을 수 있다.
 * Expression : 하나의 값으로 도출되는 문장. 다른 코드의 일부로 쓸 수 있다.
 */
fun getPassOrFail(score: Int): String {
    if( score >= 50 ) {
        return "P"
    } else {
        return "F"
    }
}

/*
 * int a = 30 + 40;
 * 위 자바 문장은 Expression이자 Statement이다.
 *
 * String grade = if( score >= 50 ) { "P"; } else { "F"; }
 * 위 자바 문장은 Statement이다. if문을 하나의 값으로 취급하지 않기 때문에 변수 초기화 시 if문을 넣을 수가 없다.
 *
 * String grade = score >= 50 ? "P" : "F";
 * 자바에서는 위와 같은 상황을 위해 3항 연산자가 있다.
 * 3항 연산자는 하나의 값으로 취급하므로 에러가 없다.
 * 위 자바 문장은 Expression이자 Statement이다.
 *
 * 코틀린에서의 if-else는 Expression이니, 아래와 같이 사용할 수 있다.
 */
fun getPassOrFail2(score: Int): String {
    return if( score >= 50 ) {
        "P"
    } else {
        "F"
    }
}

/*
 * 코틀린에서는 if-else를 Expression으로 사용할 수 있기 때문에 3항 연산자가 없다.
 * 코틀린에서 if-else if-else도 마찬가지로 Expression이다.
 */
fun getGrade(score: Int): String {
    return if( score >= 90 ) {
        "A"
    } else if( score >= 80 ) {
        "B"
    } else if( score >= 70 ) {
        "C"
    } else {
        "D"
    }
}

/*
 * 간단한 TIP
 * 어떠한 값이 특정 범위에 포함되어있는지, 아닌지 확인할 때
 *
 * 자바 : if( 0 <= score && score <= 100 ) { }
 * 코틀린 : if( score in 0..100 ) { }
 */
fun validateScore(score: Int) {
    if( score in 0..100 ) {
        // TODO : Something
    }
}

/*
 * switch와 when
 *
 * 코틀린에선 switch-case문이 사라지고, 대체할 수 있는 when 문법이 생겼다.
 * when 문법 또한 Expression이기에 if-else처럼 return문과 결합하여 사용할 수 있다.
 *
 * 자바와의 차이점
 * switch => when으로 작성
 * case => 작성하지 않고 바로 분기 작성
 * default => else로 작성
 */
fun getGradeWithSwitch(score: Int): String {
    return when ( score / 10 ) {
        9 -> "A"
        8 -> "B"
        7 -> "C"
        else -> "D"
    }
}

/*
 * 코틀린의 when 문법은 조금 더 다양한 형태로도 작성할 수 있다.
 * 분기를 작성할 때 정해진 값만으로 작성할 뿐만 아니라 다양한 조건을 가지고도 작성할 수 있다.
 */
fun getGradeWithSwitch2(score: Int): String {
    return when ( score ) {
        in 90..99 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        else -> "D"
    }
}

/*
 * when 문법 작성법
 *
 * when (값) {
 *   조건부 -> 어떠한 구문
 *   조건부 -> 어떠한 구문
 *   else -> 어떠한 구문
 * }
 *
 * 조건부에는 어떠한 Expression이라도 들어갈 수 있다. (Ex.is Type) : is 연산자(자바의 instanceof)
 * 조건부에는 복수의 조건을 확인할 수 있다. ("," 연산자)
 * 값에는 값이 없을 수도 있다. (early return처럼 사용 가능)
 */
fun startsWithA(obj: Any): Boolean {
    return when (obj) {
        is String -> obj.startsWith("A") // 코틀린의 스마트캐스트. 앞선 코드에서 타입 검증 시 뒤에서 형변환 없이 해당 타입의 메서드 호출 가능
        else -> false
    }
}

fun judgeNumber(number: Int) {
    when (number) {
        1, 0, -1 -> println("어디서 많이 본 숫자입니다.")
        else -> println("1, 0, -1이 아닙니다.")
    }
}

fun judgeNumber2(number: Int) {
    when {
        number == 0 -> println("주어진 숫자는 0입니다.")
        number % 2 == 0 -> println("주어진 숫자는 짝수입니다.")
        else -> println("주어진 숫자는 홀수입니다.")
    }
}