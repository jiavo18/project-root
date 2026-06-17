# ============================================================
# 多阶段构建 Dockerfile — 用于 Render 免费部署
# ============================================================

# ---- 阶段1：构建 ----
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
# 先下载依赖（利用 Docker 缓存层）
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- 阶段2：运行 ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# 创建非 root 用户（安全最佳实践）
RUN addgroup --system app && adduser --system --group app
USER app

# 从构建阶段复制 jar 包
COPY --from=builder /app/target/*.jar app.jar

# Render 会自动设置 PORT 环境变量，默认 8080
EXPOSE 8080

# 启动命令：使用 Render 提供的 PORT，默认 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]
