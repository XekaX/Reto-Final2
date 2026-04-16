# 📦 Proyecto: Reto-Final

## 📖 Descripción

Este proyecto es una aplicación desarrollada en Java que simula la gestión de una tienda (Frikilandia), permitiendo la administración de usuarios, trabajadores, productos y compras mediante una arquitectura MVC con acceso a base de datos MySQL.

Incluye funcionalidades como:
- Login de usuarios (clientes y trabajadores)
- Gestión de trabajadores (CRUD)
- Conexión a base de datos MySQL
- Interfaz gráfica con Swing
- Pruebas unitarias

---

## 🛠️ Tecnologías utilizadas

- Java SE
- Swing (Interfaz gráfica)
- MySQL
- JDBC
- JUnit (tests unitarios)
- Mockito (simulación en pruebas)

---

## 📦 Dependencias

Este proyecto utiliza las siguientes librerías externas:

- MySQL Connector/J (JDBC)
- JUnit 4
- Mockito

---

## 🗄️ Base de datos

El proyecto incluye el script de base de datos:

📄 `Frikilandia.sql`

---

## ▶️ Cómo usar la base de datos

1. Crear una base de datos en MySQL:

```sql
CREATE DATABASE Frikilandia;
```

2. Importar el fichero `bd.sql` en la base de datos creada.

### 🧰 Alternativa
También puedes usar una herramienta gráfica como:
- MySQL Workbench
- phpMyAdmin

---

## 📥 Instalación

Clonar el repositorio:

```bash
git clone https://github.com/XekaX/Reto-Final2.git
```

---

## 📁 Importar el proyecto

Abrir el proyecto en tu IDE:

- Eclipse
- IntelliJ IDEA
- Visual Studio Code

---

## ➕ Añadir dependencias manualmente

1. Descargar:
   - MySQL Connector/J
   - Librerías de Mockito
   - JUnit 4

2. Añadir los archivos `.jar` al proyecto:
   - Click derecho en el proyecto → Build Path → Add External JARs

---

## ⚙️ Ejecución

1. Configurar la conexión a la base de datos en el proyecto:
   - Usuario MySQL
   - Contraseña
   - URL de conexión

2. Ejecutar la aplicación desde la clase `Main`.

---

## 🧪 Tests

El proyecto incluye pruebas unitarias utilizando JUnit y Mockito.

### ▶️ Ejecución de tests:

- Click derecho en la carpeta `test`
- Run As → JUnit Test

### ⚠️ Importante:
Asegúrate de tener añadidos los `.jar` de:
- JUnit
- Mockito

---

## 👨‍💻 Autor

Proyecto desarrollado como parte de un reto final de programación en Java.

---

## 📌 Notas

- Arquitectura MVC
- Separación de capas: Vista, Controlador, Modelo y DAO
- Base de datos MySQL
```
