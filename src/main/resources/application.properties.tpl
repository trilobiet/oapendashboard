
spring.profiles.active=@activatedProperties@

spring.cache.jcache.config=classpath:ehcache.xml

server.error.whitelabel.enabled=false
server.error.path=/error

# ============== depending dev/prod: ================================

server.port=8081

spring.datasource.url=jdbc:mysql://<yourhost>:3306/irusuk?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*******
spring.datasource.password=*******

spring.devtools.add-properties=true

logging.level.org.springframework.jdbc.core.JdbcTemplate=info

# spring.jpa.show-sql=true
# spring.jpa.properties.hibernate.format_sql=true