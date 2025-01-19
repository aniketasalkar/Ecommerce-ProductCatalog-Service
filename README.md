# Ecommerce-ProductCatalog-Service

Product Catalog Service for Ecommerce Application

## API Documentation

### CategoryController

#### Create Category
- **Endpoint:** `/api/productCategoryService/category`
- **Method:** `POST`
- **Description:** Creates a new category.
- **Request Body:** `CategoryRequestDto`
- **Response:**
  - `201 Created`: Returns the created category details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

#### Get All Categories
- **Endpoint:** `/api/productCategoryService/category`
- **Method:** `GET`
- **Description:** Retrieves all categories.
- **Response:**
  - `200 OK`: Returns the list of all categories.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Get Category by Name
- **Endpoint:** `/api/productCategoryService/category/{name}`
- **Method:** `GET`
- **Description:** Retrieves a category by name.
- **Path Variable:** `name`
- **Response:**
  - `200 OK`: Returns the category details.
  - `404 Not Found`: If the category is not found.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Update Category
- **Endpoint:** `/api/productCategoryService/category/{name}`
- **Method:** `PATCH`
- **Description:** Updates a category.
- **Path Variable:** `name`
- **Request Body:** `CategoryRequestDto`
- **Response:**
  - `200 OK`: Returns the updated category details.
  - `400 Bad Request`: If the request is invalid.
  - `404 Not Found`: If the category is not found.
  - `500 Internal Server Error`: If there is an error during update.

#### Create Categories in Bulk
- **Endpoint:** `/api/productCategoryService/category/bulk`
- **Method:** `POST`
- **Description:** Creates multiple categories in bulk.
- **Request Body:** List of `CategoryRequestDto`
- **Response:**
  - `201 Created`: Returns the created categories details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

### ProductController

#### Add Product
- **Endpoint:** `/api/productCategoryService/products`
- **Method:** `POST`
- **Description:** Adds a new product.
- **Request Body:** `ProductRequestDto`
- **Response:**
  - `201 Created`: Returns the created product details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

#### Get Product by ID
- **Endpoint:** `/api/productCategoryService/products/{id}`
- **Method:** `GET`
- **Description:** Retrieves a product by ID.
- **Path Variable:** `id`
- **Response:**
  - `200 OK`: Returns the product details.
  - `404 Not Found`: If the product is not found.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Get All Products
- **Endpoint:** `/api/productCategoryService/products`
- **Method:** `GET`
- **Description:** Retrieves all products.
- **Response:**
  - `200 OK`: Returns the list of all products.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Update Product
- **Endpoint:** `/api/productCategoryService/products/{id}`
- **Method:** `PATCH`
- **Description:** Updates a product.
- **Path Variable:** `id`
- **Request Body:** `ProductPatchRequestDto`
- **Response:**
  - `200 OK`: Returns the updated product details.
  - `400 Bad Request`: If the request is invalid.
  - `404 Not Found`: If the product is not found.
  - `500 Internal Server Error`: If there is an error during update.

#### Create Products in Bulk
- **Endpoint:** `/api/productCategoryService/products/bulk`
- **Method:** `POST`
- **Description:** Creates multiple products in bulk.
- **Request Body:** List of `ProductRequestDto`
- **Response:**
  - `201 Created`: Returns the created products details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

#### Delete Product
- **Endpoint:** `/api/productCategoryService/products/{id}`
- **Method:** `DELETE`
- **Description:** Deletes a product by ID.
- **Path Variable:** `id`
- **Response:**
  - `200 OK`: If the product is deleted successfully.
  - `404 Not Found`: If the product is not found.
  - `500 Internal Server Error`: If there is an error during deletion.

### References: 
- https://medium.com/@tericcabrel/validate-request-body-and-parameter-in-spring-boot-53ca77f97fe9
- https://blog.clairvoyantsoft.com/spring-boot-creating-a-custom-annotation-for-validation-edafbf9a97a4
- https://www.baeldung.com/java-bean-validation-not-null-empty-blank
