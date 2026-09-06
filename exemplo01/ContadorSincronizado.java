public class ContadorSincronizado {
    private int contador = 0;
     
    /* 
     * A palavra-chave 'synchronized' transforma o método inteiro em 
     * uma Região Crítica. Se a Thread A chamar este método, a Thread B 
     * será bloqueada na entrada automaticamente.
     */
    public synchronized void incrementar(String nome) { // O lock inicia automaticamente aqui.
        System.out.println(nome + " entrou no bloco sincronizado.");
        System.out.flush();

        contador++;

        System.out.println(nome + " alterou o valor para: " + contador + ".");
        System.out.flush();
    } // O lock é liberado automaticamente aqui.

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