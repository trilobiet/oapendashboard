
spring.profiles.active=@activatedProperties@

spring.cache.jcache.config=classpath:ehcache.xml

spring.mvc.throw-exception-if-no-handler-found=true 
spring.web.resources.add-mappings=false

# ============== depending dev/prod: ================================

server.port=8080

spring.datasource.url=jdbc:mysql://<yourhost>:3306/irusuk?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*******
spring.datasource.password=*******

spring.devtools.add-properties=true

logging.level.org.springframework.jdbc.core.JdbcTemplate=info
