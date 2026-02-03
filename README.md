# API Automation Framework with RestAssured

A comprehensive API testing framework built with **RestAssured**, **TestNG**, and **Maven** for automated testing of REST APIs. This framework demonstrates best practices in API test automation with support for JWT authentication, extensive reporting, and CI/CD integration.

![API Automation CI](https://github.com/yourusername/simple-api-automation-restassured/workflows/API%20Automation%20CI/badge.svg)

## 🚀 Features

- **Multi-API Support**: Configurable framework supporting multiple APIs (PetStore & DummyJSON)
- **JWT Authentication Flow**: Complete implementation of JWT authentication with access/refresh tokens
- **POJO-based Requests**: Type-safe API requests using Plain Old Java Objects
- **Comprehensive Reporting**: HTML reports using ExtentReports with detailed test logs
- **Context Management**: Centralized auth context for managing tokens and cookies across tests
- **Positive & Negative Testing**: Extensive test coverage with both positive and negative scenarios
- **CI/CD Ready**: GitHub Actions workflow for automated test execution
- **Logging**: SLF4J with Logback for detailed test execution logs
- **Data-Driven Testing**: Reusable test data utilities

## 📋 Prerequisites

- **Java**: JDK 8 or higher
- **Maven**: 3.6+
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| RestAssured | 5.4.0 | REST API testing library |
| TestNG | 7.4.0 | Testing framework |
| ExtentReports | 4.1.7 | HTML reporting |
| Jackson | 2.17.1 | JSON serialization/deserialization |
| Lombok | 1.18.28 | Reduce boilerplate code |
| Logback | 1.2.13 | Logging framework |
| Maven | 3.x | Build automation |

## 📁 Project Structure

```
simple-api-automation-restassured/
├── src/test/java/
│   ├── base/
│   │   └── BaseTest.java              # Base test class with common setup
│   ├── config/
│   │   └── ApiConfig.java             # API configuration management
│   ├── context/
│   │   └── AuthContext.java           # Authentication context for tokens
│   ├── api/
│   │   ├── AuthApi.java               # Auth-related endpoints
│   │   ├── JwtAuthApi.java            # JWT authentication endpoints
│   │   └── UserApi.java               # User information endpoints
│   ├── pojo/
│   │   ├── AuthResponse.java          # Authentication response model
│   │   ├── LoginRequest.java          # Login request model
│   │   └── RefreshTokenRequest.java   # Refresh token request model
│   ├── reporting/
│   │   ├── ExtentManager.java         # ExtentReports configuration
│   │   ├── ExtentTestListener.java    # TestNG listener for reporting
│   │   └── ReportLogger.java          # Custom logger for reports
│   ├── tests/
│   │   ├── CreateUserPositiveTest.java
│   │   ├── CreateUserNegativeTest.java
│   │   ├── JwtAuthFlowTest.java
│   │   ├── JwtAuthPositiveTest.java
│   │   ├── JwtAuthNegativeTest.java
│   │   └── JwtRefreshTokenTest.java
│   └── utils/
│       ├── TestData.java              # Test data for user operations
│       └── JwtTestData.java           # Test data for JWT operations
├── src/test/resources/
│   ├── config.properties              # Configuration properties
│   └── logback.xml                    # Logging configuration
├── .github/workflows/
│   └── api-tests.yml                  # GitHub Actions CI/CD workflow
├── pom.xml                            # Maven dependencies
└── testng.xml                         # TestNG suite configuration
```

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/simple-api-automation-restassured.git
cd simple-api-automation-restassured
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Configure API Base URLs

Edit `src/test/resources/config.properties`:

```properties
petstore.base.url=https://petstore.swagger.io/v2
dummyjson.base.url=https://dummyjson.com
```

## 🏃 Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Suite

```bash
mvn test -Dtest=JwtAuthPositiveTest
```

### Run Using TestNG XML

```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run from IDE

Right-click on `testng.xml` and select **Run**

## 📊 Test Reports

After test execution, reports are generated in:

- **ExtentReports HTML**: `target/extent-report.html`
- **TestNG Reports**: `target/surefire-reports/`

Open the HTML report in a browser for detailed test execution results with screenshots and logs.

## 🧪 Test Coverage

### JWT Authentication Tests

- **JwtAuthPositiveTest**: Valid login scenarios with POJO
- **JwtAuthNegativeTest**: Invalid credentials and error handling
- **JwtAuthFlowTest**: Complete authentication flow
- **JwtRefreshTokenTest**: Token refresh functionality

### User Management Tests

- **CreateUserPositiveTest**: Successful user creation scenarios
- **CreateUserNegativeTest**: Validation and error scenarios

## 🔑 Key Concepts

### 1. API Configuration

The framework supports multiple APIs through `ApiConfig`:

```java
protected void usePetStoreApi() {
    ApiConfig.setApi(ApiType.PETSTORE);
}

protected void useDummyJsonApi() {
    ApiConfig.setApi(ApiType.DUMMYJSON);
}
```

### 2. Authentication Context

Centralized management of authentication tokens:

```java
AuthContext.setAccessToken(auth.getAccessToken());
AuthContext.setRefreshToken(auth.getRefreshToken());
AuthContext.setCookies(response.getCookies());
```

### 3. POJO-Based Requests

Type-safe API requests using POJOs:

```java
LoginRequest loginRequest = JwtTestData.validLoginRequest();
Response response = JwtAuthEndpoint.login(loginRequest);
AuthResponse auth = response.as(AuthResponse.class);
```

### 4. Reporting

Enhanced logging in reports:

```java
ReportLogger.info("Starting login test");
ReportLogger.pass("Login successful");
ReportLogger.fail("Login failed");
```

## 🔄 CI/CD Integration

The project includes GitHub Actions workflow (`.github/workflows/api-tests.yml`) that:

- Triggers on push/PR to main branch
- Sets up Java 17 environment
- Caches Maven dependencies
- Runs all tests
- Uploads ExtentReports as artifacts

## 🎯 Best Practices Implemented

1. **Page Object Model Pattern**: Separation of endpoints and test logic
2. **Data-Driven Testing**: Reusable test data in utility classes
3. **Centralized Configuration**: Single source for API configurations
4. **Context Management**: Shared authentication state across tests
5. **Comprehensive Logging**: Detailed logs for debugging
6. **Assertions**: Clear and meaningful assertions
7. **Test Independence**: Tests can run independently

## 📝 Sample Test

```java
@Test(priority = 1)
public void loginWithPojo() {
    ReportLogger.info("Starting login using POJO-based request");
    
Response response = JwtAuthApi.login(JwtTestData.validLoginRequest());
    
    Assert.assertEquals(response.getStatusCode(), 200);
    
    AuthResponse auth = response.as(AuthResponse.class);
    AuthContext.setAccessToken(auth.getAccessToken());
    
    ReportLogger.pass("Login successful and tokens captured");
}
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)

## 🙏 Acknowledgments

- [RestAssured Documentation](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/)
- [ExtentReports](https://www.extentreports.com/)
- [DummyJSON API](https://dummyjson.com/)
- [PetStore API](https://petstore.swagger.io/)

## 📞 Support

If you encounter any issues or have questions, please:
- Open an issue on GitHub
- Check existing documentation
- Review the test examples in the project

---

⭐ If you find this project helpful, please consider giving it a star!
