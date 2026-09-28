# Bài tập 09 - Spring Security

- Sinh viên: **Lê Trọng Bảo**
- MSSV: **22110106**

## Nội dung

1. `example-01-email-login`: đăng nhập bằng email và hiển thị thông tin người dùng.
2. `example-02-custom-login`: đăng nhập bằng tên tài khoản hoặc email, dùng Thymeleaf Layout.
3. `example-03-security-app`: đăng ký, xác nhận email, quên mật khẩu, phân quyền và quản lý sản phẩm.

## Chạy chương trình

Yêu cầu Java 21. Tại thư mục gốc, chạy:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd -pl example-01-email-login spring-boot:run
```

Thay tên module để chạy ví dụ khác. Mỗi ví dụ dùng cơ sở dữ liệu H2 riêng để dễ kiểm tra.

Tài khoản mẫu:

| Ví dụ | Tài khoản | Mật khẩu |
|---|---|---|
| 1 | `admin@gocsach.vn` | `123456` |
| 2 | `admin` hoặc `admin@gocsach.vn` | `123456` |
| 3 | `admin` hoặc `admin@gocsach.vn` | `123456` |

Ở Ví dụ 3, mã xác nhận được in ở cửa sổ chạy chương trình. Có thể cấu hình SMTP thật bằng biến môi trường, không lưu mật khẩu trong source.
