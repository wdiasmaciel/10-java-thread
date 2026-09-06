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

## 1) Sistema de Vendas de Ingressos (Synchronized em Método)

Uma plataforma de eventos está vendendo ingressos para um *show* internacional de grande procura. Existe um número limitado de ingressos em estoque. Se o método de compra não for totalmente protegido, duas pessoas podem clicar no botão de compra ao mesmo tempo e o sistema venderá mais ingressos do que a capacidade do estádio suporta.

- Crie uma classe `Bilheteria` com um atributo privado `int ingressosDisponiveis = 10`.
- Implemente o método `public synchronized void comprarIngresso(String nomeCliente)`.Dentro do método, o código deve verificar se ainda há ingressos disponíveis (`> 0`). 
  - Se sim, decrementa o estoque em 1 e exibe: "[Sucesso] " + nomeCliente + " comprou um ingresso. Restam: " + `ingressosDisponiveis`. 
  - Se não, exibe uma mensagem de esgotado.
- Na `Main`, dispare 12 threads de clientes simultaneamente tentando comprar ingressos no mesmo objeto `Bilheteria`.

Objetivo: compreender a simplicidade do `synchronized` no nível do método para blindar uma rotina inteira.

---

## 2) Atualização de Perfil de Usuário (Synchronized no bloco this)

Em uma rede social, um usuário decide atualizar suas informações de perfil (como biografia e *status*) a partir de dois dispositivos diferentes ao mesmo tempo. Para evitar misturar as escritas, o método usa um bloco sincronizado. No entanto, o método também faz uma operação demorada de validação de texto que não precisa de tranca.

- Crie uma classe `PerfilUsuario` com os atributos `String biografia` e `String status`.
- Crie o método `public void atualizarPerfil(String novaBio, String novoStatus, String dispositivo)`.
- Fora de qualquer bloco seguro, simule a validação do texto exibindo uma mensagem e colocando um `Thread.sleep(200)` (Validação local pesada). Logo em seguida, abra um bloco `synchronized(this) { ... }`. Dentro dele, atualize as duas variáveis com os novos valores e exiba o resultado na tela usando `System.out.flush()`.
- No Main, crie 2 *threads* (Dispositivo Móvel e Computador) tentando atualizar o mesmo perfil com textos diferentes.

Objetivo: perceber o ganho de desempenho ao usar o `synchronized(this)` para proteger apenas o trecho crítico, deixando o processamento pesado de validação rodar em paralelo.

---

## 3) Caixa de Supermercado (Synchronized com Objeto Privado Final)

Um caixa de supermercado registra o fluxo de dinheiro que entra no caixa (vendas) e o fluxo que sai (sangria/troco). Para garantir que o sistema seja robusto e seguro contra invasões ou modificações externas acidentais, a tranca do saldo do caixa não deve expor a instância do objeto (`this`), utilizando um objeto privado dedicado.

- Crie uma classe `CaixaRegistradora` com o atributo `double saldoCaixa = 100.0`.
- Declare a tranca interna recomendada: `private final Object travaSaldo = new Object();`.
- Crie os métodos `public void registrarVenda(double valor, String operador)` e `public void realizarSangria(double valor, String operador)`.
  - Em ambos os métodos, envolva a alteração da variável `saldoCaixa` usando a sintaxe `synchronized(travaSaldo) { ... }`. 
  - Exiba o saldo atualizado a cada movimentação.
- Na `Main`, crie 3 *threads* simulando operadores registrando vendas e retiradas ao mesmo tempo no mesmo caixa.

Objetivo: dominar o padrão de encapsulamento seguro da indústria com trancas dedicadas do tipo `Object final`.

## 4) Controlador de Tráfego de Downloads (Múltiplas Trancas Privadas com Object final)

Um navegador de internet possui um gerenciador que controla duas listas independentes em segundo plano: a lista de arquivos baixados com sucesso (`int downloadsConcluidos`) e a lista de downloads que falharam (`int downloadsFalhados`). Se o programador usar um único cadeado para a classe inteira, o registro de uma falha travará temporariamente o painel de conclusões de outro download saudável.

- Crie uma classe `GerenciadorDownloads` com dois contadores independentes.
- Instancie duas trancas privadas distintas e finais: `private final Object travaSucesso = new Object();` e `private final Object travaFalha = new Object();`.
- Crie o método `public void incrementarSucesso(String nomeArquivo)` que sincroniza estritamente na `travaSucesso`.
- Crie o método `public void incrementarFalha(String nomeArquivo)` que sincroniza estritamente na `travaFalha`.
- Na `Main`, crie a `Thread A` disparando múltiplos sucessos e a `Thread B` disparando múltiplas falhas.

Objetivo: aprender a trabalhar com concorrência fina (*fine-grained locking*), provando que *threads* acessando recursos diferentes no mesmo objeto não precisam esperar uma pela outra na fila se usarem trancas separadas.

---

## 5) Painel de Controle de Aeroporto (Múltiplas Trancas Privadas)

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
  
Objetivo: aprender a trabalhar com concorrência fina (*fine-grained locking*), provando que *threads* acessando recursos diferentes no mesmo objeto não precisam esperar uma pela outra na fila se usarem trancas separadas.
