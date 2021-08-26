<h1 align="center">SDK Gerencianet para ANDROID</h1>

![SDK Gerencianet para PHP](https://media-exp1.licdn.com/dms/image/C4D1BAQH9taNIaZyh_Q/company-background_10000/0/1603126623964?e=2159024400&v=beta&t=coQC_AK70vTYL3NdvbeIaeYts8nKumNHjvvIGCmq5XA)


<p align="center">
  <span><b>Português</b></span>
</p>

> SDK ANDROID para integração com a API da Gerencianet.
Para mais informações sobre parâmetros e valores, consulte a documentação da [Gerencianet](http://gerencianet.com.br).

Ir para:
* [Requisitos](#requisitos)
* [Instalação](#instalação)
* [Começando](#começando)
* [Exemplos](#exemplos)
  * [Como obter as credenciais Client_Id e Client_Secret](#como-obter-as-credenciais-client-id-e-client-secret)
  * [Como gerar um certificado Pix](#como-gerar-um-certificado-pix)
  * [Como cadastrar as chaves Pix](#como-cadastrar-as-chaves-pix)
* [Licença](#licença)

## Requisitos

- Android 7.0+
## Instalação

**Via gradle:**

```gradle
compile 'br.com.gerencianet.mobile:gn-api-sdk-android:1.0'
```


## Começando

Declare um objeto Map e adicione as informações referentes a sua conta e aplicação Gerencianet.


```java
HashMap<String, Object> options = new HashMap<String Object>();
options.put("client_id", "__CLIENT_ID_");
options.put("client_secret", "__CLIENT_SECRET_");
options.put("sandbox", __SANDBOX__);
```
Para utilizar a API emissões, instancie um objeto `Gerencianet` e passe como parametro a variável com as opções declaradas.

```java
Gerencianet gerencianet = new Gerencianet(options);
```

Para utilizar a API PIX, adicione ao Map das opções, o caminho do certificado PIX dentro do diretorio assets e instancie um objeto `Gerencianet`, passando como parametro a variável com as opções, juntamente com o context.getAssets().

```java
options.put("pix_cert", "__PATH___");
Gerencianet gerencianet = new Gerencianet(options, context.getAssets());
```

## Exemplos

Criar cobrança (Homologação):
```java
HashMap<String, Object> options = new HashMap<String Object>();
options.put("client_id", "__CLIENT_ID_");
options.put("client_secret", "__CLIENT_SECRET_");
options.put("sandbox", true);

List<Object> items = new ArrayList<Object>();

Map<String, Object> item1 = new HashMap<String, Object>();
item1.put("name", "Item 1");
item1.put("amount", 1);
item1.put("value", 1000);
items.add(item1);

Map<String, Object> body = new HashMap<String, Object>();
body.put("items", items);

try {
    Gerencianet gn = new Gerencianet(options);
    Map<String, Object> response = gn.call("createCharge", new HashMap<String,String>(), body);
    System.out.println(response);
}catch (GerencianetException e){
    System.out.println(e.getCode());
    System.out.println(e.getError());
    System.out.println(e.getErrorDescription());
}
catch (Exception e) {
    System.out.println(e.getMessage());
}
```

Criar cobrança PIX (Homologação):
```java
HashMap<String, Object> options = new HashMap<String Object>();
options.put("client_id", "__CLIENT_ID_");
options.put("client_secret", "__CLIENT_SECRET_");
options.put("pix_cert", "__PATH_ASSETS__");
options.put("sandbox", true);

Map<String, Integer> calendar = new HashMap<>();
      calendar.put("expiracao", 3600);

Map<String, String> value = new HashMap<>();
value.put("original", "0.01");

Map<String, Object> body = new HashMap<String, Object>();
body.put("calendario", calendar);
body.put("valor", value);
body.put("chave", "sua_chave");


try {
    Gerencianet gn = new Gerencianet(options, context.getAssets());
    Map<String, Object> response = gn.call("pixCreateImmediateCharge", new HashMap<String,String>(), body);
    System.out.println(response);
}catch (GerencianetException e){
    System.out.println(e.getCode());
    System.out.println(e.getError());
    System.out.println(e.getErrorDescription());
}
catch (Exception e) {
    System.out.println(e.getMessage());
}
```

Gerar PaymentToken (Homologação):
```java
HashMap<String, Object> options = new HashMap<String Object>();
options.put("account_id", "__ACCOUNT_ID__");
options.put("sandbox", true);

Map<String, Object> card = new HashMap<>();
card.put("brand", "mastercard");
card.put("number", "1111111111111111");
card.put("cvv", "123");
card.put("expiration_month", "12");
card.put("expiration_year", "1234");

try {
    Gerencianet gn = new Gerencianet(options);
    Map<String, Object> response = gn.call("paymentToken", new HashMap<String,String>(), card);
    System.out.println(response);
}catch (GerencianetException e){
    System.out.println(e.getCode());
    System.out.println(e.getError());
    System.out.println(e.getErrorDescription());
}
catch (Exception e) {
    e.printStackTrace();
    System.out.println(e.getMessage());
}
```


## **Como obter as credenciais Client-Id e Client-Secret**

### **Crie uma nova aplicação para usar a API Gerencianet:**
1. Acesse o painel da Gerencianet no menu **API**.
2. No canto esquerdo, clique em **Minhas Aplicações** depois em **Nova Aplicação**.
3. Insira um nome para a aplicação, ative a **API de emissões (Boletos e Carnês)** e **API Pix**, e escolha os escopos que deseja liberar em **Produção** e/ou **Homologação** conforme sua necessidade (lembrando que estes podem ser alterados posteriormente).
4. Clique em Criar **Nova aplicação**.

![Crie uma nova aplicação para usar a API Gerencianet](https://t-images.imgix.net/https%3A%2F%2Fapp-us-east-1.t-cdn.net%2F5fa37ea6b47fe9313cb4c9ca%2Fposts%2F603543ff4253cf5983339cf1%2F603543ff4253cf5983339cf1_88071.png?width=1240&w=1240&auto=format%2Ccompress&ixlib=js-2.3.1&s=2f24c7ea5674dbbea13773b3a0b1e95c)


### **Alterar uma aplicação existente para usar a API Pix:**
1. Acesse o painel da Gerencianet no menu **API**.
2. No canto esquerdo, clique em **Minhas Aplicações**, escolha a sua aplicação e clique no botão **Editar** (Botão laranja).
3. Ative **API Pix** e escolha os escopos que deseja liberar em **Produção** e/ou **Homologação** conforme sua necessidade (lembrando que estes podem ser alterados posteriormente)
4. Clique em **Atualizar aplicação**.

![Alterar uma aplicação existente para usar a API Pix](https://app-us-east-1.t-cdn.net/5fa37ea6b47fe9313cb4c9ca/posts/603544082060b2e9b88bc717/603544082060b2e9b88bc717_22430.png)


## **Como gerar um certificado Pix**

Todas as requisições do Pix devem conter um certificado de segurança que será fornecido pela Gerencianet dentro da sua conta, no formato PFX(.p12). Essa exigência está descrita na íntegra no [manual de segurança do PIX](https://www.bcb.gov.br/estabilidadefinanceira/comunicacaodados).

**Para gerar seu certificado:** 
1. Acesse o painel da Gerencianet no menu **API**.
2. No canto esquerdo, clique em **Meus Certificados** e escolha o ambiente em que deseja o certificado: **Produção** ou **Homologação**.
3. Clique em **Novo Certificado**.

![Para gerar seu certificado](https://app-us-east-1.t-cdn.net/5fa37ea6b47fe9313cb4c9ca/posts/603543f7d1778b2d725dea1e/603543f7d1778b2d725dea1e_85669.png)


## **Como cadastrar as chaves Pix**
O cadastro das chaves Pix pode ser feito através do aplicativo da Gerencianet ou por um endpoint da API. A seguir você encontra os passos de como registrá-las.

### **Cadastrar chave Pix pelo aplicativo mobile:**

Caso ainda não tenha nosso aplicativo instalado, clique em [Android](https://play.google.com/store/apps/details?id=br.com.gerencianet.app) ou [iOS](https://apps.apple.com/br/app/gerencianet/id1443363326), de acordo com o sistema operacional do seu smartphone, para fazer o download.

Para registrar suas chaves Pix por meio do aplicativo:
1. Acesse sua conta através do **app Gerencianet**.
2. No menu lateral, toque em **Pix** para iniciar seu registro.
3. Leia as informações que aparecem na tela e clique em **Registrar Chave**.
    Se este não for mais seu primeiro registro, toque em **Minhas Chaves** e depois no ícone (➕).
4. **Selecione os dados** que você vai cadastrar como Chave do Pix e toque em **avançar** – você deve escolher pelo menos 1 das 4 opções de chaves disponíveis (celular, e-mail, CPF e/ou chave aleatória).
5. Após cadastrar as chaves do Pix desejadas, clique em **concluir**.
6. **Pronto! Suas chaves já estão cadastradas com a gente.**

### **Cadastrar chave Pix através da API:**
O endpoint utilizado para criar uma chave Pix aleatória (evp), é o `POST /v2/gn/evp` ([Criar chave evp](https://dev.gerencianet.com.br/docs/api-pix-endpoints#section-criar-chave-evp)). Um detalhe é que, através deste endpoint é realizado o registro somente de chaves Pix do tipo aleatória.

Para consumí-lo, basta executar o exemplo  `/examples/pix/key/create.php` da nossa SDK. A requisição enviada para esse endpoint não precisa de um body. 

A resposta de exemplo abaixo representa Sucesso (201), apresentando a chave Pix registrada.
```json
{
  "chave": "345e4568-e89b-12d3-a456-006655440001"
}
```

## Licença

[MIT](LICENSE)
