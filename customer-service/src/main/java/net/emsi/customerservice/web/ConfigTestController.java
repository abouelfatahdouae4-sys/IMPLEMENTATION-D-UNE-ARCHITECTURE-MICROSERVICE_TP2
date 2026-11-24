package net.emsi.customerservice.web;

import net.emsi.customerservice.config.CustomerConfigParams;
import net.emsi.customerservice.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Test du config-service.
 * Modifier config-repo, commit, puis POST http://localhost:8081/actuator/refresh
 * -> les nouvelles valeurs sont prises en compte sans redemarrer.
 */
@RestController
@RefreshScope
public class ConfigTestController {
    @Value("${global.params.p1:0}") private int p1;
    @Value("${global.params.p2:0}") private int p2;
    @Value("${customer.params.x:0}") private int x;
    @Value("${customer.params.y:0}") private int y;

    private final GlobalConfig globalConfig;
    private final CustomerConfigParams customerConfigParams;

    public ConfigTestController(GlobalConfig globalConfig, CustomerConfigParams customerConfigParams) {
        this.globalConfig = globalConfig;
        this.customerConfigParams = customerConfigParams;
    }

    @GetMapping("/testConfig1")
    public Map<String, Integer> configTest() {
        return Map.of("p1", p1, "p2", p2, "x", x, "y", y);
    }

    @GetMapping("/globalConfig")
    public GlobalConfig globalConfig() { return globalConfig; }

    @GetMapping("/customerConfig")
    public CustomerConfigParams customerConfig() { return customerConfigParams; }
}
