# 🚍 Olho Vivo SPTrans - Android App

Este é um aplicativo Android simples que consome a API pública da SPTrans (Olho Vivo) para:

- Autenticar automaticamente usando um token válido.
- Permitir ao usuário buscar linhas de ônibus digitando o número ou parte do letreiro.

## 📱 Funcionalidades

- Autenticação automática via API.
- Busca de linhas de ônibus por número.
- Exibição das informações no app.
- Interface simples e responsiva.

## ✅ Requisitos

- Android Studio Arctic Fox ou superior.
- SDK mínimo: 21 (Lollipop)
- Conexão com a internet.
- Token da API Olho Vivo (você pode obter no site da SPTrans).

## 🛠️ Tecnologias Utilizadas

- Kotlin
- Retrofit2
- Coroutines
- OkHttp (com CookieJar para manter sessão)
- Material Components

## ▶️ Como Rodar

1. Clone este repositório:
   ```bash
   git clone https://github.com/seu-usuario/olho-vivo-app.git

2. Abra o projeto no Android Studio. 

3. Adicione seu token da SPTrans na MainActivity.kt
```bash
    private val token = "SEU_TOKEN_AQUI"
```

4. Compile e execute em um emulador com acesso à internet. 

📷 Captura de Tela

Abaixo uma imagem da tela do app:

![Captura de tela](https://github.com/user-attachments/assets/98a78f31-053f-4755-93ed-ca1223f96914)
