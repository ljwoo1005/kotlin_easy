package Lec06

/*
 * 코틀린에서 반복문을 다루는 방법
 *
 * 1. for-each문 (향상된 for문)
 * 2. 전통적인 for문
 * 3. Progression과 Range
 * 4. while문
 */

/*
 * for-each문
 */
fun printList() {

    val numbers = listOf(1L, 2L, 3L) // 컬렉션을 만드는 방법이 다르다

    /*
     * 자바에서 콜론(:)에 해당하는 부분이 in 연산자로 대체되었다
     * number에 타입이 없는데, 자동 형 추론에 의해 생략 가능한 것이다.
     * in 뒤에는 Iterable이 구현된 타입이라면 무엇이든 들어갈 수 있다.
     */
    for( number in numbers ) {
        println( number )
    }

}

/*
 * 전통적인 for문
 */
fun printNumber() {

    /*
     * .. 연산자 : 범위를 만들어내는 연산자 (좌항 < 우항) : public final operator fun rangeTo(other: kotlin.Int): kotlin.ranges.IntRange { /* compiled code */ }
     *
     *
     * IntRange는 IntProgression을 상속받고 있고, Progression은 등차수열이라는 뜻
     * public open class IntProgression
            internal constructor
            (
                    start: Int,
                    endInclusive: Int,
                    step: Int
            ) : Iterable<Int>
     * 보면 내부에 3개의 생성자 인자 : 시작 값, 종료 값, 공차(step)이 있다.
     * IntRange는 step부분에 기본값이 1로 지정되어 생성자를 호출한다.
     * 그래서 .. 연산자에 step이 없을 때 1씩 증가하거나 감소한다.
     *
     * 1..3 : 1에서 시작하고 3으로 끝나고 공차가 1인 등차수열
     * i에 1부터 3까지 1씩 올리면서 반복한다.
     */
    for( i in 1..3 ) {
        println( i )
    }

}

fun printNumber2() {

    /*
     * downTo 연산자 : 3에서 시작하고 1로 끝나고 공차가 -1인 등차수열
     *
     * public infix fun Int.downTo(to: Int): IntProgression {
           return IntProgression.fromClosedRange(this, to, -1)
       }
     *
     * 보면 step 부분이 -1을 기본값으로 받고 있다.
     *
     * i에 3부터 1까지 1씩 감소하며 반복한다.
     */
    for( i in 3 downTo 1 ) {
        println( i )
    }

}

fun printNumber3() {

    /*
     * 1..5 step 2 : 1에서 시작하고 5에서 끝나고 공차가 2인 등차수열
     *
     * public infix fun IntProgression.step(step: Int): IntProgression {
           checkStepIsPositive(step > 0, step)
           return IntProgression.fromClosedRange(first, last, if (this.step > 0) step else -step)
       }
     */
    for( i in 1..5 step 2 ) {
        println( i )
    }

}

/*
 * downTo, step 또한 함수이다.
 * 코틀린에서는 이를 "중위 호출 함수"라고 한다. 자세한 내용은 이후 강의에서..
 *
 * 변수.함수이름(argument) 대신
 * 변수 함수이름 argument 방식으로 호출
 *
 *
 * 1..5 step 2 실행 순서
 *
 * 1. 1부터 5까지 공차가 1인 등차수열 생성
 * 2. 1.에서 생성된 등차수열에 대해서 step이란 함수를 호출. 등차수열.step(2)
 * 3. 결론적으로 1부터 5까지 공차가 2인 등차수열 생성
 *
 * 한 줄 요약 : 코틀린에서 전통적인 for문은 등차수열을 이용한다!
 */

/*
 * while문
 * 자바와 완전히 동일하다. do-while문 또한 마찬가지이다.
 */
fun printNumber4() {

    var i = 1

    while( i <= 3 ) {
        println( i )
        i++
    }

}