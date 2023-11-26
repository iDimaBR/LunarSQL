# LunarSQL

## Descrição
Uma API feita para auxiliar no manuseio de conexões e consultas ao banco de dados.

## Tecnologias Utilizadas
- Java
- JDBC

## Instalação
1. Clone o repositório: `git clone https://github.com/iDimaBR/LunarSQL.git`
2. Importe o projeto em sua IDE Java preferida.

## Como Usar
1. Configure as credenciais do banco de dados no arquivo `SQLProvider.java`.
2. Exemplos de uso:

### Exemplo de consulta simples
```java
public class Exemplo {
    public static void main(String[] args) {
        SQLProvider sqlProvider = new SQLProvider("URL_DO_BANCO", "USUARIO", "SENHA");
        
        try {
            SQLResult<SeuObjeto> resultado = sqlProvider.query("SELECT * FROM sua_tabela WHERE id = ?", SeuObjeto.class, 1);
            
            // Processamento do resultado
            while (resultado.getResultSet().next()) {
                SeuObjeto objeto = resultado.getAdapter().onResult(resultado.getResultSet());
                System.out.println(objeto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
