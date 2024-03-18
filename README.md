# JemerSoft Challenge

# Requisitos Previos
Tener instalado Java 17

# Instalación
Descargar el repositorio desde
```bash
  $ git clone https://github.com/AlanFarias97/jemersoft-challenge.git
```

# Uso
1. El primer paso consiste en cargar las tablas ejecutando el servicio llamado "dataload", el cual dispara un evento donde se realiza el llamado a la POKE-API
2. Luego podrán probar los demas servicios sin problemas 🙂.
3. En el caso del servicio pokemon/list, lleva un parametro el cual es el filtro para buscar por nombre, en este caso se debe encodear en base64 el siguiente Json:
   { "name": "nombreDelPokemon" } , donde previamente van a reemplazar nombreDelPokemon por el pokemon que desean buscar, tambien es posible hacer una búsqueda aproximada.
