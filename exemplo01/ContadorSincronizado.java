public class ContadorSincronizado {
    private int contador = 0;
     
    /* 
     * A palavra-chave 'synchronized' transforma o método inteiro em 
     * uma Região Crítica. Se a Thread A chamar este método, a Thread B 
     * será bloqueada na entrada automaticamente.
     */
    public synchronized void incrementar(String nome) {
        System.out.println(nome + " entrou no bloco sincronizado.");
        contador++;
        System.out.println(nome + " alterou o valor para: " + contador);
    }

    public int getContador() {
        return contador;
    }
}
