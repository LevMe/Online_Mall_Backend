# **在线商城后端API接口文档 V1.2**

## **1\. 通用约定**

### **1.1 Base URL**

所有API的请求根路径统一为：/api/v1

### **1.2 数据格式**

所有请求体 (Request Body) 和响应体 (Response Body) 的数据格式均为 application/json; charset=utf-8。

### **1.3 统一响应体格式**

所有API成功或业务失败的响应都将包裹在以下结构中：

{  
"code": 200, // 业务状态码  
"message": "操作描述",  
"data": {  
// 具体的业务数据  
}  
}

### **1.4 认证 (Authorization)**

需要登录才能访问的接口，必须在请求头 (Header) 中携带登录时获取的 Token。  
格式: Authorization: Bearer \<你的TOKEN\>

## **2\. 用户与认证模块**

### **2.1 用户注册**

* **功能**: 创建一个新用户账户。
* **路径**: POST /users/register
* **请求体**:  
  {  
  "username": "someUser",  
  "password": "password123",  
  "email": "user@example.com"  
  }

* **成功响应示例 (201 Created)**:  
  {  
  "code": 201,  
  "message": "注册成功",  
  "data": {  
  "userId": "1",  
  "username": "someUser"  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/users/register" \-Method Post \-Body '{"username":"newUser","password":"password123","email":"new@example.com"}' \-ContentType "application/json"

### **2.2 用户登录**

* **功能**: 用户登录以获取认证 Token。
* **路径**: POST /auth/login
* **请求体**:  
  {  
  "username": "someUser",  
  "password": "password123"  
  }

* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "登录成功",  
  "data": {  
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0VXNlciIsInN1YiI6InRlc3RVc2VyIiwiaWF0IjoxNzI0NzM5NTk0LCJleHAiOjE3MjUzNDQzOTR9.xxxxxxxx",  
  "userInfo": {  
  "userId": "1",  
  "username": "testUser",  
  "avatarUrl": "https://placehold.co/100x100/EEE/31343C?text=Avatar"  
  }  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/auth/login" \-Method Post \-Body '{"username":"testUser","password":"password123"}' \-ContentType "application/json"

### **2.3 获取当前登录用户信息**

* **功能**: 获取当前已登录用户的详细信息。
* **认证**: 需要 Token。
* **路径**: GET /users/me
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "获取成功",  
  "data": {  
  "userId": "1",  
  "username": "testUser",  
  "email": "test@example.com",  
  "avatarUrl": "https://placehold.co/100x100/EEE/31343C?text=Avatar",  
  "createdAt": "2025-08-27T08:30:00Z"  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/users/me" \-Method Get \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的TOKEN\>" }

## **3\. 商品与分类模块**

### **3.1 获取商品分类列表**

* **功能**: 获取所有的商品分类。
* **路径**: GET /categories
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "获取成功",  
  "data": \[  
  { "id": 1, "name": "电子产品", "parentId": 0, ... },  
  { "id": 2, "name": "图书音像", "parentId": 0, ... }  
  \]  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/categories" \-Method Get

### **3.2 获取商品列表**

* **功能**: 根据多种条件分页获取商品列表。
* **路径**: GET /products
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "获取成功",  
  "data": {  
  "total": 128,  
  "page": 1,  
  "pageSize": 10,  
  "items": \[  
  {  
  "id": 101,  
  "name": "智能手机 Pro",  
  "price": 4999.00,  
  "mainImageUrl": "https://path/to/image.jpg"  
  }  
  \]  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/products" \-Method Get

### **3.3 获取商品详情**

* **功能**: 根据商品ID获取单个商品的详细信息。
* **路径**: GET /products/{productId}
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "获取成功",  
  "data": {  
  "id": 101,  
  "name": "智能手机 Pro",  
  "price": 4999.00,  
  "stock": 99,  
  "description": "一款性能卓越的智能手机...",  
  "imageUrls": \["https://path/to/image1.jpg"\],  
  "specs": { "CPU": "SuperChip A18" }  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/products/101" \-Method Get

## **4\. 购物车模块 (所有接口均需认证)**

### **4.1 添加商品到购物车**

* **功能**: 将指定商品添加到当前用户的购物车。
* **路径**: POST /cart/items
* **成功响应示例 (201 Created)**:  
  {  
  "code": 201,  
  "message": "添加成功",  
  "data": null  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/cart/items" \-Method Post \-Body '{"productId":101, "quantity":1}' \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的TOKEN\>" } \-ContentType "application/json"

### **4.2 查看购物车**

* **功能**: 获取当前用户购物车中的所有商品。
* **路径**: GET /cart
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "获取成功",  
  "data": {  
  "totalPrice": 5298.00,  
  "items": \[  
  {  
  "cartItemId": "cart-item-uuid-1",  
  "productId": 101,  
  "quantity": 1,  
  "name": "智能手机 Pro",  
  "price": 4999.00,  
  "imageUrl": "https://path/to/image.jpg"  
  }  
  \]  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/cart" \-Method Get \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的TOKEN\>" }

### **4.3 更新购物车商品数量**

* **功能**: 修改购物车中某个商品的数量。
* **路径**: PUT /cart/items/{cartItemId}
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "更新成功",  
  "data": null  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/cart/items/\<在此处粘贴CART\_ITEM\_ID\>" \-Method Put \-Body '{"quantity":2}' \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的TOKEN\>" } \-ContentType "application/json"

### **4.4 从购物车删除商品**

* **功能**: 从购物车中移除一个或多个商品。
* **路径**: DELETE /cart/items
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "删除成功",  
  "data": null  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/cart/items" \-Method Delete \-Body '{"cartItemIds":\["\<在此处粘贴CART\_ITEM\_ID\>"\]}' \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的TOKEN\>" } \-ContentType "application/json"

## **5\. 用户行为模块 (需认证)**

### **5.1 上报用户行为**

* **功能**: 用于算法模型训练的数据上报。
* **路径**: POST /behaviors/track
* **成功响应示例 (202 Accepted)**:  
  {  
  "code": 202,  
  "message": "行为已记录",  
  "data": null  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/behaviors/track" \-Method Post \-Body '{"eventType":"click", "productId":301, "timestamp":"2025-08-26T11:45:00Z"}' \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的TOKEN\>" } \-ContentType "application/json"

## **6\. 管理员模块 (需管理员角色Token)**

### **6.1 添加新商品**

* **功能**: 添加一个新商品。
* **路径**: POST /admin/products
* **成功响应示例 (201 Created)**:  
  {  
  "code": 201,  
  "message": "商品添加成功",  
  "data": {  
  "id": 103,  
  "name": "新商品",  
  "price": 99.99,  
  "stock": 100,  
  "categoryId": 1,  
  ...  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/admin/products" \-Method Post \-Body '{"name":"新商品","price":99.99,"stock":100,"categoryId":1}' \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的ADMIN-TOKEN\>" } \-ContentType "application/json"

### **6.2 更新商品信息**

* **功能**: 更新一个已存在的商品。
* **路径**: PUT /admin/products/{productId}
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "商品更新成功",  
  "data": {  
  "id": 103,  
  "name": "更新后的商品",  
  "price": 88.88,  
  ...  
  }  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/admin/products/103" \-Method Put \-Body '{"name":"更新后的商品","price":88.88}' \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的ADMIN-TOKEN\>" } \-ContentType "application/json"

### **6.3 删除商品**

* **功能**: 删除一个商品。
* **路径**: DELETE /admin/products/{productId}
* **成功响应示例 (200 OK)**:  
  {  
  "code": 200,  
  "message": "商品删除成功",  
  "data": null  
  }

* **PowerShell 测试命令**:  
  Invoke-RestMethod \-Uri "http://localhost:8080/api/v1/admin/products/103" \-Method Delete \-Headers @{ "Authorization" \= "Bearer \<在此处粘贴你的ADMIN-TOKEN\>" }  
