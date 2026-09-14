package Lec18

/*
 * 코틀린에서 컬렉션을 함수형으로 다루는 방법
 *
 * 1. 필터와 맵
 * 2. 다양한 컬렉션 처리 기능
 * 3. List를 Map으로
 * 4. 중첩된 컬렉션 처리
 */

data class Fruit(
    val id: Long,
    val name: String,
    val factoryPrice: Long,
    val currentPrice: Long?
) {

    fun nullOrValue(): Fruit? {
        if ( this.currentPrice == null ) return null;
        return this;
    }

    val isSamePrice: Boolean
        get() = factoryPrice == currentPrice

}

val fruits = listOf(
    Fruit(1, "사과", 1_000L, 1_500L),
    Fruit(2, "사과", 1_000L, 1_500L),
    Fruit(3, "사과", 1_000L, 1_500L),
    Fruit(4, "사과", 1_000L, 1_500L),
    Fruit(5, "사과", 1_000L, 1_500L)
)

/*
 * 1. 필터와 맵
 *
 * 과일가게에서 이런 요구사항이 있다고 해보자.
 *  - 사과만 주세요!
 *  - 사과의 가격들을 알려주세요!
 * 이런 상황에서 코틀린에서 Functional Programing을 어떻게 할 수 있냐 하면
 */

// 사과만 주세요!
val apples = fruits.filter { fruit -> fruit.name == "사과" }

// 만약 필터에서 인덱스가 필요하다면?
val apples2 = fruits.filterIndexed { idx, fruit ->
    println(idx)
    fruit.name == "사과"
}

// 사과의 가격들을 알려주세요!
val applePrices = fruits.filter { fruit -> fruit.name == "사과" }
    .map { fruit -> fruit.currentPrice }

// map에서 인덱스가 필요하다면?
val applePrices2 = fruits.filter { fruit -> fruit.name == "사과" }
    .mapIndexed { idx, fruit ->
        println(idx)
        fruit.currentPrice
    }

// Mapping의 결과가 null이 아닌 것만 가져오고 싶다면?
val values = fruits.filter { fruit -> fruit.name == "사과" }
    .mapNotNull { fruit -> fruit.nullOrValue() }

// 위 기능들을 이용해 지난 번에 보았던 filterFruits를 함수형으로 바꿔보자.
private fun filterFruits(fruits: List<Lec17.Fruit>, filter: (Lec17.Fruit) -> Boolean): List<Lec17.Fruit> {

    val results = mutableListOf<Lec17.Fruit>()

    for ( fruit in fruits ) {
        if ( filter(fruit) ) {
            results.add(fruit)
        }
    }

    return results

}

private fun filterFruits2(fruits: List<Fruit>, filter: (Fruit) -> Boolean): List<Fruit> {

    // 위와 아래가 같은 기능을 한다. filter()의 인자는 함수이기 때문이다.
//    return fruits.filter { fruit -> filter(fruit) }
    return fruits.filter( filter )

}

/*
 * 2. 다양한 컬렉션 처리 기능
 *
 * 모든 과일이 사과인가요?! : all()
 * -> 인자에 들어가는 함수의 결과가 전부 true이면 최종 결과가 true, 하나라도 아니면 false가 된다.
 * 모든 과일이 사과가 아닌가요?! : none()
 * -> 인자에 들어가는 함수의 결과가 전부 false이면 최종 결과가 true, 하나라도 맞다면 false가 된다.
 * 사과가 하나라도 있나요?! : any()
 * -> 인자에 들어가는 함수의 결과 중 하나라도 true이면 최종 결과가 true, 전부 아니라면 false가 된다.
 */
val isAllApple = fruits.all { fruit -> fruit.name == "사과" } // fruits 요소가 전부 사과라면 true
val isNoApple = fruits.none { fruit -> fruit.name == "사과" } // fruits 요소가 전부 사과가 아니라면 true
val isAnyApple = fruits.any { fruit -> fruit.name == "사과" } // fruits 요소 중 하나라도 사과라면 true

/*
 * 총 과일 개수가 몇 개인가요?! : count()
 */
val fruitCount = fruits.count()

/*
 * 낮은 가격 순으로 보여주세요! : sortedBy() -> (오름차순) 정렬 / sortedByDescending : (내림차순) 정렬
 */
val sortedFruits = fruits.sortedBy { fruit -> fruit.currentPrice }
val descendingSortedFruits = fruits.sortedByDescending { fruit -> fruit.currentPrice }

/*
 * 과일이 몇 종류 있죠?! : distinctBy() -> 변형된 값을 기준으로 중복을 제거
 *
 * 아래는 과일의 이름(변형된 값)을 기준으로 중복을 제거한 후,
 * map으로 과일의 이름만 남겼다.
 */
val distinctFruitNames = fruits.distinctBy { fruit -> fruit.name }
    .map { fruit -> fruit.name }

/*
 * 첫 번째 과일만 주세요!
 * -> first() : 첫 번째 값을 가져온다 (무조건 null이 아니어야 함)
 * -> firstOrNull() : 첫 번째 값 또는 null을 가져온다
 * 마지막 과일만 주세요!
 * -> last() : 마지막 값을 가져온다 (무조건 null이 아니어야 함)
 * -> lastOrNull() : 마지막 값 또는 null을 가져온다
 */

/*
 * 3. List를 Map으로
 *
 * [ 과일이름 : List<과일> ] 구조의 Map이 필요해요! (value가 List)
 * -> List.groupBy { item -> key로 쓰일 요소 }
 */
val map: Map<String, List<Fruit>> = fruits.groupBy { fruit -> fruit.name }

/*
 * [ id : 과일 ] 구조의 Map이 필요해요! (value가 단일 Fruit)
 * -> List.associateBy { item -> key로 쓰일 요소 }
 */
val map2: Map<Long, Fruit> = fruits.associateBy { fruit -> fruit.id }

/*
 * [ 과일이름 : List<출고가> ] 구조의 Map이 필요해요!
 * -> List.groupBy( { item -> key로 쓰일 요소 }, { item -> value List로 쓰일 요소 } )
 * 이 때 들어가는 함수 인자가 2개이기 때문에 소괄호 안에 인자를 작성해야 한다.
 */
val map3: Map<String, List<Long>> = fruits.groupBy( { fruit -> fruit.name }, { fruit -> fruit.factoryPrice } )

/*
 * [ id : 출고가 ] 구조의 Map이 필요해요!
 * -> List.associateBy( { item -> key로 쓰일 요소 }, { item -> value로 쓰일 요소 } )
 * 이 때 들어가는 함수 인자가 2개이기 때문에 소괄호 안에 인자를 작성해야 한다.
 */
val map4: Map<Long, Long> = fruits.associateBy( { fruit -> fruit.id }, { fruit -> fruit.factoryPrice } )

/*
 * Map에 대해서도 앞서 설명한 기능들을 모두 사용할 수 있다.
 */
val map5: Map<String, List<Fruit>> = fruits.groupBy { fruit -> fruit.name }
    .filter { (key, value) -> key == "사과" }

/*
 * 4. 중첩된 컬렉션 처리
 */
val fruitsInList: List<List<Fruit>> = listOf(
    listOf(
        Fruit(1L, "사과", 1_000L, 1_500L),
        Fruit(2L, "사과", 1_000L, 1_500L),
        Fruit(3L, "사과", 1_000L, 1_500L),
        Fruit(4L, "사과", 1_000L, 1_500L)
    ),
    listOf(
        Fruit(5L, "바나나", 3_000L, 3_200L),
        Fruit(6L, "바나나", 3_200L, 3_200L),
        Fruit(7L, "바나나", 2_500L, 3_200L),
    ),
    listOf(
        Fruit(8L, "수박", 10_000L, 10_000)
    )
)

/*
 * 출고가와 현재가가 동일한 과일을 골라주세요!
 * -> flatMap() : 2차원 배열, 2차원 List 등 중첩 구조로 있는 요소들을 조건(힘수)를 거쳐 1차원으로 풀어준다.
 */
val samePriceFruits = fruitsInList.flatMap { list ->
    list.filter { fruit -> fruit.factoryPrice == fruit.currentPrice } // 단일 리스트로 바뀔 때의 조건을 람다로 걸어준다.
}

/*
 * 위 코드의 경우 람다가 중첩된 구조이다.
 * 코틀린에서는 이를 확장 함수, 확장 프로퍼티 등을 이용하여 리펙토링할 수 있다.
 */
val List<Fruit>.samePriceFilter: List<Fruit>
    get() = this.filter(Fruit::isSamePrice)

val samePriceFruits2 = fruitsInList.flatMap { list -> list.samePriceFilter }

/*
 * List<List<Fruit>>를 List<Fruit>로 바꿔주세요!
 * -> flatten() : 2차원 배열, 2차원 List등 중첩 구조로 있는 요소들을 1차원으로 풀어준다.
 */
val fruits2 = fruitsInList.flatten()