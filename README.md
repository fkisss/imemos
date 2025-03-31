# iMemos - Memos的第三方安卓客户端

## 项目概述

iMemos是一个为Memos笔记软件开发的第三方安卓客户端应用，旨在提供美观、易用的移动端体验。

## 功能特点

- **美观的用户界面**：采用Material Design 3设计语言，提供现代化的用户体验
- **自定义主题**：支持自定义主页主题，包括颜色、字体和布局
- **完整的笔记功能**：支持创建、编辑、删除和搜索笔记
- **Markdown支持**：完整支持Markdown格式的笔记编辑和预览
- **离线模式**：支持在无网络环境下查看和编辑笔记
- **多服务器支持**：可以连接到不同的Memos服务器实例

## 技术架构

- **开发语言**：Kotlin
- **UI框架**：Jetpack Compose
- **架构模式**：MVVM (Model-View-ViewModel)
- **网络请求**：Retrofit + OkHttp
- **本地存储**：Room数据库
- **依赖注入**：Hilt
- **异步处理**：Kotlin Coroutines + Flow
- **图片加载**：Coil

## API集成

iMemos通过Memos的开放API与服务器进行通信，主要包括以下API端点：

- 用户认证与令牌管理
- 笔记的CRUD操作
- 用户设置管理
- 资源上传与管理

## 项目结构

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/imemos/
│   │   │   ├── api/            # API接口和网络相关
│   │   │   ├── data/           # 数据层，包括模型和存储库
│   │   │   ├── di/             # 依赖注入
│   │   │   ├── ui/             # UI组件和屏幕
│   │   │   │   ├── auth/       # 认证相关界面
│   │   │   │   ├── memo/       # 笔记相关界面
│   │   │   │   ├── settings/   # 设置界面
│   │   │   │   ├── theme/      # 主题相关
│   │   │   │   └── components/ # 可复用组件
│   │   │   ├── util/           # 工具类
│   │   │   └── IMemoApplication.kt
│   │   ├── res/                # 资源文件
│   │   └── AndroidManifest.xml
│   └── test/                   # 测试代码
└── build.gradle
```

## 开发计划

1. **基础架构搭建**：创建项目结构，配置依赖，设置基本架构
2. **API集成**：实现与Memos服务器的API通信
3. **数据层实现**：设计数据模型和本地存储
4. **UI开发**：实现各个界面和组件
5. **主题定制**：实现主题自定义功能
6. **测试与优化**：进行功能测试和性能优化

## 开发环境

- Android Studio
- Gradle
- Android SDK 21+
- Kotlin 1.8+