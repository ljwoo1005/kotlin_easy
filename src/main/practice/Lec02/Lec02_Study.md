# [Lec02] 코틀린에서 null을 다루는 방법 (학습 가이드)

본 문서는 **Lec02 강의 노트(`Lec02Main.kt`, `Lec02Main.java`, `Person.java`)** 및 제작된 **실습 문제(Quiz01 ~ Quiz03)**를 완벽하게 이해하고 체화할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [코틀린이 null을 대하는 철학 (10억 달러짜리 실수의 극복)](#1-코틀린이-null을-대하는-철학-10억-달러짜리-실수의-극복)
2. [Nullable 타입(`Type?`)과 Non-null 타입(`Type`)](#2-nullable-타입type과-non-null-타입type)
3. [Safe Call 연산자 (`?.`)](#3-safe-call-연산자-)
4. [Elvis 연산자 (`?:`)와 그 확장 패턴](#4-elvis-연산자-와-그-확장-패턴)
5. [스마트 캐스트(Smart Cast)와 Null Check](#5-스마트-캐스트smart-cast와-null-check)
6. [null 아님 단언 (`!!`)과 사용 지양 원칙](#6-null-아님-단언-과-사용-지양-원칙)
7. [플랫폼 타입 (Platform Types)과 Java 상호운용성](#7-플랫폼-타입-platform-types과-java-상호운용성)
8. [실습 퀴즈(Quiz01~03) 심층 분석 및 해설](#8-실습-퀴즈quiz0103-심층-분석-및-해설)
9. [핵심 요약 치트시트 (Cheat Sheet)](#9-핵심-요약-치트시트-cheat-sheet)

---

## 1. 코틀린이 null을 대하는 철학 (10억 달러짜리 실수의 극복)

1965년 퀵 정렬(Quick Sort)의 창시자인 토니 호어(Tony Hoare)는 `null` 참조를 고안한 것을 두고 다음과 같이 회고했습니다:
> *"그것은 10억 달러짜리 실수였습니다. 단순히 구현하기 쉬웠다는 이유로 null 참조를 만들었습니다. 그로 인해 지난 수십 년간 수많은 오류와 취약점, 시스템 충돌이 발생했습니다."*

자바(Java)를 포함한 기존 언어들에서 가장 흔하게 발생하는 예외는 단연 `NullPointerException (NPE)`입니다. 자바에서는 모든 참조 타입 변수가 언제든 `null`을 가질 수 있기 때문에, 방어적인 `if (obj != null)` 코드가 도배되거나 깜빡하고 검사를 빼먹었을 때 런타임 장애로 직결되었습니다.

코틀린은 이러한 고통을 뿌리 뽑기 위해 **"런타임에 터지는 NPE를 컴파일 타임 에러로 끌어올린다"**는 대원칙 하에 설계되었습니다.

---

## 2. Nullable 타입(`Type?`)과 Non-null 타입(`Type`)

코틀린의 타입 시스템은 **null을 담을 수 있는 타입**과 **담을 수 없는 타입**을 완전히 다른 별개의 타입으로 분리하여 취급합니다.

```
       Any (모든 객체의 최상위 루트)
      /   \
    String  Person
      |       |
   String? Person?  (null을 허용하는 상위 타입)
      \       /
        null
```

### (1) 기본은 Non-null (null 절대 불허)
물음표(`?`)가 붙지 않은 일반 타입은 **절대로 null을 허용하지 않습니다.**
```kotlin
val str: String = "ABC"
// str = null // ❌ 컴파일 에러! Null can not be a value of a non-null type String
println(str.length) // ⭕ 100% 안전하게 호출 가능 (NPE 발생 불가)
```

### (2) Nullable 타입: 물음표(`?`)
오직 타입 선언 뒤에 명시적으로 `?`를 붙였을 때만 null을 대입할 수 있습니다.
```kotlin
val str: String? = "ABC"
// str.length // ❌ 컴파일 에러! Only safe (?.) or non-null asserted (!!.) calls are allowed
```
- 컴파일러 관점에서 `String?`은 '문자열이거나 혹은 null일 수 있는 미확정 상태'입니다.
- 따라서 코틀린 컴파일러는 `str.length`와 같이 직접적인 멤버 접근을 **컴파일 단계에서 원천 봉쇄**합니다.

---

## 3. Safe Call 연산자 (`?.`)

Safe Call 연산자(`?.`)는 Nullable 변수의 프로퍼티나 메서드에 안전하게 접근할 수 있도록 해주는 코틀린 고유의 연산자입니다.

```kotlin
val str: String? = "ABC"
val length: Int? = str?.length
```

### 동작 메커니즘
```
          [ str?.length ]
                 |
        str이 null인가?
          /           \
       [Yes]          [No]
         |              |
    그대로 null 반환    우변(.length) 정상 실행
```

1. **null이 아닌 경우**: 우변의 프로퍼티/메서드를 정상 실행하여 결과를 반환합니다.
2. **null인 경우**: 우변을 **아예 실행하지 않고 `null`을 반환**합니다. (NPE가 발생하지 않음)
3. **결과 타입**: 원래 반환 타입이 `Int`라면, Safe Call을 거친 결과의 타입은 `Int?`가 됩니다.

### 다단계 연쇄 호출 (Chaining)
중첩된 객체 구조를 탐색할 때 자바의 끝없는 null 체크 계단을 단 한 줄로 대체할 수 있습니다.
```kotlin
// Java 스타일
String domain = null;
if (order != null && order.getUser() != null && order.getUser().getEmail() != null) {
    domain = order.getUser().getEmail().substringAfter("@");
}

// Kotlin Safe Call 연쇄
val domain: String? = order?.user?.email?.substringAfter("@")
```
중간에 `order`, `user`, `email` 중 단 하나라도 null이면 전체 표현식이 즉시 안전하게 `null`로 평가됩니다.

---

## 4. Elvis 연산자 (`?:`)와 그 확장 패턴

Elvis 연산자(`?:`)는 기호를 90도 회전했을 때 록스타 엘비스 프레슬리의 시그니처 헤어스타일을 닮았다고 하여 붙여진 이름입니다.

```kotlin
val result = 좌변 ?: 우변
```
- **좌변의 결과가 null이 아니면**: 좌변의 값을 그대로 사용합니다.
- **좌변의 결과가 null이면**: 우변의 값을 대체 채택합니다.

### 패턴 1: 기본값 대체 (Fallback Value)
Safe Call과 결합하여 null일 때 안전한 기본값을 지정합니다.
```kotlin
val str: String? = null
val len: Int = str?.length ?: 0 // str이 null이므로 0이 됨
```

### 패턴 2: `?: throw` (표현식으로서의 예외 던지기)
자바에서는 `throw`가 문장(Statement)이어서 삼항 연산자나 대입식 우변에 둘 수 없었지만, **코틀린에서는 `throw`가 `Nothing` 타입을 반환하는 표현식(Expression)**입니다.
따라서 Elvis 연산자의 우변에 바로 위치할 수 있습니다.
```kotlin
// null이면 즉시 예외 발생, null이 아니면 Non-null String으로 변수에 할당됨
val name: String = person?.name ?: throw IllegalArgumentException("이름은 필수입니다.")
```

### 패턴 3: `?: return` (조기 반환, Early Return)
함수 진입부에서 필수 입력값이 null인 경우 함수 실행을 즉시 종료하는 가드 절(Guard Clause)을 극도로 간결하게 작성할 수 있습니다.
```kotlin
fun processOrder(order: Order?) {
    val currentOrder = order ?: return // order가 null이면 즉시 함수 종료
    
    // 이 아래 줄부터 currentOrder는 컴파일러에 의해 Non-null Order 타입으로 스마트 캐스트됨!
    println("주문 번호: ${currentOrder.id}")
}
```

---

## 5. 스마트 캐스트(Smart Cast)와 Null Check

코틀린 컴파일러는 코드의 제어 흐름(Control Flow)을 분석하여, **null이 아님이 검증된 지점 이후부터는 Nullable 타입을 자동으로 Non-null 타입으로 승격(Smart Cast)**시킵니다.

### (1) `if` 분기를 통한 스마트 캐스트
```kotlin
fun printLength(str: String?) {
    // 여기서는 str.length 불가능 (String?)
    if (str == null) {
        return
    }
    // 이 지점부터는 str이 절대 null일 수 없음을 컴파일러가 인지!
    println(str.length) // ⭕ Safe Call(?.) 없이 바로 접근 가능 (스마트 캐스트: String)
}
```

### (2) Elvis Early Return을 통한 스마트 캐스트
```kotlin
fun calculate(number: Long?): Long {
    val num = number ?: return 0L
    return num + 100L // ⭕ num은 Long 타입으로 스마트 캐스트되어 연산 가능
}
```

---

## 6. null 아님 단언 (`!!`)과 사용 지양 원칙

`!!` 연산자(Not-null assertion)는 컴파일러에게 **"이 변수는 Nullable 타입이지만, 내 명예를 걸고 절대 null이 아니라고 확신하니 강제로 Non-null로 취급해라"**라고 명령하는 것입니다.

```kotlin
val str: String? = getNullableString()
val len: Int = str!!.length // 만약 str이 null이라면?!
```

### 왜 `!!`를 절대적으로 지양해야 하는가?
1. **런타임 NPE의 부활**: 만약 프로그래머의 예상과 달리 null이 들어온다면, 아무런 경고 없이 런타임에 `KotlinNullPointerException`이 발생합니다.
2. **부실한 오류 원인**: `order!!.user!!.email!!`처럼 단언 연산자를 남발하면, 에러 발생 시 어느 객체가 null이었는지 추적하기가 매우 어렵습니다.
3. **코틀린 철학의 정면 위배**: 컴파일 타임에 널 안전성을 보장하려는 언어의 핵심 설계를 스스로 무력화하는 행위입니다.

> 💡 **Best Practice**:
> - `!!`는 스프링 테스트 코드 등 특정 프레임워크 생명주기상 100% 확신이 있는 극소수의 상황을 제외하고는 **절대 사용하지 않는 것**을 원칙으로 합니다.
> - 대신 **Safe Call(`?.`)**, **Elvis 기본값(`?: 0`)**, **명시적 예외 발생(`?: throw IllegalStateException("...")`)**을 사용하세요.

---

## 7. 플랫폼 타입 (Platform Types)과 Java 상호운용성

코틀린은 기존 Java 라이브러리 및 코드와 100% 완벽하게 상호 운용(Interoperability)되도록 설계되었습니다. 그렇다면 **자바에서 넘어온 값의 널 가능성은 코틀린에서 어떻게 취급될까요?**

### (1) 자바 애노테이션이 존재하는 경우
코틀린 컴파일러가 애노테이션을 인식하여 타입을 명확하게 매핑합니다.
- 자바의 `@Nullable String` -> 코틀린의 `String?` (Nullable)
- 자바의 `@NotNull String`   -> 코틀린의 `String` (Non-null)

### (2) 자바 애노테이션이 전혀 없는 경우: 플랫폼 타입 (`Type!`)
자바 표준 라이브러리나 많은 레거시 코드에는 널 관련 애노테이션이 없습니다. 이때 코틀린 컴파일러는 이 값이 null이 가능한지 아닌지 알 수 없으므로, 이를 **플랫폼 타입(Platform Type)**으로 간주합니다.
- IDE 표기: `String!` (느낌표 표기는 컴파일러 내부 표기이며 코드에 직접 작성할 수는 없음)
- **위험성**: 코틀린 컴파일러는 플랫폼 타입 변수를 `String`으로 취급할 수도 있고 `String?`으로 취급할 수도 있도록 허용합니다. 만약 개발자가 `String`으로 선언하여 받았다가 실제 런타임에 null이 넘어오면 즉시 NPE가 터집니다.

```kotlin
val person = Person("홍길동") // Java 클래스

// [위험한 코드] 개발자가 자바 코드를 맹신하고 Non-null로 받음
val name: String = person.name // person.getName()이 null을 반환하면 런타임 NPE 발생!

// [안전한 방어 코드] 자바 연동 지점에서는 명시적으로 Nullable로 받아 방어
val safeName: String? = person.name
val finalName: String = person.name ?: "이름 없음"
```

> 💡 **Java 상호운용성 원칙**:
> 1. 자바 코드를 코틀린에서 호출할 때는 항상 해당 메서드가 null을 반환할 가능성이 있는지 확인한다.
> 2. 자바 코드를 감싸는 **Kotlin 래퍼(Wrapper) 계층을 두어**, 널 가능성을 코틀린 단에서 조기에 제어하고 비즈니스 영역으로 null 위험이 전파되지 않도록 격리한다.

---

## 8. 실습 퀴즈(Quiz01~03) 심층 분석 및 해설

### 📘 Quiz01: Safe Call & Elvis 기본 체화 (`Quiz01_Problem.kt` / `Quiz01_Solution.kt`)
- **출제 의도**: `?.`와 `?:` 연산자의 기본 문법 1:1 체화.
- **핵심 해답 분석**:
  ```kotlin
  // 1. Safe Call과 Elvis 결합: null이면 기본값 0 반환
  private fun getSafeLength(str: String?): Int {
      return str?.length ?: 0
  }

  // 2. 단일 Safe Call: null이면 null 전파
  private fun getUpperOrNull(str: String?): String? {
      return str?.uppercase()
  }
  ```
- **주요 포인트**:
  - `str?.length`의 결과가 `Int?`이므로, `?: 0`을 붙임으로써 최종 반환 타입이 Non-null인 `Int`로 완성됩니다.

---

### 📘 Quiz02: Java to Idiomatic Kotlin 리팩토링 (`Quiz02_Problem.kt` / `Quiz02_Solution.kt`)
- **출제 의도**: Java의 전형적인 3대 if-null 분기 로직을 코틀린 표현식으로 변환 (Case A 적용).
- **비교 분석 표**:

| 대상 로직 | Java 스타일 (명령형 분기) | Idiomatic Kotlin (표현식) | 핵심 차이점 |
| :--- | :--- | :--- | :--- |
| **1. null이면 예외** | `if (str == null) throw new IllegalArgumentException(...); return str.startsWith("A");` | `str?.startsWith("A") ?: throw IllegalArgumentException(...)` | `throw`가 표현식이므로 Elvis 우변에 합성 가능 |
| **2. null이면 null** | `if (str == null) return null; return str.startsWith("A");` | `str?.startsWith("A")` | Safe Call 단독 사용으로 불필요한 null 분기문 제거 |
| **3. null이면 기본값** | `if (str == null) return false; return str.startsWith("A");` | `str?.startsWith("A") ?: false` | `?: 기본값`으로 간결한 Fallback 처리 |

---

### 📘 Quiz03: 실무 미니 시나리오 (`Quiz03_Problem.kt` / `Quiz03_Solution.kt`)
- **출제 의도**: 중첩 도메인 모델에서의 연쇄 탐색, Early Return, 안전한 예외 발생.
- **핵심 해답 분석**:
  ```kotlin
  // 1. 다단계 연쇄 Safe Call과 널 가드
  private fun getUserEmailDomain(order: Quiz03Order?): String {
      val email = order?.user?.email ?: return "UNKNOWN"
      return if (email.contains("@")) email.substringAfter("@") else "UNKNOWN"
  }

  // 2. Elvis Early Return 패턴 및 스마트 캐스트
  private fun calculateTotalPayment(order: Quiz03Order?): Int {
      val currentOrder = order ?: return 0 // null이면 조기 종료
      val discount = currentOrder.couponDiscount ?: 0 // 스마트 캐스트 활용
      return maxOf(0, currentOrder.itemPrice - discount)
  }

  // 3. !! 대신 비즈니스 메시지를 담은 예외 던지기
  private fun getRequiredUserName(order: Quiz03Order?): String {
      return order?.user?.name ?: throw IllegalStateException("주문 고객 정보가 누락되었습니다.")
  }
  ```
- **실무 시사점**:
  - `val currentOrder = order ?: return 0` 패턴은 불필요한 들여쓰기(Indent)를 방지하고, 이후 코드에서 안전하게 Non-null 인스턴스로 작업할 수 있게 해주는 코틀린 실무의 필수 관용구입니다.

---

## 9. 핵심 요약 치트시트 (Cheat Sheet)

```markdown
1. 기본 원칙:
   👉 코틀린의 모든 일반 타입은 Non-null이다. null을 담으려면 반드시 '?'를 붙여라 (Type?).

2. Safe Call (?.) :
   👉 null이 아니면 실행하고, null이면 우변을 아예 실행하지 않고 null을 반환한다.
   👉 다단계 참조는 'a?.b?.c' 형태로 간결하게 연결한다.

3. Elvis (?:) :
   👉 앞의 결과가 null일 때 대체할 값을 우변에 지정한다.
   👉 우변에 기본값뿐 아니라 '?: throw Exception()' 이나 '?: return'도 사용 가능하다.

4. 널 아님 단언 (!!) 지양:
   👉 '!!'는 런타임 NPE를 부르는 폭탄이다.
   👉 100% 확신이 들더라도 '?: throw IllegalStateException("...")'으로 명확한 메시지를 남겨라.

5. Java 연동 시 주의:
   👉 애노테이션 없는 자바 코드는 플랫폼 타입(Type!)이므로 언제든 NPE가 날 수 있다.
   👉 코틀린 단에서 방어적으로 Nullable로 취급하거나 래핑하여 안전을 확보하라.
```
