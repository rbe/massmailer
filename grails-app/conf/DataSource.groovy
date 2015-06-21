dataSource {
    pooled = true
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
environments {
    development {
        dataSource {
            driverClassName = "com.mysql.jdbc.Driver"
            username = "user"
            password = "password"
            dbCreate = "update"
            url = "jdbc:mysql://127.0.0.1:3306/database"
        }
    }
    test {
        dataSource {
            driverClassName = "org.hsqldb.jdbcDriver"
            username = "sa"
            password = ""
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            driverClassName = "com.mysql.jdbc.Driver"
            username = "user"
            password = "password"
            dbCreate = "update"
            url = "jdbc:mysql://127.0.0.1:3306/database"
        }
    }
}
