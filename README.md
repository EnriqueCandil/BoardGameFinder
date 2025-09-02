BoardGameFinder es una aplicación web para descubrir, gestionar y explorar juegos de mesa.
El proyecto está pensado para servir como una API REST y un frontend ligero, con posibilidad de ampliación a futuro.

Tecnologías utilizadas:

- Java 17 + Spring Boot → Backend principal, con API REST.

- Spring Security + JWT → Autenticación y autorización seguras.

- JPA / Hibernate → Persistencia de datos en la base de datos.

- MySql → Base de datos relacional.

- JavaScript Vanilla (Fetch API) → Frontend ligero que consume la API.

- Bootstrap + CSS personalizado → Estilos y diseño responsivo.

Tecnologías a implementar:

- Docker → Contenerización de la aplicación y la base de datos.

- GitHub Actions → Automatización de tests, build y despliegues (CI/CD).

- Angular (posible futuro) → Reemplazo del frontend actual por un SPA más robusto.

Funcionalidades actuales:

- Registro e inicio de sesión de usuarios (con JWT).

- Búsqueda de juegos de mesa con filtros (nombre, jugadores, tiempo, editorial, edad, categorías).

- Gestión de juegos favoritos de cada usuario.

- Panel de administración para crear, editar y eliminar juegos.


Cómo ejecutar el proyecto:

- Clonar el rep:
git clone https://github.com/EnriqueCandil/BoardGameFinder.git

- Entrar al dir:
cd BoardGameFinder

- Configura la base de datos en MySql

- Lanzar la aplicación:
./mvnw spring-boot:run

- Probar conexión en http://localhost:8080
