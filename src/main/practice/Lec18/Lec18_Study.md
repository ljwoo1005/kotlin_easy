# [Lec18] 코틀린에서 컬렉션을 함수형으로 다루는 방법 (학습 가이드)

본 문서는 **Lec18 강의 노트([`Lec18Main.kt`](file:///c:/IntelliJWorkspace/kotlin/kotlin_easy/src/main/kotlin/Lec18/Lec18Main.kt))** 및 제작된 **실습 문제([`Quiz01`](file:///c:/IntelliJWorkspace/kotlin/kotlin_easy/src/main/practice/Lec18/Quiz01_Problem.kt) ~ [`Quiz03`](file:///c:/IntelliJWorkspace/kotlin/kotlin_easy/src/main/practice/Lec18/Quiz03_Problem.kt))**를 완벽하게 이해하고 실무에 적용할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [컬렉션 조작의 패러다임 전환: Java 명령형 vs Kotlin 함수형](#1-컬렉션-조작의-패러다임-전환-java-명령형-vs-kotlin-함수형)
2. [필터와 맵의 기본기: filter, map, 그리고 mapNotNull](#2-필터와-맵의-기본기-filter-map-그리고-mapnotnull)
3. [조건 검사와 안전한 탐색: all, none, any 및 집계 함수](#3-조건-검사와-안전한-탐색-all-none-any-및-집계-함수)
4. [List를 Map으로 변환하는 두 가지 축: groupBy vs associateBy](#4-list를-map으로-변환하는-두-가지-축-groupby-vs-associateby)
5. [중첩 컬렉션의 마법: flatMap과 평탄화(Flattening)의 모든 것](#5-중첩-컬렉션의-마법-flatmap과-평탄화flattening의-모든-것)
6. [클래스 프로퍼티와 확장 프로퍼티를 활용한 리팩토링 원리](#6-클래스-프로퍼티와-확장-프로퍼티를-활용한-리팩토링-원리)
7. [실습 퀴즈(Quiz01 ~ Quiz03) 심층 분석 및 해설](#7-실습-퀴즈quiz01--quiz03-심층-분석-및-해설)
8. [핵심 요약 치트시트 (Cheat Sheet)](#8-핵심-요약-치트시트-cheat-sheet)

---

## 1. 컬렉션 조작의 패러다임 전환: Java 명령형 vs Kotlin 함수형

### (1) Java의 전통적인 명령형(Imperative) 컬렉션 처리와 한계
Java에서 컬렉션 데이터를 다룰 때 오랫동안 사용해 온 방식은 **"어떻게(How)"** 처리할 것인가를 기술하는 명령형 외부 반복문이었습니다.

```java
// Java: 사과만 골라 가격 리스트 생성하기
List<Long> applePrices = new ArrayList<>();
for (Fruit fruit : fruits) {
    if ("사과".equals(fruit.getName())) {
        if (fruit.getCurrentPrice() != null) {
            applePrices.add(fruit.getCurrentPrice());
        }
    }
}
```
- **문제점**:
  1. 임시 가변 리스트(`applePrices`)를 생성하고 상태를 계속 변경해야 합니다.
  2. 비즈니스 로직(사과 필터링, 가격 추출)보다 반복문(`for`)과 조건문(`if`), `add` 같은 제어 구조가 코드의 대부분을 차지합니다.
  3. 로직이 복잡해질수록 중첩 들여쓰기(Indent)가 깊어지고 가독성이 급격히 저하됩니다.

### (2) Java 8 Stream API의 등장과 아쉬운 점
Java 8에서는 선언형 처리를 위해 Stream API가 도입되었습니다.
```java
List<Long> applePrices = fruits.stream()
    .filter(fruit -> "사과".equals(fruit.getName()))
    .map(Fruit::getCurrentPrice)
    .filter(Objects::nonNull)
    .collect(Collectors.toList());
```
- 스트림을 열기 위한 `.stream()` 호출과 최종 결과를 모으는 `.collect(Collectors.toList())` 보일러플레이트가 매번 강제됩니다.
- 기본형 스트림(`IntStream`, `DoubleStream`)과 객체 스트림이 분리되어 있어 변환이 번거롭습니다.

### (3) Kotlin의 혁신: 인라인 확장 함수 기반의 직관적 함수형 API
코틀린은 표준 라이브러리 차원에서 `Iterable<T>`에 풍부한 고차 함수들을 **`inline` 확장 함수**로 제공합니다:

```kotlin
val applePrices = fruits
    .filter { it.name == "사과" }
    .mapNotNull { it.currentPrice }
```
- 별도의 스트림 객체를 생성하거나 수집(`collect`)할 필요 없이, 컬렉션 인스턴스에서 직접 메서드를 체이닝합니다.
- `inline` 키워드 덕분에 람다 객체 생성 오버헤드 없이 컴파일 시점에 일반 루프 코드로 인라인 치환되어 뛰어난 성능을 보장합니다.

---

## 2. 필터와 맵의 기본기: filter, map, 그리고 mapNotNull

### (1) `filter`와 `filterIndexed`
조건을 만족하는(람다의 반환값이 `true`인) 요소만 골라내어 새로운 리스트를 반환합니다.

```kotlin
// 사과만 골라내기
val apples = fruits.filter { fruit -> fruit.name == "사과" }

// 인덱스가 필요한 경우 filterIndexed 활용
val evenIndexApples = fruits.filterIndexed { idx, fruit ->
    idx % 2 == 0 && fruit.name == "사과"
}
```

### (2) `map`과 `mapIndexed`
각 요소를 지정한 변환 람다식을 거쳐 다른 형태의 값이나 객체로 사상(Transform)합니다.

```kotlin
// 과일 객체에서 가격만 추출
val prices: List<Long?> = fruits.map { it.currentPrice }

// 인덱스를 결합하여 문자열 목록 생성
val indexedNames = fruits.mapIndexed { idx, fruit -> "${idx + 1}번: ${fruit.name}" }
```

### (3) `mapNotNull`의 우아함
데이터 처리 중 `null`이 발생할 수 있는 경우, `mapNotNull`은 변환과 null 필터링을 한 번에 수행합니다:

```kotlin
// 기존 방식: map 후 filterNotNull (리스트가 2번 생성됨)
val prices1 = fruits.map { it.currentPrice }.filterNotNull()

// 권장 방식: mapNotNull (단일 순회로 처리)
val prices2 = fruits.mapNotNull { it.currentPrice }
```

### (4) 고차 함수에 함수 자체를 인자로 전달하기
코틀린에서는 함수가 1급 시민이므로, 조건 검사 로직(`(Fruit) -> Boolean`)을 변수나 파라미터로 받아 `filter`에 그대로 넘길 수 있습니다:

```kotlin
// 강의 예제 (Lec18Main.kt)
private fun filterFruits(fruits: List<Fruit>, filter: (Fruit) -> Boolean): List<Fruit> {
    // 람다를 풀어서 전달하는 대신, 함수 파라미터 자체를 직접 전달 가능!
    // return fruits.filter { fruit -> filter(fruit) }
    return fruits.filter(filter)
}
```

---

## 3. 조건 검사와 안전한 탐색: all, none, any 및 집계 함수

### (1) 술어(Predicate) 삼총사: `all`, `none`, `any`
컬렉션의 요소들이 특정 조건을 만족하는지 전체적으로 판별할 때 사용합니다.

| 함수 | 의미 | 수학적 기호 | 반환 조건 |
| :--- | :--- | :---: | :--- |
| **`all { predicate }`** | 모든 요소가 참인가? | $\forall x$ | **모든** 요소가 조건을 만족해야 `true` |
| **`any { predicate }`** | 하나라도 참인가? | $\exists x$ | **단 하나라도** 조건을 만족하면 즉시 `true` (Short-circuit) |
| **`none { predicate }`** | 모두 거짓인가? | $\neg \exists x$ | **모든** 요소가 조건을 만족하지 않아야 `true` |

```kotlin
val isAllApple = fruits.all { it.name == "사과" }
val isAnyExpensive = fruits.any { (it.currentPrice ?: 0L) >= 10_000L }
val isNoEmptyName = fruits.none { it.name.isBlank() }
```

> [!WARNING]
> **공집합의 참 (Vacuous Truth) 주의점**  
> 비어있는 컬렉션(`emptyList()`)에서 `all { ... }`을 호출하면 **항상 `true`**를 반환합니다. 조건에 위배되는 반례가 존재하지 않기 때문입니다.  
> 만약 "데이터가 1개 이상 존재하면서 모두 조건을 만족해야 함"을 검증하려면 `fruits.isNotEmpty() && fruits.all { ... }` 형태로 작성해야 합니다.

### (2) 개수 세기와 정렬: `count`, `sortedBy`, `distinctBy`
- **`count()`**: 전체 개수 반환 (`size`와 동일)  
  - `count { it.price > 3000 }`처럼 조건을 주면 조건을 만족하는 개수만 효율적으로 계산합니다.
- **`sortedBy { ... }`**: 지정한 키 기준 **오름차순** 정렬
- **`sortedByDescending { ... }`**: 지정한 키 기준 **내림차순** 정렬
- **`distinctBy { ... }`**: 특정 키 셀렉터를 기준으로 중복 요소를 제거 (최초로 발견된 요소 유지)

### (3) 안전한 단일 원소 조회: `firstOrNull()`, `lastOrNull()`
- `first()` / `last()`: 컬렉션이 비어있으면 `NoSuchElementException` 런타임 예외 발생.
- `firstOrNull()` / `lastOrNull()`: 컬렉션이 비어있을 경우 안전하게 `null`을 반환.

```kotlin
// 안전하게 가장 비싼 과일의 이름 꺼내기
val topFruitName = fruits
    .filter { it.currentPrice != null }
    .sortedByDescending { it.currentPrice }
    .firstOrNull()
    ?.name
```

---

## 4. List를 Map으로 변환하는 두 가지 축: groupBy vs associateBy

컬렉션을 Map 형태로 구조화할 때, 목적에 따라 `groupBy`와 `associateBy`를 정확히 구분해서 사용해야 합니다.

```
                  ┌─ 1:N 매핑 (Value가 List)  ───> groupBy
List<Fruit> ──────┤
                  └─ 1:1 매핑 (Value가 단일객체) ──> associateBy
```

### (1) `groupBy`: 1:N 그룹화 (`Map<K, List<V>>`)
동일한 키를 갖는 요소들을 하나의 `List`로 묶습니다.

```kotlin
// 1) 기본 사용: [과일 이름 : List<과일 객체>]
val group1: Map<String, List<Fruit>> = fruits.groupBy { it.name }

// 2) 값 변환(valueTransform) 추가: [과일 이름 : List<출고가>]
// 인자가 2개이므로 소괄호 안에 람다 2개를 작성합니다.
val group2: Map<String, List<Long>> = fruits.groupBy(
    { fruit -> fruit.name },         // Key Selector
    { fruit -> fruit.factoryPrice }   // Value Transform
)
```

### (2) `associateBy`: 1:1 매핑 (`Map<K, V>`)
고유한 식별자(ID 등)를 키로 삼아 단일 요소를 매핑합니다.

```kotlin
// 1) 기본 사용: [ID : 과일 객체]
val map1: Map<Long, Fruit> = fruits.associateBy { it.id }

// 2) 값 변환 추가: [ID : 출고가]
val map2: Map<Long, Long> = fruits.associateBy(
    { fruit -> fruit.id },
    { fruit -> fruit.factoryPrice }
)
```

> [!CAUTION]
> **`associateBy` 사용 시 키 중복(Key Collision) 위험**  
> `associateBy`는 키가 중복될 경우 **나중에 순회된 요소가 이전 요소를 덮어씁니다**.  
> 고유성이 보장되지 않는 속성(예: 과일 이름, 카테고리 등)으로 매핑할 때는 반드시 `groupBy`를 사용해야 데이터 유실을 막을 수 있습니다.

---

## 5. 중첩 컬렉션의 마법: flatMap과 평탄화(Flattening)의 모든 것

### (1) `flatMap`의 본질: `map` + `flatten`
`flatMap`은 2차원 리스트, 2차원 배열 등 **중첩된 구조를 변환(`map`)하면서 동시에 1차원으로 풀어내는(`flatten`)** 고차 함수입니다.

```
fruitsInList: [ [사과1, 사과2], [바나나3], [수박4] ]
                   │
                   ▼ transform (예: 조건에 맞는 요소만 추출)
              [ [사과1], [], [수박4] ]
                   │
                   ▼ flatten (중첩 리스트를 껍질 벗겨 단일 리스트로 병합)
결과:          [ 사과1, 수박4 ]
```

### (2) 코틀린 표준 라이브러리의 실제 내부 구현 소스코드
코틀린 표준 라이브러리(`kotlin-stdlib`)의 `Iterable<T>.flatMap`은 다음과 같이 구현되어 있습니다:

```kotlin
public inline fun <T, R> Iterable<T>.flatMap(transform: (T) -> Iterable<R>): List<R> {
    return flatMapTo(ArrayList<R>(), transform)
}

public inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.flatMapTo(
    destination: C, 
    transform: (T) -> Iterable<R>
): C {
    for (element in this) {
        val list = transform(element) // 1. 각 요소를 컬렉션(Iterable)으로 변환
        destination.addAll(list)      // 2. 변환된 컬렉션의 요소들을 단일 결과 컬렉션에 누적
    }
    return destination
}
```

### (3) 단계별 실행 메커니즘
1. **누적용 단일 리스트 생성**: 내부적으로 빈 `ArrayList<R>`인 `destination`을 생성합니다.
2. **외부 리스트 반복 (`for (element in this)`)**:
   - 외부 리스트에 담긴 각 요소(서브 리스트)를 하나씩 꺼냅니다.
3. **변환 실행 (`val list = transform(element)`)**:
   - 전달한 람다식이 실행되어, 각 서브 리스트로부터 필터링되거나 가공된 새로운 `Iterable<R>`을 반환받습니다.
4. **평탄화 병합 (`destination.addAll(list)`)**:
   - 반환받은 서브 리스트 자체를 통째로 넣는 것(`add`)이 아니라, 그 서브 리스트 안의 **원소들을 하나씩 꺼내어 누적 리스트에 덧붙입니다(`addAll`)**.
5. **반환**: 모든 순회가 끝나면 중첩이 완전히 해제된 1차원 `List<R>`이 반환됩니다.

### (4) `flatMap` vs `flatten`의 차이점
- **`flatten()`**: 추가적인 변환이나 필터링 없이, 단순히 2차원 리스트(`List<List<T>>`)를 1차원 리스트(`List<T>`)로 그대로 합칠 때 사용합니다.
- **`flatMap { ... }`**: 리스트를 1차원으로 합치는 과정에서 **각 요소의 변환, 필터링, 프로퍼티 추출 등이 함께 이루어질 때** 사용합니다.

```kotlin
val nested = listOf(listOf(1, 2), listOf(3, 4))

// 1. 단순 평탄화
val flat1 = nested.flatten() // [1, 2, 3, 4]

// 2. 변환을 동반한 평탄화 (각 숫자를 10배 곱하기)
val flat2 = nested.flatMap { list -> list.map { it * 10 } } // [10, 20, 30, 40]
```

---

## 6. 클래스 프로퍼티와 확장 프로퍼티를 활용한 리팩토링 원리

### (1) 문제 상황: 2중 중첩 람다의 가독성 저하
출고가와 현재가가 동일한 과일을 2차원 리스트에서 골라내려 할 때, 초기의 코드는 다음과 같습니다:

```kotlin
val samePriceFruits = fruitsInList.flatMap { list ->
    list.filter { fruit -> fruit.factoryPrice == fruit.currentPrice }
}
```
- `flatMap` 블록 안에 `filter` 람다가 중첩되어 들여쓰기가 깊어집니다.
- 도메인 규칙(`factoryPrice == currentPrice`)이 서비스 로직 한가운데 노출되어 캡슐화가 깨집니다.

이를 코틀린의 고급 기능 3가지를 조합하여 우아하게 리팩토링할 수 있습니다.

---

### (2) 리팩토링 1단계: 클래스 커스텀 게터 프로퍼티 (`Fruit.isSamePrice`)
```kotlin
data class Fruit(
    val id: Long,
    val name: String,
    val factoryPrice: Long,
    val currentPrice: Long?
) {
    // 커스텀 게터를 가진 계산 프로퍼티
    val isSamePrice: Boolean
        get() = currentPrice != null && factoryPrice == currentPrice
}
```
- **동작 원리**:
  - `val` 프로퍼티에 `get()`만 정의하고 초기값을 대입하지 않으면, 컴파일러는 메모리에 값을 저장하는 **뒷받침하는 필드(Backing Field)**를 생성하지 않습니다.
  - 프로퍼티에 접근할 때마다 게터 블록의 표현식을 평가하여 반환합니다.
  - 바이트코드(Java 관점)에서는 `public final boolean isSamePrice()` 메서드로 컴파일됩니다.
- **효과**: "출고가와 현재가가 같은가?"라는 판단 책임을 [`Fruit`](file:///c:/IntelliJWorkspace/kotlin/kotlin_easy/src/main/kotlin/Lec18/Lec18Main.kt#L12-L27) 도메인 모델 내부로 캡슐화합니다.

---

### (3) 리팩토링 2단계: 멤버 참조 (Property Reference: `Fruit::isSamePrice`)
```kotlin
// 기존 람다
list.filter { fruit -> fruit.isSamePrice }

// 멤버 참조 활용
list.filter(Fruit::isSamePrice)
```
- **동작 원리**:
  - `Fruit::isSamePrice`는 `Fruit` 클래스의 `isSamePrice` 프로퍼티를 가리키는 **멤버 참조(Callable / Property Reference)**입니다.
  - 리플렉션 타입인 `KProperty1<Fruit, Boolean>`이자 `(Fruit) -> Boolean` 함수 타입과 완벽히 호환됩니다.
  - 따라서 불필요한 매개변수 선언(`fruit ->`)을 생략하고 함수형 인자로 직접 전달할 수 있습니다.

---

### (4) 리팩토링 3단계: 컬렉션 확장 프로퍼티 (`List<Fruit>.samePriceFilter`)
```kotlin
val List<Fruit>.samePriceFilter: List<Fruit>
    get() = this.filter(Fruit::isSamePrice)
```
- **동작 원리**:
  1. **수신 객체 타입(Receiver Type) 한정**:
     - 임의의 모든 `List`가 아니라, 원소 타입이 `Fruit`인 `List<Fruit>`에만 적용되는 확장 프로퍼티를 선언합니다.
  2. **Backing Field 부재와 필수 커스텀 게터**:
     - 확장은 기존 클래스에 실제로 필드를 삽입할 수 없으므로 상태(값) 저장이 불가능하며, 반드시 `get()` 게터 구문으로 작성해야 합니다. 게터 내부의 `this`는 호출 시점의 `List<Fruit>` 인스턴스를 가리킵니다.
  3. **JVM 정적 메서드 컴파일**:
     - 바이트코드 변환 시 수신 객체를 첫 번째 매개변수로 받는 정적 메서드로 컴파일됩니다:
       ```java
       public static final List<Fruit> getSamePriceFilter(List<Fruit> $this$samePriceFilter) {
           return CollectionsKt.filter($this$samePriceFilter, Fruit::isSamePrice);
       }
       ```
- **효과**: 과일 목록 필터링 책임이 전용 확장 프로퍼티로 분리되어 코드 재사용성이 극대화됩니다.

---

### (5) 최종 결과 비교
```kotlin
// [Before] 복잡한 중첩 람다
val samePriceFruits = fruitsInList.flatMap { list ->
    list.filter { fruit -> fruit.factoryPrice == fruit.currentPrice }
}

// [After] 도메인 모델 + 확장 프로퍼티를 활용한 선언형 코드
val samePriceFruits = fruitsInList.flatMap { list -> list.samePriceFilter }

// 또는 최상위 참조 형태로도 가능
val samePriceFruits = fruitsInList.flatMap(List<Fruit>::samePriceFilter)
```
- **단일 과일 책임**: `Fruit.isSamePrice`
- **과일 목록 책임**: `List<Fruit>.samePriceFilter`
- **평탄화 조합**: `flatMap`
- 각 계층별 책임이 명확히 분리되고, 코드가 마치 자연어처럼 직관적으로 읽히게 됩니다.

---

## 7. 실습 퀴즈(Quiz01 ~ Quiz03) 심층 분석 및 해설

### 📘 Quiz01: 기초 컬렉션 함수형 API 체화
- **핵심 문제 1 (`findInStockFruitNames`)**:
  - `fruits.mapNotNull { if (it.currentPrice != null) it.name else null }`
  - null 안전성을 보장하면서 변환과 필터링을 한 번에 수행합니다.
- **핵심 문제 2 (`checkFruitStockStatus`)**:
  - `Triple(fruits.all { it.factoryPrice > 0 }, fruits.any { (it.currentPrice ?: 0L) >= 5000L }, fruits.none { it.name.isBlank() })`
  - 조건 판별에 루프와 boolean 플래그 변수를 쓰는 구태의연한 방식을 완벽히 탈피합니다.
- **핵심 문제 3 (`getTopPricedFruitInfo`)**:
  - `validFruits.sortedByDescending { it.currentPrice }.firstOrNull()?.name`
  - `first()` 대신 `firstOrNull()`을 사용하여 빈 목록 입력 시에도 안전하게 null을 반환합니다.

### 📗 Quiz02: Java to Idiomatic Kotlin 리팩토링
- **핵심 문제 1 (`groupFruitsByName`)**:
  - `fruits.groupBy { it.name }`
  - Java의 `Map<String, List<Fruit>>` 루프 및 `putIfAbsent` 로직을 단 1줄로 리팩토링합니다.
- **핵심 문제 2 (`mapFruitIdToFactoryPrice`)**:
  - `fruits.associateBy({ it.id }, { it.factoryPrice })`
  - Key Selector와 Value Transform 람다 2개를 소괄호 안에 명시적으로 전달하여 1:1 매핑을 완성합니다.
- **핵심 문제 3 (`flattenSamePriceFruits`)**:
  - `val List<Quiz18Fruit>.samePriceFilter: List<Quiz18Fruit> get() = this.filter(Quiz18Fruit::isSamePrice)`
  - `fruitBoxes.flatMap { box -> box.samePriceFilter }`
  - 강의 170~205줄의 리팩토링 철학을 그대로 실전에 적용해 봅니다.

### 📙 Quiz03: 청과물 마켓 통합 통계 & 추천기 (실전 시나리오)
- **핵심 문제 1 (`getUniqueFruitNamesFromBoxes`)**:
  - `boxes.flatMap { it.fruits }.distinctBy { it.name }.map { it.name }`
  - 2차원 상자 객체 목록에서 과일들을 풀고, 중복을 제거한 뒤 이름만 추출하는 깔끔한 파이프라인.
- **핵심 문제 2 (`calculateCategoryAveragePrices`)**:
  - `fruits.groupBy { it.category }.mapValues { (_, list) -> list.map { it.currentPrice }.average() }`
  - `groupBy`와 `mapValues`, 그리고 컬렉션 내장 `average()` 함수를 결합한 불변 통계 연산.
- **핵심 문제 3 (`findRecommendedDiscountFruits`)**:
  - `boxes.flatMap { it.fruits }.filter { it.currentPrice <= maxBudget && it.factoryPrice > it.currentPrice }.sortedByDescending { it.factoryPrice - it.currentPrice }`
  - 동적 계산식(`factoryPrice - currentPrice`)을 `sortedByDescending`에 넘겨 간결하게 정렬합니다.

---

## 8. 핵심 요약 치트시트 (Cheat Sheet)

### (1) 함수형 컬렉션 처리 메서드 총정리

| 기능 분류 | 코틀린 함수 | 반환 타입 | 핵심 요약 |
| :--- | :--- | :--- | :--- |
| **필터링** | `filter { P }` | `List<T>` | 조건 `P`가 true인 요소만 추출 |
| | `filterIndexed { idx, it -> P }` | `List<T>` | 인덱스와 함께 조건 검사 |
| | `filterNotNull()` | `List<T>` | null인 요소를 제거한 non-null 리스트 반환 |
| **변환** | `map { T }` | `List<R>` | 각 요소를 변환 함수 `T`를 거쳐 사상 |
| | `mapNotNull { T }` | `List<R>` | 변환 결과 중 null이 아닌 것만 수집 |
| **조건 판별** | `all { P }` | `Boolean` | 모든 요소가 참일 때 true (빈 리스트는 항상 true) |
| | `any { P }` | `Boolean` | 하나라도 참이면 true (단락 평가) |
| | `none { P }` | `Boolean` | 모든 요소가 거짓일 때 true |
| **정렬 & 중복** | `sortedBy { K }` | `List<T>` | 키 `K` 기준 오름차순 정렬 |
| | `sortedByDescending { K }` | `List<T>` | 키 `K` 기준 내림차순 정렬 |
| | `distinctBy { K }` | `List<T>` | 키 `K` 기준 중복 제거 (첫 번째 요소 유지) |
| **단일 조회** | `firstOrNull()` | `T?` | 첫 번째 요소 반환 (없으면 null) |
| | `lastOrNull()` | `T?` | 마지막 요소 반환 (없으면 null) |
| **Map 변환** | `groupBy { K }` | `Map<K, List<T>>` | 1:N 그룹화 |
| | `associateBy { K }` | `Map<K, T>` | 1:1 매핑 (키 중복 시 덮어씀) |
| **중첩 해제** | `flatten()` | `List<T>` | `List<List<T>>`를 1차원 `List<T>`로 단순 병합 |
| | `flatMap { T }` | `List<R>` | 각 요소를 컬렉션으로 변환 후 1차원으로 병합 |

---

### (2) 꼭 기억해야 할 코틀린 베스트 프랙티스
1. **명령형 for문 대신 선언형 고차 함수 사용**: 가변 변수(`var`)와 수동 리스트 누적을 피하고, 불변(`val`) 체이닝을 지향하세요.
2. **2중 중첩 람다는 리팩토링 신호(Code Smell)**: `flatMap` 내부에 또 다른 람다가 길어진다면, 도메인 프로퍼티(`isSamePrice`), 멤버 참조(`Class::prop`), 확장 프로퍼티(`List<T>.prop`)로 분리하세요.
3. **`associateBy` vs `groupBy` 엄격 구분**: 키가 고유하지 않은 데이터를 `associateBy`로 변환하면 데이터가 소실되므로 반드시 `groupBy`를 사용하세요.
4. **예외 없는 안전한 연산자 선호**: `first()`보다는 `firstOrNull()`, `map` + `filterNotNull`보다는 `mapNotNull`을 사용하는 습관을 들이세요.
