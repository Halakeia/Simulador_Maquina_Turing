#  Simulador de Máquina de Turing  

Este simulador de Máquina de Turing foi desenvolvido em **Java** na plataforma **IntelliJ** e permite a execução de autômatos de Turing em diferentes modos de operação, oferecendo uma abordagem prática para compreender esse modelo computacional teórico. O programa funciona lendo um arquivo **.txt** contendo o código da máquina, carregando as instruções passo a passo e executando as computações conforme a entrada fornecida.

## Sobre a Máquina de Turing  

A **Máquina de Turing**, proposta por **Alan Turing** em 1936, é um modelo matemático fundamental na teoria da computação. Ela consiste em:  
- Uma **fita**, onde os símbolos são lidos e escritos.  
- Um **cabeçote** que se move sobre a fita, alterando seu conteúdo de acordo com regras predefinidas.  
- Um conjunto de **estados** e **transições** que determinam o comportamento da máquina.  

### Definição Formal  

Uma Máquina de Turing é formalmente definida como uma **7-tupla**:  
**M = (Q, Σ, Γ, δ, q₀, q_accept, q_reject)**  

Onde:  
- **Q**: Conjunto finito de estados.  
- **Σ**: Alfabeto de entrada (excluindo o símbolo branco).  
- **Γ**: Alfabeto da fita (contém **Σ** e o símbolo branco).  
- **δ**: Função de transição **δ: Q × Γ → Q × Γ × {L, R, *}**, que define a mudança de estado, a substituição do símbolo e a movimentação do cabeçote (esquerda, direita ou imóvel).  
- **q₀**: Estado inicial.  
- **q_accept**: Estado de aceitação.  
- **q_reject**: Estado de rejeição.  

Este modelo é essencial para compreender **computabilidade e complexidade**, sendo a base da teoria dos autômatos e da programação.  

## Funcionalidades  

O simulador suporta diferentes modos de execução:  

- **Modo silencioso (-r)**: Executa a máquina até a finalização sem exibir os passos intermediários.  
- **Modo passo a passo (-v)**: Exibe cada etapa da execução, mostrando o estado atual da fita e do cabeçote.  
- **Modo computa até N (-s)**: Executa a máquina por **N passos** e exibe o resultado.  
- **Modo (-head)**: Mostra o conteúdo do cabeçote durante a execução.  

## Como Executar  

1. Clone o repositório:  
   ```bash
   git clone https://github.com/seu-usuario/simulador-turing.git
   cd simulador-turing
   ```
2. Compile o código Java:  
   ```bash
   javac SimuladorTuring.java
   ```
3. Execute o simulador com o modo desejado:  
   ```bashEste simulador de Máquina de Turing foi desenvolvido em **Java** na plataforma **IntelliJ** e permite a execução de autômatos de Turing em diferentes modos de operação, oferecendo uma abordagem prática para compreender esse modelo computacional teórico. O programa funciona lendo um arquivo **.txt** contendo o código da máquina, carregando as instruções passo a passo e executando as computações conforme a entrada fornecida.
   simturing -r nomeMaquinaEste simulador de Máquina de Turing foi desenvolvido em **Java** na plataforma **IntelliJ** e permite a execução de autômatos de Turing em diferentes modos de operação, oferecendo uma abordagem prática para compreender esse modelo computacional teórico. O programa funciona lendo um arquivo **.txt** contendo o código da máquina, carregando as instruções passo a passo e executando as computações conforme a entrada fornecida.
   simturing -v nomeMaquina
   simturing -s nomeMaquina
   simturing -head nomeMaquina
   ```
4. Trocar o delimitador da cabeça
   ```bash
   simturing -head "<>"
   simturing -head "()"
   ```  

##  Tecnologias  

- **Java**  
- **IntelliJ IDEA**  
