public class ContadorSincronizado {
    private int contador = 0;

    /*
     * A palavra-chave 'synchronized' transforma um bloco em
     * uma Região Crítica. Se a Thread A entrar neste bloco, a
     * Thread B será bloqueada na entrada automaticamente.
     */
    public void incrementar(String nome) {
        // Avisa que a thread chegou e vai disputar o cadeado agora:
        System.out.println(nome + " tentando obter o lock...");
        System.out.flush();

        synchronized (this) { // O lock no 'this' inicia automaticamente aqui.
            System.out.println(nome + " entrou no bloco sincronizado.");
            System.out.flush();
            contador++;
            System.out.println(nome + " alterou o valor para: " + contador + ".");
            System.out.flush();
        } // O lock do 'this' é liberado automaticamente aqui.

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