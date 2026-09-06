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
  - Se outra pessoa estivesse usando sua classe e resolvesse fazer algo como `synchronized(suaClasseSincronizada) { Thread.sleep(999999); }` no código dela, ela travaria o seu método sincronizado inteiro se você usasse `synchronized(this)`. 
  - Com o objeto `trava` privado, ninguém sabe que esse objeto existe.
  
- Também auxilia em casos de múltiplas trancas na mesma classe: 
  - Se a sua classe tivesse duas variáveis independentes (ex: `contadorVendas` e `contadorEstoque`), você poderia criar duas trancas privadas distintas (`travaVendas` e `travaEstoque`). 
  - Isso permitiria que uma `thread` mexesse nas vendas e outra no estoque ao mesmo tempo, algo impossível usando o `this`, que bloquearia a classe inteira.

# Exercícios

## Painel de Controle de Aeroporto (Múltiplas Trancas Privadas)

Você está desenvolvendo o sistema de monitoramento de um terminal de aeroporto. O sistema precisa gerenciar duas informações em tempo real: o número de passageiros que fizeram *check-in* (`int passageiros`) e a quantidade de bagagens despachadas (`int bagagens`).

Como o fluxo de passageiros e o fluxo de bagagens são processos totalmente independentes, usar `synchronized(this)` travaria o sistema inteiro sempre que uma única bagagem fosse registrada, impedindo que um passageiro fizesse *check-in* no mesmo instante.

Para resolver esse problema de desempenho, você deve implementar o padrão de `múltiplas trancas privadas dedicadas`.

### Requisitos de Implementação:
- A classe `PainelAeroporto`: crie dois atributos inteiros privados: `passageiros` e `bagagens`, ambos iniciando em 0.

- Crie dois objetos privados e finais para servirem como trancas dedicadas: 
  - `private final Object travaPassageiros = new Object();`
  - `private final Object travaBagagens = new Object();`
  
- Implemente o método `public void registrarCheckIn(String atendente)`. Ele deve sincronizar apenas na `travaPassageiros`, incrementar o contador de passageiros e exibir o estado na tela usando `System.out.flush()`.

- Implemente o método `public void registrarBagagem(String esteira)`. Ele deve sincronizar apenas na `travaBagagens`, incrementar o contador de bagagens e exibir o estado na tela usando `System.out.flush()`.

- A classe Principal (`Main`): instancie um único objeto `PainelAeroporto`. 
  - Crie a `Thread A` (Atendimento), que simula um loop inserindo 3 passageiros chamando `registrarCheckIn`.
  - Crie a `Thread B` (Logística), que simula um *loop* inserindo 3 bagagens chamando `registrarBagagem`.
  - Dispare as duas *threads* simultaneamente.
  
  - Console: oo rodar o programa, as mensagens de registro de passageiros e de bagagens devem se misturar de forma limpa. Você deve perceber que, enquanto uma *thread* está executando o bloco seguro de passageiros, a outra *thread* não fica bloqueada para registrar a bagagem, pois os cadeados digitais são completamente diferentes.
  