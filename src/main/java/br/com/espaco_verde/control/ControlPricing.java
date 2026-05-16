package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.PricingConfigDTO;
import br.com.espaco_verde.entity.PricingCategory;
import br.com.espaco_verde.entity.PricingMethod;
import br.com.espaco_verde.service.ServicePricing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/pricing")
public class ControlPricing {

    @Autowired
    private ServicePricing servicePricing;

    @GetMapping("/config")
    public ResponseEntity<PricingConfigDTO> getConfig() {
        PricingConfigDTO config = servicePricing.getCurrentConfig();
        return ResponseEntity.ok(config);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<PricingCategory>> getClasses() {
        List<PricingCategory> classes = servicePricing.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    @PutMapping("/config")
    public ResponseEntity<?> updatePricingConfiguration(@RequestBody PricingConfigDTO pricingConfigDTO){
        servicePricing.updatePricingConfig(pricingConfigDTO);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/categories")
    public ResponseEntity<?> updatePricingCategory(@RequestBody Map<Integer, BigDecimal> categories){
        servicePricing.updatePricingCategories(categories);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calc")
    public ResponseEntity<?> calculatePrices(@RequestParam PricingMethod method){
        servicePricing.recalculateAllProducts(method);
        return ResponseEntity.ok(Map.of("message", "Preços calculados com sucesso!"));
    }
}
