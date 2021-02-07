package HomeWork06;
import java.util.Arrays;

public class HomeWork06 {

    public static void main(String[] args) {
        int[] testArr1 = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        int[] testArr2 = {1, 1, 1, 4, 4, 1, 4, 4};

        System.out.println(Arrays.toString(new HomeWork06().remakeArray(testArr1)));
        System.out.println("---------------");
        System.out.println(new HomeWork06().hasAnyRequestedNumbers(testArr2));
    }

    public int[] remakeArray(int[] arr) {
        int startingIndex = 0;
        boolean hasRequiredNumber = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 4) {
                startingIndex = i;
                hasRequiredNumber = true;
            }
        }
        if (!hasRequiredNumber) {
            throw new RuntimeException("Нет чевёрок!");
        }

        int[] out = new int[arr.length - startingIndex - 1];
        int j = 0;
        for (int i = startingIndex + 1; i < arr.length; i++) {
            out[j] = arr[i];
            j++;
        }
        return out;
    }

    public boolean hasAnyRequestedNumbers(int[] arr) {
        int num1 = 0;
        int num2 = 0;

        for (int i : arr) {
            if (i == 1) num1++;
            if (i == 4) num2++;
        }
        return num1 != 0 && num2 != 0;
    }

}
