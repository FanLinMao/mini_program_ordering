# 私人厨房后台后端

## 技术栈

- Java 17
- Spring Boot 3
- MyBatis-Plus
- MySQL

## 目录分层

- `controller`：接口层
- `service`：服务接口层
- `service/impl`：服务实现层
- `dao`：数据访问层
- `entity`：实体层
- `common`：通用返回和公共能力

## 当前内容

- Spring Boot 基础工程
- MyBatis-Plus 配置
- 实体/DAO/Service/Controller 骨架
- 数据库建表脚本

## 启动方式

1. 先执行 `sql/schema.sql`
2. 修改 `application.yml` 中数据库账号密码
3. 运行启动类 `AdminBackendApplication`
