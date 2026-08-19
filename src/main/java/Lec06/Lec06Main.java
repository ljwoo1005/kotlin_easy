package Lec06;

import java.util.Arrays;
import java.util.List;

public class Lec06Main {

    private void printList() {

        List<Long> numbers = Arrays.asList(1L, 2L, 3L);

        for( long number : numbers ) {
            System.out.println(number);
        }

    }

    private void printNumber() {

        for( int i=1; i<=3; i++ ) {
            System.out.println( i );
        }

    }

    private void printNumber2() {

        for( int i=3; i>=1; i-- ) {
            System.out.println( i );
        }

    }

    private void printNumber3() {

        for( int i=1; i<=5; i+=2 ) {
            System.out.println( i );
        }

    }

    private void printNumber4() {

        int i = 1;

        while( i <= 3 ) {
            System.out.println( i );
            i++;
        }

    }

}
