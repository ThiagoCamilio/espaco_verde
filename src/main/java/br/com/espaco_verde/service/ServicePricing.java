package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.PricingConfigDTO;
import br.com.espaco_verde.entity.PricingCategory;
import br.com.espaco_verde.entity.PricingConfig;
import br.com.espaco_verde.entity.PricingMethod;
import br.com.espaco_verde.entity.Product;
import br.com.espaco_verde.repository.RepositoryPricingCategory;
import br.com.espaco_verde.repository.RepositoryPricingConfig;
import br.com.espaco_verde.repository.RepositoryProduto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class ServicePricing {

    @Autowired
    private RepositoryPricingConfig repositoryPricingConfig;

    @Autowired
    private RepositoryPricingCategory repositoryPricingCategory;

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Transactional
    public void updatePricingConfig(PricingConfigDTO pricingConfigDTO){

        PricingConfig config = repositoryPricingConfig.findById(1).orElse(new PricingConfig());
        config.setExpectedRevenue(pricingConfigDTO.expectedRevenue());
        config.setVariableExpenses(pricingConfigDTO.variableExpenses());
        config.setFixedExpenses(pricingConfigDTO.fixedExpenses());

        repositoryPricingConfig.save(config);

    }

    @Transactional
    public void updatePricingCategories(Map<Integer, BigDecimal> categories ){

        categories.forEach((categoryId, margin) -> {
            repositoryPricingCategory.findById(categoryId).ifPresent(category ->{
                category.setMargin(margin);
                repositoryPricingCategory.save(category);
            });
        });
    }

    @Transactional
    public void recalculateAllProducts(PricingMethod method){

        PricingConfig config = repositoryPricingConfig.findById(1).orElseThrow(() -> new IllegalStateException("Configuração de precificação não encontrada"));

        List<Product> products = repositoryProduto.findAll();

        BigDecimal fixedExpenses = config.getFixedExpenses();
        BigDecimal variableExpenses = config.getVariableExpenses();

        for(Product product : products){
            if(product.getPrecoCusto() == null || product.getPricingCategory() == null){
                continue;
            }

            BigDecimal costPrice = product.getPrecoCusto();
            BigDecimal margin = product.getPricingCategory().getMargin();

            BigDecimal suggestedPrice = calculatePrice(method, costPrice, fixedExpenses, variableExpenses, margin);
            if(product.isUseSuggestedPrice()){
                product.setPreco(suggestedPrice);
            }
            product.setSuggestedPrice(suggestedPrice);

        }

        repositoryProduto.saveAll(products);
    }

    private BigDecimal calculatePrice(PricingMethod method, BigDecimal costPrice, BigDecimal fixedExpenses, BigDecimal variableExpenses, BigDecimal margin) {

        switch (method){
            case MARKUP_DIVISOR:
                BigDecimal discountsPlusMargin = fixedExpenses.add(variableExpenses).add(margin);

                if(discountsPlusMargin.compareTo(BigDecimal.ONE) >= 0){
                    throw new IllegalArgumentException("A soma dos custos e margem ultrapassa 100%");
                }
                BigDecimal divisor = BigDecimal.ONE.subtract(discountsPlusMargin);
                return costPrice.divide(divisor, 2, RoundingMode.HALF_UP);

            case LUCRO_SOBRE_O_CUSTO_COM_REPASSE:
                BigDecimal discounts = BigDecimal.ONE.subtract(variableExpenses.add(fixedExpenses));
                BigDecimal profitMargin = BigDecimal.ONE.add(margin);
                BigDecimal profitPlusCost = profitMargin.multiply(costPrice);
                return profitPlusCost.divide(discounts, 2, RoundingMode.HALF_UP);

            default:
                throw new IllegalArgumentException("Método de precificação desconhecido: ");
        }
    }

    public PricingConfigDTO getCurrentConfig() {
        PricingConfig config = repositoryPricingConfig.findById(1).orElse(new PricingConfig());
        return new PricingConfigDTO(config);
    }

    public List<PricingCategory> getAllClasses() {
        return repositoryPricingCategory.findAll();
    }
}
