# 📧 Mailchimp Marketing API — Spring Boot Backend

A **Spring Boot** backend that wraps the **Mailchimp Marketing API** into clean, RESTful endpoints. Manage audiences, subscribers, campaigns, templates, and reports through a single, well-structured Java service.

---

## 🚀 Features

- **Audience Management** — Create, update, delete, and fetch stats for Mailchimp lists
- **Subscriber Management** — Add, upsert, tag, and track subscriber activity
- **Campaign Lifecycle** — Create → Set content → Schedule / Send → Report
- **Template Management** — Create and manage reusable HTML email templates
- **MD5 Subscriber Hashing** — Automatic email normalisation as required by Mailchimp
- **Global Exception Handling** — Consistent error responses across all endpoints
- **Swagger UI** — Interactive API documentation at `/swagger-ui.html`
- **Actuator** — Health and metrics endpoints out of the box
- **Validation** — Bean Validation on all request bodies

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.4 |
| HTTP Client | Spring RestTemplate (Basic Auth interceptor) |
| API Docs | SpringDoc OpenAPI 3 / Swagger UI |
| Validation | Jakarta Bean Validation |
| Logging | SLF4J + Logback |
| Build Tool | Maven |
| Testing | JUnit 5, Mockito |

---

## 📁 Project Structure

```
mailchimp-marketing/
├── src/
│   ├── main/
│   │   ├── java/com/mailchimp/marketing/
│   │   │   ├── MailchimpMarketingApplication.java
│   │   │   ├── config/
│   │   │   │   ├── MailchimpProperties.java       # @ConfigurationProperties binding
│   │   │   │   ├── MailchimpClientConfig.java     # RestTemplate + Basic Auth interceptor
│   │   │   │   └── OpenApiConfig.java             # Swagger / OpenAPI bean
│   │   │   ├── controller/
│   │   │   │   ├── AudienceController.java        # GET/POST/PATCH/DELETE /audiences
│   │   │   │   ├── SubscriberController.java      # Nested under /audiences/{id}/subscribers
│   │   │   │   ├── CampaignController.java        # Full campaign lifecycle
│   │   │   │   ├── TemplateController.java        # HTML template CRUD
│   │   │   │   └── HealthController.java          # /ping, /info
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── CreateAudienceRequest.java
│   │   │   │   │   ├── SubscriberRequest.java
│   │   │   │   │   ├── CampaignRequest.java
│   │   │   │   │   ├── CampaignContentRequest.java
│   │   │   │   │   └── TagRequest.java
│   │   │   │   └── response/
│   │   │   │       └── ApiResponse.java           # Generic wrapper {success, message, data}
│   │   │   ├── exception/
│   │   │   │   ├── MailchimpApiException.java
│   │   │   │   └── GlobalExceptionHandler.java    # @RestControllerAdvice
│   │   │   └── service/
│   │   │       ├── AudienceService.java
│   │   │       ├── SubscriberService.java
│   │   │       ├── CampaignService.java
│   │   │       ├── TemplateService.java
│   │   │       └── impl/
│   │   │           ├── AudienceServiceImpl.java
│   │   │           ├── SubscriberServiceImpl.java
│   │   │           ├── CampaignServiceImpl.java
│   │   │           └── TemplateServiceImpl.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/mailchimp/marketing/
│           ├── MailchimpMarketingApplicationTest.java
│           └── SubscriberServiceImplTest.java
├── .env.example
├── .gitignore
└── pom.xml
```

---

## ⚙️ Configuration

### 1. Get Your Mailchimp API Key

1. Log in to [Mailchimp](https://mailchimp.com)
2. Go to **Account → Extras → API Keys**
3. Click **Create A Key**
4. Copy the key — it ends with your server prefix, e.g. `abc123def456...xyz-us14`

### 2. Set Environment Variables

```bash
cp .env.example .env
```

Edit `.env`:

```env
MAILCHIMP_API_KEY=your-api-key-here-us14
MAILCHIMP_SERVER_PREFIX=us14
```

> **Tip:** The server prefix is the part after the dash at the end of your API key.

### 3. application.yml (already configured)

```yaml
mailchimp:
  api:
    key: ${MAILCHIMP_API_KEY}
    server-prefix: ${MAILCHIMP_SERVER_PREFIX:us1}
    base-url: https://${mailchimp.api.server-prefix}.api.mailchimp.com/3.0
```

---

## ▶️ Running the Application

### Prerequisites

- Java 17+
- Maven 3.8+

### Steps

```bash
# Clone the repository
git clone https://github.com/gajendra-ingle/mailchimp-marketing.git
cd mailchimp-marketing

# Set environment variables (Linux/macOS)
export MAILCHIMP_API_KEY=your-api-key
export MAILCHIMP_SERVER_PREFIX=us1

# Build and run
mvn spring-boot:run
```

The app starts on **http://localhost:8080**

### Verify Connection

```bash
curl http://localhost:8080/api/v1/ping
```

Expected response:

```json
{
  "success": true,
  "message": "Mailchimp API connected successfully",
  "data": { "health_status": "Everything's Chimpy!" },
  "timestamp": "2025-01-01T10:00:00"
}
```

---

## 📖 API Reference

### Swagger UI

Once running, open: **http://localhost:8080/swagger-ui.html**

All endpoints are fully documented with request/response schemas.

---

### Audiences

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/audiences` | List all audiences |
| `GET` | `/api/v1/audiences/{listId}` | Get audience by ID |
| `GET` | `/api/v1/audiences/{listId}/stats` | Get audience statistics |
| `POST` | `/api/v1/audiences` | Create a new audience |
| `PATCH` | `/api/v1/audiences/{listId}` | Update an audience |
| `DELETE` | `/api/v1/audiences/{listId}` | Delete an audience |

**Create Audience — Request Body:**

```json
{
  "name": "My Newsletter",
  "permission_reminder": "You signed up on our website",
  "email_type_option": false,
  "contact": {
    "company": "Acme Corp",
    "address1": "123 Main St",
    "city": "Chicago",
    "state": "IL",
    "zip": "60601",
    "country": "US"
  },
  "campaign_defaults": {
    "from_name": "Acme Team",
    "from_email": "news@acme.com",
    "subject": "Our Latest News",
    "language": "EN_US"
  }
}
```

---

### Subscribers

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/audiences/{listId}/subscribers` | List all subscribers |
| `GET` | `/api/v1/audiences/{listId}/subscribers/{email}` | Get subscriber by email |
| `POST` | `/api/v1/audiences/{listId}/subscribers` | Add new subscriber |
| `PUT` | `/api/v1/audiences/{listId}/subscribers/{email}` | Upsert subscriber |
| `PATCH` | `/api/v1/audiences/{listId}/subscribers/{email}/status` | Update status |
| `POST` | `/api/v1/audiences/{listId}/subscribers/{email}/tags` | Add/remove tags |
| `GET` | `/api/v1/audiences/{listId}/subscribers/{email}/activity` | Get activity history |
| `DELETE` | `/api/v1/audiences/{listId}/subscribers/{email}` | Delete subscriber |

**Add Subscriber — Request Body:**

```json
{
  "email_address": "jane@example.com",
  "status": "subscribed",
  "merge_fields": {
    "FNAME": "Jane",
    "LNAME": "Doe"
  },
  "tags": ["vip", "newsletter"]
}
```

> **Note:** Email addresses are automatically MD5-hashed when used in path variables, as required by Mailchimp.

---

### Campaigns

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/campaigns` | List all campaigns |
| `GET` | `/api/v1/campaigns/{campaignId}` | Get campaign by ID |
| `POST` | `/api/v1/campaigns` | Create campaign |
| `PATCH` | `/api/v1/campaigns/{campaignId}` | Update campaign |
| `GET` | `/api/v1/campaigns/{campaignId}/content` | Get campaign content |
| `PUT` | `/api/v1/campaigns/{campaignId}/content` | Set campaign HTML/content |
| `POST` | `/api/v1/campaigns/{campaignId}/actions/send` | Send immediately |
| `POST` | `/api/v1/campaigns/{campaignId}/actions/schedule` | Schedule send |
| `POST` | `/api/v1/campaigns/{campaignId}/actions/unschedule` | Unschedule |
| `POST` | `/api/v1/campaigns/{campaignId}/actions/test` | Send test email |
| `GET` | `/api/v1/campaigns/{campaignId}/report` | Get performance report |
| `DELETE` | `/api/v1/campaigns/{campaignId}` | Delete campaign |

**Create Campaign — Request Body:**

```json
{
  "type": "regular",
  "recipients": {
    "list_id": "abc123def"
  },
  "settings": {
    "subject_line": "Your monthly update 🎉",
    "preview_text": "See what's new this month",
    "title": "March Newsletter",
    "from_name": "Acme Team",
    "reply_to": "news@acme.com"
  }
}
```

**Set Campaign Content:**

```json
{
  "html": "<html><body><h1>Hello *|FNAME|*!</h1></body></html>"
}
```

**Schedule Campaign:**

```
POST /api/v1/campaigns/{id}/actions/schedule?scheduleTime=2025-12-01T10:00:00+00:00
```

---

### Templates

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/templates` | List all templates |
| `GET` | `/api/v1/templates/{templateId}` | Get template by ID |
| `GET` | `/api/v1/templates/{templateId}/default-content` | Get default content |
| `POST` | `/api/v1/templates` | Create HTML template |
| `PATCH` | `/api/v1/templates/{templateId}` | Update template |
| `DELETE` | `/api/v1/templates/{templateId}` | Delete template |

---

### Health & Info

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/ping` | Verify Mailchimp API connection |
| `GET` | `/api/v1/info` | Get Mailchimp account info |
| `GET` | `/actuator/health` | Spring Boot health check |

---

## 🔄 API Response Format

All endpoints return a consistent wrapper:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2025-03-28T10:30:00"
}
```

Error responses:

```json
{
  "success": false,
  "message": "Audience not found: abc123",
  "timestamp": "2025-03-28T10:30:00"
}
```

Validation errors:

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "emailAddress": "Valid email address is required",
    "name": "Audience name is required"
  }
}
```

---

## 🧪 Running Tests

```bash
mvn test
```

Tests cover MD5 hashing logic and Spring context loading.

---

## 🔮 Future Enhancements

- [ ] Batch subscriber operations (bulk add/remove)
- [ ] Webhook receiver for subscribe/unsubscribe events
- [ ] Segment management endpoints
- [ ] Redis caching for audience and template metadata
- [ ] Docker Compose setup
- [ ] Spring Security for API key authentication on own endpoints

---
