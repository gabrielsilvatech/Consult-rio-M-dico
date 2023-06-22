# consultorio_medico
Projeto de uma API Rest com 3 classes domain, médico, paciente e consulta. Onde implemento o CRUD de requisições simulando o cadastro e exclusão lógica de médico e paciente e a marcação de consulta.
Para atualização dos médicos e pacientes e as marcações de consultas utilizamos as regras de negócio, de acordo com as classes service.
Projeto com Spring Security onde o processo de "login" é stateless, através da geração e validação de um Token, sendo necessário passar esse Token nas requisições.
integração com banco de dados MySql, seguindo o padrão com classe domain, interface respository e Record para os DTO.
Teste Automatizados dos Controller, repositoty e service.
