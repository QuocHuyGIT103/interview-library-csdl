Hệ thống Quản lý Thư viện (JDBC, JPA và ACID)
Dự án này thể hiện một Hệ thống Quản lý Thư viện sử dụng MySQL, JDBC và JPA (Hibernate), với trọng tâm là các tính chất giao dịch ACID. Dự án trình bày các khái niệm cơ sở dữ liệu, ánh xạ đối tượng-quan hệ (ORM) và quản lý phụ thuộc bằng Maven.
Tính năng

Quản lý sách, thành viên và hồ sơ mượn sách trong cơ sở dữ liệu MySQL.
Thực hiện các thao tác CRUD bằng JDBC và JPA.
Minh họa các tính chất ACID (Nguyên tử, Nhất quán, Cô lập, Bền vững) thông qua quản lý giao dịch.
Sử dụng Maven để quản lý phụ thuộc.

Các tính chất ACID được minh họa

Nguyên tử (Atomicity): Đảm bảo các giao dịch mượn sách (cập nhật trạng thái sách và thêm hồ sơ mượn) được hoàn thành toàn bộ hoặc bị hủy bỏ (rollback).
Nhất quán (Consistency): Áp dụng các ràng buộc như khóa ngoại và ISBN duy nhất, xử lý lỗi trùng lặp và kiểm tra tính sẵn có.
Cô lập (Isolation): Sử dụng mức cô lập REPEATABLE READ của MySQL để xử lý các giao dịch đồng thời.
Bền vững (Durability): Dựa vào InnoDB để lưu trữ dữ liệu vĩnh viễn sau khi commit.

Yêu cầu

MySQL Server và MySQL Workbench.
Java JDK 11 hoặc cao hơn.
Maven 3.6.0 hoặc cao hơn.

Thiết lập

Cài đặt MySQL và tạo cơ sở dữ liệu:CREATE DATABASE LibraryDB;

Chạy các script SQL trong thư mục sql/ để tạo bảng và chèn dữ liệu:mysql -u root -p LibraryDB < sql/create_tables.sql
mysql -u root -p LibraryDB < sql/insert_data.sql

Cập nhật thông tin đăng nhập cơ sở dữ liệu:
Với JDBC: Cập nhật file src/main/java/com/example/library/LibraryJDBCDemo.java.
Với JPA: Cập nhật file src/main/resources/META-INF/persistence.xml.

Biên dịch và chạy bằng Maven:mvn clean install
mvn exec:java -Dexec.mainClass="com.example.library.LibraryJDBCDemo"
mvn exec:java -Dexec.mainClass="com.example.library.LibraryJPADemo"

Cấu trúc dự án
LibraryDBJDBCJPADemo/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── com/
│ │ │ │ ├── example/
│ │ │ │ │ ├── library/
│ │ │ │ │ │ ├── LibraryJDBCDemo.java
│ │ │ │ │ │ ├── Book.java
│ │ │ │ │ │ ├── BorrowRecord.java
│ │ │ │ │ │ ├── LibraryJPADemo.java
│ │ ├── resources/
│ │ │ ├── META-INF/
│ │ │ │ ├── persistence.xml
├── sql/
│ ├── create_tables.sql
│ ├── insert_data.sql
├── pom.xml
├── README.md
├── .gitignore

Tác giả

[Vũ Quốc Huy] (https://github.com/QuocHuyGIT103)

Dự án này được tạo để thể hiện kỹ năng về JDBC, JPA và các tính chất ACID cho một buổi phỏng vấn thực tập Java.
