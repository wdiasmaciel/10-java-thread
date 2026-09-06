# 10-java-thread

# synchronized

- A palavra-chave `synchronized` cria exclusão mútua em Java. 

- Ela monitora o próprio objeto instanciado (usando o mecanismo interno conhecido como Intrinsic Lock ou Monitor).

- Diferentemente do `ReentrantLock`, você não precisa chamar `lock()`/`tryLock()` ou `unlock()` manualmente:

 - Java gerencia a tranca automaticamente ao entrar e sair do método ou bloco.

---

# public synchronized void metdo(...) { ... }

- Cria um método sincronizado.

- Neste caso, a palavra-chave `synchronized` transforma o método inteiro em uma Região Crítica. Se a `Thread A` chamar este método, a `Thread B` será bloqueada na entrada automaticamente.

---

# synchronized (this) { ... }

- Ao invés de criar um método totalmente sincronizado, cria um bloco sincronizado (`synchronized (this)`). 

- Essa é considerada uma prática de arquitetura muito superior no mercado.

- Ao usar `synchronized (this)`, você restringe o bloqueio apenas às linhas que realmente mexem no recurso compartilhado. 
  - Isso libera as demais linhas (instruções) para rodarem fora da tranca, melhorando a concorrência geral do sistema.
  
---

# private final Object trava = new Object();
# synchronized (trava) { ... }

- Objeto privado de tranca (`private final Object trava = new Object();`).

  - Esta abordagem encapsula o mecanismo de sincronização por completo. 
  - Como o objeto trava é privado, nenhum código de fora da classe consegue interceptá-lo ou causar um travamento acidental (conhecido como `lock contention` ou `sabotagem de tranca`).
  - É o padrão recomendado para evitar que códigos externos travem sua classe por acidente.


