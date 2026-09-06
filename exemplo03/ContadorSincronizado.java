public class ContadorSincronizado {
    private int contador = 0;
    
    /* --- Objeto Privado Genérico (Lock Dedicado) ---
     * Usamos 'final' para garantir que a referência do objeto de tranca nunca mude.
     */
    private final Object trava = new Object();

    public void incrementar(String nome) {
        
        // Log executado fora do bloco: mostra o momento em que a thread chega para disputar o cadeado (lock):
        System.out.println(nome + " tentando obter o lock...");
        System.out.flush();

        // Sincronizamos no objeto privado 'trava' em vez de usar o 'this':
        synchronized (trava) { // O lock no objeto 'trava' inicia automaticamente aqui.
            System.out.println(nome + " entrou no bloco sincronizado.");
            System.out.flush();
            
            contador++;

            System.out.println(nome + " alterou o valor para: " + contador + ".");
            System.out.flush();
        } // O lock no objeto 'trava' é liberado automaticamente aqui.

        System.out.println(nome + " saiu do bloco sincronizado.");
        System.out.flush();
    }

    public int getContador() {
        return contador;
    }
}

/*
 * OBS: 
 * System.out.flush(): Java às vezes agrupa caracteres em buffers 
 * antes de lançar na tela, o que pode bagunçar a ordem visual dos 
 * prints no terminal de testes. O .flush() força a saída imediata 
 * do texto, garantindo que o log na tela reflita o instante em que 
 * a thread passou por ali.
 */