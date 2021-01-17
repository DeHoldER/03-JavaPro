package HomeWork01;

public class TaskHard {

    public static void main(String[] args) {
        // Создаём коробки для фруктов
        Box<Apple> appleBox = new Box<>();
        Box<Orange> orangeBox1 = new Box<>();
        // такая же коробка с апельсинами для проверки метода 'compare'
        Box<Orange> orangeBox2 = new Box<>();

        // Кладём фрукты в коробки
        for (int i = 0; i < 10; i++) {
            appleBox.add(new Apple());
            orangeBox1.add(new Orange());
            orangeBox2.add(new Orange());
        }

        // Считаем сколько где чего
        System.out.println("В коробке с яблоками " + appleBox.getItemsNumber() + " яблок. Общий вес: " + appleBox.getWeight() + " кг");
        System.out.println("В orangeBox1 " + orangeBox1.getItemsNumber() + " апельсинов. Общий вес: " + orangeBox1.getWeight() + " кг");
        System.out.println("В orangeBox2 " + orangeBox1.getItemsNumber() + " апельсинов. Общий вес: " + orangeBox1.getWeight() + " кг");
        System.out.println();

        // Сравниваем коробки
        System.out.println("Сравнение коробок appleBox и orangeBox1: " + appleBox.compare(orangeBox1));
        System.out.println("Сравнение коробок orangeBox1 и orangeBox2: " + orangeBox1.compare(orangeBox2));
        System.out.println();

        // Пересыпаем апельсины из orangeBox1 в orangeBox2
        orangeBox1.pourOver(orangeBox2);
        System.out.println("В orangeBox1 теперь " + orangeBox1.getItemsNumber() + " апельсинов. Общий вес: " + orangeBox1.getWeight() + " кг");
        System.out.println("В orangeBox2 теперь " + orangeBox2.getItemsNumber() + " апельсинов. Общий вес: " + orangeBox2.getWeight() + " кг");
    }
}
