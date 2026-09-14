package Lec19

import Lec18.Fruit
import Lec19.a.printHelloWorld as printHelloWorldA
import Lec19.b.printHelloWorld as printHelloWorldB

/*
 * 코틀린의 이모저모
 *
 * 1. Type Alias와 as import
 * 2. 구조분해와 componentN 함수
 * 3. Jump와 Label
 * 4. TakeIf와 TakeUnless
 */

/*
 * 1. Type Alias와 as import
 *
 * 긴 이름의 클래스 혹은 함수 타입이 있을 때 축약하거나 더 좋은 이름을 쓰고 싶다!
 * 아래 함수를 한 번 보자.
 */
fun filterFruits(fruits: List<Fruit>, filter: (Fruit) -> Boolean): (Fruit) -> Boolean { TODO() }

/*
 * filter의 타입이 Fruit 객체를 받고 Boolean을 반환하는 함수 타입이고,
 * filterFruits 함수의 반환 타입도 filter와 마찬가지이다.
 * 함수 타입은 기본적으로 너무 길다. 여기서 파라미터가 더 추가되기라도 하면 점점 더 길어진다.
 *
 * 여기서 typealias라는 키워드를 사용한다.
 */
typealias FruitFilter = (Fruit) -> Boolean

fun filterFruits2(fruits: List<Fruit>, filter: FruitFilter): FruitFilter { TODO () }

/*
 * 비슷하게 긴 이름의 클래스를 컬렉션에 사용할 때도 간단히 줄일 수 있다.
 */
data class UltraSuperGuardianTribe(
    val name: String
)

typealias USGTMap = Map<String, UltraSuperGuardianTribe>

val map = mutableListOf<USGTMap>()

/*
 * 다른 패키지에 같은 이름의 함수를 동시에 가져오고 싶다면?
 *
 * as import : 어떤 클래스나 함수를 import할 때 이름을 바꾸는 기능
 *
 * a와 b 패키지의 같은 이름의 함수를 as import를 사용하여 호출하는 코드이다.
 *
 * import Lec19.a.printHelloWorld as printHelloWorldA
 * import Lec19.b.printHelloWorld as printHelloWorldB
 */
fun callPrintHelloWorld() {

    printHelloWorldA()
    printHelloWorldB()

}

/*
 * 2. 구조분해와 componentN 함수
 *
 * 구조분해 : 복합적인 값을 분해하여 여러 변수를 한 번에 초기화하는 것
 */
data class Person(
    val name: String = "LJW",
    val age: Int = 30
)

fun createPerson() {

    val person = Person()
    val (name, age) = person // 구조분해
    /*
     * 위의 구조분해 문법을 풀어서 작성하면 다음과 같다.
     */
    val name2 = person.component1()
    val age2 = person.component2()

    println("이름 : ${name}, 나이 : ${age}")

}

/*
 * data class는 기본적으로 componentN이라는 함수도 자동으로 만들어준다.
 * N은 data class 프로퍼티의 순서를 의미하며, component1() 함수는 data class의 첫 번째 프로퍼티를 가져온다.
 * 프로퍼티의 이름이 기준이 아니라 프로퍼티의 순서가 기준이다.
 */

/*
 * data class가 아닐 때에도 구조분해를 사용하고 싶을 때도 있다.
 * 이 때는 componentN() 함수를 직접 구현해줄 수도 있다.
 *
 * 주의할 점은 componentN() 함수는 연산자의 속성을 가지고 있기 때문에
 * 사실은 연산자 오버로딩을 하는 것 처럼 간주되어야 한다
 */
class Person2(
    val name: String = "LJW",
    val age: Int = 30
) {

    operator fun component1(): String {
        return this.name
    }

    operator fun component2(): Int {
        return this.age
    }

}

fun createPerson2() {

    val person = Person2()
    val (name, age) = person

    println("이름 : ${name}, 나이 : ${age}")

}

/*
 * 3. Jump와 Label
 *
 *  - return
 *      기본적으로 가장 가까운 enclosing function 또는 익명함수로 값이 반환된다
 *  - break
 *      가장 가까운 루프가 제거된다
 *  - continue
 *      가장 가까운 루프를 다음 step으로 보낸다
 *
 * for문 및 while문에서 break, continue 기능은 자바와 완전히 동일하다.
 * 단!!! forEach 구문은 조금 다르다.
 * forEach 구문에서는 break, continue를 사용할 수 없다!!!
 */
fun callForEach() {

    val numbers = listOf(1, 2, 3)
    numbers
        .map { number -> number + 1 }
        .forEach { number ->
            println(number)
            break // 'break' and 'continue' are only allowed inside loops.
        }

}

/*
 * 만약 forEach에서 break를 사용하고 싶다면
 * 1. forEach 부분을 run 블록으로 감싼다
 * 2. break 대신 return@run을 사용한다
 *
 * forEach에서 continue를 사용하고 싶다면
 * 1. continue 대신 return@forEach를 사용한다.
 */

fun callForEach2() {

    val numbers = listOf(1, 2, 3)

    // forEach에서의 break
    run {
        numbers.map { number -> number + 1 }
            .forEach { number ->
                println(number)
                return@run
            }
    }

    // forEach에서의 continue
    numbers.map { number -> number + 1 }
        .forEach { number ->
            if ( number == 3 ) return@forEach
            println(number)
        }

}

/*
 * break, continue를 사용한다면 가급적이면 for문을 사용하는 것을 추천한다.
 */

/*
 * 위에서 @를 사용하여 코드를 작성했다.
 *
 * 코틀린에는 "라벨" 이라는 기능이 있다.
 * 특정 expression에 라벨이름@ 을 붙여 하나의 라벨로 간주하고, break, continue, return 등을 사용하는 기능
 */
fun doubleFor() {

    // 이중 for문을 라벨을 사용하여 바깥의 for문 break
    abc@ for ( i in 1..100 ) {

        for ( j in 1..100 ) {
            if ( j == 2 ) break@abc
            println("${i} ${j}") // 1 1 출력되고 반복 끝남
        }

    }

}

/*
 * 아까 보았던 return@run이나 return@forEach 같은 경우도
 * run 블록을 return하여 블록 내용 자체를 중단시키기에 forEach의 중단 효과처럼 보였던 것이고,
 * forEach 블록 자체를 한 번 return하여 그 자리에서 다음 요소의 반복으로 넘어가 continue의 효과처럼 보였던 것이다.
 *
 * 그러나 라벨을 사용한 Jump는 사용하지 않는 것을 강력 추천한다!!!
 */

/*
 * 4. TakeIf와 TakeUnless
 *
 * 코틀린에서는 메서드 체이닝을 위한 특이한 함수를 제공한다.
 */
fun getNumberOrNull(number: Int): Int? {

    return if ( number <= 0 ) {
        null
    } else {
        number
    }

}

/*
 * 위의 코드를 코틀린이 제공하는 함수를 사용하여 간략하게 표현할 수 있다.
 */
fun getNumberOrNull2(number: Int): Int? {

    /*
     * takeIf : 주어진 조건을 만족하면 그 값을 반환, 그렇지 않으면 null을 반환한다.
     */
    return number.takeIf { it > 0 }

}

fun getNumberOrNull3(number: Int): Int? {

    /*
     * takeUnless : 주어진 조건을 만족하지 않으면 그 값을 반환, 그렇지 않으면 null을 반환한다.
     */
    return number.takeUnless { it <= 0 }

}