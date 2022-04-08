
spring.profiles.active=@activatedProperties@

spring.cache.jcache.config=classpath:ehcache.xml

server.error.whitelabel.enabled=false
server.error.path=/error

# timeout session after 24 hours 
server.servlet.session.timeout=1440m

# ============== depending dev/prod: ================================

server.port=8081

spring.datasource.url=jdbc:mysql://<yourhost>:3306/irusuk?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*******
spring.datasource.password=*******

spring.devtools.add-properties=true

logging.level.org.springframework.jdbc.core.JdbcTemplate=info

# spring.jpa.show-sql=true
# spring.jpa.properties.hibernate.format_sql=true

logging.file.name=spring-boot-dashboard.log