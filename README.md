# 用户管理系统 + 数据看板

> 🎯 **项目定位**：面向 HR 展示「AI 辅助全栈开发能力」的全栈 Demo 项目

## ✨ AI 辅助声明

**本项目的代码约 80% 由 Claude Code 和 GitHub Copilot 辅助生成，我负责需求分析、架构设计、代码调试和功能整合。**

- 后端：Spring Boot 3.2 项目框架、JWT 认证流程、数据统计查询由 AI 生成
- 前端：Vue3 页面组件、ECharts 图表配置、Axios 拦截器由 AI 生成
- 我做的事情：设计数据库表结构、配置 Spring Security 权限规则、调试前后端联调问题、编写业务逻辑优化、部署到云平台

---

## 🛠 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **后端框架** | Spring Boot | 3.2.5 |
| **编程语言** | Java | 17 |
| **安全认证** | Spring Security + JWT (JJWT 0.12.5) | - |
| **数据库(默认)** | H2 内存数据库 | - |
| **数据库(可选)** | MySQL | 8.0 |
| **ORM** | Spring Data JPA (Hibernate) | - |
| **构建工具** | Maven | 3.x |
| **前端框架** | Vue 3 | 3.4.x |
| **构建工具** | Vite | 5.2.x |
| **UI 组件库** | Element Plus | 2.7.x |
| **图表库** | ECharts 5 + vue-echarts | 5.5 / 6.7 |
| **状态管理** | Pinia | 2.1.x |
| **HTTP 客户端** | Axios | 1.7.x |

---

## 📁 项目结构

```
project-root/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/               # API 控制器
│   │   │   ├── AuthController.java   # 注册 & 登录接口
│   │   │   ├── UserController.java   # 用户 CRUD 接口
│   │   │   └── StatsController.java  # 数据统计接口
│   │   ├── model/
│   │   │   └── User.java             # 用户实体类
│   │   ├── dto/                      # 数据传输对象
│   │   │   ├── LoginRequest.java     # 登录请求
│   │   │   ├── RegisterRequest.java  # 注册请求
│   │   │   ├── UserResponse.java     # 用户信息响应
│   │   │   └── StatsResponse.java    # 统计数据响应
│   │   ├── repository/
│   │   │   └── UserRepository.java   # 数据访问层
│   │   ├── service/
│   │   │   ├── AuthService.java      # 认证业务逻辑
│   │   │   └── UserService.java      # 用户管理业务逻辑
│   │   ├── security/                 # 安全配置
│   │   │   ├── JwtUtil.java          # JWT 工具类
│   │   │   ├── JwtFilter.java        # JWT 认证过滤器
│   │   │   └── SecurityConfig.java   # Spring Security 配置
│   │   └── DemoApplication.java      # 启动类
│   ├── src/main/resources/
│   │   └── application.yml           # 应用配置
│   └── pom.xml                       # Maven 依赖管理
├── frontend/                          # Vue3 前端
│   ├── src/
│   │   ├── api/                      # Axios API 封装
│   │   │   ├── index.js              # Axios 实例 & 拦截器
│   │   │   ├── auth.js               # 认证 API
│   │   │   ├── user.js               # 用户管理 API
│   │   │   └── stats.js              # 数据统计 API
│   │   ├── components/               # 公共组件
│   │   │   ├── Layout.vue            # 主布局
│   │   │   ├── Sidebar.vue           # 侧边栏导航
│   │   │   └── Header.vue            # 顶部导航栏
│   │   ├── views/                    # 页面组件
│   │   │   ├── Login.vue             # 登录/注册页
│   │   │   ├── Dashboard.vue         # 数据看板页
│   │   │   └── UserManage.vue        # 用户管理页
│   │   ├── router/
│   │   │   └── index.js              # Vue Router 路由配置
│   │   ├── store/
│   │   │   └── index.js              # Pinia 状态管理
│   │   ├── App.vue                   # 根组件
│   │   └── main.js                   # 入口文件
│   ├── index.html
│   ├── vite.config.js                # Vite 配置（含代理）
│   └── package.json
└── README.md
```

---

## 🚀 快速开始（本地运行）

### 前置要求

| 工具 | 最低版本 | 说明 |
|------|----------|------|
| **Java JDK** | 17+ | [下载地址](https://adoptium.net/) |
| **Maven** | 3.6+ | [下载地址](https://maven.apache.org/download.cgi) 或使用 IDE 内置 |
| **Node.js** | 18+ | [下载地址](https://nodejs.org/)（推荐 LTS 版本） |
| **npm** | 9+ | 随 Node.js 一起安装 |

### 检查环境是否就绪

```bash
# 检查 Java 版本
java -version
# 预期输出：openjdk version "17.x.x" ...

# 检查 Maven 版本
mvn -version
# 预期输出：Apache Maven 3.x.x ...

# 检查 Node.js 版本
node -v
# 预期输出：v18.x.x 或 v20.x.x ...

# 检查 npm 版本
npm -v
```

---

### 第一步：启动后端 (Spring Boot)

```bash
# 1. 进入后端目录
cd backend

# 2. 下载 Maven 依赖（首次运行需要，约 2-5 分钟）
mvn clean install -DskipTests

# 3. 启动 Spring Boot 应用
mvn spring-boot:run

# 看到以下输出则表示启动成功：
# ========================================
#   用户管理系统启动成功！
#   API 地址: http://localhost:8080
#   H2 控制台: http://localhost:8080/h2-console
#   管理员账号: admin / admin123
# ========================================
```

> **💡 提示**：项目默认使用 H2 内存数据库，无需安装 MySQL。启动后会自动创建演示数据。
>
> 如需切换到 MySQL，请编辑 `backend/src/main/resources/application.yml`，注释 H2 配置并启用 MySQL 配置。

---

### 第二步：启动前端 (Vue3)

打开**一个新的终端窗口**：

```bash
# 1. 进入前端目录
cd frontend

# 2. 安装 npm 依赖（首次运行需要，约 1-3 分钟）
npm install

# 3. 启动 Vite 开发服务器
npm run dev

# 看到以下输出则表示启动成功：
# VITE v5.x.x  ready in xxx ms
# ➜  Local:   http://localhost:5173/
```

---

### 第三步：访问应用

1. 打开浏览器，访问 **http://localhost:5173**
2. 使用演示账号登录：
   - **管理员**：`admin` / `admin123`（可以删除用户）
   - **普通用户**：`张三` / `admin123`（或其他测试用户）
3. 查看「数据看板」的统计图表
4. 在「用户管理」中体验增删改查功能

---

## 📡 API 接口文档

### 基础地址

```
http://localhost:8080/api
```

### 认证接口（无需登录）

| 方法 | 路径 | 说明 | 请求体 |
|------|------|------|--------|
| POST | `/api/auth/register` | 用户注册 | `{ username, password, email }` |
| POST | `/api/auth/login` | 用户登录 | `{ account, password }` |

### 用户管理接口（需要登录）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/users?page=0&size=10` | 获取用户列表（分页） | 登录用户 |
| GET | `/api/users/{id}` | 获取单个用户信息 | 登录用户 |
| PUT | `/api/users/{id}` | 更新用户信息 | 登录用户 |
| DELETE | `/api/users/{id}` | 删除用户 | **仅管理员** |

### 数据统计接口（需要登录）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/stats/dashboard` | 获取数据看板统计 |

### 接口测试示例

```bash
# 1. 登录（获取 Token）
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"account":"admin","password":"admin123"}'

# 返回示例：
# {
#   "token": "eyJhbGciOiJIUzI1NiJ9...",
#   "username": "admin",
#   "email": "admin@example.com",
#   "role": "ROLE_ADMIN"
# }

# 2. 获取用户列表（使用上面返回的 Token）
curl http://localhost:8080/api/users?page=0\&size=5 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."

# 3. 获取数据统计
curl http://localhost:8080/api/stats/dashboard \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## 🔐 权限设计

| 角色 | 说明 | 权限 |
|------|------|------|
| `ROLE_USER` | 普通用户（默认） | 查看用户列表、查看数据看板、修改自己的信息 |
| `ROLE_ADMIN` | 管理员 | 所有权限 + 删除用户 + 修改他人角色 |

- 前端：管理员才能看到「删除」按钮
- 后端：`@PreAuthorize` + Spring Security 配置双重保护

---

## 🗄 数据库切换指南

### 默认：H2 内存数据库

不需要任何配置，启动即用。数据在应用关闭后**不会持久化**（每次重启数据重置为初始演示数据）。

### 切换到 MySQL

**步骤 1：创建 MySQL 数据库**

```sql
CREATE DATABASE user_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**步骤 2：修改 `application.yml`**

编辑 `backend/src/main/resources/application.yml`：

1. **注释**掉 H2 的 datasource 配置
2. **取消注释** MySQL 的 datasource 配置，填入你的数据库连接信息
3. 在 JPA 配置中取消注释 `database-platform: org.hibernate.dialect.MySQLDialect`

**步骤 3：添加 MySQL 依赖**

`pom.xml` 中已经包含 `mysql-connector-j`，无需额外添加。

**步骤 4：重启后端**

```bash
cd backend
mvn spring-boot:run
```

---

## 🌐 部署指南

### 后端部署到 Render（免费）

[Render](https://render.com) 提供免费的 Spring Boot 托管服务。

1. 将项目推送到 GitHub 仓库
2. 登录 [Render Dashboard](https://dashboard.render.com)
3. 点击 **New + → Web Service**
4. 连接你的 GitHub 仓库
5. 配置：
   - **Name**：`user-management-api`
   - **Runtime**：`Java`
   - **Build Command**：`cd backend && mvn clean install -DskipTests`
   - **Start Command**：`cd backend && mvn spring-boot:run`
   - **免费套餐**：选择 Free
6. 添加环境变量（如需要切换 MySQL）：
   - `SPRING_DATASOURCE_URL`：MySQL 连接 URL
   - `SPRING_DATASOURCE_USERNAME`：数据库用户名
   - `SPRING_DATASOURCE_PASSWORD`：数据库密码
7. 点击 **Deploy Web Service**
8. 部署完成后，后端 API 地址为 `https://user-management-api.onrender.com`

> ⚠️ **注意**：免费套餐在 15 分钟无请求后会自动休眠，首次请求会有约 30 秒的冷启动延迟。

### 前端部署到 Vercel（免费）

[Vercel](https://vercel.com) 提供免费的静态网站托管，对 Vue3/Vite 项目支持极好。

1. 将项目推送到 GitHub 仓库
2. 登录 [Vercel Dashboard](https://vercel.com/dashboard)
3. 点击 **Add New → Project**
4. 导入你的 GitHub 仓库
5. 配置：
   - **Framework Preset**：`Vite`
   - **Root Directory**：`frontend`
   - **Build Command**：`npm run build`（自动识别）
   - **Output Directory**：`dist`（自动识别）
6. **重要**：添加环境变量（生产环境 API 地址）：
   - `VITE_API_BASE_URL`：`https://user-management-api.onrender.com/api`
7. 还需要修改 `vite.config.js` 的代理配置，因为 Vercel 部署后跨域需要后端 CORS 处理。

   **生产环境适配方案**：后端已经配置了 CORS 允许所有来源。Vercel 部署后，需要修改前端 `src/api/index.js` 中的 `baseURL`：

   ```javascript
   // 将 baseURL 改为从环境变量读取
   baseURL: import.meta.env.VITE_API_BASE_URL || '/api'
   ```

   同时在 `frontend` 根目录创建 `.env.production` 文件：
   ```
   VITE_API_BASE_URL=https://user-management-api.onrender.com/api
   ```

8. 点击 **Deploy**
9. 部署完成后，前端地址为 `https://你的项目名.vercel.app`

### 使用 Docker 一键部署（可选）

项目根目录下的 `docker-compose.yml` 可以一键启动 MySQL + 后端 + 前端：

```bash
# 在项目根目录执行
docker-compose up -d
```

---

## 📝 开发笔记

### 关键设计决策

1. **为什么使用 H2 而不是 MySQL？**
   - 作为 Demo，H2 避免了安装数据库的步骤，面试官可以一键启动
   - 代码中保留了完整的 MySQL 切换方案

2. **为什么密码是 BCrypt 加密？**
   - Spring Security 推荐的密码加密算法
   - 自动加盐，难以被彩虹表破解

3. **为什么用 JWT 而不是 Session？**
   - 前后端分离架构的标准做法
   - 无状态，方便水平扩展
   - Token 存储在 localStorage，刷新不丢失

4. **Vite 代理的作用？**
   - 开发时将 `/api` 请求转发到后端 `localhost:8080`
   - 避免浏览器跨域限制（CORS）

### 常见问题排查

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 后端启动报端口占用 | 8080 端口已被占用 | 修改 `application.yml` 中的 `server.port` |
| 前端请求报 401 | Token 过期或无效 | 重新登录获取新 Token |
| 前端请求报 CORS 错误 | 跨域配置问题 | 检查后端 `SecurityConfig` 的 CORS 配置 |
| H2 控制台无法访问 | Spring Security 拦截 | 已配置允许 `/h2-console/**` |
| ECharts 图表不显示 | ECharts 组件未注册 | 检查 `Dashboard.vue` 中的 `use()` 注册 |
| `mvn` 命令无法识别 | Maven 未安装或未配置环境变量 | 参考 Maven 官方文档配置 PATH |

---

## 🤝 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) — 强大的 Java 后端框架
- [Vue.js](https://vuejs.org/) — 渐进式前端框架
- [Element Plus](https://element-plus.org/) — 优秀的 Vue3 UI 组件库
- [ECharts](https://echarts.apache.org/) — 数据可视化图表库
- [JJWT](https://github.com/jwtk/jjwt) — Java JWT 工具库
- **Claude Code** — AI 辅助编程，大幅提升开发效率
- **GitHub Copilot** — 智能代码补全

---

> 📅 创建日期：2026-06-17
>
> 💻 本项目为 AI 辅助全栈开发的 Demo 展示，欢迎用于学习和面试演示。
