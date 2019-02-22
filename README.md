# uft-pos-siad-alg-geneticos

## Construção

O projeto usa o Maven para resolução de dependências e construção. Necessário instala-lo: 

```
# com Apt
sudo apt install maven

# com Dnf
sudo dnf install maven
```


Depois basta executar a construção:

```
mvn clean package
```

## Execução

### Usando o plugin do Maven:

```
mvn exec:java
```

Os parâmetros de entrada estão definidos no `pom.xml`.

### Executando direto o Jar:

```
java -jar jsjsjjssj.jar -D<arquivo da planta> -D<largura da planta em metros> -D<altura da planta em metros> -D<qtd. de pontos de acesso> -D<raio cobertura pto. acesso>
```

Exemplo:

```
java -jar jajsj.jar -Dblueprint-marked.png -D40 -D19 -D3 -D5
```


