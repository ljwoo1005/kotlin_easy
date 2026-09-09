package Lec17

import java.io.BufferedReader
import java.io.FileReader

/*
 * 코틀린에서 람다를 다루는 방법
 *
 * 1. 자바에서 람다를 다루기 위한 노력
 * 2. 코틀린에서의 람다
 * 3. Closure
 * 4. 다시 try with resources
 */

/*
 * 2. 코틀린에서의 람다
 *
 * 자바와는 근본적으로 다른 한 가지가 있다.
 *
 * 코틀린에서는 함수를 1급 시민으로 간주한다.
 * 코틀린에서는 함수가 그 자체로 값이 될 수 있다.
 * 변수에 할당할 수도, 파라미터로 넘길 수도 있다.
 *
 * 람다(익명함수)를 변수에 직접 넣어보자.
 */
val fruits = listOf(
    Fruit("사과", 1_000),
    Fruit("사과", 1_200),
    Fruit("사과", 1_200),
    Fruit("사과", 1_500),
    Fruit("바나나", 3_000),
    Fruit("바나나", 3_200),
    Fruit("바나나", 2_500),
    Fruit("수박", 10_000),
)

// 함수 선언문에 함수 이름이 없다.
// 즉, 이름 없는 함수, 람다이다.
val isApple: (Fruit) -> Boolean = fun(fruit: Fruit): Boolean {
    return fruit.name == "사과"
}

// 중괄호와 화살표를 사용하는 익명 함수
val isApple2: (Fruit) -> Boolean = { fruit: Fruit -> fruit.name == "사과" }

// 변수에 할당한 함수를 호출하는 두 가지 방법
fun callIsApple() {

    isApple(fruits[0]) // 함수처럼 소괄호를 바로 작성하는 방법
    isApple2.invoke(fruits[1]) // invoke라는 함수를 통하여 호출하는 방법

}

/*
 * 코틀린에서는 함수에 타입이 있는데, [ (파라미터 타입...) -> 반환 타입 ] 으로 선언한다.
 * (a: Int) -> Int
 * (a: Int, b: Int) -> Int
 */

/*
 * 자바에 있는 filterFruits를 코틀린으로 옮겨보자.
 * 자바에서는 함수 부분의 파라미터 타입이 Predicate 인터페이스였는데,
 * 코틀린에서는 함수 자체를 받을 수 있으므로 파라미터 타입을 함수 타입으로 작성한다.
 */
private fun filterFruits(fruits: List<Fruit>, filter: (Fruit) -> Boolean): List<Fruit> {

    val results = mutableListOf<Fruit>()

    for ( fruit in fruits ) {
        if ( filter(fruit) ) {
            results.add(fruit)
        }
    }

    return results

}

fun callFilterFruits() {

    filterFruits(fruits, isApple) // 함수 자체를 파라미터로 넘길 수 있다.
    filterFruits(fruits, { fruit: Fruit -> fruit.name == "사과" }) // 변수에 할당된 함수가 아닌 그냥 함수 자체도 넘길 수 있다.

    /*
     * 코틀린에서 익명 함수를 파라미터로 넘길 때 중요한 문법이 하나 있다.
     * 어떤 함수의 마지막 파라미터가 함수일 때, 해당 함수의 중괄호를 소괄호 밖으로 뺄 수 있다.
     */
    filterFruits(fruits) { fruit: Fruit -> fruit.name == "사과" }

    /*
     * 파라미터 선언할 때 함수의 타입을 선언했기 때문에 파라미터로 들어올 함수의 매개변수가 어떤 타입이고, 어떤 타입을 반환하는지 추론할 수 있다.
     * 따라서 파라미터가 될 함수의 매개변수 타입을 생략할 수 있다.
     */
    filterFruits(fruits) { fruit -> fruit.name == "사과" }

    /*
     * 여기서 파라미터가 될 함수의 매개변수가 단 한 개만 존재한다면, 변수 선언을 생략하고 "it"이라는 특수 필드를 사용할 수 있다.
     * 여기서 it은 단 한 개 존재하는 매개변수를 의미한다.
     */
    filterFruits(fruits) { it.name == "사과" }

    /*
     * 람다의 경우 여러 줄을 작성할 수도 있다.
     * 이 때, 마지막 줄의 결과가 람다의 반환값이다. (별도의 return 키워드는 사용하지 않는다.)
     */
    filterFruits(fruits) {
        println("사과만 주세요~")
        it.name == "사과"
    }

}

/*
 * 3. Closure
 *
 * 코틀린은 람다가 시작하는 지점에 참조하고 있는 변수들을 "모두 포획"하여 그 정보를 가지고 있다.
 * 이렇게 람다가 실행되는 시점에 쓰고 있는 변수들을 모두 포획한 데이터 구조를 Closure라고 부른다.
 */
fun callFilterFruit3() {

    /*
     * 자바와 똑같은 코드를 작성했는데, 코틀린에선 아무런 문제 없이 동작한다.
     */
    var targetFruitName = "바나나"
    targetFruitName = "수박"
    filterFruits(fruits) { it.name == targetFruitName }

}

/*
 * 4. 다시 try with resources
 *
 * 7강에서 보았던 use 함수를 파헤쳐보자.
 */
fun readFile(path: String) {
    BufferedReader(FileReader(path)).use { reader ->
        println(reader.readLine())
    }
}

/*
 * public inline fun <T : Closeable?, R> T.use(block: (T) -> R): R { }
 *
 * use는 Closeable 구현체 T에 대한 확장 함수이다.
 * 받고 있는 파라미터가 block이라는 이름을 가진 함수이다.
 * 즉, 람다를 받도록 만들어진 함수이다.
 */