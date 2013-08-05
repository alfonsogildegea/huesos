huesos
======
Cuando los snippets no son suficientes


### ¿Qué es un hueso?

Los huesos son las partes en las que se compone un esqueleto, y un esqueleto para un informático es una 
plantilla de texto que se ajusta a una necesidad específica.
Por ejemplo, podemos tener huesos para porciones de código fuente, para estandarizar la documentación, para que todas las páginas de una wiki tengan la misma estructura, para que las entradas de un blog sean uniformes, para guardar SQL's, para tener listas de tareas para resolver un problema concreto, etc.


### ¿Cómo se usan?
Es tan simple como lanzar un programa pasándole como parámetro una plantilla de texto y ésta se cargará automáticamente en el portapapeles.

```
Ejemplos:

$ java -jar huesos.jar -p clientes_con_ventas_ult_ejercicio.sql
$ java -jar huesos.jar -p tareas_alta_usuario.txt
$ java -jar huesos.jar -p funcion_listado_horizontal.txt
```

