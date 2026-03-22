## 动境 App — 开发进度记录

### 项目概述

动境 App 是一款基于 Jetpack Compose 的 Android 运动健身应用，提供课程学习、训练播放、AR 姿态检测、数据统计、视频管理与上传等能力。

### 技术栈

| 类别 | 技术选型 |
|------|----------|
| 语言 | Kotlin |
| UI | Jetpack Compose（Material 3） |
| 架构 | MVVM（部分页面：`ViewModel` + `Repository` + `StateFlow`） |
| 导航 | Navigation Compose |
| 图片 | Coil |
| 异步 | Kotlin Coroutines |
| 视频播放 | AndroidX **Media3**（ExoPlayer 迁移后） |
| 姿态估计 | **MediaPipe Tasks Vision**（Pose Landmarker，`assets` 内 `pose_landmarker_lite.task`） |
| 相机 | **CameraX**（预览 + `ImageAnalysis`） |
| 本地存储 | **Room**（训练会话记录） |
| 代码生成 | **KSP**（须与 Kotlin 版本对齐，当前示例：`2.2.10-2.0.2`） |
| 网络 | Retrofit + OkHttp（演示上传：`httpbin.org`） |
| 权限 | Accompanist Permissions |

> 说明：README 中曾写的 ARCore / Sceneform 未接入；若需 AR 空间锚点再单独评估。

---

### 已完成功能

#### 1. 基础与导航

- Gradle Kotlin DSL、Compose、KSP + Room 编译链路
- 底部导航（首页 / 课程 / AR / 数据 / 我的），首页与其他 Tab 切换逻辑（含回到干净首页栈）
- 全屏页隐藏底栏：`training/*`、`video/player/*`、`video/upload`
- 主题与 `MainActivity` 入口

#### 2. 首页（`ui/home`）

- 搜索栏、轮播推荐区、功能入口（开始训练、AR、数据中心、视频库）
- 课程分类入口（有氧 / 力量 / 瑜伽 / 拉伸）跳转课程列表并带初始筛选

#### 3. 课程（`ui/courses` + `data/repository/CourseRepository`）

- 课程网格、筛选（含**拉伸**分类、`FlowRow` 布局）、排序、下拉刷新
- 课程详情、「开始训练」进入播放页
- 数据经 **`CourseRepository`** 访问（底层仍为 `CourseService` 内存数据，便于替换为网络）

#### 4. 训练播放（`ui/training` + `ui/media`）

- 路由 `training/{courseId}`，**Media3** 播放课程视频或演示流（`DemoMedia`）
- **`TrainingPlayerViewModel`**：退出页面时将时长写入 **Room**（`training_sessions`）

#### 5. 视频模块（`ui/video`）

- **视频库**：缩略图（Coil）、点击进入 `video/player/{videoId}` 播放
- **视频上传**：选本地视频、读取字节、**Retrofit multipart** 上传演示接口、进度条（`ProgressRequestBody` + `VideoUploadViewModel`）
- **视频对比**：双路播放器 + 统一进度条与播放控制
- 视频库顶栏入口：上传、对比

#### 6. 数据统计（`ui/stats`）

- 对接 **`StatsService`** 演示数据：周柱状图、月折线示意、身体数据对比条、目标 **LinearProgressIndicator**
- **`StatsViewModel`**：展示本周 **Room** 累计训练分钟（与演示数据并列说明）

#### 7. 个人中心（`ui/profile`）

- 用户信息、身体数据、训练统计卡片、设置与其它入口（仍为本地演示数据，未接账号系统）

#### 8. AR 训练（`ui/ar`）

- 相机权限、**前置 CameraX 预览**
- **MediaPipe Pose Landmarker**（LIVE_STREAM）+ Compose **Canvas** 骨架/关节点叠加
- **`ARService`**：保留与 UI 分工说明及简单文案类接口（非算法核心）

#### 9. 数据与网络层（节选）

- `data/local`：`TrainingSessionEntity`、`TrainingSessionDao`、`DongJingDatabase`
- `data/repository`：`TrainingRepository`、`CourseRepository`、`VideoUploadRepository`
- `data/network`：`NetworkModule`、`VideoUploadApi`、`ProgressRequestBody`

---

### 后续建议开发模块

以下能力尚未实现或仅为演示/占位，可按产品优先级排期。

| 模块 | 说明 |
|------|------|
| **后端对接** | 课程 / 用户 / 视频列表与上传改为真实 API；替换 `httpbin.org` 与 `CourseService` 内存数据 |
| **账号与持久化** | 登录注册、Token、`User`/`UserStats` 入库或同步；个人中心设置项落地页 |
| **搜索** | 首页搜索栏接入课程/教练检索（本地或远程） |
| **视频压缩与分片** | 大文件上传前压缩、分片/断点续传（依赖服务端协议） |
| **视频下载与分享** | `VideoService` 中删除/列表与本地缓存、系统分享 |
| **统计深化** | 图表库（如 Vico）或更复杂趋势；`StatsService` 由 Room 聚合真实周/月数据；数据导出 |
| **AR 进阶** | 动作识别与纠错逻辑、训练计数/卡路里与课程联动；可选 **ARCore** 场景化 |
| **课程视频源** | 统一 CDN / 鉴权播放；详情页「开始训练」与版权策略 |
| **工程与发布** | `data_extraction_rules` 收紧、ProGuard（MediaPipe/CameraX）、自动化测试、性能与无障碍 |

---

### 项目结构（主要源码）

```
app/src/main/java/com/example/dongjingapp/
├── data/
│   ├── DemoMedia.kt
│   ├── local/              # Room：训练会话
│   ├── model/              # Course、User
│   ├── network/            # Retrofit 演示上传
│   ├── repository/         # Course / Training / VideoUpload
│   └── service/            # Course、User、Stats、Video、AR
├── ui/
│   ├── ar/                 # ARScreen（CameraX + MediaPipe）
│   ├── courses/
│   ├── home/
│   ├── media/              # Media3 封装
│   ├── navigation/
│   ├── profile/
│   ├── stats/              # StatsScreen、StatsCharts、StatsViewModel
│   ├── theme/
│   ├── training/           # TrainingPlayerScreen、ViewModel
│   └── video/
├── util/                   # 如 weekStartMillis
└── MainActivity.kt

app/src/main/assets/
└── pose_landmarker_lite.task   # MediaPipe 姿态模型（构建需存在）
```

---

### 开发规范（摘要）

- UI 以 Jetpack Compose + Material 3 为主；导航使用类型安全路由字符串时注意与 `BottomNavigationBar` 一致。
- 异步使用协程；Room 访问在后台线程或通过 Flow/`suspend`。
- **KSP 版本必须与 `libs.versions.toml` 中 Kotlin 版本匹配**（否则 Room 生成的 Dao 会与协程签名冲突）。
- 镜像图标优先使用 `Icons.AutoMirrored.Filled.*`（如返回、对比箭头），避免弃用告警。

---

### 构建说明

- 构建系统：Gradle Kotlin DSL，Android Gradle Plugin 与 Kotlin 以工程 `libs.versions.toml` / 根 `build.gradle.kts` 为准。
- `minSdk` 24，`targetSdk` 36（以 `app/build.gradle.kts` 为准）。
- 需要 **网络权限** 播放演示视频、上传演示接口；需要 **相机与存储/视频读权限** 用于 AR 与上传。

---

### 下一步可执行项（参考）

1. 对接业务后端与鉴权，替换演示 `NetworkModule` 与 `CourseService`。
2. 将训练/身体数据与统计报表完全打通 Room 与远程同步。
3. 完善 AR 动作业务逻辑与测试机型的 MediaPipe 兼容性验证。
4. 补充 UI/集成测试与发布前混淆、备份规则与安全审计。
