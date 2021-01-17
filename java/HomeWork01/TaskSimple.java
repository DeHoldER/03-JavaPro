package HomeWork01;

import java.util.ArrayList;

public class TaskSimple {

    //1. Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);

    public static void main(String[] args) {

    String[] testArr = new String[5];

        for (int i = 0; i < testArr.length; i++) {
            testArr[i] = "test " + i;
        }

        System.out.println("До обработки методом:");
        System.out.println(testArr[2]);
        System.out.println(testArr[4]);

        arrSwapElements(testArr,2,4);

        System.out.println("После обработки методом:");
        System.out.println(testArr[2]);
        System.out.println(testArr[4]);

        System.out.println("\nПреобразование массива в ListArray:");
        System.out.println(convertToArrayList(testArr).getClass());
    }

    public static void arrSwapElements(Object[] arr, int elementIndex1, int elementIndex2) {
        Object buffer;
        try {
            buffer = arr[elementIndex1];
            arr[elementIndex1] = arr[elementIndex2];
            arr[elementIndex2] = buffer;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("Параметры вне массива");
        }
    }

    public static ArrayList<Object> convertToArrayList(Object[] arr) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            arrayList.add(arr[i]);
        }
        return arrayList;

        // IDEA сократила всё это до одной строчки :)
//        return new ArrayList<>(Arrays.asList(arr));
    }
}
