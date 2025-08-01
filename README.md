# 📝 Notes App

<div align="center">

![Java](https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.14-brightgreen?style=for-the-badge&logo=spring)
![H2 Database](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge&logo=h2)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.1.3-purple?style=for-the-badge&logo=bootstrap)

**Um aplicativo web moderno para gerenciamento de notas pessoais com autenticação segura**

[🚀 Demo](#-como-executar) • [📖 Documentação](#-funcionalidades) • [🛠️ Tecnologias](#️-tecnologias-utilizadas) 

</div>

---

## ✨ Funcionalidades

### 🔐 **Autenticação & Segurança**
- ✅ Registro de usuários com validação completa
- ✅ Login/logout seguro com Spring Security
- ✅ Hash de senhas com BCrypt
- ✅ Controle de sessões e proteção CSRF
- ✅ Isolamento completo de dados por usuário

### 📋 **Gerenciamento de Notas**
- ✅ Criar, editar, visualizar e excluir notas
- ✅ Busca inteligente por título e conteúdo
- ✅ Interface responsiva e intuitiva
- ✅ Auto-save de rascunhos
- ✅ Contador de caracteres em tempo real

### 🎨 **Interface Moderna**
- ✅ Design responsivo com Bootstrap 5
- ✅ Ícones Font Awesome
- ✅ Animações suaves e feedback visual
- ✅ Tema claro e profissional
- ✅ Compatível com dispositivos móveis

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 11+** - Linguagem de programação
- **Spring Boot 2.7.14** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados embarcado
- **Maven** - Gerenciamento de dependências

### Frontend
- **Thymeleaf** - Template engine
- **Bootstrap 5.1.3** - Framework CSS
- **Font Awesome 6.0** - Ícones
- **JavaScript ES6** - Interatividade

---

## 🚀 Como Executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.6+
- Git

### Instalação

1. **Clone o repositório**
```bash
git clone https://github.com/lucasaugustodev/notes-app.git
cd notes-app
```

2. **Compile o projeto**
```bash
mvn clean compile
```

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

4. **Acesse no navegador**
```
http://localhost:8080
```

### 🐳 Docker (Opcional)

```bash
# Build da imagem
docker build -t notes-app .

# Executar container
docker run -p 8080:8080 notes-app
```


---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/mycompany/app/
│   │   ├── config/          # Configurações Spring
│   │   ├── controller/      # Controllers REST/Web
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositórios Spring Data
│   │   ├── security/        # Configurações de segurança
│   │   ├── service/         # Lógica de negócio
│   │   └── App.java         # Classe principal
│   └── resources/
│       ├── static/          # CSS, JS, imagens
│       ├── templates/       # Templates Thymeleaf
│       └── application.properties
└── test/                    # Testes unitários
```

---

## 🔧 Configuração

### Banco de Dados
O projeto usa H2 Database por padrão. Para alterar:

```properties
# application.properties
spring.datasource.url=jdbc:h2:file:./notes.db
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

### Porta do Servidor
```properties
server.port=8080
```

### Logs
```properties
logging.level.com.mycompany.app=DEBUG
```

---

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar com coverage
mvn test jacoco:report
```

---

## 📚 API Endpoints

### Autenticação
- `GET /login` - Página de login
- `POST /login` - Processar login
- `GET /register` - Página de registro
- `POST /register` - Processar registro
- `POST /logout` - Logout

### Notas
- `GET /dashboard` - Dashboard principal
- `GET /notes/new` - Formulário nova nota
- `POST /notes` - Criar nota
- `GET /notes/{id}` - Visualizar nota
- `GET /notes/{id}/edit` - Editar nota
- `POST /notes/{id}` - Atualizar nota
- `POST /notes/{id}/delete` - Excluir nota

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📋 Roadmap

- [ ] 📱 App mobile com React Native
- [ ] 🌙 Modo escuro
- [ ] 📎 Upload de arquivos/imagens
- [ ] 🏷️ Sistema de tags
- [ ] 📤 Exportar notas (PDF, Markdown)
- [ ] 🔄 Sincronização em nuvem
- [ ] 👥 Compartilhamento de notas
- [ ] 🔍 Busca avançada com filtros

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Lucas Augusto**
- GitHub: [@lucasaugustodev](https://github.com/lucasaugustodev)
- Email: augustolucasg@gmail.com

---

## 🙏 Agradecimentos

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework incrível
- [Bootstrap](https://getbootstrap.com/) - CSS framework
- [Font Awesome](https://fontawesome.com/) - Ícones lindos
- [H2 Database](https://www.h2database.com/) - Banco de dados rápido

---

<div align="center">

**⭐ Se este projeto te ajudou, considere dar uma estrela!**

Made with ❤️ and ☕ by [Lucas Augusto](https://github.com/lucasaugustodev)

</div>
