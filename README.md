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

- Auxilia na proteção contra má prática externa: 
  - Se outra pessoa estivesse usando sua classe e resolvesse fazer algo como `synchronized(suaClasseSincronizada) { Thread.sleep(999999); }` no código dela, ela travaria o seu método sincronizado inteiro se você usasse synchronized(this). 
  - Com o objeto `trava` privado, ninguém sabe que esse objeto existe.
  
- Também auxilia em casos de múltiplas trancas na mesma classe: 
  - Se a sua classe tivesse duas variáveis independentes (ex: `contadorVendas` e `contadorEstoque`), você poderia criar duas trancas privadas distintas (`travaVendas` e `travaEstoque`). 
  - Isso permitiria que uma `thread` mexesse nas vendas e outra no estoque ao mesmo tempo, algo impossível usando o `this`, que bloquearia a classe inteira.
  
