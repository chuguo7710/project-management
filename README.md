# 御驾亲征 · Royal Campaign

> 一个前后端分离的团队协作平台：项目管理、成员协作、任务看板，带完整的 JWT 鉴权与角色权限模型。

个人全栈实践项目。后端 Spring Boot + MyBatis + MySQL，前端 Vue 3 + TypeScript，接口与前端类型逐字段对齐。

---

## ✨ 功能

| 模块 | 状态 | 说明 |
| --- | --- | --- |
| 账号体系 | ✅ | 注册、登录、会话校验（JWT）、BCrypt 密码加密；注册时自动初始化个人资料 |
| 角色权限 | ✅ | 全局三角色 `ROOT / ADMIN / USER`，接口级 `@RequireRole` + AOP 校验 |
| 项目管理 | ✅ | 分页列表、按项目名/负责人模糊搜索、新建、编辑、删除 |
| 成员管理 | ✅ | 成员抽屉、按用户名添加、移除；项目内 OWNER 与全局角色双重校验 |
| 任务看板 | 🚧 | 多标签工作区已完成（打开/切换/关闭/上限淘汰），任务实体尚未落库 |
| 数据看板 | ⬜ | 待实现 |

## 🧱 技术栈

| 分层 | 选型 |
| --- | --- |
| 后端框架 | Java 17 · Spring Boot 4.0 · MyBatis |
| 数据存储 | MySQL 8 · Druid 连接池 |
| 认证鉴权 | jjwt 0.12 · spring-security-crypto（BCrypt）· AOP |
| 前端框架 | Vue 3.5 · TypeScript 6 · Vite 8 |
| 前端生态 | Element Plus · Pinia · Vue Router · Axios · Sass |
| 工程质量 | Lombok · vue-tsc 类型检查 · Prettier |

## 🏗 项目结构

```
project-management
├── backend
│   └── src/main/java/com/example
│       ├── common                 # 横切能力
│       │   ├── annotation         # @RequireRole
│       │   ├── aspect             # RoleAspect 权限切面
│       │   ├── interceptor        # JwtInterceptor 登录校验
│       │   ├── exception          # 全局异常处理
│       │   ├── enums              # Role / ProjectRole
│       │   └── utils
│       └── modules
│           ├── user               # 账号：controller / service / mapper / dto / vo
│           └── project            # 项目与成员关系：同上
└── frontend
    └── src
        ├── api                    # 每个文件对应一个后端模块，只负责发请求
        ├── composables            # 领域逻辑（useRequest、分页切片）
        ├── stores                 # Pinia 跨页面状态
        ├── types                  # 与后端逐字段对齐的类型定义
        └── views                  # 按路由分域，就近放组件与组合式函数
```

## 🔐 权限模型

两层角色：**全局角色**决定能做什么操作类别，**项目内角色**决定能操作哪个项目。

| 层级 | 取值 | 存储位置 |
| --- | --- | --- |
| 全局角色 | `ROOT(1)` / `ADMIN(2)` / `USER(3)` | `accounts.role` |
| 项目内角色 | `OWNER(1)` / `MEMBER(2)` | `project_account.my_project_role` |

| 操作 | 要求 |
| --- | --- |
| 查看项目列表 | 登录即可 |
| 查看项目成员 | 是该项目的成员 |
| 创建项目 | `ADMIN` / `ROOT`，创建者自动成为该项目 `OWNER` |
| 编辑 / 删除项目 | `ADMIN` / `ROOT`，且是该项目 `OWNER` |
| 添加 / 移除成员 | 是该项目的 `OWNER` |

> `ROOT` 在 Service 层跳过项目范围校验，可直接操作任意项目。

- 第一层由 `JwtInterceptor` 解析 token 写入 `ThreadLocal` 会话上下文；
- 第二层由 `@RequireRole` 声明 + `RoleAspect` 拦截；
- 项目范围校验在 Service 层完成（`project_account` 中查当前用户的 `my_project_role`）。

## 🔁 前端请求调度器

`frontend/src/composables/useRequest.ts`

把「请求并发时怎么办」收敛成四种可组合策略，业务层按场景选一种：

| 模式 | 并发时的行为 | 典型场景 |
| --- | --- | --- |
| `latest` | 新请求中断旧请求，被中断的调用方收到 `undefined` | 搜索、筛选、翻页 |
| `dedupe` | 有请求在飞时，新的调用直接返回 `undefined` | 提交类按钮，防重复提交 |
| `queue` | 串行排队，前一个失败不影响后续 | 顺序敏感的写操作 |
| `coalesce` | 短时间内的多次调用合并为一次 | 拖拽、滚动等高频触发 |

统一约定：

- 所有方法返回 `Promise<R | undefined>`，`undefined` 表示「本次调用被调度器吞掉了」，与「请求失败」（reject）严格区分；
- 请求取消（`AbortController`）不弹错误提示，也不视为失败；
- `isLoading` 由调度器统一维护，业务层不再各自维护一份 loading 状态。

## ⚡ 快速开始

### 1. 准备数据库

建库（utf8mb4），表结构见下方「数据表」一节：

```sql
CREATE DATABASE management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 2. 启动后端

```bash
cd backend
# 修改 src/main/resources/application.yaml：数据库账号密码、jwt.secret
mvn spring-boot:run
```

服务默认运行在 `http://localhost:8080`，统一接口前缀 `/api`（`server.servlet.context-path`）。

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

Vite 已把 `/api` 代理到 `http://localhost:8080`，开发环境接口前缀由 `.env.development` 的 `VITE_API_URL` 提供。

## 📡 接口一览

统一响应体，`code = 1` 表示成功：

```json
{ "code": 1, "msg": "success", "data": {} }
```

| 方法 | 路径 | 说明 | 权限 |
| --- | --- | --- | --- |
| POST | `/auth/register` | 注册 | 公开 |
| POST | `/auth/login` | 登录，返回 JWT | 公开 |
| GET | `/auth/session` | 校验会话 | 登录 |
| GET | `/user/me` | 当前用户信息 | 登录 |
| GET | `/project/list` | 项目分页 + 搜索 | 登录 |
| GET | `/project/my` | 我参与的项目 | 登录 |
| POST | `/project` | 创建项目 | ADMIN / ROOT |
| PUT | `/project/{projectId}` | 编辑项目 | OWNER |
| DELETE | `/project/{projectId}` | 删除项目 | OWNER |
| GET | `/project/{projectId}/members` | 成员列表 | 项目成员 |
| POST | `/project/{projectId}/members` | 添加成员 | OWNER |
| DELETE | `/project/{projectId}/members/{userId}` | 移除成员 | OWNER |

分页接口统一返回 `{ records, total }`。

## 🗄 数据表

| 表 | 作用 | 关键字段 |
| --- | --- | --- |
| `accounts` | 账号与全局角色 | `user_id`、`username`、`password`（BCrypt）、`role`、`status`、`create_time` |
| `account_profile` | 个人资料，与账号一对一 | `user_id`、`nickname`、`avatar`、`gender`、`bio`、`update_time` |
| `projects` | 项目主体 | `project_id`、`project_name`、`status`、`leader_id`、`progress`、`description`、`create_time` |
| `project_account` | 用户与项目的多对多关系及项目内角色 | `project_id`、`user_id`、`my_project_role`、`join_time` |

## 🗺 路线图

- [x] 账号体系与 JWT 鉴权
- [x] 角色权限（全局 + 项目内）
- [x] 项目 CRUD、搜索、分页
- [x] 成员管理
- [ ] 任务看板（任务实体、状态流转、拖拽排序）
- [ ] 数据看板（项目进度、成员产出统计）
- [ ] 用户管理后台（列表、改角色、启用禁用）
- [ ] 修改密码、Token 刷新

## 📄 说明

本项目为个人学习与求职作品，用于实践前后端分离架构、鉴权体系与状态管理。
