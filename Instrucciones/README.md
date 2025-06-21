# 🧠 Neuromotion Helm Charts

Repositorio de Helm Charts para desplegar los microservicios y el frontend de **Neuromotion** en Kubernetes.

---

## 📦 Charts disponibles

| Chart                   | Versión | Imagen | Descripción                                                  |
|-------------------------|---------|--------|--------------------------------------------------------------|
| `ms-prerequisites`      | 1.0.0   | —      | Crea el namespace `ms-app` y el Secret con credenciales DB   |
| `mysql-doctores`        | 0.1.0   | 8.0    | Base de datos MySQL para el microservicio `ms-doctores`      |
| `mysql-usuarios`        | 0.1.0   | 8      | Base de datos MySQL para el microservicio `ms-usuarios`      |
| `ms-doctores`           | 1.0.0   | v2     | Microservicio Spring Boot `ms-doctores`                      |
| `ms-usuarios`           | 1.0.0   | v2     | Microservicio Spring Boot `ms-usuarios`                      |
| `neuromotion-frontend`  | 0.1.0   | v1     | Frontend de Neuromotion (Angular)                            |

---

## 🚀 Instalación completa paso a paso

### Paso 1: Agregar el repositorio Helm

```bash
helm repo add neuromotion https://rmcabrera.github.io/neuromotion-charts/
helm repo update
```

### Paso 2: Instalar los pre-requisitos

Instala el chart `ms-prerequisites` para crear el namespace `ms-app` y el Secret con las credenciales de la base de datos.

```bash
helm install ms-prerequisites neuromotion/ms-prerequisites 
```

### Paso 3: Instalar las bases de datos

Instala las bases de datos MySQL para los microservicios `ms-doctores` y `ms-usuarios`.

```bash
helm install mysql-doctores neuromotion/mysql-doctores -n ms-app
helm install mysql-usuarios neuromotion/mysql-usuarios -n ms-app
```

### Paso 4: Instalar los microservicios

Instala los microservicios `ms-doctores` y `ms-usuarios`.

```bash
helm install ms-doctores neuromotion/ms-doctores -n ms-app
helm install ms-usuarios neuromotion/ms-usuarios -n ms-app
```

### Paso 5: Instalar el Frontend

Instala el frontend de Neuromotion.

```bash
helm install neuromotion-frontend neuromotion/neuromotion-frontend -n ms-app
```

---

### Paso 6: Acceder al Frontend

Una vez desplegado el frontend, puedes acceder a él a través de Minikube:

```bash
minikube service neuromotion-frontend-service -n ms-app
```

---

## 🗑️ Desinstalación

Para desinstalar todos los charts y limpiar los recursos:

```bash
helm uninstall ms-doctores -n ms-app
helm uninstall ms-usuarios -n ms-app
helm uninstall neuromotion-frontend -n ms-app
helm uninstall mysql-doctores -n ms-app
helm uninstall mysql-usuarios -n ms-app
kubectl delete pvc --all -n ms-app
kubectl delete pv --all
helm uninstall ms-prerequisites
```

### Limpieza adicional (Opcional)

Para limpiar el repositorio Helm local y las imágenes de Docker en Minikube:

```bash
helm repo remove neuromotion
```

Para limpiar las imágenes de Docker en Minikube:

```bash
minikube ssh
docker images | grep ms-usuarios # Puedes ajustar el filtro según las imágenes que quieras ver
docker rmi mcabrerac/ms-doctores:v2
docker rmi mcabrerac/ms-usuarios:v2
docker rmi mcabrerac/neuromotion-frontend:v1
exit
```
