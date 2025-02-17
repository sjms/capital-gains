# Code Challenge: Capital Gains

Este é um programa de linha de comando (CLI) que calcula o imposto a ser pago sobre lucros ou prejuízos de operações no mercado financeiro de ações.



### Entrada:

O programa recebe listas, uma por linha, de operações do mercado financeiro de ações em formato `json` através da entrada padrão (`stdin`). 

#### Cada operação desta lista contém os seguintes campos:

| Nome      | Significado                                                      |
|-----------|------------------------------------------------------------------|
| operation | Se a operação é uma operação de compra ( buy ) ou venda ( sell ) |
| unit-cost | Preço unitário da ação em uma moeda com duas casas decimais      |
| quantity  | Quantidade de ações negociadas                                   |

#### Este é um exemplo da entrada:

```
[{"operation":"buy", "unit-cost":10.00, "quantity": 10000},
{"operation":"sell", "unit-cost":20.00, "quantity": 5000}]
[{"operation":"buy", "unit-cost":20.00, "quantity": 10000},
{"operation":"sell", "unit-cost":10.00, "quantity": 5000}]
```
As operações estarão na ordem em que elas ocorreram, ou seja, a segunda operação na lista aconteceu
depois da primeira e assim por diante.\
Cada linha é uma simulação independente, o seu programa não deve manter o estado obtido em uma linha para as outras.\
A última linha da entrada será uma linha vazia.

### Saída:

Para cada linha da entrada, o programa deve retornar uma lista contendo o imposto pago para cada operação
recebida.\
Os elementos desta lista devem estar codificados em formato `json` e a saída deve ser retornada
através da saída padrão (`stdout`). 

O retorno é composto pelo seguinte campo:

| Nome | Significado                             |
|------|-----------------------------------------|
| tax  | O valor do imposto pago em uma operação |

#### Este é um exemplo da saída:

```
[{"tax":0.00}, {"tax":10000.00}]
[{"tax":0.00}, {"tax":0.00}]
```
A lista retornada pelo programa deve ter o mesmo tamanho da lista de operações processadas na entrada.\
Por exemplo, se foram processadas três operações (buy, buy, sell), o retorno do programa deve ser uma lista com três valores que representam o imposto pago em cada operação.


## Regras do Ganho de Capital:

### O programa deve lidar com dois tipos de operações (buy e sell) e ele deve seguir as seguintes regras:

- O percentual de imposto pago é de 20% sobre o lucro obtido na operação. Ou seja, o imposto vai ser pago quando há uma operação de venda cujo preço é superior ao preço médio ponderado de compra.
- Para determinar se a operação resultou em lucro ou prejuízo, você pode calcular o preço médio ponderado, então quando você compra ações você deve recalcular o preço médio ponderado utilizando essa fórmula: `nova-média-ponderada = ((quantidade-de-ações-atual * média-ponderada-atual) + (quantidade-de-ações-compradas * valor-de-compra)) / (quantidade-de-ações-atual + quantidade-de-ações-compradas)`. Por exemplo, se você comprou 10 ações por `R$ 20,00`, vendeu 5, depois comprou outras 5 por `R$ 10,00`, a média ponderada é ((5 x 20.00) + (5 x 10.00)) / (5 + 5) = 15.00.
-  Você deve usar o prejuízo passado para deduzir múltiplos lucros futuros, até que todo o prejuízo seja deduzido.
- Prejuízos acontecem quando você vende ações a um valor menor do que o preço médio ponderado de
compra. Neste caso, nenhum imposto deve ser pago e você deve subtrair o prejuízo dos lucros seguintes, antes de calcular o imposto.
- Você não paga nenhum imposto se o valor total da operação (custo unitário da ação x quantidade) for menor ou igual a R$ 20000,00. Use o valor total da operação e não o lucro obtido para determinar se o imposto deve ou não ser pago. E não se esqueça de deduzir o prejuízo dos lucros seguintes.
- Nenhum imposto é pago em operações de compra. 
- Você pode assumir que nenhuma operação vai vender mais ações do que você tem naquele momento.


## Uma explicação sobre as decisões técnicas e arquiteturais do desafio

Tentei aplicar algumas praticas de Clean Code/Clean Architecture pensando em um codigo limpo, organizado, facil de enteder e de realizar manutencoes caso necessario.

* Defini 3 casos de uso sendo eles:
  * ProcessOrderUseCase - Servico reponsavel por processar uma ou mais operacoes. Recebe uma lista de operacoes (compra/venda) e delega o processamento de cada operacao.
  * BuysStockUseCase - Servico reponsavel pelas regras de negocio de compras de acoes. Processa uma operacao de compra e retorna a taxa aplicada.
  * SellStockUseCase - Servico reponsavel pelas regras de negocio de venda de acoes. Processa uma operacao de venda e retorna a taxa aplicada.


## Executando a aplicação (opcoes)

### Executando o jar:
- Verifique se o computador possui JRE que suporte a versao do java 17 ou superior. Instale se necessario
- Abra o terminal do sistema operacional
- Navegue até a pasta que se encontra os codigos da aplicação (`..\capital-gains\target`)
- Execute o codigo fonte java com o comando  `java -jar capital-gains-0.0.1.jar`
- Insira a string no formato `json` no terminal

### Executando no docker:
- Abra o terminal do sistema operacional
- Execute o comando `docker build -t capital-gains-app .` para build da aplicacao
- Execute o comando `docker run -it --rm -p 8080:8080 capital-gains-app` para subir a aplicacao
- Insira a string no formato `json` no terminal

## Executando localmente:
- Abra o projeto na sua IDE preferida
- Execute a classe `Main.java`
- Insira a string no formato `json` no terminal

#### Exemplo
```
[{"operation":"buy", "unit-cost":10.00, "quantity": 10000}, {"operation":"sell", "unit-cost":20.00, "quantity": 5000}]
```
