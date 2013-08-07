huesos
======
Cuando los snippets no son suficientes


## ¿Qué es un hueso?

Los huesos son las partes en las que se compone un esqueleto, y un esqueleto para un informático es una 
plantilla de texto que se ajusta a una necesidad específica.
Por ejemplo, podemos tener huesos para porciones de código fuente, para estandarizar la documentación, para que todas las páginas de una wiki tengan la misma estructura, para que las entradas de un blog sean uniformes, para guardar SQL's, para tener listas de tareas para resolver un problema concreto, etc.


## ¿Cómo se usan?

Es tan simple como lanzar un programa pasándole como parámetro una plantilla de texto y ésta se cargará automáticamente en el portapapeles.

```
Ejemplos:

$ cd huesos
$ java -jar huesos.jar -p esqueletos/funcion1.txt
```

En la práctica, no es bueno irte a la línea de comandos y escribir este chorizo, lo más sencillo es enlazarlo a un punto de menú de tu editor preferido o meterlo en una hoja excel con un hipervínculo a un enlace simbólico o a un VBA.


### Tokens

¿Recuerdas la opción "Combinar correspondencia" del Word? La idea es escribir una carta modelo y reemplazar unos campos por textos de un origen de datos. De manera análoga, para un Hueso, un token es un campo que se reemplaza por una cadena de texto.
Así si tenemos una plantilla con el esqueleto de un mantenimiento simple y el nombre del programa aparece en muchos sitios, podemos usar el token <PROGRAMA> y el Hueso copiará al portapapeles la plantilla reemplazando estos tokens por un texto que te pedirá en la linea de comandos. 

```
Ej:

$ java -jar huesos.jar -p esqueletos/funcion2.txt
```

