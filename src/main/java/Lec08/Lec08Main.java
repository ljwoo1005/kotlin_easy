package Lec08;

public class Lec08Main {

    /*
     * 두 정수를 받아 더 큰 정수를 반환하는 예제
     */
    public int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    /*
     * 특정 문자열을 몇 번 반복할지 정하는 예제
     */
    public void repeat(String str, int num, boolean useNewLine) {
        for (int i = 0; i < num; i++) {
            if (useNewLine) {
                System.out.println(str);
            } else {
                System.out.print(str);
            }
        }
    }

    /*
     * 만약 대부분의 경우에서 useNewLine을 true로 사용한다 하면, 자바에서는 Overloading으로 함수를 더 편리하게 사용할 수 있다.
     */
    public void repeat(String str, int num) {
        repeat(str, num, true);
    }

    /*
     * 이번엔 대부분의 경우에서 useNewLine을 true로 하면서, 3번씩 반복해서 출력한다면?
     */
    public void repeat(String str) {
        repeat(str, 3, true);
    }

    /*
     * 문자열을 N개 받아 출력하는 예제
     * 자바에서는 타입... 을 사용하면 가변인자 사용!
     */
    public static void printAll(String... strings) {
        for (String str : strings) {
            System.out.println(str);
        }
    }

    public static void callPrintAll() {
        // 가변인자 함수 호출 방법
        // #1 : 배열을 직접 사용
        String[] strs = new String[] {"A", "B", "C"};
        printAll(strs);

        // #2 : 콤마를 사용
        printAll("A", "B", "C");
    }

}
