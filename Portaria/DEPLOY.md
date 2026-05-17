# 🚀 Deploy — Portaria API

## Variáveis de ambiente obrigatórias

Configure estas variáveis no painel do Railway ou Render **antes** de fazer o deploy:

| Variável | Descrição | Exemplo |
|---|---|---|
| `DB_URL` | JDBC URL do Oracle | `jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL` |
| `DB_USER` | Usuário do banco | `rm560694` |
| `DB_PASS` | Senha do banco | `SuaSenha` |
| `RABBITMQ_HOST` | Host do broker | `guppy-01.rmq6.cloudamqp.com` |
| `RABBITMQ_USERNAME` | Usuário RabbitMQ | `xvjgbzqt` |
| `RABBITMQ_PASSWORD` | Senha RabbitMQ | `sua_senha` |
| `RABBITMQ_VHOST` | Virtual host | `xvjgbzqt` |
| `URL_APPLICATION` | URL pública desta API | `https://portaria.railway.app` |
| `URL_ML` | URL do serviço de ML | `https://portaria-ml.railway.app` |
| `FIREBASE_CREDENTIALS` | JSON do service account Firebase (numa linha só) | `{"type":"service_account",...}` |

---

## Railway

1. Crie um novo projeto em [railway.app](https://railway.app)
2. Clique em **"Deploy from GitHub repo"** (ou faça upload da pasta)
3. Railway detecta o `Dockerfile` automaticamente
4. Vá em **Variables** e adicione todas as variáveis acima
5. A variável `PORT` é injetada automaticamente pelo Railway — não precisa configurar
6. Deploy!

**URL do Swagger após deploy:**
`https://seu-projeto.railway.app/swagger-ui/index.html`

---

## Render

1. Crie um novo **Web Service** em [render.com](https://render.com)
2. Conecte o repositório GitHub
3. Em **Environment**, selecione **Docker**
4. Adicione as variáveis de ambiente na aba **Environment Variables**
5. Render também injeta `PORT` automaticamente
6. Deploy!

---

## CORS — Expo Web

O CORS está configurado para aceitar qualquer origem (`allowedOriginPattern("*")`),
então o Expo Web vai funcionar normalmente. Se quiser restringir a origens específicas
em produção, edite `CorsConfig.java` e substitua `"*"` pela URL do seu frontend.

---

## Correções aplicadas nesta versão

- ✅ **CORS corrigido**: `SecurityConfig` agora chama `.cors(Customizer.withDefaults())`
  e libera requisições `OPTIONS` (preflight) — resolve o erro no Expo Web
- ✅ **CorsConfig refatorado**: usa `CorsConfigurationSource` (padrão Spring Security 6)
  ao invés de `CorsFilter`, e `addAllowedOriginPattern` ao invés de `addAllowedOrigin`
- ✅ **MLClient**: URL lida de `${URL_ML}` com fallback para localhost
- ✅ **EncomendaService**: chamada ao ML dentro de try/catch — falha no ML não derruba o registro de encomenda
- ✅ **SwaggerConfig**: servidor relativo (`/`) funciona em qualquer URL de deploy
- ✅ **application.properties**: `server.port=${PORT:8080}` — compatível com Railway e Render
- ✅ **Dockerfile**: build em dois estágios com usuário não-root, cache de dependências otimizado
- ✅ **Credenciais removidas**: `.env` e `firebase-serviceaccount.json` não estão no projeto
