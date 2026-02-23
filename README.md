<-------- AppUnTN Backend — Plataforma de resúmenes colaborativos -------->

AppUnTN es una plataforma web diseñada para estudiantes y docentes de la UTN.
Permite subir, descargar, puntuar y comentar resúmenes, junto con un sistema de roles (Alumno, Profesor, Administrador) y herramientas de gestión para docentes y admins.

Este repositorio contiene la API REST (Backend) desarrollada en Java con Spring Boot.

<-- Tecnologías utilizadas -->

Backend

- Java 17+
- Spring Boot
- Spring Security (Autenticación y Autorización basada en roles)
- JWT (JSON Web Tokens)
- Spring Data JPA / Hibernate
- MySQL (Base de datos relacional)
- Lombok (Reducción de código boilerplate)
- Jackson (Serialización y manejo de DTOs)

Frontend (proyecto separado)

- Angular 20
- Signals API
- HTML / CSS
- Consumo de API REST vía HttpClient

<-- Funcionalidades principales -->

Seguridad y Autenticación:

- Generación y validación de tokens JWT.
- Protección de rutas a nivel de método mediante `@PreAuthorize`.
- Control estricto de accesos según el rol del usuario (ADMIN, USER).

Gestión de Usuarios:

- Endpoints para registro e inicio de sesión.
- Lógica de actualización de perfiles (contraseñas, datos personales).
- Eliminación segura de usuarios verificando permisos del solicitante.

Gestión de Documentos (Resúmenes):

- Almacenamiento de archivos físicos directamente en la base de datos (`LONGBLOB`).
- Generación de respuestas blindadas (DTOs) para evitar sobrecarga de datos.
- Búsqueda y filtrado relacional.

Estructura Académica (Filtros en Cascada):

- Relaciones complejas controladas (Universidad -> Carrera -> Materia).
- Prevención de recursividad (bucle infinito de JSON) mediante anotaciones como `@JsonIgnore` y `@JsonIgnoreProperties`.

<-- Estructura del proyecto -->

/src/main/java/utn/TpFinal/AppUnTN
├── /controller
├── /service
├── /model
├── /repository
├── /DTO
├── /Security
└── /Exceptions
/src/main/resources
└── application.properties

-- Descripción de Carpetas

- */controller*: Controladores REST que exponen los endpoints de la API (UserController, DocumentController).
- */service*: Lógica de negocio y procesamiento de datos (UserService, DocumentService).
- */model*: Entidades JPA mapeadas a las tablas de la base de datos (User, Document, Subject, Career, University).
- */repository*: Interfaces de Spring Data JPA para el acceso a datos.
- */DTO*: Objetos de Transferencia de Datos para enviar solo la información necesaria al frontend.
- */Security*: Configuración de Spring Security, filtros JWT y validaciones.
- */Exceptions*: Clases para el manejo de errores HTTP personalizados.

<-- Instalación y ejecución -->

1- Clonar el repositorio.
-- en consola:
      git clone https://github.com/usuario/AppUnTN-Backend.git
      cd AppUnTN-Backend

2- Configurar la Base de Datos.
-- Asegurate de tener MySQL corriendo en tu entorno local.
-- En tu gestor de base de datos, creá un esquema vacío:
      CREATE DATABASE appuntn;
-- Revisá el archivo `src/main/resources/application.properties` para asegurar que las credenciales de MySQL (usuario y contraseña) coincidan con las de tu máquina. Hibernate creará las tablas automáticamente (`ddl-auto=update`).

3- Ejecutar la aplicación.
-- Podés correrlo directamente desde tu IDE (IntelliJ IDEA / Eclipse) dándole a "Run" en la clase principal `AppUnTnApplication.java`.
-- O mediante consola con Maven:
      mvn spring-boot:run

La API estará corriendo en:
      http://localhost:8080/

<-- Conexión con el Frontend -->
Asegurate de que el Frontend de Angular esté corriendo en `http://localhost:4200/`. El backend tiene configurado el `@CrossOrigin` para permitir las peticiones desde este puerto.

Principales Endpoints expuestos:

POST   /api/auth/login
POST   /api/users/register
GET    /api/users/me
GET    /api/users/getAllUsers
DELETE /api/users/deleteUser

POST   /api/documents/add
GET    /api/documents/getAll
GET    /api/admin/universities
GET    /api/admin/careers
GET    /api/admin/subjects

<-- Roles y permisos -->

Authority: USER       | Puede registrarse, subir y descargar documentos, actualizar su perfil.
Authority: ROLE_ADMIN | Tiene acceso total a los endpoints bloqueados con @PreAuthorize. Puede gestionar usuarios y eliminar administradores.

<-- Licencia -->
MIT – libre para uso académico y educativo.
