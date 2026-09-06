public class Main {
    public static void main(String[] args) throws InterruptedException {
        ContadorSincronizado contadorSincronizado = new ContadorSincronizado();

        // Criamos duas threads que usam o mesmo objeto contadorSincronizado:
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 3; i++) 
                contadorSincronizado.incrementar("Thread_A");
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0; i < 3; i++) 
                contadorSincronizado.incrementar("Thread_B");
        });

        t1.start();
        t2.start();
    }
}
