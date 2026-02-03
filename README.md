<div align="center">

# 🎬 Movie Microservices Platform

### *Hệ thống quản lý phim theo kiến trúc Microservices*

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17-red.svg)](https://angular.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

---

### 📚 Dự án học thuật | 🎓 Academic Project

*Nghiên cứu và triển khai kiến trúc Microservices, DevOps practices và Cloud-native technologies*

[🚀 Quick Start](#-quick-start) • [📖 Documentation](#-tài-liệu) • [🏗️ Architecture](#️-kiến-trúc-hệ-thống) • [🤝 Contributing](CONTRIBUTING.md)

</div>

---

## 📋 Mục lục

- [� Giới thiệu](#-Dgiới-thiệu)
- [🎯 Mục tiêu học tập](#-mục-tiêu-học-tập)
- [🏗️ Kiến trúc hệ thống](#️-kiến-trúc-hệ-thống)
- [🛠️ Tech Stack](#️-tech-stack)
- [🧩 Microservices](#-microservices)
- [� Quick Start](#-quick-start)
- [📊 Monitoring](#-monitoring--observability)
- [� CI/CD Pipeline](#-cicd-pipeline)
- [🧪 Testing](#-testing)
- [📖 Tài liệu](#-tài-liệu)
- [🤝 Contributing](#-contributing)
- [� License](#-license)

---

## 📌 Giới thiệu

**Movie Microservices Platform** là một dự án học thuật nhằm nghiên cứu và áp dụng các công nghệ hiện đại trong phát triển phần mềm. Dự án tập trung vào việc xây dựng một hệ thống phân tán theo kiến trúc **Microservices**, với đầy đủ các thành phần của một ứng dụng production-ready.

### 🎓 Phạm vi học thuật

Dự án này được phát triển với mục đích:

- 📚 **Nghiên cứu kiến trúc**: Tìm hiểu và áp dụng Microservices architecture patterns
- 🔬 **Thực hành công nghệ**: Hands-on experience với Spring Boot, Angular, Docker, gRPC
- 🛠️ **DevOps practices**: CI/CD, containerization, monitoring, logging
- 🌐 **Distributed systems**: Inter-service communication, data consistency, fault tolerance
- 📊 **Observability**: Metrics, logging, tracing trong hệ thống phân tán

### ⚠️ Lưu ý quan trọng

> **Disclaimer**: Đây là dự án học thuật, không phải production system. Dự án không cung cấp chức năng streaming video thực tế mà tập trung vào kiến trúc và công nghệ backend/DevOps.

---

## 🎯 Mục tiêu học tập

### 🏛️ Kiến trúc & Design Patterns

-  **Microservices Architecture**: Service decomposition, bounded contexts
-  **API Gateway Pattern**: Centralized routing và load balancing
-  **Database per Service**: Polyglot persistence strategy
-  **Circuit Breaker Pattern**: Fault tolerance và resilience
-  **Event-Driven Architecture**: Asynchronous communication (future)

### 🔧 Backend Technologies

-  **Spring Boot 3.x**: Modern Java framework với Spring Cloud
-  **gRPC**: High-performance inter-service communication
-  **PostgreSQL**: Relational database với JPA/Hibernate
-  **Redis**: Caching, rate limiting, session management
-  **Spring Security + OAuth2**: Authentication và authorization

### 🎨 Frontend Development

-  **Angular 17**: Modern SPA framework với TypeScript
-  **RxJS**: Reactive programming patterns
-  **Lazy Loading**: Performance optimization
-  **Auth Guards**: Route protection và security

### 🐳 DevOps & Cloud-Native

-  **Docker & Docker Compose**: Containerization
-  **GitHub Actions**: CI/CD automation
-  **Prometheus & Grafana**: Metrics và visualization
-  **Loki & Promtail**: Centralized logging
-  **Health Checks**: Service monitoring và auto-recovery

### 🔮 Future Roadmap

-  **Kubernetes**: Container orchestration
-  **Service Mesh (Istio)**: Advanced traffic management
-  **Apache Kafka**: Event streaming platform
-  **Distributed Tracing (Jaeger)**: Request tracing
-  **API Documentation (Swagger)**: OpenAPI specification

---

## 🏗️ Kiến trúc hệ thống

### � System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         🌐 Client Layer                          │
│                     (Angular SPA - Port 4200)                    │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    🚪 API Gateway (Port 8080)                    │
│              Spring Cloud Gateway + Load Balancer               │
└──────────┬──────────────────────────┬────────────────────┬──────┘
           │                          │                    │
           ▼                          ▼                    ▼
┌──────────────────┐      ┌──────────────────┐   ┌──────────────────┐
│  🔐 Auth Service │      │ 💬 Comment Svc   │   │  🎬 Movie Svc    │
│   Port: 8081     │◄────►│   Port: 8083     │   │  (Future)        │
│   gRPC: 9081     │ gRPC │   gRPC: 9083     │   │                  │
└────────┬─────────┘      └────────┬─────────┘   └──────────────────┘
         │                         │
         ▼                         ▼
┌──────────────────┐      ┌──────────────────┐
│  🗄️ PostgreSQL   │      │  ⚡ Redis Cache   │
│   Port: 5432     │      │   Port: 6379     │
│  (User, Movies)  │      │ (Cache, Session) │
└──────────────────┘      └──────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    📊 Observability Stack                        │
│  Prometheus (9090) + Grafana (3000) + Loki (3100)              │
└─────────────────────────────────────────────────────────────────┘
```

### 🎯 Architecture Patterns

| Pattern | Implementation | Purpose |
|---------|---------------|---------|
| 🏛️ **Microservices** | Service decomposition | Scalability, maintainability |
| 🚪 **API Gateway** | Spring Cloud Gateway | Single entry point, routing |
| 🗄️ **Database per Service** | PostgreSQL instances | Data isolation, autonomy |
| ⚡ **Caching** | Redis | Performance optimization |
| 🔒 **JWT + OAuth2** | Spring Security | Authentication & authorization |
| 📡 **gRPC** | Protocol Buffers | Inter-service communication |
| 📊 **Observability** | Prometheus + Grafana | Monitoring & alerting |
| 🐳 **Containerization** | Docker Compose | Deployment consistency |

---

## 🛠️ Tech Stack

<table>
<tr>
<td width="50%" valign="top">

### 🔙 Backend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| ☕ **Java** | 17 | Programming language |
| 🍃 **Spring Boot** | 3.2.1 | Application framework |
| 🌥️ **Spring Cloud Gateway** | 2023.0.0 | API Gateway |
| 🔐 **Spring Security** | 6.x | Security framework |
| � **OAuth2 Client** | - | Google authentication |
| 📡 **gRPC** | 1.60.0 | RPC framework |
| 🗄️ **PostgreSQL** | 15 | Relational database |
| ⚡ **Redis** | 7 | In-memory cache |
| � **Lombok** | - | Boilerplate reduction |
| 📊 **Micrometer** | - | Metrics collection |

</td>
<td width="50%" valign="top">

### 🎨 Frontend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| 🅰️ **Angular** | 17 | SPA framework |
| 📘 **TypeScript** | 5.2 | Type-safe JavaScript |
| 🎨 **SCSS** | - | Styling |
| 🔄 **RxJS** | 7.8 | Reactive programming |
| 🛣️ **Angular Router** | 17 | Client-side routing |
| 🔒 **Auth Guards** | - | Route protection |

</td>
</tr>
<tr>
<td width="50%" valign="top">

### 🐳 DevOps & Infrastructure

| Technology | Version | Purpose |
|------------|---------|---------|
| 🐳 **Docker** | Latest | Containerization |
| 🐙 **Docker Compose** | v3.8 | Multi-container orchestration |
| 🔄 **GitHub Actions** | - | CI/CD automation |
| 🐋 **Docker Hub** | - | Container registry |

</td>
<td width="50%" valign="top">

### 📊 Monitoring & Observability

| Technology | Version | Purpose |
|------------|---------|---------|
| 📈 **Prometheus** | Latest | Metrics collection |
| 📊 **Grafana** | Latest | Visualization |
| 📝 **Loki** | 2.9.0 | Log aggregation |
| 📤 **Promtail** | 2.9.0 | Log shipping |
| 🔍 **Redis Insight** | Latest | Redis GUI |

</td>
</tr>
</table>

---

## 🧩 Microservices

### 🔐 Auth Service

<table>
<tr>
<td width="30%"><strong>📍 Endpoints</strong></td>
<td width="70%">HTTP: <code>8081</code> | gRPC: <code>9081</code></td>
</tr>
<tr>
<td><strong>🗄️ Database</strong></td>
<td>PostgreSQL</td>
</tr>
<tr>
<td><strong>⚡ Cache</strong></td>
<td>Redis (JWT blacklist, sessions)</td>
</tr>
</table>

**🎯 Responsibilities:**

- 🔑 **Authentication**: Google OAuth2 integration
- 🎫 **JWT Management**: Token generation, validation, blacklisting
- 👤 **User Management**: User profile CRUD operations
- 🔒 **Authorization**: Role-based access control
- 📡 **gRPC Services**: `VerifyToken`, `Logout`, `IsTokenBlacklisted`, `GetUserInfo`

**🔧 Key Features:**

-  JWT token blacklist với Redis (logout functionality)
-  OAuth2 social login (Google)
-  Secure password hashing
-  Session management
-  Health checks và metrics

---

### 💬 Comment Service

<table>
<tr>
<td width="30%"><strong>📍 Endpoints</strong></td>
<td width="70%">HTTP: <code>8083</code> | gRPC: <code>9083</code></td>
</tr>
<tr>
<td><strong>🗄️ Database</strong></td>
<td>PostgreSQL</td>
</tr>
<tr>
<td><strong>⚡ Cache</strong></td>
<td>Redis (rate limiting)</td>
</tr>
</table>

**🎯 Responsibilities:**

- 💬 **Comment Management**: CRUD operations for movie comments
- 🚦 **Rate Limiting**: Anti-spam protection với Redis
- 👥 **User Comments**: Per-user comment history
- 🎬 **Movie Comments**: Comments aggregation by movie
- 📡 **gRPC Services**: `GetCommentsByMovie`, `CreateComment`, `UpdateComment`, `DeleteComment`

**🔧 Key Features:**

-  Rate limiting (max comments per time window)
-  Comment validation và sanitization
-  Pagination support
-  User authentication integration
-  Real-time comment updates (future: WebSocket)

---

### 🌐 API Gateway

<table>
<tr>
<td width="30%"><strong>📍 Port</strong></td>
<td width="70%"><code>8080</code></td>
</tr>
<tr>
<td><strong>🔧 Framework</strong></td>
<td>Spring Cloud Gateway</td>
</tr>
</table>

**🎯 Responsibilities:**

- 🚪 **Routing**: Request routing to appropriate microservices
- 🔀 **Load Balancing**: Distribute traffic across service instances
- 🛡️ **CORS**: Cross-origin resource sharing configuration
- 🔐 **Security**: Centralized authentication checks
- 📊 **Monitoring**: Request/response logging và metrics

**🔧 Key Features:**

-  Dynamic routing configuration
-  Circuit breaker pattern (future)
-  Request/response transformation
-  Rate limiting at gateway level (future)

---

### 🎨 Frontend Application

<table>
<tr>
<td width="30%"><strong>📍 Port</strong></td>
<td width="70%"><code>4200</code> (dev) | <code>80</code> (prod)</td>
</tr>
<tr>
<td><strong>🔧 Framework</strong></td>
<td>Angular 17 + TypeScript</td>
</tr>
</table>

**🎯 Features:**

- 🎨 **Modern UI**: Responsive design với SCSS
- 🔐 **Authentication**: Google OAuth2 login flow
- 🛣️ **Routing**: Client-side routing với lazy loading
- 🔒 **Guards**: Route protection based on auth status
- 📱 **Responsive**: Mobile-first design approach
- ⚡ **Performance**: Code splitting và lazy loading

---

## ⚡ Redis Usage Patterns

Redis đóng vai trò quan trọng trong hệ thống với nhiều use cases:

### 🔒 JWT Blacklist

```
Key Pattern: jwt:blacklist:{token}
TTL: Token expiration time
Purpose: Invalidate tokens on logout
```

**Flow:**
1. User logout → Token added to blacklist
2. Subsequent requests → Check blacklist first
3. Token expires → Auto-removed from Redis

### 🚦 Rate Limiting

```
Key Pattern: rate:limit:{userId}:{endpoint}
TTL: Time window (e.g., 60 seconds)
Purpose: Prevent spam và abuse
```

**Configuration:**
- Comments: 5 requests per minute per user
- Configurable per endpoint

### 💾 Caching Strategy

```
Key Pattern: cache:{entity}:{id}
TTL: Configurable (default: 1 hour)
Purpose: Reduce database load
```

**Cached Data:**
- Movie information
- User profiles
- Popular content

---

## 📊 Monitoring & Observability

### 📈 Stack
- **Prometheus** (Port 9090): Thu thập metrics
- **Grafana** (Port 3000): Visualization dashboards
- **Loki** (Port 3100): Log aggregation
- **Promtail**: Log collection từ containers

### 🎯 Metrics được theo dõi
- HTTP request rate & latency
- JVM metrics (heap, threads, GC)
- Database connection pool
- Redis operations
- gRPC call statistics

---

## 🚀 Quick Start

### 📋 Yêu cầu hệ thống

- Docker Desktop (Windows/Mac) hoặc Docker Engine (Linux)
- Git
- 8GB RAM trở lên
- 10GB disk space

### 🔧 Cài đặt và chạy

**1. Clone repository:**
```bash
git clone https://github.com/haivoDA22TTD/movie-microservices.git
cd movie-microservices
```

**2. Cấu hình environment variables:**

Sửa file `.env` với thông tin của bạn:
```env
# Google OAuth2 (Bắt buộc)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GOOGLE_REDIRECT_URI=http://localhost:8081/login/oauth2/code/google

# JWT Secret (Bắt buộc - generate một secret mạnh)
JWT_SECRET=your_jwt_secret_here

# Database (Có thể giữ mặc định)
POSTGRES_PASSWORD=postgres

# TMDB API (Optional - để xem thông tin phim)
TMDB_API_KEY=your_tmdb_api_key
```

**3. Khởi động toàn bộ hệ thống:**
```bash
docker-compose up -d
```

**4. Kiểm tra services đã chạy:**
```bash
docker-compose ps
```

Tất cả services phải có status `healthy` hoặc `running`.

**5. Truy cập ứng dụng:**

| Service | URL | Credentials |
|---------|-----|-------------|
| Frontend | http://localhost:4200 | Login with Google |
| API Gateway | http://localhost:8080 | - |
| Auth Service | http://localhost:8081 | - |
| Comment Service | http://localhost:8083 | - |
| Grafana | http://localhost:3000 | admin / admin |
| Prometheus | http://localhost:9090 | - |
| Redis Insight | http://localhost:5540 | - |

---

## 🧪 Testing

### Test toàn bộ hệ thống:
```bash
test-all.bat
```

### Test JWT Blacklist:
```bash
test-jwt-blacklist-simple.bat
```

### Test gRPC Health:
```bash
test-grpc-health.bat
```

### Test APIs:
```bash
test-apis.bat
```

---

## 🔄 CI/CD Pipeline

### 📦 CI Workflow (`.github/workflows/ci.yml`)

**Trigger:** Push hoặc Pull Request vào `main` hoặc `develop`

**Jobs:**
1. **Test Backend Services** - Chạy unit tests cho tất cả services
2. **Build Backend Services** - Build JAR files
3. **Build Frontend** - Build Angular production
4. **Docker Build Test** - Test Docker image builds

### 🚀 CD Workflow (`.github/workflows/cd.yml`)

**Trigger:** Push vào `main` hoặc tạo tag `v*`

**Jobs:**
1. **Build and Push Docker Images** - Build và push lên Docker Hub
2. **Deploy to VPS** (Optional) - Deploy lên VPS nếu có

---

## ⚙️ Setup CI/CD

### 1. Fork/Clone repository về GitHub của bạn

### 2. Tạo Docker Hub account (miễn phí)
- Đăng ký tại: https://hub.docker.com

### 3. Thêm GitHub Secrets

Vào **Settings → Secrets and variables → Actions → New repository secret**

**Bắt buộc:**
```
DOCKER_USERNAME=your_docker_hub_username
DOCKER_PASSWORD=your_docker_hub_password_or_token
```

**Optional (chỉ khi có VPS):**
```
VPS_HOST=your_vps_ip
VPS_USERNAME=your_vps_username
VPS_SSH_KEY=your_private_ssh_key
```

### 4. Enable VPS Deploy (Optional)

Nếu bạn có VPS và muốn auto-deploy:

Vào **Settings → Secrets and variables → Actions → Variables → New repository variable**
```
Name: ENABLE_VPS_DEPLOY
Value: true
```

**Nếu không có VPS:** Không cần làm gì, CI/CD vẫn chạy bình thường và skip phần deploy.

### 5. Push code lên GitHub

```bash
git add .
git commit -m "Setup CI/CD"
git push origin main
```

CI/CD sẽ tự động chạy! Xem kết quả tại tab **Actions** trên GitHub.

---

## 🐳 Docker Images

Sau khi CI/CD chạy thành công, images sẽ được push lên Docker Hub:

```
your_username/movie-auth-service:latest
your_username/movie-comment-service:latest
your_username/movie-api-gateway:latest
your_username/movie-frontend:latest
```

### Chạy từ Docker Hub (Production):

```bash
# Sửa DOCKER_USERNAME trong .env
DOCKER_USERNAME=your_docker_hub_username

# Chạy production compose
docker-compose -f docker-compose.prod.yml up -d
```

---

## 📁 Cấu trúc dự án

```
movie-microservices/
├── .github/
│   └── workflows/
│       ├── ci.yml              # CI pipeline
│       └── cd.yml              # CD pipeline
├── backend/
│   ├── api-gateway/            # Spring Cloud Gateway
│   ├── auth-service/           # Authentication service
│   └── comment-service/        # Comment service
├── frontend/                   # Angular application
├── monitoring/
│   ├── grafana/               # Grafana dashboards
│   ├── loki/                  # Loki config
│   ├── prometheus/            # Prometheus config
│   └── promtail/              # Promtail config
├── docker-compose.yml         # Local development
├── docker-compose.prod.yml    # Production (Docker Hub images)
├── .env                       # Environment variables
└── README.md
```

---

## 🔐 Bảo mật

- 🔑 **JWT Authentication** với Google OAuth2
- 🔒 **JWT Blacklist** với Redis khi logout
- 🚦 **Rate Limiting** chống spam (Redis)
- 🛡️ **CORS Configuration** chặn unauthorized origins
- 🔐 **Spring Security** bảo vệ endpoints
- 🚪 **API Gateway** làm single entry point

---

## 📊 Monitoring Dashboard

Sau khi hệ thống chạy, truy cập Grafana:

1. Mở http://localhost:3000
2. Login: `admin` / `admin`
3. Vào **Dashboards → Movie Microservices**

**Metrics hiển thị:**
- Request rate & latency per service
- JVM memory & CPU usage
- Database connection pool
- Redis operations
- gRPC call statistics
- Error rates

---

## 🐛 Troubleshooting

### Services không healthy?

```bash
# Xem logs
docker-compose logs auth-service
docker-compose logs comment-service

# Restart services
docker-compose restart auth-service
```

### Port đã được sử dụng?

Sửa ports trong `docker-compose.yml`:
```yaml
ports:
  - "8081:8081"  # Đổi port bên trái, VD: "8091:8081"
```

### Redis không kết nối được?

```bash
# Kiểm tra Redis
docker-compose logs redis

# Test Redis connection
docker exec -it movie-redis redis-cli ping
# Phải trả về: PONG
```

### JWT Blacklist không hoạt động?

```bash
# Kiểm tra Redis keys
docker exec -it movie-redis redis-cli
> KEYS jwt:blacklist:*

# Nếu không có keys sau logout, check logs
docker-compose logs auth-service
```

---

## ☁️ Định hướng phát triển

- [ ] Triển khai **Kubernetes (K8s)**
- [ ] **Service Mesh** với Istio
- [ ] **Event-Driven Architecture** với Kafka
- [ ] **Distributed Tracing** với Jaeger/Zipkin
- [ ] **API Documentation** với Swagger/OpenAPI
- [ ] **E2E Testing** với Playwright
- [ ] **Load Testing** với K6
- [ ] **Blue-Green Deployment**

---

## 📚 API Documentation

### Auth Service APIs

**POST** `/logout`
```bash
curl -X POST http://localhost:8081/logout \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**GET** `/verify-token`
```bash
curl -X GET http://localhost:8081/verify-token \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Comment Service APIs

**GET** `/api/comments/movie/{movieId}`
```bash
curl http://localhost:8083/api/comments/movie/123
```

**POST** `/api/comments`
```bash
curl -X POST http://localhost:8083/api/comments \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"movieId": 123, "content": "Great movie!"}'
```

---

## 🧠 Lưu ý học thuật

⚠️ Dự án được xây dựng **chỉ nhằm mục đích học tập và nghiên cứu**:
- Không cung cấp chức năng streaming video thực tế
- Không dùng cho mục đích thương mại
- Đang trong giai đoạn phát triển và cải thiện

---

## 👨‍💻 Tác giả

**Tên:** Võ Chí Hải (haivoDev)

**GitHub:** [haivoDA22TTD]

**Email:** [110122068@st.tvu.edu.vn]

---

## 📄 License

Dự án này được phát hành dưới giấy phép MIT. Xem file [LICENSE](LICENSE) để biết thêm chi tiết.

---

## ⭐ Đóng góp

Mọi đóng góp đều được chào đón! Vui lòng:

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

---

## 🙏 Lời cảm ơn

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Angular](https://angular.io/)
- [Docker](https://www.docker.com/)
- [Redis](https://redis.io/)
- [PostgreSQL](https://www.postgresql.org/)
- [gRPC](https://grpc.io/)
- [Prometheus](https://prometheus.io/)
- [Grafana](https://grafana.com/)
- [TMDB API](https://www.themoviedb.org/documentation/api)

---

## 📞 Liên hệ

Nếu bạn có câu hỏi hoặc gặp vấn đề, vui lòng:
- Mở [Issue](https://github.com/haivoDA22TTD/movie-microservices/issues)
- Hoặc liên hệ qua email: [110122068@st.tvu.edu.vn]

---

**⭐ Nếu dự án hữu ích, đừng quên cho một star nhé! ⭐**
