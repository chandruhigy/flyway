package com.example.product.connections;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class FlywayMigrationInitializer {

    @Autowired
    private RoutingDataSource routingDataSource;

    @Value("${application.module.name}")
    String moduleName;


    private TenantFinder tenantFinder;

    public void migrate() {
        String scriptLocation = "db/migration";
        tenantFinder = TenantFinder.getInstance();
        if(tenantFinder.getAllTenants()==null || tenantFinder.getAllTenants().size()==0){
            tenantFinder.loadRedisIntoMap();
        }
        for (Tenant tenant : tenantFinder.getAllTenants()) {
            String dbName =  tenant.getName()+"_"+moduleName;
            Flyway flyway = Flyway.configure()
                    .locations(scriptLocation)
                    .baselineOnMigrate(Boolean.TRUE)
                    .dataSource(routingDataSource.getDataSourceByTenant(tenant))
                    .schemas(dbName)
                    .load();

            flyway.migrate();
        }
    }
}
