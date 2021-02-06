package HomeWork05;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    Semaphore SEMAPHORE = new Semaphore(MainClass.CARS_COUNT / 2);

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            try {
                if (!SEMAPHORE.tryAcquire()) {
                    System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                    SEMAPHORE.acquire();
                }
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                SEMAPHORE.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
