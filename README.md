# Bài tập Spring Security

- Sinh viên: **Lê Trọng Bảo**
- MSSV: **22110106**

## Nội dung

1. `example-01-email-login`: đăng nhập bằng email và hiển thị thông tin người dùng.
2. `example-02-custom-login`: đăng nhập bằng tên tài khoản hoặc email, dùng Thymeleaf Layout.
3. `example-03-security-app`: đăng ký, xác nhận email, quên mật khẩu, phân quyền và quản lý sản phẩm.
4. `example-04-jwt-nimbus`: đăng ký, đăng nhập và bảo vệ REST API bằng JWT của Nimbus.

## Chạy chương trình

Yêu cầu Java 21. Tại thư mục gốc, chạy:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd -pl example-04-jwt-nimbus spring-boot:run
```

Thay tên module để chạy ví dụ khác. Mỗi ví dụ dùng cơ sở dữ liệu H2 riêng để dễ kiểm tra.

Tài khoản mẫu:

| Ví dụ | Tài khoản | Mật khẩu |
|---|---|---|
| 1 | `admin@gocsach.vn` | `123456` |
| 2 | `admin` hoặc `admin@gocsach.vn` | `123456` |
| 3 | `admin` hoặc `admin@gocsach.vn` | `123456` |
| 4 | `admin@gocsach.vn` | `123456` |

Ở Ví dụ 3, mã xác nhận được in ở cửa sổ chạy chương trình khi `MAIL_ENABLED=false`. Đặt `MAIL_ENABLED=true` và cấu hình các biến `MAIL_*` để gửi email thật; không lưu mật khẩu trong source.

Ví dụ 4 chạy tại `http://localhost:8084`. Đăng nhập trên trang chủ để nhận JWT; trang hồ sơ gọi `/users/me` với Bearer token. Có thể đặt khóa riêng qua biến `JWT_SECRET` (tối thiểu 32 byte).
