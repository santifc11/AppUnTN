<-------- AppUnTN — Plataforma de resúmenes colaborativos -------->

AppUnTN es una plataforma web diseñada para estudiantes y docentes de la UTN.
Permite subir, descargar, puntuar y comentar resúmenes, junto con un sistema de roles (Alumno, Profesor, Administrador) y herramientas de gestión para docentes y admins.

Este repositorio contiene el Frontend desarrollado en Angular 20.

<-- Tecnologías utilizadas -->

Frontend

- Angular 20
- Signals API
- HTML / CSS
- JWT Authentication (envío de token vía headers)
- Consumo de API REST (HttpClient)

Backend (proyecto separado)

- Spring Boot
- Spring Security con JWT
- MySQL
- Lombok / DTOs / Repositorios JPA

<-- Funcionalidades principales -->

Usuarios:

- Registro de alumnos y profesores
- Inicio de sesión con JWT
- Edición de perfil: nombre, apellido, email, ciudad, descripción y contraseña
- Vista previa y descarga de resúmenes

Profesores:

- Asignación de materias que dictan
- Visualización y gestión de sus materias
- Filtro por materia y vista previa de resúmenes

Administradores:

- Gestión completa de usuarios
- Registrar nuevos administradores
- Eliminar usuarios (excepto a sí mismos y admin root)
- Filtrado por nombre, usuario, email y rol
- Gestión de administradores (vista específica)

Resúmenes: 

- Subida de archivos PDF
- Vista previa
- Búsqueda y filtrado por materia
- Comentarios y puntuaciones

<-- Estructura del proyecto -->

/src
├── /app
│   ├── /pages
│   │   ├── /profile
│   │   ├── /login
│   │   ├── /register
│   │   ├── /admin-admins
│   │   ├── /admin-usuarios
│   │   ├── /subjects
│   │   └── /documents
│   └── /services
│       ├── auth.ts
│       ├── user-service.ts
│       └── document-service.ts
└── /assets

-- Descripción de Carpetas

- */app/pages*: Contiene las páginas de la aplicación
  - /profile: Página de perfil de usuario
  - /login: Página de inicio de sesión
  - /register: Página de registro
  - /admin-admins: Panel de administración de administradores
  - /admin-usuarios: Panel de administración de usuarios
  - /subjects: Gestión de materias
  - /documents: Gestión de documentos

- */app/services*: Servicios de la aplicación
  - auth.ts: Servicio de autenticación
  - user-service.ts: Servicio de gestión de usuarios
  - document-service.ts: Servicio de gestión de documentos

- */assets*: Recursos estáticos (imágenes, fuentes, etc.)

<-- Instalación y ejecución -->

1- Clonar el repositorio.
-- en consola:
      git clone https://github.com/usuario/AppUnTN-Frontend.git
      cd AppUnTN-Frontend

2- Instalar dependencias.
-- en consola:
      npm install

3- Ejecutar en modo desarrollo.
-- en consola:
      ng serve

La aplicación estará corriendo en:
      http://localhost:4200/

<-- Conexión con el Backend -->
El backend debe estar corriendo en:
      http://localhost:8080

Rutas principales:

POST /api/auth/login
POST /api/users/register
POST /api/users/profile
PUT  /api/users/updateUser
GET  /api/users/getAllUsers
DELETE /api/users/deleteUser

<-- Roles y permisos -->

Alumno        | Subir/descargar resúmenes, editar perfil
Profesor      |	Todo lo anterior + asignarse materias
Administrador |	Gestión de usuarios, admins, eliminación, registro de nuevos admins

<-- Contribuciones -->

Pull requests son bienvenidos!
Para cambios importantes, crear primero un issue para discutir lo propuesto.

<-- Licencia -->
MIT – libre para uso académico y educativo.



