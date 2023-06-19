# GPC
## Gestão de  pessoas e contatos

Api construída para fins de processo seletivo em entrevista de emprego


## Features

- Possuir ao menos os endpoints: GET(Buscar uma única Pessoa), GET (Busca paginada opção de filtro para retornar várias pessoas), POST, PUT, DELETE
- O cadastro de pessoa deve ter os campos: Id, Nome, CPF, Data de nascimento.
- A pessoa deve possuir uma lista de contatos (relacionamento um para muitos) com os campos: Id, Nome, Telefone e Email.
- Os dados devem ser persistidos utilizando um banco de dados relacional.

## Tecnologias utilizadas

Dillinger uses a number of open source projects to work properly:

- [Java] - versão 17
- [Spring boot] - versão 3.1.0
- [Swagger] - Open Api 3
- [Postgresql] - versão 12
- [Container] - Docker

## Deploy

O deploy e esteira esta sendo feito pela ferramenta Caprover que tenho hospedado em servidor próprio integrado ao github, onde toda entrega que faço na branch main é iniciado o processo de construção e deploy no servidor.

## Documentação

A documentação da Api se encontra disponível no link aseguir:
https://gpc-app.andersondias.net.br/api/swagger-ui/index.html
## License

MIT

**Free Software, Hell Yeah!**