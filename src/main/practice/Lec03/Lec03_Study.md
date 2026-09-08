# [Lec03] 코틀린에서 Type을 다루는 방법 (학습 가이드)

본 문서는 **Lec03 강의 노트(`Lec03Main.kt`, `Lec03Main.java`)** 및 제작된 **실습 문제(Quiz01 ~ Quiz03)**를 완벽하게 이해하고 체화할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [코틀린의 타입 시스템 철학](#1-코틀린의-타입-시스템-철학)
2. [기본 타입(Primitive Types)과 명시적 형변환](#2-기본-타입primitive-types과-명시적-형변환)
3. [객체 타입 캐스팅과 스마트 캐스트(Smart Cast)](#3-객체-타입-캐스팅과-스마트-캐스트smart-cast)
4. [코틀린의 3가지 특이한 타입 (Any, Unit, Nothing)](#4-코틀린의-3가지-특이한-타입-any-unit-nothing)
5. [코틀린의 문자열 다루기 (템플릿, 멀티라인, 인덱싱)](#5-코틀린의-문자열-다루기-템플릿-멀티라인-인덱싱)
6. [실습 퀴즈(Quiz01~03) 심층 분석 및 해설](#6-실습-퀴즈quiz0103-심층-분석-및-해설)
7. [핵심 요약 치트시트 (Cheat Sheet)](#7-핵심-요약-치트시트-cheat-sheet)

---

## 1. 코틀린의 타입 시스템 철학

자바(Java)는 유연성과 편의성을 위해 기본형 간의 **암시적 프로모션(Implicit Promotion)**이나 런타임 캐스팅에 의존하는 경우가 많았습니다.
하지만 이러한 묵시적 변환은 예기치 않은 데이터 손실, 오버플로우, 그리고 런타임 `ClassCastException`이라는 고질적인 문제를 야기했습니다.

코틀린은 이러한 문제를 해결하기 위해 다음의 3대 설계 원칙을 고수합니다:
1. **타입 안전성(Type Safety)의 절대성**: 암시적 변환을 완전히 배제하고, 모든 기본형 변환은 개발자가 의도를 갖고 명시적으로 수행하도록 강제합니다.
2. **보일러플레이트 제거**: `instanceof` 후 매번 수동으로 강제 캐스팅하던 자바의 비효율을 컴파일러 수준의 **스마트 캐스트(Smart Cast)**로 우아하게 자동화합니다.
3. **완전한 타입 계층 구조**: 자바의 `void`나 원시형/참조형의 이원화 문제를 극복하고, 최상위 `Any`부터 최하위 `Nothing`까지 하나의 완벽하고 일관된 계층으로 통합했습니다.

---

## 2. 기본 타입(Primitive Types)과 명시적 형변환

### (1) 리터럴에 따른 타입 추론

코틀린은 변수의 초기 리터럴 값을 보고 타입을 자동으로 결정합니다:

```kotlin
val n1 = 3       // Int (기본 정수형)
val n2 = 3L      // Long (L 접미사)
val n3 = 3.0f    // Float (f 접미사)
val n4 = 3.0     // Double (기본 실수형)
```

### (2) 자바의 암시적 변환 vs 코틀린의 명시적 변환

자바에서는 더 큰 타입으로의 확장은 자동으로 처리되었습니다:
```java
// Java
int number1 = 4;
long number2 = number1; // ⭕ 암시적 변환 (Implicit Cast)
```

그러나 코틀린에서는 타입 간 크기와 관계없이 **암시적 변환이 전면 금지**됩니다:
```kotlin
// Kotlin
val number1: Int = 4
// val number2: Long = number1 // ❌ 컴파일 에러! (Type mismatch: expected 'Long', actual 'Int')
val number2: Long = number1.toLong() // ⭕ 명시적 변환 메서드 호출 필수
```

> **왜 코틀린은 암시적 변환을 막았을까?**  
> 박싱(Boxing)된 객체 간의 비교나 제네릭 컬렉션에서 `Int`와 `Long`이 섞이면 `equals()`나 `hashCode()`가 일치하지 않아 치명적인 버그가 발생하기 때문입니다.  
> (예: Java에서 `Integer.valueOf(4).equals(Long.valueOf(4))`는 `false`를 반환합니다.)

### (3) 주요 변환 메서드와 정수 나눗셈 주의점

코틀린의 모든 기본형은 `.toByte()`, `.toShort()`, `.toInt()`, `.toLong()`, `.toFloat()`, `.toDouble()`, `.toChar()` 메서드를 제공합니다.

> ⚠️ **정수 나눗셈 시 흔히 하는 실수 (Pitfall)**:
> ```kotlin
> val a = 1
> val b = 4
> val ratio1 = (a / b).toDouble()         // ❌ 0.0 (정수 나눗셈이 먼저 수행되어 0이 된 후 Double 변환)
> val ratio2 = a.toDouble() / b.toDouble() // ⭕ 0.25 (나눗셈 연산 전에 Double로 변환)
> ```

### (4) Nullable 기본형의 안전한 변환

변수가 `Int?`와 같이 Nullable일 경우, 메서드를 직접 호출할 수 없습니다.  
이때 **Safe Call(`?.`)**과 **Elvis(`?:`)** 연산자를 결합하여 안전하게 변환합니다:

```kotlin
val number: Int? = null
val result: Long = number?.toLong() ?: 0L // number가 null이면 기본값 0L 할당
```

---

## 3. 객체 타입 캐스팅과 스마트 캐스트(Smart Cast)

### (1) 자바의 구식 캐스팅 패턴

```java
// Java
public void printAge(Object obj) {
    if (obj instanceof Person) {
        Person person = (Person) obj; // 수동 형변환 필수 (중복 코드)
        System.out.println(person.getAge());
    }
}
```

### (2) 코틀린의 스마트 캐스트 (`is`, `!is`)

코틀린에서는 `is` 키워드로 대상 타입을 검사하면, 컴파일러가 해당 조건문 블록 안에서 대상을 **해당 타입으로 자동 캐스팅(Smart Cast)**해 줍니다:

```kotlin
fun printAge(obj: Any) {
    if (obj is Person) {
        // obj가 자동으로 Person 타입으로 취급됨 (별도의 as Person 불필요!)
        println(obj.age) 
    }

    if (obj !is Person) {
        // Person이 아닌 경우의 로직 처리
    }
}
```

### (3) 안전한 캐스트 연산자 (`as` vs `as?`)

| 연산자 | 동작 방식 | 실패 시 결과 | 적합한 사용 시점 |
| :--- | :--- | :--- | :--- |
| `as` | 강제 캐스팅 (Unsafe Cast) | `ClassCastException` 또는 `NullPointerException` | 100% 해당 타입임이 확실할 때 |
| `as?` | 안전한 캐스트 (Safe Cast) | `null` 반환 (예외 없음) | 타입이 다를 수 있거나 null일 가능성이 있는 모든 상황 |

```kotlin
// 1. as (위험)
val person = obj as Person // obj가 null이거나 Person이 아니면 앱 비정상 종료!

// 2. as? (안전한 관용구)
val personOrNull = obj as? Person // 실패 시 안전하게 null 반환
val age = (obj as? Person)?.age   // Safe Call과 결합하여 한 줄로 안전하게 프로퍼티 추출!
```

---

## 4. 코틀린의 3가지 특이한 타입 (Any, Unit, Nothing)

```
              Any? (모든 것의 슈퍼타입, null 허용)
                |
               Any  (모든 Non-null 타입의 최상위 루트)
             /  |  \
       String  Int  Person ...
             \  |  /
             Nothing (모든 Non-null 타입의 최하위 바텀타입)
                |
            Nothing? (null 리터럴의 타입)
```

### (1) `Any`: 모든 객체의 조상
- Java의 `java.lang.Object`에 대응하며, 모든 코틀린 클래스의 최상위 슈퍼타입입니다.
- **차이점**: 자바의 원시 타입(`int`, `double`)은 `Object`에 담길 수 없지만, 코틀린의 모든 기본형(`Int`, `Double`)도 `Any`의 서브타입입니다.
- 기본적으로 Non-null이므로, null을 허용하려면 `Any?`로 표현해야 합니다.
- 제공 메서드: `equals()`, `hashCode()`, `toString()`

### (2) `Unit`: 자바의 void를 대체하는 싱글톤 객체
- 자바의 `void`와 동일하게 "반환값이 없음"을 나타냅니다.
- **차이점**: `void`는 아무것도 반환하지 않는 특수 키워드이지만, `Unit`은 **실제로 메모리에 단 하나의 인스턴스만 존재하는 싱글톤 객체(타입)**입니다.
- 따라서 제네릭 타입 인자로 사용할 수 있습니다:
  ```kotlin
  // Java에서는 Void 타입을 쓰고 return null; 을 해야 했음
  // Kotlin에서는 Unit을 그대로 사용
  interface Callback<T> {
      fun call(): T
  }
  class ActionCallback : Callback<Unit> {
      override fun call() {
          println("실행 완료") // return 생략 가능 (컴파일러가 Unit 반환)
      }
  }
  ```

### (3) `Nothing`: 절대 정상 종료되지 않는 함수
- 함수가 정상적으로 값을 반환하며 끝나지 않고, **무조건 예외를 던지거나 무한 루프를 도는 경우**를 명시적으로 나타내는 타입입니다.
- 코틀린의 모든 타입의 최하위 서브타입(**Bottom Type**)입니다.

```kotlin
// 예외 전담 함수 정의
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}

// Nothing의 강력함: when 식이나 엘비스 연산자에서 표현식의 일부로 자연스럽게 결합
val payment = (raw as? Payment) ?: fail("결제 정보 누락")
// 컴파일러는 위 줄 이후 payment가 절대 null이 아님(Non-null)을 100% 확신함!
```

---

## 5. 코틀린의 문자열 다루기 (템플릿, 멀티라인, 인덱싱)

### (1) 문자열 템플릿 (String Interpolation)
Java의 복잡한 `String.format()`이나 `StringBuilder`를 대체합니다:

```kotlin
val name = "홍길동"
val age = 25

// 1. 단순 변수: $name
println("이름: $name")

// 2. 복합 표현식 또는 멤버 참조: ${expression}
println("이름: ${person.name}, 내년 나이: ${person.age + 1}세")
```

> 💡 **Best Practice**:  
> 단순 변수라도 `${name}` 형태로 중괄호를 붙여주는 습관을 들이면, 뒤에 바로 한글 조사가 붙거나 일괄 변환/정규식 작업 시 파싱 오류를 미연에 방지할 수 있습니다.

### (2) 여러 줄 문자열 (Multi-line Strings)
큰따옴표 3개(`"""`)를 사용하며, 줄바꿈과 공백이 그대로 보존됩니다.  
코드의 들여쓰기를 정돈하기 위해 **`.trimIndent()`**를 반드시 함께 호출합니다:

```kotlin
val receipt = """
    [영수증]
    - 번호: ${tx.id}
    - 금액: ${tx.amount}원
""".trimIndent() // 공통 들여쓰기 공백을 자동으로 잘라내어 왼쪽 정렬시킴
```

### (3) 문자열 인덱싱 (String Indexing)
자바의 `str.charAt(index)` 대신 파이썬이나 배열처럼 **대괄호 인덱싱(`str[index]`)**을 지원합니다:

```kotlin
val lang = "KOTLIN"
val firstChar: Char = lang[0]                 // 'K'
val lastChar: Char = lang[lang.length - 1]     // 'N'
```

---

## 6. 실습 퀴즈(Quiz01~03) 심층 분석 및 해설

### 📘 Quiz01: 기본 타입 명시적 변환 및 Nullable 안전 처리
- **학습 핵심**: `toDouble()`, `toLong()`, `?.toLong() ?: 0L`, `name[0]`
- **모범 코드 분석**:
  ```kotlin
  // 1. 정수 나눗셈 소수점 보존
  private fun calculateRatio(count: Int, total: Int): Double {
      if (total <= 0) return 0.0
      return count.toDouble() / total.toDouble()
  }

  // 2. Safe Call과 Elvis를 결합한 기본형 null 방어
  private fun addBonusPoints(base: Long, bonus: Int?): Long {
      val bonusLong = bonus?.toLong() ?: 0L
      return base + bonusLong
  }

  // 3. 인덱싱과 템플릿의 결합
  private fun formatInitials(name: String): String {
      return "[${name[0]}...${name[name.length - 1]}]"
  }
  ```

---

### 📘 Quiz02: Java to Idiomatic Kotlin 리팩토링
- **학습 핵심**: `is` 스마트 캐스트, `as?` 안전 캐스팅, 문자열 템플릿
- **비교 분석**:

| 처리 항목 | Java 기존 방식 | Kotlin 관용적 방식 |
| :--- | :--- | :--- |
| **타입 확인 & 캐스팅** | `if (obj instanceof Person) { Person p = (Person) obj; ... }` | `if (obj is Person) { ... obj.name ... }` (자동 스마트 캐스트) |
| **실패 가능한 값 추출** | `if (obj instanceof Person) return ((Person) obj).getAge(); else return null;` | `(obj as? Person)?.age` (단 1줄의 표현식) |
| **문자열 결합** | `p.getName() + "(" + p.getAge() + "세)"` | `"${obj.name}(${obj.age}세)"` |

---

### 📘 Quiz03: 실전 결제 이벤트 감사 리포터
- **학습 핵심**: `Any?` 다형성 파싱, `when (x) { is Type -> }`, `Nothing`, `"""...""".trimIndent()`
- **모범 코드 분석**:
  ```kotlin
  // 1. Nothing 타입 예외 함수
  private fun fail(message: String): Nothing {
      throw IllegalArgumentException(message)
  }

  // 2. when 식과 is 스마트 캐스트
  private fun normalizeAmount(rawAmount: Any?): Long {
      return when (rawAmount) {
          is Long -> rawAmount
          is Int -> rawAmount.toLong()
          is String -> rawAmount.toLongOrNull() ?: fail("유효하지 않은 금액 형식: $rawAmount")
          else -> fail("지원하지 않는 금액 타입입니다: $rawAmount")
      }
  }

  // 3. 멀티라인 영수증 조립
  private fun generateReceipt(target: Any?): String {
      if (target !is Quiz03Payment) {
          fail("결제 정보가 올바르지 않습니다: $target")
      }
      return """
          [결제 영수증]
          - 거래번호: ${target.txId}
          - 고객명: ${target.user[0]}** (${target.user})
          - 결제금액: ${target.amount}원
      """.trimIndent()
  }
  ```

---

## 7. 핵심 요약 치트시트 (Cheat Sheet)

```markdown
1. 기본형 변환 원칙:
   👉 코틀린에 암시적 변환은 없다! 무조건 '.toLong()', '.toDouble()'을 호출하라.
   👉 나눗셈 연산 전 변환: '(a / b).toDouble()' (❌)  vs  'a.toDouble() / b' (⭕)

2. 타입 캐스팅 가이드:
   👉 확인과 동시에 사용하려면: 'if (obj is Type)' (스마트 캐스트)
   👉 타입 불일치 시 null로 흘려보내려면: '(obj as? Type)?.property'
   👉 'as'는 예외(ClassCastException)를 던지므로 가급적 지양하고 'as?'를 선호하라.

3. 특이한 타입 3총사:
   👉 Any: 자바의 Object 역할 + 모든 Primitive의 슈퍼타입 (null 원하면 Any?)
   👉 Unit: 자바의 void 역할이지만 실제 존재하는 싱글톤 객체 (제네릭 인자 가능)
   👉 Nothing: 정상 종료되지 않는 함수 (throw). Bottom Type으로 모든 식과 호환.

4. 문자열 다루기:
   👉 '$name'보다 '${name}' 습관화 (가독성, 리팩토링, 안전성).
   👉 여러 줄 문자열은 반드시 '"""...""".trimIndent()'로 들여쓰기 공백 제거.
   👉 특정 위치 문자는 'str.charAt(i)' 대신 배열처럼 'str[i]'.
```
