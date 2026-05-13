package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.PricingConfig;
import br.com.espaco_verde.entity.User;

import java.math.BigDecimal;
import java.util.Map;

public record PricingConfigDTO(BigDecimal expectedRevenue, BigDecimal fixedExpenses, BigDecimal variableExpenses, Map<Integer, BigDecimal> pricingCategories) {


    public PricingConfigDTO(PricingConfig p){

        this(
                p.getExpectedRevenue(),
                p.getFixedExpenses(),
                p.getVariableExpenses(),
                null
        );

    }

    public PricingConfig toEntity(){

        PricingConfig p = new PricingConfig();
        p.setExpectedRevenue(this.expectedRevenue);
        p.setFixedExpenses(this.fixedExpenses);
        p.setVariableExpenses(this.variableExpenses);
        return p;

    }

}
