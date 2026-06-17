package com.example.demo.service;

import com.example.demo.dto.StatsResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 用户管理服务
 * 处理用户的增删改查和数据统计业务逻辑
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 分页查询用户列表
     *
     * @param page 页码（从 0 开始）
     * @param size 每页数量
     * @return 分页的用户数据
     */
    public Page<UserResponse> getUsers(int page, int size) {
        // 按创建时间倒序排列（最新注册的排前面）
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(pageRequest).map(UserResponse::fromEntity);
    }

    /**
     * 根据 ID 查询单个用户
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + id));
        return UserResponse.fromEntity(user);
    }

    /**
     * 更新用户信息（邮箱和角色）
     *
     * @param id    用户 ID
     * @param email 新邮箱
     * @param role  新角色
     * @return 更新后的用户信息
     */
    public UserResponse updateUser(Long id, String email, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + id));

        // 更新邮箱（如果提供了新邮箱）
        if (email != null && !email.isBlank()) {
            // 检查新邮箱是否已被其他用户使用
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("该邮箱已被其他用户使用");
            }
            user.setEmail(email);
        }

        // 更新角色（如果提供了新角色）
        if (role != null && !role.isBlank()) {
            user.setRole(role);
        }

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    /**
     * 删除用户（需要管理员权限）
     *
     * @param id 用户 ID
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * 初始化演示数据
     * 如果数据库中没有用户，则自动创建管理员和几个测试用户
     */
    public void initDemoData() {
        if (userRepository.count() == 0) {
            // 创建管理员账户
            User admin = User.builder()
                    .username("admin")
                    .password("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy")  // password: admin123
                    .email("admin@example.com")
                    .role("ROLE_ADMIN")
                    .enabled(true)
                    .createdAt(LocalDateTime.now().minusDays(7))  // 模拟一周前创建
                    .build();
            userRepository.save(admin);

            // 创建普通测试用户（模拟不同时间注册，用于展示数据看板趋势图）
            String[] usernames = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
            String defaultPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"; // admin123

            for (int i = 0; i < usernames.length; i++) {
                // 设置不同的注册时间（过去7天内）
                int daysAgo = new Random().nextInt(7); // 随机 0-6 天前
                User user = User.builder()
                        .username(usernames[i])
                        .password(defaultPassword)
                        .email(usernames[i] + "@example.com")
                        .role("ROLE_USER")
                        .enabled(true)
                        .createdAt(LocalDateTime.now().minusDays(daysAgo).minusHours(new Random().nextInt(24)))
                        .build();
                userRepository.save(user);
            }

            System.out.println(">>> 演示数据已初始化！管理员账号: admin / admin123");
        }
    }

    /**
     * 获取数据统计信息
     * - 用户总数
     * - 今日新增
     * - 近7天注册趋势
     */
    public StatsResponse getStats() {
        // 1. 用户总数
        long totalUsers = userRepository.count();

        // 2. 今日新增用户数（从今天 00:00:00 开始计算）
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        long todayNewUsers = userRepository.countByCreatedAtAfter(todayStart);

        // 3. 近7天注册趋势
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Object[]> rawData = userRepository.countDailyRegistrations(sevenDaysAgo);

        // 构建完整7天的数据（没有注册的日子补0）
        List<Map<String, Object>> weeklyTrend = new ArrayList<>();
        Map<String, Long> countMap = new LinkedHashMap<>();

        // 将查询结果转为 Map（日期 -> 数量）
        for (Object[] row : rawData) {
            countMap.put(row[0].toString(), (Long) row[1]);
        }

        // 补全7天的数据
        for (int i = 6; i >= 0; i--) {
            String date = LocalDate.now().minusDays(i).toString();
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date);
            dayData.put("count", countMap.getOrDefault(date, 0L));
            weeklyTrend.add(dayData);
        }

        return StatsResponse.builder()
                .totalUsers(totalUsers)
                .todayNewUsers(todayNewUsers)
                .weeklyTrend(weeklyTrend)
                .build();
    }
}
