# Elasticsearch Setup Guide

## Prerequisites
- Docker and Docker Compose installed
- Java 17+
- Maven 3.6+

## Quick Start

### 1. Start Elasticsearch and Supporting Services
```bash
# Navigate to the project directory
cd ecom-java-backend

# Start all services (Elasticsearch, Kibana, MySQL)
docker-compose up -d

# Verify Elasticsearch is running
curl http://localhost:9200/_cluster/health
```

### 2. Start the Spring Boot Application
```bash
mvn spring-boot:run
```

### 3. Initial Data Sync
Once the application is running, sync existing products to Elasticsearch:
```bash
# Bulk sync all products to Elasticsearch
curl -X POST http://localhost:8080/search/admin/sync-all
```

## Using Elasticsearch Search

### Basic Search Endpoints

#### 1. Elasticsearch Advanced Search
```bash
curl -X POST http://localhost:8080/search/elasticsearch \
  -H "Content-Type: application/json" \
  -d '{
    "query": "laptop",
    "category": "electronics",
    "minPrice": 100,
    "maxPrice": 1000,
    "pageNumber": 0,
    "itemsPerPage": 20
  }'
```

#### 2. Fuzzy Search (Typo Tolerant)
```bash
curl "http://localhost:8080/search/fuzzy?q=laptpo&page=0&size=10"
```

#### 3. Autocomplete Suggestions
```bash
curl "http://localhost:8080/search/elasticsearch/suggestions?query=lap"
```

#### 4. Similar Products
```bash
curl "http://localhost:8080/search/similar/550e8400-e29b-41d4-a716-446655440000?limit=5"
```

#### 5. Quick Search
```bash
curl "http://localhost:8080/search/quick?q=gaming&category=electronics&useElasticsearch=true"
```

## Configuration

### Elasticsearch Settings (application.properties)
```properties
# Elasticsearch Configuration
elasticsearch.host=localhost
elasticsearch.port=9200
elasticsearch.username=
elasticsearch.password=

# Search Configuration
search.default.size=20
search.max.size=100
search.enable.elasticsearch=true
```

### Docker Environment Variables
You can modify `docker-compose.yml` to customize:
- Memory allocation for Elasticsearch
- Security settings
- Cluster configuration

## Monitoring and Management

### Kibana Dashboard
- Access Kibana at: http://localhost:5601
- Create index patterns for `products`
- Build custom dashboards for search analytics

### Health Checks
```bash
# Elasticsearch health
curl http://localhost:9200/_cluster/health

# Application health
curl http://localhost:8080/actuator/health

# Search service health
curl http://localhost:8080/search/filters
```

## Features

### 1. Advanced Text Search
- Multi-field search across title, description, keywords
- Fuzzy matching for typo tolerance
- Relevance scoring and boosting
- Field-specific weight boosting (title^3, description^2)

### 2. Powerful Filtering
- Category filtering
- Price range filtering
- Brand filtering
- Stock availability filtering
- Tag-based filtering
- Product status filtering

### 3. Smart Suggestions
- Real-time autocomplete
- Prefix-based suggestions
- Search-as-you-type functionality

### 4. Similar Products
- More Like This queries
- Content-based recommendations
- Related product discovery

### 5. Performance Features
- Fast search response times
- Scalable indexing
- Automatic data synchronization
- Caching support

## Troubleshooting

### Common Issues

1. **Elasticsearch not starting:**
   ```bash
   # Check logs
   docker logs ecom-elasticsearch
   
   # Increase memory if needed
   docker-compose down
   # Edit docker-compose.yml ES_JAVA_OPTS
   docker-compose up -d
   ```

2. **Search returning no results:**
   ```bash
   # Check if products are indexed
   curl http://localhost:9200/products/_count
   
   # Re-sync if needed
   curl -X POST http://localhost:8080/search/admin/sync-all
   ```

3. **Connection refused:**
   ```bash
   # Verify Elasticsearch is running
   docker ps | grep elasticsearch
   
   # Check application.properties settings
   # Ensure elasticsearch.host and port are correct
   ```

## Production Considerations

1. **Security**: Enable Elasticsearch security in production
2. **Clustering**: Use multiple Elasticsearch nodes for high availability
3. **Monitoring**: Set up proper logging and monitoring
4. **Backup**: Configure automated index backups
5. **Performance**: Tune JVM settings and index configurations

## API Documentation

Full API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs
