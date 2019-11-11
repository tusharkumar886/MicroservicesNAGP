# Run following docker commands for mysql database

docker pull mysql/mysql-server:5.7
docker run --name=mysql1 -d mysql/mysql-server:5.7
docker logs mysql1
Copy generated one time password
docker exec -it mysql1 mysql -uroot -p
Enter generated password
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
mysql> CREATE SCHEMA banking;


# Run following docker commands to run the application.
docker network create cloud
docker run --network cloud --name mysql1 -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=banking mysql/mysql-server:5.7
docker exec -it mysql1 mysql -uroot -p
Enter root password
mysql> CREATE USER 'root'@'%' IDENTIFIED BY 'root';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

docker run --net cloud --name=eureka -d -p 8761:8761 tusharkumar886/eureka-server
docker run --net cloud --name=zuul-server -d -p 8765:8765 tusharkumar886/zuul-server
docker run --net cloud --name=config-server -d -p 8888:8888 tusharkumar886/config-server
docker run --net cloud --name=customer-service -d -p 8000:8000 tusharkumar886/customer-service
docker run --net cloud --name=banking-service -d -p 8100:8100 tusharkumar886/banking-service


To load dynamic config properties:
docker exec -it config-server bash
curl http://localhost:8888/config-server/dev

To load dynamic config properties in client application:
docker exec -it customer-service bash
curl -X POST localhost:8100/actuator/refresh
