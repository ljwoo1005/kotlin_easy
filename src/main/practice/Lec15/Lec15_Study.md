# [Lec15] 코틀린에서 배열과 컬렉션을 다루는 방법 (학습 가이드)

본 문서는 **Lec15 강의 노트(`Lec15Main.kt`, `Lec15Main.java`)** 및 제작된 **실습 문제(Quiz01 ~ Quiz03)**를 완벽하게 이해하고 체화할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [코틀린 컬렉션의 핵심 철학: 불변(Immutable) vs 가변(Mutable)의 명확한 분리](#1-코틀린-컬렉션의-핵심-철학-불변immutable-vs-가변mutable의-명확한-분리)
2. [코틀린의 배열(Array) 다루기 (`arrayOf`, `indices`, `plus`, `withIndex`)](#2-코틀린의-배열array-다루기-arrayof-indices-plus-withindex)
3. [표준 컬렉션 3대장 정복: List, Set, Map](#3-표준-컬렉션-3대장-정복-list-set-map)
4. [대괄호 인덱싱(`[]`)과 중위 함수 `to`의 비밀](#4-대괄호-인덱싱-과-중위-함수-to의-비밀)
5. [안전한 컬렉션 순회 기법: `withIndex()`와 구조분해 할당](#5-안전한-컬렉션-순회-기법-withindex와-구조분해-할당)
6. [컬렉션의 Nullability 3단계 심층 분석 (`List<T?>`, `List<T>?`, `List<T?>?`)](#6-컬렉션의-nullability-3단계-심층-분석-listt-listt-listt)
7. [Java와의 상호운용성(Interoperability) 및 플랫폼 타입 주의점](#7-java와의-상호운용성interoperability-및-플랫폼-타입-주의점)
8. [실습 퀴즈(Quiz01~03) 심층 분석 및 해설](#8-실습-퀴즈quiz0103-심층-분석-및-해설)
9. [핵심 요약 치트시트 (Cheat Sheet)](#9-핵심-요약-치트시트-cheat-sheet)

---

## 1. 코틀린 컬렉션의 핵심 철학: 불변(Immutable) vs 가변(Mutable)의 명확한 분리

### (1) Java 컬렉션의 태생적 한계
Java의 컬렉션 프레임워크(`java.util.List`, `java.util.Map` 등)는 기본적으로 모든 컬렉션 인터페이스에 `add()`, `remove()`, `put()`과 같은 상태 변경 메서드를 포함하고 있습니다.
Java에서 불변 컬렉션을 만들려면 다음과 같이 런타임 래퍼를 사용해야 했습니다:
```java
// Java의 런타임 불변화 방식
List<String> immutableList = Collections.unmodifiableList(originalList);
immutableList.add("새 요소"); // 컴파일은 정상 통과하지만, 런타임에 UnsupportedOperationException 발생!
```
이 방식의 치명적인 문제점은 **불변 위반 여부를 컴파일 시점에 검증할 수 없고 런타임 에러로만 터진다는 점**입니다.

### (2) 코틀린의 컴파일 타임 계층 분리
코틀린은 설계 단계부터 컬렉션 인터페이스를 **읽기 전용(불변, Read-Only / Immutable)**과 **수정 가능(가변, Mutable)**으로 엄격히 이원화했습니다.

```
       [Iterable]
           ▲
           │
       [Collection]
       ▲          ▲
       │          │
    [List]    [MutableList]
 (읽기 전용)   (add/remove 가능)
```

- **`List<T>`**: 원소를 읽는 기능(`get()`, `size` 등)만 제공하며, 원소를 추가/삭제하는 메서드가 아예 존재하지 않습니다.
- **`MutableList<T>`**: `List<T>`를 상속하며, `add()`, `remove()`, `clear()` 등의 수정 메서드를 추가로 제공합니다.

> 💡 **Best Practice 원칙**:  
> 항상 **불변 컬렉션(`listOf`, `setOf`, `mapOf`)을 우선적으로 사용**하고, 데이터의 추가/삭제가 반드시 필요한 비즈니스 로직 영역에서만 가변 컬렉션(`mutableListOf` 등)을 선별하여 사용합니다.

---

## 2. 코틀린의 배열(Array) 다루기 (`arrayOf`, `indices`, `plus`, `withIndex`)

코틀린에서 배열은 자바의 원시 배열(`int[]`, `String[]`)과 달리 제네릭 클래스 `Array<T>`로 표현됩니다.

### (1) 배열 생성: `arrayOf()`
```kotlin
val array = arrayOf(100, 200, 300)
```
- 컴파일러가 원소 타입을 자동으로 추론하여 `Array<Int>`로 생성합니다.
- 기본 원시 타입의 박싱 오버헤드를 피하기 위해 `intArrayOf()`, `longArrayOf()` 등 특화 배열 함수도 제공합니다.

### (2) 배열 인덱스 범위 프로퍼티: `array.indices`
```kotlin
for (i in array.indices) {
    println("$i 번째 값: ${array[i]}")
}
```
- `array.indices`는 `0..array.lastIndex`에 해당하는 `IntRange` 객체를 반환합니다.
- `array.length`나 `array.size - 1`을 수동 계산하며 발생할 수 있는 오프바이원(Off-by-one) 오류를 원천 차단합니다.

### (3) 배열 요소 확장: `array.plus()`
```kotlin
val newArray = array.plus(400) // 기존 배열은 유지되고, 400이 추가된 새로운 Array 반환
```
- 기존 배열 인스턴스를 직접 수정(Mutate)하지 않고, 새 원소가 추가된 새 복사본 배열을 반환하므로 불변성과 함수형 프로그래밍 철학에 부합합니다.

---

## 3. 표준 컬렉션 3대장 정복: List, Set, Map

### (1) List (순서가 있고 중복을 허용하는 선형 자료구조)
- **불변 List**: `listOf(100, 200)`
  - 원소의 타입이 명확하면 제네릭 생략 가능.
  - 빈 리스트는 `emptyList<Int>()` 형태로 타입을 명시하거나, 문맥상 추론 가능할 때 `emptyList()` 사용.
- **가변 List**: `mutableListOf(100, 200)`
  - 기본 구현체는 Java의 `ArrayList`입니다.
  - `numbers.add(300)`으로 요소 추가 가능.

### (2) Set (순서가 없고 중복을 허용하지 않는 집합)
- **불변 Set**: `setOf(100, 200, 100)` -> 실제 요소는 `[100, 200]`
- **가변 Set**: `mutableSetOf(100, 200)`
  - 기본 구현체는 Java의 `LinkedHashSet`으로, 요소의 삽입 순서를 기억합니다.

### (3) Map (Key-Value 쌍으로 데이터를 저장하는 사전식 자료구조)
- **불변 Map**: `mapOf(1 to "MONDAY", 2 to "TUESDAY")`
  - 중위(infix) 함수 `to`를 활용하여 직관적으로 Key-Value 쌍을 연결합니다.
  - Key 순회: `map.keys`
  - Key-Value 순회: `map.entries`
- **가변 Map**: `mutableMapOf<Int, String>()`
  - 기본 구현체는 Java의 `LinkedHashMap`입니다.
  - 요소 추가/수정: 자바식 `map.put(key, value)` 대신 `map[key] = value`를 사용합니다.

---

## 4. 대괄호 인덱싱(`[]`)과 중위 함수 `to`의 비밀

### (1) 대괄호 인덱싱 연산자 (`operator fun get/set`)
코틀린에서는 배열뿐만 아니라 `List`와 `Map` 모두에서 대괄호(`[]`) 문법을 지원합니다.

```kotlin
// List 조회
val first = list[0]          // 컴파일러 변환: list.get(0)

// Map 조회 및 할당
val day = map[1]             // 컴파일러 변환: map.get(1)
mutableMap[2] = "TUESDAY"    // 컴파일러 변환: mutableMap.put(2, "TUESDAY")
```
- 자바의 장황한 메서드 호출(`list.get(i)`, `map.get(k)`)을 제거하여 코드 가독성을 극대화합니다.
- 주의: `map[key]`는 해당 키가 존재하지 않을 수 있으므로 **반환 타입이 항상 Nullable(`V?`)**입니다.

### (2) 중위(infix) 함수 `to`의 동작 원리
`mapOf(1 to "MONDAY")`에서 `to`는 키워드가 아니라 코틀린 표준 라이브러리의 중위 확장 함수입니다:
```kotlin
// 코틀린 표준 라이브러리 내부 선언
public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
```
- `1 to "MONDAY"`는 실제로 `Pair(1, "MONDAY")` 인스턴스를 생성합니다.
- `mapOf` 가변인자(`vararg pairs: Pair<K, V>`)로 전달되어 Map이 초기화됩니다.

---

## 5. 안전한 컬렉션 순회 기법: `withIndex()`와 구조분해 할당

전통적인 자바 인덱스 루프(`for (int i = 0; i < list.size(); i++)`)는 다음과 같은 위험이 있습니다:
1. `i <= list.size()`와 같은 오프바이원 경계 오류
2. 루프 내부에서 `list.get(i)`를 중복 호출하는 번거로움

### (1) `withIndex()`의 우아함
코틀린은 인덱스와 요소를 동시에 안전하게 순회할 수 있는 `withIndex()`를 제공합니다:

```kotlin
val fruits = listOf("사과", "바나나", "체리")

for ((idx, fruit) in fruits.withIndex()) {
    println("${idx}번째 과일: $fruit")
}
```
- `withIndex()`는 각 루프 반복마다 `IndexedValue(index, value)` 객체를 제공합니다.
- 코틀린의 **구조분해 선언(Destructuring Declaration)** 덕분에 `(idx, fruit)` 형태로 바로 언패킹하여 사용할 수 있습니다.

---

## 6. 컬렉션의 Nullability 3단계 심층 분석 (`List<T?>`, `List<T>?`, `List<T?>?`)

코틀린의 널 안정성(Null Safety)은 제네릭 컬렉션과 결합할 때 물음표(`?`)의 위치에 따라 3가지 완전히 다른 의미를 갖습니다.

```
       ┌───────────────────────────────┐
       │   List   <   Int   ?   >   ?  │
       └───────────────────────────────┘
                                │   │
  원소(Element)의 null 가능성 ──┘   │
  리스트(List) 자체의 null 가능성 ──┘
```

| 표기법 | 리스트 자체의 null 가능 여부 | 내부 원소의 null 가능 여부 | 실무 예시 |
| :--- | :---: | :---: | :--- |
| **`List<Int?>`** | ❌ (절대 null 아님) | ⭕ (원소에 null 가능) | `[1, null, 3]` (정상 리스트이나 일부 데이터가 결측치인 경우) |
| **`List<Int>?`** | ⭕ (리스트 자체가 null 가능) | ❌ (원소는 절대 null 아님) | `null` 또는 `[1, 2, 3]` (결과 데이터셋 자체가 없거나 전체 유효한 경우) |
| **`List<Int?>?`** | ⭕ (리스트 자체가 null 가능) | ⭕ (원소에 null 가능) | `null` 또는 `[1, null, 3]` (외부 레거시 시스템 연동 등 가장 방어적인 타입) |

### 실무 방어 패턴: Early Return과 Smart Cast
```kotlin
fun calculateTotal(items: List<Quiz03Item?>?): Int {
    // 1단계: 리스트 자체가 null인 경우 조기 반환
    if (items == null) return 0

    var total = 0
    // 2단계: 리스트 순회 및 개별 요소 null 검사
    for (item in items) {
        if (item != null) {
            // 이 블록 안에서 item은 non-null Quiz03Item으로 스마트 캐스트됨!
            total += item.price
        }
    }
    return total
}
```

---

## 7. Java와의 상호운용성(Interoperability) 및 플랫폼 타입 주의점

Java와 Kotlin을 혼용하는 프로젝트에서 컬렉션을 다룰 때는 두 언어의 철학 차이로 인한 위험을 인지해야 합니다.

### (1) Java는 불변/가변을 구분하지 않는다!
- 코틀린에서 `listOf("A", "B")`로 불변 리스트를 만들어 Java 메서드에 넘기면, Java는 이를 일반 `java.util.List`로 인식합니다.
- Java 코드에서 `list.add("C")`를 호출하면 코틀린 컴파일러의 의도와 달리 리스트의 불변성이 깨지거나 런타임 오류가 발생할 수 있습니다.
- **대응책**: Kotlin -> Java 전달 시 방어가 필요하다면 `Collections.unmodifiableList()`로 감싸거나 defensive copy를 수행합니다.

### (2) Java는 Nullability를 구분하지 않는다 (플랫폼 타입)!
- Java에서 반환된 `List<String>`을 코틀린이 받을 때, 코틀린 컴파일러는 이것이 `List<String>`, `List<String?>`, `List<String>?`, `List<String?>?` 중 무엇인지 알 수 없습니다. 이를 **플랫폼 타입(`List<String>!`)**이라 부릅니다.
- **대응책**: Java API와 통신하는 지점에서 즉시 명시적인 코틀린 타입으로 Wrapping하고 null 검사를 수행하여 영향 범위를 경계면으로 제한합니다.

---

## 8. 실습 퀴즈(Quiz01~03) 심층 분석 및 해설

### 📘 Quiz01: 불변 컬렉션 생성 및 대괄호 접근 (`Quiz01_Problem.kt` / `Quiz01_Solution.kt`)
- **출제 목적**: `listOf`, `mapOf(k to v)`, 대괄호 인덱싱(`[]`), 빈 리스트 예외 처리 체화.
- **핵심 코드**:
  ```kotlin
  private fun getFirstAndLast(numbers: List<Int>): Pair<Int, Int> {
      if (numbers.isEmpty()) {
          throw IllegalArgumentException("리스트가 비어 있습니다.")
      }
      return Pair(numbers[0], numbers[numbers.lastIndex])
  }

  private fun createDayMap(): Map<Int, String> = mapOf(
      1 to "MONDAY",
      2 to "TUESDAY",
      3 to "WEDNESDAY"
  )

  private fun getDayNameOrDefault(map: Map<Int, String>, day: Int): String {
      return map[day] ?: "UNKNOWN"
  }
  ```
- **해설 포인트**:
  - `numbers.size - 1` 대신 `numbers.lastIndex` 확장 프로퍼티를 사용하는 것이 코틀린다운 스타일입니다.
  - `map[day]`는 nullable 반환이므로 Elvis 연산자(`?: "UNKNOWN"`)를 통해 간결하게 기본값을 지정할 수 있습니다.

---

### 📘 Quiz02: Java to Idiomatic Kotlin 리팩토링 (`Quiz02_Problem.kt` / `Quiz02_Solution.kt`)
- **출제 목적**: Java의 번거로운 `HashMap + put()`과 인덱스 for 루프를 코틀린의 `mapOf` 및 `withIndex()`로 개선.
- **리팩토링 비교표**:

| Java 스타일 (기존) | Idiomatic Kotlin 스타일 (리팩토링) | 개선 효과 |
| :--- | :--- | :--- |
| `Map<K, V> map = new HashMap<>();`<br>`map.put("Apple", 1000);`<br>`return Collections.unmodifiableMap(map);` | `mapOf("Apple" to 1000)` | 단 1줄의 선언적 코드로 불변 Map 생성 |
| `for (int i=0; i<items.size(); i++) {`<br>`  res.add(i + "번: " + items.get(i));`<br>`}` | `for ((idx, item) in items.withIndex()) {`<br>`  result.add("${idx}번: ${item}")`<br>`}` | 경계 조건 오류 방지, 구조분해와 문자열 템플릿 결합 |

- **설계 주의점**:
  - 함수 내부에서는 누적을 위해 `mutableListOf()`를 사용하더라도, 반환 시에는 상위 불변 인터페이스인 `List<String>`으로 반환하여 캡슐화를 유지합니다.

---

### 📘 Quiz03: 컬렉션 Nullability 실무 장바구니 계산 (`Quiz03_Problem.kt` / `Quiz03_Solution.kt`)
- **출제 목적**: `List<Quiz03Item?>?` 복합 null 가능성 안전 처리 및 도메인 유효성 검증(`IllegalArgumentException`).
- **핵심 코드**:
  ```kotlin
  private fun calculateValidTotalPrice(items: List<Quiz03Item?>?): Int {
      if (items == null) return 0

      var total = 0
      for (item in items) {
          if (item != null) {
              if (item.price < 0) {
                  throw IllegalArgumentException("상품 가격은 음수일 수 없습니다.")
              }
              total += item.price
          }
      }
      return total
  }
  ```
- **해설 포인트**:
  - `items == null` 조기 반환으로 리스트 null 가능성(`List?`)을 즉시 방어합니다.
  - 루프 내 `item != null` 분기를 통해 `item`이 non-null로 자동 스마트 캐스트되어 안전하게 `item.price`에 접근할 수 있습니다.

---

## 9. 핵심 요약 치트시트 (Cheat Sheet)

```markdown
1. 컬렉션 생성 기본:
   👉 불변 컬렉션: listOf(), setOf(), mapOf(k to v)
   👉 가변 컬렉션: mutableListOf(), mutableSetOf(), mutableMapOf()
   👉 황금률: "우선 불변으로 만들고, 꼭 필요한 경우에만 가변으로 바꾼다!"

2. 요소 접근과 조작:
   👉 조회: list[index], map[key] (대괄호 연산자)
   👉 수정: mutableList[index] = val, mutableMap[key] = val
   👉 맵 초기화 중위 함수: key to value (Pair 생성)

3. 인덱스 순회:
   👉 for ((idx, value) in list.withIndex()) { ... }

4. 컬렉션 Nullability 3총사:
   👉 List<Int?>  : 리스트는 Not-Null, 원소는 Nullable
   👉 List<Int>?  : 리스트는 Nullable, 원소는 Not-Null
   👉 List<Int?>? : 리스트도 Nullable, 원소도 Nullable
```
