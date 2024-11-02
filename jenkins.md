### Запуск Jenkins в Docker
[Документация](https://www.jenkins.io/doc/book/installing/docker/#downloading-and-running-jenkins-in-docker)
1. Создать network
```
docker network create jenkins
```
2. Запустить docker:dind
```
docker run --name jenkins-docker --rm --detach \
  --privileged --network jenkins --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  --publish 2376:2376 \
  docker:dind --storage-driver overlay2
```
3. Создать свой образ Jenkins+Docker
```
docker build -t <имя образа> .
```
4. Запустить Jenkins
```
docker run --name jenkins --restart=on-failure --detach --privileged --network jenkins --env DOCKER_HOST=tcp://docker:2376 --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 --publish 8080:8080 --publish 50000:50000 --volume jenkins-data:/var/jenkins_home  --volume jenkins-docker-certs:/certs/client:ro  myjenkins:2.479.1-1
```


