# 吃火宝 - 美食外卖平台

## 项目简介

吃火宝是一个全栈式美食外卖平台，提供完整的点餐、订单管理、店铺管理等功能。项目采用前后端分离架构，包含后台管理系统和用户端应用。


## 技术栈

### 后端技术
- **框架**: Spring Boot
- **数据库**: MySQL
- **构建工具**: Maven
- **主要功能**: RESTful API、数据统计、订单处理、商品管理

### 前端技术
- **管理后台**: Vue 3 + Vite + Element Plus
- **用户端**: Vue 3 + Vite
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP 请求**: Axios


## 主要功能

### 后端功能
- 用户认证与授权
- 商品管理（增删改查）
- 分类管理
- 订单管理
- 店铺管理
- 数据统计与分析
- 文件上传

### 管理后台功能
- 登录/注册
- 数据看板（销售统计、订单趋势等）
- 商品管理
- 分类管理
- 订单管理
- 店铺管理
- 用户管理
- 个人资料

### 用户端功能
- 登录/注册
- 店铺列表展示
- 店铺详情浏览
- 商品点餐
- 购物车
- 订单提交
- 订单查询
- 个人中心


## 快速开始

### 环境要求
- JDK 1.8+
- MySQL 5.7+
- Node.js 14+
- Maven 3.6+

### 数据库配置
1. 创建 MySQL 数据库
2. 导入 sql/takeout.sql 文件
3. 修改 springboot/src/main/resources/application.yml 中的数据库连接配置

### 后端启动
cd springboot
mvn clean install
mvn spring-boot:run
服务默认运行在：http://localhost:8080


### 管理后台前端启动
cd vue-admin
npm install
npm run dev
访问地址：http://localhost:5173

### 用户端前端启动
cd vue-front
npm install
npm run dev
访问地址：http://localhost:5174


## 默认账号

### 管理员账号
- 用户名：admin
- 密码：123456

## API 接口

### 主要接口
- /api/user - 用户相关接口
- /api/product - 商品相关接口
- /api/category - 分类相关接口
- /api/orders - 订单相关接口
- /api/shop - 店铺相关接口
- /api/statistics - 统计相关接口

详细接口文档请参考代码中的 Controller 层实现。

## 开发团队

本项目由 Java全栈1 班第九组开发完成

## 许可证

MIT License
