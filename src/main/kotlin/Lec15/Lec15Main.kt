package Lec15

/*
 * 코틀린에서 배열과 컬렉션을 다루는 방법
 *
 * 1. 배열
 * 2. 코틀린에서의 Collection - List, Set, Map
 * 3. 컬렉션의 null 가능성, Java와 함께 사용하기
 */

/*
 * 1. 배열
 */
fun makeArray() {

    val array = arrayOf(100, 200)

    /*
     * public val <T> Array<out T>.indices: IntRange
     *     get() = IntRange(0, lastIndex)
     *
     * array.indices = 0부터 마지막 인덱스까지의 IntRange 객체를 반환
     */
    for (i in array.indices) {
        println("${i} ${array[i]}")
    }

    // 코틀린에서는 배열에 요소를 간단하게 추가할 수 있다.
    val plusArray = array.plus(300)

    /*
     * array.withIndex() = 배열의 인덱스와 값을 동시에 반환
     */
    for ((idx , value) in plusArray.withIndex()) {
        println("${idx} ${value}")
    }

}

/*
 * 2. 코틀린에서의 Collection
 *
 * 컬렉션을 만들어줄 때 먼저 불변인지, 가변인지 설정해야 한다.
 *
 * 가변(Mutable) 컬렉션 : 컬렉션에 element를 추가, 삭제할 수 있다.
 * 불변 컬렉션 : 컬렉션에 element를 추가, 삭제할 수 없다.
 *
 * 불변 컬렉션은 쉽게 말해서 Collection을 만들자마자
 * Collection.unmodifiableList() 등을 붙여주는 것이다.
 *
 * 그리고 불변 컬렉션이라 하더라도
 * Reference Type인 Element의 필드는 바꿀 수 있다.
 */

/*
 * 불변 List
 */
fun makeImmutableList() {

    val numbers = listOf(100, 200) // 내부 요소가 Int인 것을 컴파일러가 알아서 추론하기 때문에 제네릭 생략 가능
    val emptyList = emptyList<Int>() // 내부 요소가 비어있기 때문에 컴파일러가 타입 추론이 불가능하여 제네릭 붙여야함

    // 하지만 emptyList의 타입이 추론 가능한 상황이라면 제네릭을 생략할 수 있다.
    list(emptyList())

    // 하나 가져오기
    // 코틀린에서 리스트의 요소 조회는 배열처럼 대괄호를 사용할 수 있다.
    println(numbers[0])

    // 순회
    for (number in numbers) {
        println(number)
    }

    // 인덱스와 값을 동시에 순회
    for ((idx, value) in numbers.withIndex()) {
        println("${idx} ${value}")
    }

}

private fun list(list: List<Int>) { }

/*
 * 가변 List
 * 가변 List의 기본 구현체는 ArrayList이다.
 */
fun makeMutableList() {

    val numbers = mutableListOf(100, 200)
    numbers.add(300) // 요소 추가는 자바와 동일

}

/*
 * 간단한 Tip
 * 우선 불변 리스트를 만들고, 꼭 필요한 경우 가변 리스트로 바꾸자!
 */

/*
 * 불변 Set
 *
 * 집합은 List와 다르게 순서가 없고, 같은 element는 하나만 존재할 수 있다.
 * 자료구조적 의미만 제외하면 모든 기능이 List와 비슷하다.
 */
fun makeImmutableSet() {

    val numbers = setOf(100, 200)

    // 순회
    for (number in numbers) {
        println(number)
    }

    // 인덱스와 값을 함께 순회
    for ((idx, value) in numbers.withIndex()) {
        println("${idx} ${value}")
    }

}

/*
 * 가변 Set
 * 기본 구현체는 LinkedHashSet이다.
 */
fun makeMutableSet() {

    val numbers = mutableSetOf(100, 200)
    numbers.add(300)

}

/*
 * 불변 Map
 */
fun makeImmutableMap() {

    // 변경할 수 없는 Map이기에 객체 생성과 동시에 값을 세팅한다.
    // 이 때 중위함수 to를 같이 사용한다. (to의 반환값은 Pair 클래스 객체)
    // 즉, mapOf() 함수는 파라미터로 Pair 객체를 받는다.
    val map = mapOf(1 to "MONDAY", 2 to "TUESDAY")

    // key 순회
    for (key in map.keys) {
        println(key)
        println(map[key])
    }

    // key, value 순회
    for ((key, value) in map.entries) {
        println("${key} ${value}")
    }

}

/*
 * 가변 Map
 */
fun makeMutableMap() {

    val oldMap = mutableMapOf<Int, String>()

    // 자바 방식
    oldMap.put(1, "MONDAY")
    oldMap.put(2, "TUESDAY")

    // 코틀린 방식
    // 1번 key에 MONDAY를 넣는다
    oldMap[1] = "MONDAY"
    // 2번 key에 TUESDAY를 넣는다
    oldMap[2] = "TUESDAY"

}

/*
 * 3. 컬렉션의 null 가능성, Java와 함께 사용하기
 *
 * 다음 3가지 타입이 있다.
 *  - List<Int?>
 *  - List<Int>?
 *  - List<Int?>?
 *
 * 물음표의 위치에 따라 null 가능성이 미묘하게 바뀐다.
 *
 *  - List<Int?>    : 리스트에 null이 들어갈 수 있지만, 리스트는 절대 null이 아님
 *  - List<Int>?    : 리스트에는 null이 들어갈 수 없지만, 리스트는 null일 수 있다
 *  - List<Int?>?   : 리스트에 null이 들어갈 수도 있고, 리스트가 null일 수도 있다
 *
 * 물음표의 위치에 따라 null 가능성 의미가 달라지므로 차이를 잘 이해해야 한다!
 */

/*
 * 자바에서는 컬렉션 객체에 가변, 불변을 구분하지 않는다.
 *
 * 코틀린에서 불변 리스트를 만든 후, 자바에서 해당 불변 리스트를 가져온다.
 * 자바는 리스트의 가변, 불변을 구분하지 않기 때문에 해당 리스트에 element를 추가할 수 있다.
 * 추가 후에 코틀린에 다시 돌려주면 코틀린 입장에서는 불변 리스트에 element가 추가가 된 것이다.
 * 이로 인해 코틀린에서 오류가 발생할 수 있다.
 *
 * 자바에서는 nullable과 non-nullable 타입을 구분하지 않는다.
 *
 * 코틀린에서 non-nullable 리스트를 만든 후, 자바에서 해당 리스트를 가져온다.
 * 자바는 null 가능성을 구분하지 않기 때문에 해당 리스트에 null을 추가할 수 있다.
 * 추가 후에 코틀린에 다시 돌려주면 코틀린 입장에서는 non-nullable 리스트에 null이 추가가 된 것이다.
 * 이로 인해 코틀린에서 오류가 발생할 수 있다.
 *
 * 따라서 코틀린 쪽의 컬렉션이 자바에서 호출되면 컬렉션 내용이 변할 수 있음을 감안해야한다.
 * kotlin -> java -> kotlin 시 방어 로직을 구축한다던가,
 * 코틀린 쪽에서 Collections.unmodifiableXXX() 메서드를 활용하여 변경 자체를 막을 수는 있다.
 *
 * 반대로 코틀린에서 자바 쪽 컬렉션을 가져갈 때는 플랫폼 타입을 신경써야 한다.
 *
 * 자바에서 만든 List<Integer>를 코틀린에서 가져간다 해보자.
 * 이 때 코틀린은 해당 리스트가 List<Int?> | List<Int>? | List<Int?>? 3가지 경우의 수를 구분하질 못한다.
 * 결국 자바 코드를 보며 맥락을 확인하고, 자바 코드를 가져오는 지점을 코틀린 코드로 wrapping하여 영향 범위를 최소화하는 것이 좋다.
 */