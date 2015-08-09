package main;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Mkhitar on 17.03.2015.
 */
public class Utils {

    public static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static int[] nearlySorted(int[] array, int shuffleSize) {
        Random r = new Random();
        for (int i = 0; i < array.length; i += shuffleSize) {
            //Prevents us from getting index out of bounds, while still getting a shuffle of the
            //last set of un shuffled array, but breaks for loop if the number of unshuffled array is 1
            if (i + shuffleSize >= array.length) {

                int[] a = Arrays.copyOfRange(array, 0, shuffleSize);
                int[] b = Arrays.copyOfRange(array, shuffleSize, array.length);

                System.arraycopy(b, 0, array, 0, b.length);
                System.arraycopy(a, 0, array, b.length, a.length);

                return array;
            }

            if (i % shuffleSize == 0) {
                for (int j = i; j < i + shuffleSize; j++) {
                    // Pick random element to swap from our small section of the array.
                    int k = r.nextInt(shuffleSize);
                    // Swap.
                    int tmp = array[k];
                    array[k] = array[j];
                    array[j] = tmp;
                }
            }
        }

        return array;
    }

}
