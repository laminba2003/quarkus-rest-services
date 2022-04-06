# SpringBoot REST Service Helm Chart

## Introduction

This chart bootstraps a [SpringBoot REST Service](https://github.com/laminba2003/spring-rest-services) deployment on a [Kubernetes](http://kubernetes.io) cluster using the [Helm](https://helm.sh) package manager.

## Features

This simple SpringBoot Rest Service is built around these capabilities :

- Persistence with JPA and Spring Data
- Data Migration with Liquibase
- Caching with Redis
- Exception Handling
- Logging with log4j2
- Oauth2 Security with OpenId connect
- Monitoring and Observability with Micrometer and Wavefront
- Distributed Tracing with Sleuth


## Installing the Chart

To install the chart with the release name `my-release`:

```console
$ helm repo add spring-rest https://laminba2003.github.io/spring-rest-services/
$ helm install my-release spring-rest/spring-rest --version 0.1.0
```

The command above deploys the REST service on the Kubernetes cluster in the default namespace. The [configuration](#configuration) section lists the parameters that can be configured during installation.

> **Tip**: List all releases using `helm list`

## Uninstalling the Chart

To uninstall/delete the `my-release` deployment:

```console
$ helm delete my-release
```

The command above removes all the Kubernetes components associated with the chart and deletes the release.

## Configuration

The following table lists the configurable parameters of the chart and their default values.

Parameter | Description | Default
--------- | ----------- | -------
`serviceAccounts.create` | If true, create the service account | `false`
`serviceAccounts.name` | name of the service account to use or create | ``
`image.repository` | api image repository | `laminba2003/spring-rest`
`image.tag` | image tag | `0.0.1-SNAPSHOT`
`image.pullPolicy` | image pullPolicy | `Always`
`replicaCount` | number of api replicas | `1`
`service.type` | api service type | `ClusterIP`
`service.port` | api service port | `9090`
`service.annotations` | api service annotations | `{}`
`resources` | api resource requests and limits | `{}`
`nodeSelector` | api node selector labels for pod assignment | `{}`
`tolerations` | api toleration for pod assignment | `[]`
`affinity` | api affinity for pod assignment | `{}`
`autoscaling.enabled` | If true, create an HPA for the api | `false`
`autoscaling.minReplicas` | Minimum number of replicas for the HPA | `1`
`autoscaling.maxReplicas` | Maximum number of replicas for the HPA | `1`
`autoscaling.targetCPUUtilizationPercentage` | Target CPU utilisation percentage to scale | `80`
`api.configuration.datasource.url` | the mysql datasource url | `jdbc:mysql://mysql/spring_training?enabledTLSProtocols=TLSv1.2`
`api.configuration.datasource.username` | the mysql user name | `cm9vdA==`
`api.configuration.datasource.password` | the mysql user password | `cGFzc2Vy`
`api.configuration.jwt.jwk` | the jwt key set url | `http://keycloak/auth/realms/training/protocol/openid-connect/certs`
`api.configuration.redis.host` | redis url | `redis-master`
`api.configuration.redis.port` | redis port | `6379`
`api.configuration.redis.password` | redis password | `cGFzc2Vy`
`api.wavefront.token` | the wavefront api token | `YThlYzY3MGUtNjc1OS00YWUzLWE0MzUtOTNjY2Y2ODNlZTIy`


Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`. For example,

```console
$ helm install spring-rest/spring-rest --name my-release \
    --set replicaCount=5
```

Alternatively, a YAML file that specifies the values for the above parameters can be provided while installing the chart. For example,

```console
$ helm install spring-rest/spring-rest --name my-release -f values.yaml
```

> **Tip**: You can use the default [values.yaml](values.yaml)