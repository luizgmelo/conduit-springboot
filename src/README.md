<h1 align="center" style="font-weight: bold;">Conduit Spring Boot</h1>

<p align="center">
 <a href="#tech">Technologies</a> • 
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
 <a href="#contribute">Contribute</a>
</p>

<p align="center">
    <b>This project is a clone of Medium.com blog post</b>
</p>

<h2 id="technologies">💻 Technologies</h2>

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL 

<h2 id="started">🚀 Getting started</h2>

Here you describe how to run your project locally

<h3>Prerequisites</h3>

- [Java 21](https://www.oracle.com/java/technologies/downloads/)
- [Git](https://github.com)

<h3>Cloning</h3>


```bash
git clone https://github.com/luizgmelo/conduit-springboot.git
```

<h3>Starting</h3>

- Clone project
- Build project
- Execute project

```bash
$ cd conduit-springboot

$ ./mvnw clean package

$ java -jar target/conduit-0.0.1-SNAPSHOT.jar
```

<h2 id="routes">📍 API Endpoints</h2>
​
| route               | description                                          
|----------------------|-----------------------------------------------------
| <kbd>GET /authenticate</kbd>     | retrieves user info see [response details](#get-auth-detail)
| <kbd>POST /authenticate</kbd>     | authenticate user into the api see [request details](#post-auth-detail)

<h3 id="get-auth-detail">GET /authenticate</h3>

**RESPONSE**
```json
{
  "name": "Fernanda Kipper",
  "age": 20,
  "email": "her-email@gmail.com"
}
```

<h3 id="post-auth-detail">POST /authenticate</h3>

**REQUEST**
```json
{
  "username": "fernandakipper",
  "password": "4444444"
}
```

**RESPONSE**
```json
{
  "token": "OwoMRHsaQwyAgVoc3OXmL1JhMVUYXGGBbCTK0GBgiYitwQwjf0gVoBmkbuyy0pSi"
}
```
