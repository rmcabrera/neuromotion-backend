# 🚀 Guía de Instalación en Azure ☁️

**Antes de comenzar, ingrese al Cloud Shell de Azure.**

**🔑 Paso 0: Login to Azure**

```bash
az login
```

**✅ Paso 1: Validar y Registrar los proveedores necesarios**

```bash
az provider show --namespace Microsoft.Insights --query "registrationState"
```

**Debe devolver "Registered".

**Ejecuta estos dos comandos para registrar el proveedor:

```bash
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.ContainerService
```

**➕ Paso 2: Crear el grupo de recursos**

```bash
az group create --name aks-resource-group --location brazilsouth
```

```Resultado  
{            
  "id": "/subscriptions/29d22f3b-c14d-4a8d-a355-bdfced5435e5/resourceGroups/aks-resource-group",
  "location": "brazilsouth",
  "managedBy": null,
  "name": "aks-resource-group",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null,
  "type": "Microsoft.Resources/resourceGroups"
}
```

**⚙️ Paso 3: Crear el clúster AKS**

```bash
az aks create --resource-group aks-resource-group --name myAKSCluster --node-count 2 --enable-addons monitoring --generate-ssh-keys --location brazilsouth --node-vm-size Standard_D2s_v6
```

**🔑 Paso 4: Obtener las credenciales del clúster AKS**

```bash
az aks get-credentials --resource-group aks-resource-group --name myAKSCluster
```

**✔️ Paso 5: Verificar el estado de los nodos en el clúster**

```bash
kubectl get nodes
```

**➕ Paso 6: Agregar el repositorio Helm de Neuromotion**

```bash
helm repo add neuromotion https://rmcabrera.github.io/neuromotion-charts/; 
helm repo update
```

**⬇️ Paso 7: Instalar los pre-requisitos**

```bash
helm install ms-prerequisites neuromotion/ms-prerequisites
```

**🗄️ Paso 8: Instalar las bases de datos**

```bash
helm install mysql-doctores neuromotion/mysql-doctores -n ms-app; 
helm install mysql-usuarios neuromotion/mysql-usuarios -n ms-app
```

**🚀 Paso 9: Instalar los microservicios**

```bash
helm install ms-doctores neuromotion/ms-doctores -n ms-app; 

helm install ms-usuarios neuromotion/ms-usuarios -n ms-app

```

**🖥️ Paso 10: Instalar el Frontend**

```bash
helm install neuromotion-frontend neuromotion/neuromotion-frontend -n ms-app
```

**🌍 Paso 11: Exponer el Frontend con LoadBalancer**

```bash
kubectl patch svc neuromotion-frontend-service -n ms-app -p '{"spec": {"type": "LoadBalancer"}}'
```

**✔️ Paso 12: Verificar el estado de los servicios**

```bash
kubectl get svc -n ms-app
```

**✔️ Paso 13: Obtener la IP pública del Frontend**

```bash
kubectl get svc neuromotion-frontend-service -n ms-app
```

**🌐 Paso 14: Acceder al Frontend a través de la IP pública proporcionada**


**Para Finalizar el cluster
az aks stop --name myAKSCluster --resource-group aks-resource-group
