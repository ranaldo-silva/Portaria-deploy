# 🗓️ Cronograma de Desenvolvimento - Sprint 2
## Sistema de Gestão de Portaria

**Período:** 11/10/2025 – 09/11/2025

---

## 1. Planejamento e Alinhamento Inicial (Data: 11/10/2025)
**Responsável:** Equipe
**Atividades:**
- Reunião de revisão da Sprint 1 e definição de novas entregas.
- Identificação de melhorias e ajustes necessários para tornar o sistema funcional.
- Planejamento técnico das integrações com Oracle, Swagger e Mobile.
- Distribuição equilibrada de tarefas entre os membros.

**Status:** CONCLUÍDO

---

## 2. Refatoração do Backend e Implementação de DTOs (Período: 11/10/2025 – 09/11/2025)
**Responsável:** Lucas da Ressurreição Barbosa
**Atividades:**
- Refatoração completa das classes e controllers para adoção de DTOs (Request e Response).
- Implementação de novo endpoint solicitado pelo professor.
- Revisão de tratamento de erros e ajustes de validação.
- Alinhamento da camada de serviço com o novo modelo de dados.

**Status:** CONCLUÍDO

---

## 3. Integração com Oracle Database (Período: 11/10/2025 – 09/11/2025)
**Responsável:** Lucas da Ressurreição Barbosa
**Atividades:**
- Substituição definitiva do H2 pelo Oracle Database.
- Configuração de conexão e variáveis de ambiente no projeto.
- Criação e execução de procedures e triggers para automação de IDs.
- Testes de persistência e consistência de dados via JPA/Hibernate.

**Status:** CONCLUÍDO

---

## 4. Documentação e Swagger (Período: 11/10/2025 – 09/11/2025)
**Responsável:** Lucas da Ressurreição Barbosa
**Atividades:**
- Integração do Swagger/OpenAPI ao projeto.
- Documentação detalhada dos endpoints com exemplos de uso.
- Criação da pasta `docs/` e adição de capturas de tela.
- Testes de acessibilidade da documentação via navegador.

**Status:** CONCLUÍDO

---

## 5. Procedures, Relatórios e Estrutura do Banco (Período: 11/10/2025 – 09/11/2025)
**Responsável:** Fabrício José da Silva
**Atividades:**
- Criação de functions, procedures e relatórios no Oracle Database.
- Implementação de CRUDs baseados em procedures.
- Ajustes estruturais solicitados pelo professor.
- Adição de nova tabela para suportar melhorias do sistema.

**Status:** CONCLUÍDO

---

## 6. Desenvolvimento da Interface Web (Período: 11/10/2025 – 09/11/2025)
**Responsável:** Fabrício José da Silva
**Atividades:**
- Desenvolvimento da interface web em .NET.
- Integração direta com o backend Java.
- Testes de comunicação e refinamento da interface.
- Ajuste de componentes visuais conforme feedback interno.

**Status:** CONCLUÍDO

---

## 7. Aplicativo Mobile e QA (Período: 11/10/2025 – 09/11/2025)
**Responsável:** Ranaldo José da Silva
**Atividades:**
- Criação do app mobile em React com base no protótipo da Sprint 1.
- Implementação de fluxos de navegação e chamadas à API.
- Execução do escopo de testes QA para garantir estabilidade.
- Criação das imagens Docker e padronização do ambiente DevOps.

**Status:** CONCLUÍDO

---

## 8. Testes Finais e Validação (Data: 08/11/2025 – 09/11/2025)
**Responsável:** Equipe
**Atividades:**
- Testes completos entre backend, web e mobile.
- Validação da integração com o banco Oracle.
- Revisão de logs, endpoints e ambiente Docker.
- Correção final de bugs e limpeza de código.

**Status:** CONCLUÍDO

---

## 9. Documentação Final no Repositório (Data: 09/11/2025)
**Responsável:** Lucas da Ressurreição Barbosa
**Atividades:**
- Atualização do README.md e inclusão dos diagramas.
- Adição do `CRONOGRAMA_SPRINT_2.md` e demais evidências.
- Inclusão dos prints e coleções de testes Postman.
- Validação do link do vídeo e conferência de acesso ao repositório.

**Status:** CONCLUÍDO

---

## 👥 Responsabilidades da Equipe

### Lucas da Ressurreição Barbosa - RM560179
**Funções:** Java Advanced, IoT, Documentação Técnica
**Atividades:** Refatoração de backend, integração com Oracle, implementação de DTOs e Swagger.

### Fabrício José da Silva - RM560694
**Funções:** Banco de Dados e .NET
**Atividades:** Criação de procedures, relatórios e interface web.

### Ranaldo José da Silva - RM559210
**Funções:** DevOps, QA e Front Mobile
**Atividades:** Desenvolvimento do app React, testes QA e criação de imagens Docker.

---

## 🔁 Evolução da Sprint 1 → Sprint 2

### Sprint 1 - Estrutura Conceitual
- Definição de escopo, entidades e diagramas.
- Protótipo inicial e endpoints mockados.
- Banco H2 usado apenas para simulação.

### Sprint 2 - Versão Funcional
- Sistema operacional com Oracle Database.
- Backend refatorado e documentado.
- Procedures e relatórios implementados.
- Aplicativo mobile ativo e integrado.
- DevOps configurado com Docker.

---

## ⏱️ Distribuição de Tempo

| Fase | Tempo Estimado | Tempo Real |
|------|---|----|
| Planejamento e Alinhamento | 2h | 2h |
| Refatoração Backend / DTOs | 5h | 10h |
| Integração com Oracle | 4h | 2h |
| Documentação / Swagger | 3h | 2.5h |
| Procedures e Relatórios | 4h | 4h |
| Interface Web | 5h | 12h|
| Mobile e QA | 5h | 4h |
| Testes Finais e Validação | 3h| 2h |
| **TOTAL** | **30h** | **25.5h** |

---

## ✅ Critérios de Aceite

- Endpoints operacionais e integrados entre camadas.
- Oracle Database funcional e sincronizado.
- DTOs implementados em todas as operações.
- Documentação via Swagger acessível e atualizada.
- Procedures, relatórios e Docker devidamente configurados.
- Aplicativo React integrado e testado.
- Repositório atualizado e acessível.

---

## 📌 Notas Importantes

- Todas as alterações commitadas no GitHub.
- Testes de integração e QA realizados com sucesso.
- Repositório completo com diagramas, prints e coleções Postman.
- Documentação ajustada conforme feedback dos professores.
- Evidência da evolução clara entre as sprints.
