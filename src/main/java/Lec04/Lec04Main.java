package Lec04;

public class Lec04Main {

    public static void main(String[] args) {

        /* compareTo */
        JavaMoney money1 = new JavaMoney(2_000L);
        JavaMoney money2 = new JavaMoney(1_000L);

        if( money1.compareTo(money2) > 0 ) {
            System.out.println("Money1이 Money2보다 금액이 큽니다");
        }

        /* 동등성, 동일성 */
        JavaMoney m1 = new JavaMoney(1_000L);
        JavaMoney m2 = m1;
        JavaMoney m3 = new JavaMoney(1_000L);

        System.out.println(m1 == m2); // 두 객체가 "동일"한가? : true
        System.out.println(m1 == m3); // 두 객체가 "동일"한가? : false
        System.out.println(m1.equals(m3)); // 두 객체의 값이 "동등"한가? : true

        /* 직접 정의한 연산자를 사용 */
        JavaMoney jm1 = new JavaMoney(1_000L);
        JavaMoney jm2 = new JavaMoney(2_000L);
        System.out.println(jm1.plus(jm2)); // 3000

    }

}
