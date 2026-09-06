# 10-java-thread

# synchronized

- A palavra-chave `synchronized` cria exclusão mútua em Java. 

- Ela monitora o próprio objeto instanciado (usando o mecanismo interno conhecido como Intrinsic Lock ou Monitor).

- Diferentemente do `ReentrantLock`, você não precisa chamar `lock()`/`tryLock()` ou `unlock()` manualmente:

 - Java gerencia a tranca automaticamente ao entrar e sair do método ou bloco.
