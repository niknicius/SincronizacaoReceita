
# SincronizaçãoReceita

Sistema simulador de envio de contas para a Receita Federal.

O sistema recebe um arquivo contendo contas, e realiza o "envio" para o sistema da Receita Federal, produzindo ao fim um novo arquivo contendo o resultado da operação.

Para fins de performance, o processamento das contas é realizado em chunks de 1000 itens.




## Compilando localmente

Para realizar o build da aplicação execute o comando Maven a seguir:

```bash
  mvn package
```
## Uso/Exemplos
Para processar um arquivo de contas utilize o comando abaixo, substituindo `[filename]` pelo arquivo a ser processado.

```bash
  java -jar SincronizacaoReceita-1.0.0.jar [filename]
```

O sistema espera receber como argumento um arquivo csv contendo as contas a serem enviadas.
O arquivo deve obrigatóriamente estar formatado tal como o exemplo abaixo


```csv
agencia;conta;saldo;status
0101;12225-6;100,00;A
0101;12226-8;3200,50;A
3202;40011-1;-35,12;I
3202;54001-2;0,00;P
3202;00321-2;34500,00;B
```

Uma vez que o processamento termina, o sistema cria um novo arquivo CSV, localizado em `output/outputData.csv` com os mesmos dados do arquivo lido, porém com uma nova coluna armazenando o resultado do processo de envio da conta.


## Licença

[MIT](https://choosealicense.com/licenses/mit/)

