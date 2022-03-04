## 项目简介

- **该项目是支付系统，提供了`微信支付: WXPAY_NATIVE` 和 `支付宝支付: ALIPAY_PC` 两种支付方式，提供接口供调用**

- 技术栈：JDK + SpringBoot + Redis + MySQL + RabbitMQ + MyBatis

<hr/>

### 使用说明：

- 在使用前请先安装好 `RabbitMQ3.8.2` 、 `JDK1.8` 、 `MySQL5.7` 并启动
- 使用 IDEA 进行开发
- 创建数据库 `pay`，导入 `sql/pay.sql` 数据库文件
- 项目启动前请先创建一个队列： `payNotify`
- 接口格式为 `GET` 请求：`http://127.0.0.1:8081/pay/create?payType=(ALIPAY_PC/WXPAY_NATIVE)&orderId=xxxxx&amount=xxxxx`
    - 可选参数：
        1. orderName: 订单名
        2. userId: 支付用户ID