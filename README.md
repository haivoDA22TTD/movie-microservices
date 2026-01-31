# 🎬 Hệ Thống Web Xem Phim – Kiến Trúc Microservices

> Dự án cá nhân mang tính **học thuật**, nhằm nghiên cứu và áp dụng **kiến trúc Microservices**, các công nghệ backend, frontend và DevOps hiện đại thông qua việc xây dựng một hệ thống web xem phim.

---

## 📌 Giới thiệu dự án

**Movie Streaming Microservices** là một hệ thống web được xây dựng theo kiến trúc **Microservices**, tập trung vào khả năng mở rộng, dễ bảo trì và triển khai thực tế.

Dự án **không tập trung vào việc streaming video thực tế**, mà chú trọng vào:
- Thiết kế kiến trúc hệ thống
- Phân tách service theo nghiệp vụ
- Áp dụng các công nghệ hiện đại trong phát triển phần mềm

---

## 🎯 Mục tiêu dự án

- 🔹 Áp dụng kiến trúc **Microservices** vào hệ thống thực tế  
- 🔹 Sử dụng **đa dạng cơ sở dữ liệu (Polyglot Persistence)**  
- 🔹 Áp dụng **Redis** cho cache, rate limiting và bảo mật  
- 🔹 Triển khai **Distributed Tracing & Monitoring**  
- 🔹 Xây dựng **CI/CD pipeline** với GitHub Actions  
- 🔹 Container hóa ứng dụng bằng **Docker** và deploy lên **VPS**  
- 🔹 Định hướng mở rộng sang **Kubernetes (K8s)** trong tương lai  

---

## 🏗️ Kiến trúc hệ thống

### 🔧 Phong cách kiến trúc
- Kiến trúc Microservices
- RESTful API
- API Gateway
- Triển khai bằng container

### 🧩 Sơ đồ kiến trúc tổng thể (mô tả)


---

## 🧱 Thiết kế các Microservices

### 👤 User Service
- Đăng ký, đăng nhập người dùng
- Xác thực và phân quyền (JWT)
- Quản lý thông tin người dùng
- Blacklist token bằng Redis

📦 **Cơ sở dữ liệu:** MongoDB  
📌 **Lý do lựa chọn:** Dữ liệu linh hoạt, dễ mở rộng schema

---

### 🎞️ Movie Service
- Quản lý thông tin phim
- Tích hợp API từ **TMDB**
- Tìm kiếm, lọc và phân trang phim

📦 **Cơ sở dữ liệu:** PostgreSQL  
📌 **Lý do lựa chọn:** Hỗ trợ tốt cho truy vấn phức tạp và dữ liệu quan hệ

---

### ⭐ Favorite / History Service
- Quản lý danh sách phim yêu thích
- Lưu lịch sử xem phim của người dùng

📦 **Cơ sở dữ liệu:** MySQL  
📌 **Lý do lựa chọn:** Đơn giản, ổn định, phù hợp dữ liệu quan hệ cơ bản

---

## 🗄️ Chiến lược quản lý dữ liệu

### 🔁 Polyglot Persistence
Mỗi microservice sở hữu **cơ sở dữ liệu riêng**, giúp:
- Giảm phụ thuộc giữa các service
- Dễ mở rộng và bảo trì
- Tăng tính ổn định cho hệ thống

| Service | Cơ sở dữ liệu |
|------|--------------|
| User Service | MongoDB |
| Movie Service | PostgreSQL |
| Favorite Service | MySQL |

---

## ⚡ Redis trong hệ thống

Redis được sử dụng cho các mục đích sau:

- 🚀 **Cache dữ liệu** phim thường xuyên truy cập
- 🚦 **Giới hạn tần suất truy cập (Rate Limiting)**
- 🔒 **Blacklist JWT Token** khi người dùng đăng xuất
- ♻️ **Redis Sentinel** đảm bảo tính sẵn sàng cao (High Availability)

---

## 📊 Giám sát & Distributed Tracing

### 📈 Công cụ sử dụng
- Grafana  
- (Có thể mở rộng: Prometheus, Tempo)

### 🎯 Mục đích
- Theo dõi luồng request qua nhiều service
- Phát hiện điểm nghẽn hiệu năng
- Giám sát trạng thái hệ thống theo thời gian thực

---

## 🖥️ Frontend

### 🧰 Công nghệ sử dụng
- Angular
- SCSS
- PrimeNG

### ✨ Đặc điểm
- Single Page Application (SPA)
- Kiến trúc module rõ ràng
- Lazy Loading
- Auth Guard bảo vệ route
- Giao diện thân thiện người dùng

---

## 🔐 Bảo mật hệ thống

- 🔑 Xác thực bằng JWT
- 👮 Phân quyền người dùng (USER / ADMIN)
- 🚦 Rate Limiting chống spam
- 🔒 Blacklist token với Redis
- 🛡️ Kiểm soát truy cập qua API Gateway

---

## 🔄 CI/CD Pipeline

### ⚙️ Công cụ
- GitHub Actions
- Docker
- Docker Hub
- VPS

### 🔁 Quy trình
1. Build các microservice backend
2. Build frontend Angular
3. Build Docker Image
4. Push Image lên Docker Hub
5. Deploy ứng dụng lên VPS bằng Docker Compose

---

## 🐳 Container hóa & Triển khai

- Mỗi service chạy trong một container độc lập
- Database và Redis được container hóa
- Triển khai bằng Docker Compose
- Sẵn sàng chuyển sang Kubernetes

---

## ☁️ Định hướng phát triển tương lai

- 🚀 Triển khai Kubernetes (K8s)
- 📦 Sử dụng Helm
- 📈 Tự động scale service
- 🕸️ Service Mesh (Istio)
- ⚙️ Quản lý cấu hình tập trung

---

## 📚 API bên thứ ba

- 🎬 **TMDB (The Movie Database API)**  
  Sử dụng để lấy dữ liệu phim (tên phim, poster, thể loại, mô tả, …)  
  Phục vụ mục đích học tập, không thương mại.

---


---

## 🧠 Lưu ý học thuật

⚠️ Dự án được xây dựng **chỉ nhằm mục đích học tập và nghiên cứu**,  
không cung cấp chức năng streaming video thực tế và không dùng cho mục đích thương mại,
hiện tại đang trong giai đoạn phát triển.

---

## 👨‍💻 Tác giả

 **Tên:** Võ Chí Hải(haivoDev)

---

## ⭐ Lời cảm ơn

- Spring Boot
- Angular
- Docker
- Redis
- TMDB API
- Cộng đồng mã nguồn mở ❤️

---


