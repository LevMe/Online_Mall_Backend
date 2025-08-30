# **在线商城项目 \- 前后端API接口需求文档 (V1.2)**

| 版本 | 日期 | 作者 | 修订说明 |  
| V1.2 | 2024-08-26 | (你的产品经理) | 明确了商品列表接口在首页的默认推荐逻辑 |  
| V1.1 | 2024-08-26 | (你的产品经理) | 根据讨论，将推荐模块与商品列表接口合并 |  
| V1.0 | 2024-05-17 | (你的产品经理) | 初稿创建，定义核心功能模块API |

## **1\. 项目概述**

本文档旨在定义“个性化推荐在线商城”项目中，**前端应用**与**后端服务**之间的API接口。目标是为前后端开发团队提供一份清晰、准确的协作契约，涵盖用户、商品、推荐及后台管理等核心功能。

## **2\. 通用约定**

### **2.1 Base URL**

所有API的请求根路径统一为：/api/v1

### **2.2 数据格式**

所有请求(Request Body)和响应(Response Body)的数据格式均为 application/json; charset=utf-8。

### **2.3 HTTP 状态码**

* 200 OK: 请求成功。  
* 201 Created: 资源创建成功。  
* 400 Bad Request: 请求参数错误或无效。  
* 401 Unauthorized: 用户未认证（未登录）。  
* 403 Forbidden: 用户无权限访问该资源。  
* 404 Not Found: 请求的资源不存在。  
* 500 Internal Server Error: 服务器内部错误。

### **2.4 统一响应体格式**

为了便于前端统一处理，所有API成功响应都将包裹在以下结构中：

{  
  "code": 200,  
  "message": "操作成功",  
  "data": {  
    // 具体的业务数据  
  }  
}

* code: 业务状态码，200 代表成功，其他值代表具体业务错误。  
* message: 对本次请求结果的描述信息。  
* data: 实际返回的数据对象。如果请求没有返回数据，data可以为null或{}。

## **3\. 接口详细设计**

### **3.1 用户功能模块 (User Module)**

#### **3.1.1 用户注册**

* **功能描述**: 创建一个新的用户账户。  
* **请求方法**: POST  
* **请求路径**: /users/register  
* **请求体 (Request Body)**:  
  {  
    "username": "newUser",  
    "password": "password123",  
    "email": "user@example.com"  
  }

* **成功响应 (201 Created)**:  
  {  
    "code": 201,  
    "message": "注册成功",  
    "data": {  
      "userId": "12345",  
      "username": "newUser"  
    }  
  }

#### **3.1.2 用户登录**

* **功能描述**: 用户使用凭证登录，获取认证Token。  
* **请求方法**: POST  
* **请求路径**: /auth/login  
* **请求体 (Request Body)**:  
  {  
    "username": "newUser",  
    "password": "password123"  
  }

* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "登录成功",  
    "data": {  
      "token": "jwt.token.string",  
      "userInfo": {  
        "userId": "12345",  
        "username": "newUser",  
        "avatarUrl": "https://path/to/avatar.jpg"  
      }  
    }  
  }

  *注：后续需要认证的接口，前端需在请求头中携带此Token，如 Authorization: Bearer jwt.token.string*

#### **3.1.3 获取当前登录用户信息**

* **功能描述**: 获取当前已登录用户的详细信息。  
* **请求方法**: GET  
* **请求路径**: /users/me  
* **认证要求**: 需要Token  
* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "获取成功",  
    "data": {  
      "userId": "12345",  
      "username": "newUser",  
      "email": "user@example.com",  
      "avatarUrl": "https://path/to/avatar.jpg",  
      "createdAt": "2024-05-17T10:00:00Z"  
    }  
  }

### **3.2 商品功能模块 (Product Module)**

#### **3.2.1 获取商品分类列表**

* **功能描述**: 获取所有的商品分类，用于导航栏或分类页面。  
* **请求方法**: GET  
* **请求路径**: /categories  
* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "获取成功",  
    "data": \[  
      { "id": 1, "name": "电子产品" },  
      { "id": 2, "name": "图书音像" },  
      { "id": 3, "name": "家居生活" }  
    \]  
  }

#### **3.2.2 获取商品列表（支持搜索、分类和推荐）**

* **功能描述**: 根据分类、关键词搜索、或推荐类型等条件分页获取商品列表。  
* **业务逻辑说明**:  
  * **首页默认推荐**: 当 categoryId 和 keyword 均未提供时，此接口将作为首页的默认商品列表。后端会根据用户登录状态自动决定推荐类型：  
    * **已登录用户**: 返回个性化推荐。  
    * **未登录用户**: 返回热门推荐。  
  * **参数优先级**: 如果 recommendationType 被显式提供，它的优先级最高。  
* **请求方法**: GET  
* **请求路径**: /products  
* **请求参数 (Query Parameters)**:  
  * recommendationType (string, optional): 推荐类型。可选值为 'personalized' (个性化推荐) 或 'hot' (热门推荐)。用于显式指定推荐类型，优先级最高。  
  * categoryId (number, optional): 分类ID  
  * keyword (string, optional): 搜索关键词  
  * page (number, default: 1): 页码  
  * pageSize (number, default: 10): 每页数量  
* **成功响应 (200 OK)**:  
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

#### **3.2.3 获取商品详情**

* **功能描述**: 根据商品ID获取单个商品的详细信息。  
* **请求方法**: GET  
* **请求路径**: /products/{productId}  
* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "获取成功",  
    "data": {  
      "id": 101,  
      "name": "智能手机 Pro",  
      "price": 4999.00,  
      "stock": 99,  
      "description": "一款性能卓越的智能手机...",  
      "imageUrls": \[  
        "https://path/to/image1.jpg",  
        "https://path/to/image2.jpg"  
      \],  
      "specs": {  
        "CPU": "SuperChip A18",  
        "RAM": "12GB"  
      }  
    }  
  }

### **3.3 购物车模块 (Cart Module)**

#### **3.3.1 添加商品到购物车**

* **功能描述**: 将指定商品添加到当前用户的购物车。  
* **请求方法**: POST  
* **请求路径**: /cart/items  
* **认证要求**: 需要Token  
* **请求体 (Request Body)**:  
  {  
    "productId": 101,  
    "quantity": 1  
  }

* **成功响应 (201 Created)**:  
  {  
    "code": 201,  
    "message": "添加成功",  
    "data": null  
  }

#### **3.3.2 查看购物车**

* **功能描述**: 获取当前用户购物车中的所有商品。  
* **请求方法**: GET  
* **请求路径**: /cart  
* **认证要求**: 需要Token  
* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "获取成功",  
    "data": {  
      "totalPrice": 5298.00,  
      "items": \[  
        {  
          "cartItemId": "cart-item-uuid-1",  
          "productId": 101,  
          "name": "智能手机 Pro",  
          "price": 4999.00,  
          "quantity": 1,  
          "imageUrl": "https://path/to/image.jpg"  
        },  
        {  
          "cartItemId": "cart-item-uuid-2",  
          "productId": 205,  
          "name": "无线蓝牙耳机",  
          "price": 299.00,  
          "quantity": 1,  
          "imageUrl": "https://path/to/headphone.jpg"  
        }  
      \]  
    }  
  }

#### **3.3.3 更新购物车商品数量**

* **功能描述**: 修改购物车中某个商品的数量。  
* **请求方法**: PUT  
* **请求路径**: /cart/items/{cartItemId}  
* **认证要求**: 需要Token  
* **请求体 (Request Body)**:  
  {  
    "quantity": 2  
  }

* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "更新成功",  
    "data": null  
  }

#### **3.3.4 从购物车删除商品**

* **功能描述**: 从购物车中移除一个或多个商品。  
* **请求方法**: DELETE  
* **请求路径**: /cart/items  
* **认证要求**: 需要Token  
* **请求体 (Request Body)**:  
  {  
    "cartItemIds": \["cart-item-uuid-1", "cart-item-uuid-2"\]  
  }

* **成功响应 (200 OK)**:  
  {  
    "code": 200,  
    "message": "删除成功",  
    "data": null  
  }

### **3.4 推荐功能模块 (Recommendation Module)**

#### **3.4.1 上报用户行为**

* **功能描述**: 前端上报用户对商品的关键行为（如点击），用于算法模型训练。  
* **请求方法**: POST  
* **请求路径**: /behaviors/track  
* **认证要求**: 需要Token  
* **请求体 (Request Body)**:  
  {  
    "eventType": "click", // 'click', 'addToCart', 'purchase'  
    "productId": 302,  
    "timestamp": "2024-05-17T10:15:00Z"  
  }

* 成功响应 (202 Accepted):  
  注：这类追踪接口通常快速响应，后端异步处理，因此使用202状态码，且无需返回data。  
  {  
    "code": 202,  
    "message": "行为已记录",  
    "data": null  
  }

### **3.5 系统管理员功能模块 (Admin Module)**

*注：所有管理员接口需要管理员角色权限，建议使用独立路径前缀 /api/v1/admin*

(此处为简化版，仅列出核心接口)

* GET /admin/users: 获取用户列表（分页）  
* GET /admin/products: 获取商品列表（分页）  
* POST /admin/products: 添加新商品  
* PUT /admin/products/{productId}: 更新商品信息  
* DELETE /admin/products/{productId}: 删除商品

**文档结束**
