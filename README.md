# Taller Multi-threading 

## Objetivo

Aprender los conceptos fundamentales de multi-threading en Java implementando ejemplos prácticos para entender cómo manejar múltiples hilos en una aplicación.

---

## Pre-requisitos

- Conocimientos básicos de Java
- Maven instalado (`mvn -version`)
- JDK 8 o superior (`java -version`)

---

## Estructura del Proyecto

```
Taller-Multi-threading/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── taller/
                    ├── ThreadExample.java
                    ├── RunnableExample.java
                    ├── SynchronizedExample.java
                    ├── ExecutorServiceExample.java
                    ├── CallableExample.java
                    └── CustomExample.java
```

---

## Paso 1: Configuración del Proyecto

El proyecto usa Maven con la siguiente configuración en `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.taller</groupId>
    <artifactId>taller-multi-threading</artifactId>
    <version>1.0-SNAPSHOT</version>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

Para compilar el proyecto:

```powershell
mvn compile
```

---

## Paso 2: Ejemplos de Multi-threading en Java

### 1. Creación de Hilos con `Thread`

**Archivo:** `src/main/java/com/taller/ThreadExample.java`

```java
package com.taller;

public class ThreadExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
```

**Explicación:** Se crea un hilo usando la clase `Thread` con una expresión lambda como `Runnable`. El hilo imprime un contador del 0 al 4 con una pausa de 1 segundo entre cada iteración. `thread.start()` inicia la ejecución.

**Ejecución:**
```powershell
mvn exec:java "-Dexec.mainClass=com.taller.ThreadExample"
```

**Output:**

<!-- CAPTURA: Pega aquí la imagen thread-example.png (output con Thread: 0 al Thread: 4) -->
<img width="1494" height="497" alt="image" src="https://github.com/user-attachments/assets/a97eb972-7c9b-4463-b341-d314551018a5" />


---

### 2. Creación de Hilos con `Runnable`

**Archivo:** `src/main/java/com/taller/RunnableExample.java`

```java
package com.taller;

public class RunnableExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Runnable: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
```

**Explicación:** Se define la lógica del hilo en un objeto `Runnable` separado y luego se pasa al constructor de `Thread`. Esto permite reutilizar la misma tarea en múltiples hilos.

**Ejecución:**
```powershell
mvn exec:java "-Dexec.mainClass=com.taller.RunnableExample"
```

**Output:**

<!-- CAPTURA: Pega aquí la imagen runnable-example.png (output con Runnable: 0 al Runnable: 4) -->
<img width="1474" height="501" alt="image" src="https://github.com/user-attachments/assets/32ab11c3-a9e7-405c-9161-235664a2d248" />


---

### 3. Sincronización de Hilos con `synchronized`

**Archivo:** `src/main/java/com/taller/SynchronizedExample.java`

```java
package com.taller;

public class SynchronizedExample {
    private static int counter = 0;

    public static synchronized void increment() {
        counter++;
    }

    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter: " + counter);
    }
}
```

**Explicación:** Sin `synchronized`, dos hilos incrementando el mismo contador producirían un resultado incorrecto por condiciones de carrera (*race conditions*). La palabra clave `synchronized` garantiza que solo un hilo ejecute `increment()` a la vez. El resultado siempre será `Counter: 2000` (2 hilos × 1000 iteraciones).

**Ejecución:**
```powershell
mvn exec:java "-Dexec.mainClass=com.taller.SynchronizedExample"
```

**Output:**

<!-- CAPTURA: Pega aquí la imagen synchronized-example.png (output con Counter: 2000) -->
<img width="1524" height="402" alt="image" src="https://github.com/user-attachments/assets/6f9316f1-412c-40c1-8ed9-785f537ba084" />


---

### 4. Uso de `ExecutorService`

**Archivo:** `src/main/java/com/taller/ExecutorServiceExample.java`

```java
package com.taller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task 1: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task 2: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        executor.submit(task1);
        executor.submit(task2);
        executor.shutdown();
    }
}
```

**Explicación:** `ExecutorService` gestiona un pool de hilos reutilizables, evitando el costo de crear y destruir hilos manualmente. `newFixedThreadPool(2)` crea exactamente 2 hilos. `shutdown()` cierra el pool después de que todas las tareas terminen.

**Ejecución:**
```powershell
mvn exec:java "-Dexec.mainClass=com.taller.ExecutorServiceExample"
```

**Output:**

<!-- CAPTURA: Pega aquí la imagen executor-example.png (output con Task 1 y Task 2 intercalados) -->
<img width="1468" height="607" alt="image" src="https://github.com/user-attachments/assets/5c71272d-d0ba-47e2-a3b1-0f6099b0a63d" />


---

### 5. Uso de `Callable` y `Future`

**Archivo:** `src/main/java/com/taller/CallableExample.java`

```java
package com.taller;

import java.util.concurrent.*;

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                sum += i;
                Thread.sleep(1000);
            }
            return sum;
        };

        Future<Integer> future = executor.submit(task);

        try {
            Integer result = future.get();
            System.out.println("Sum: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
```

**Explicación:** A diferencia de `Runnable`, `Callable` puede retornar un valor. `Future` representa el resultado de una tarea asíncrona — `future.get()` bloquea el hilo principal hasta que el resultado esté disponible. El resultado esperado es `Sum: 10` (0+1+2+3+4).

**Ejecución:**
```powershell
mvn exec:java "-Dexec.mainClass=com.taller.CallableExample"
```

**Output:**

<!-- CAPTURA: Pega aquí la imagen callable-example.png (output con Sum: 10) -->
<img width="1467" height="407" alt="image" src="https://github.com/user-attachments/assets/4cabd875-bb74-4969-8164-2c4978eb18b1" />


---

## Paso 3: Ejemplo Propio — Simulación de Hilos Concurrentes

**Archivo:** `src/main/java/com/taller/CustomExample.java`

Este ejemplo es la versión Java del `demo.py` del taller. Tres hilos corren en paralelo, cada uno con un contador y pausas aleatorias de 1 a 3 segundos, demostrando ejecución concurrente real.

```java
package com.taller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

/**
 * Ejemplo propio: simulación del demo.py del taller.
 * Tres hilos corren en paralelo, cada uno imprime un contador
 * con pausas aleatorias, equivalente al threading de Python.
 */
public class CustomExample {
    private static final Random random = new Random();

    static void worker(String name) {
        for (int i = 1; i <= 5; i++) {
            System.out.println(name + " - Contador: " + i);
            try {
                Thread.sleep((random.nextInt(3) + 1) * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(name + " - Completado");
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> worker("Hilo 1"));
        executor.submit(() -> worker("Hilo 2"));
        executor.submit(() -> worker("Hilo 3"));

        executor.shutdown();

        while (!executor.isTerminated()) {
            Thread.sleep(500);
        }

        System.out.println("Hilos completados");
    }
}
```

**Explicación:** Se usa un `ExecutorService` con 3 hilos para lanzar 3 workers en paralelo. Cada worker imprime su contador con un delay aleatorio entre 1 y 3 segundos, igual que el `demo.py` de referencia. El orden de los prints varía en cada ejecución porque los hilos compiten por el procesador.

**Ejecución:**
```powershell
mvn exec:java "-Dexec.mainClass=com.taller.CustomExample"
```

**Output:**

<!-- CAPTURA: Pega aquí la imagen custom-example.png (output con Hilo 1, 2 y 3 intercalados y "Hilos completados") -->
<img width="1512" height="791" alt="image" src="https://github.com/user-attachments/assets/d62258cf-6645-42f3-814c-bd4003fcdbda" />


---

## Paso 4: Resumen y Conclusión

### Estrategias cubiertas

| Estrategia | Cuándo usarla |
|---|---|
| `Thread` | Tareas simples y únicas |
| `Runnable` | Separar lógica de la tarea del hilo |
| `synchronized` | Proteger recursos compartidos entre hilos |
| `ExecutorService` | Gestionar pools de hilos reutilizables |
| `Callable` + `Future` | Tareas que retornan un resultado |

### Conclusión

El multi-threading permite ejecutar múltiples tareas de forma concurrente, mejorando el rendimiento y la capacidad de respuesta de las aplicaciones. Las técnicas vistas — desde la creación básica con `Thread` hasta la gestión avanzada con `ExecutorService` y `Callable` — son la base para construir aplicaciones Java concurrentes, escalables y robustas.

---

## Cómo correr el proyecto

```powershell
# Compilar
mvn compile

# Correr cualquier ejemplo
mvn exec:java "-Dexec.mainClass=com.taller.NombreDelEjemplo"
```

**Entorno usado:**
- Java 17 (BellSoft Liberica JDK)
- Apache Maven 3.9.14
- Windows 11
