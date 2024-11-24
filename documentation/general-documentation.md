== Method

=== Arquitectura de la Aplicación

La aplicación sigue utilizando Spring Reactive y PostgreSQL, pero se añade la funcionalidad de autenticación y autorización con JWT. Los componentes clave son:

- **Frontend (React + Tailwind):** Maneja la interacción del usuario, incluyendo la autenticación.
- **Backend (Spring Reactive):** Provee servicios REST para:
  - Consultar la OMDB API.
  - Manejar CRUD para contenidos, colecciones y usuarios.
  - Autenticación basada en tokens JWT.
- **Base de Datos Relacional (PostgreSQL):** Incluye las tablas y relaciones definidas.

=== Esquema de Base de Datos

Aquí está el esquema actualizado de la base de datos:

```sql
CREATE TABLE content_types (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL
);

CREATE TABLE contents (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title TEXT NOT NULL,
    description TEXT,
    release_date DATE,
    content_type_id BIGINT REFERENCES content_types (id),
    duration INT,
    rating NUMERIC(3, 1)
);

CREATE TABLE collections (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    user_id BIGINT NOT NULL
);

CREATE TABLE collection_contents (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    collection_id BIGINT REFERENCES collections (id),
    content_id BIGINT REFERENCES contents (id)
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL
);

=== Diagrama de Componentes

```plantuml
@startuml
package "Frontend (React + Tailwind)" {
  [UI de Búsqueda]
  [Vista de Detalle]
}

package "Backend (Spring Reactive)" {
  [Controlador de Búsqueda General]
  [Controlador de Detalle]
  [Servicio de Base de Datos]
}

package "OMDB API" {
  [API de Búsqueda]
  [API por IMDbID]
}

package "Base de Datos Relacional (PostgreSQL)" {
  [Tabla de Películas]
}

[UI de Búsqueda] --> [Controlador de Búsqueda General]
[Vista de Detalle] --> [Controlador de Detalle]
[Controlador de Búsqueda General] --> [API de Búsqueda]
[Controlador de Detalle] --> [API por IMDbID]
[Controlador de Detalle] --> [Tabla de Películas]
@enduml
