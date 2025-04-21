# ArkAuth: A Lightweight Identity Authentication Platform

ArkAuth 是一个认证平台，结合密码学与系统设计，专注于轻量级身份认证方案的实现与验证。该平台采用 Java 实现核心加密模块（基于 JPBC 的双线性配对），Go 实现平台逻辑服务，通过 gRPC 进行跨语言远程调用，支持注册、登录、密钥管理等功能

---

## 🔧 项目结构

```
Ark-Auth/
├── ark-crypto/        # Java加密模块（JPBC）
├── ark-platform/      # Go后端服务（认证平台）
├── web/               # 前端HTML模板
├── proto/             # gRPC定义
├── docs/              # 文档
├── scripts/           # 启动脚本
├── test-data/         # 实验数据
└── README.md
```

---

## 🔐 功能概述

- ✖ 用户注册 / 登录
- ✖ 密钥对生成与管理
- ✅ 身份认证流程（基于双线性配对的挑战-响应）
- ✖ 可拓展的加密服务接口
- ✖ 前后端分离结构设计，支持远程调用

---

## 📡 通信机制

本项目通过 **gRPC** 实现 Java 加密服务与 Go 平台服务之间的跨语言通信，传输结构化数据，保持高效与安全。

---

## 📂 使用说明

```bash
# 启动加密服务（Java）
cd ark-crypto
./run.sh

# 启动平台服务（Go）
cd ark-platform
go run main.go

# 访问前端页面
open web/login.html
```

