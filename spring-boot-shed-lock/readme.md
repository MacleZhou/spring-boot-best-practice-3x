# JDepend，一个Java依赖的分析师！
https://mp.weixin.qq.com/s/MZbAcF05OeOtuUrt4KT-oQ

# DbUtils：Java数据库操作的简化专家！让JDBC不再繁琐！
https://mp.weixin.qq.com/s/_fMlKZmatMY5r_Qg4pLgjA

# Spring Boot集成ShedLock实现分布式定时任务

https://mp.weixin.qq.com/s/srYbbrjU3QfbKeFXjdQb3g

https://github.com/lukas-krecan/ShedLock

```sql
# MySQL, MariaDB
CREATE TABLE shedlock(name VARCHAR(64) NOT NULL, lock_until TIMESTAMP(3) NOT NULL,
                      locked_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3), locked_by VARCHAR(255) NOT NULL, PRIMARY KEY (name));

# Postgres
CREATE TABLE shedlock(name VARCHAR(64) NOT NULL, lock_until TIMESTAMP NOT NULL,
                      locked_at TIMESTAMP NOT NULL, locked_by VARCHAR(255) NOT NULL, PRIMARY KEY (name));

# Oracle
CREATE TABLE shedlock(name VARCHAR(64) NOT NULL, lock_until TIMESTAMP(3) NOT NULL,
                      locked_at TIMESTAMP(3) NOT NULL, locked_by VARCHAR(255) NOT NULL, PRIMARY KEY (name));

# MS SQL
CREATE TABLE shedlock(name VARCHAR(64) NOT NULL, lock_until datetime2 NOT NULL,
                      locked_at datetime2 NOT NULL, locked_by VARCHAR(255) NOT NULL, PRIMARY KEY (name));

# DB2
CREATE TABLE shedlock(name VARCHAR(64) NOT NULL PRIMARY KEY, lock_until TIMESTAMP NOT NULL,
                      locked_at TIMESTAMP NOT NULL, locked_by VARCHAR(255) NOT NULL);

```