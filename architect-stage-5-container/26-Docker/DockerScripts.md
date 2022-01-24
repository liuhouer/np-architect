# Docker Scripts for reference  

# hello-world  
docker run hello-world  

# Redis caching without data persistence  
docker run -d redis:3.2 redis-server  
docker exec -it d86cd855fc54 redis-cli  
set name feiyang  
get name  
exit  

# Nginx reverse proxy  
docker run -p 80:80 -d nginx  

# Env preparation  
#get script from get.docker.com, get docker from aliyun mirror: mirrors.aliyun.com  
#run as root  
curl -fsSL get.docker.com -o get-docker.sh  
sh get-docker.sh --mirror Aliyun  
systemctl daemon-reload  
systemctl restart docker  

# Container lifecycle 
docker create nginx  
docker start 398edf535ba8  
docker stop 398edf535ba8  
docker kill 398edf535ba8  
docker start 398edf535ba8  
docker pause 398edf535ba8  
docker unpause 398edf535ba8  
docker exec -it 398edf535ba8 /bin/bash  
exit  

docker rm -f 398edf535ba8  
docker run -d -c 4000 nginx  
docker run -d -m 200M --memory-swap=400M nginx  
docker run -d --blkio-weight 300 nginx  

docker run -d redis  
docker logs -f f13ba5508331  

# Dockerfile  
mkdir test1  
tar -czvf test1.tar.gz test1  
touch test3  

vi Dockerfile  
```
#Owned by Zhang Feiyang  
FROM debian  
MAINTAINER Feiyang  
ADD test1.tar.gz .  
COPY test3 .  
RUN mkdir test2  
ENTRYPOINT ["/bin/sh"]  
CMD ["-c", "ls -l"]  
```
# storage  
docker run -v data:/data -d redis:3.2 redis-server  
