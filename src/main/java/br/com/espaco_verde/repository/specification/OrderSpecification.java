package br.com.espaco_verde.repository.specification;

import br.com.espaco_verde.DTO.ReportFilterDTO;
import br.com.espaco_verde.entity.Order;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> withFilter(ReportFilterDTO filter){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(Long.class != query.getResultType()){
                root.fetch("items", JoinType.LEFT);
                query.distinct(true);
            }

            if(filter.productName() != null){
                Join<Object, Object> itemsJoin = root.join("items", JoinType.INNER);

                String productName ="%" + filter.productName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(itemsJoin.get("product").get("nome")),productName));
            }

            if(filter.productType() != null){
                Join<Object, Object> itemsJoin = root.join("items", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(itemsJoin.get("product").get("tipo"), filter.productType()));
            }

            if(filter.status() != null){
              predicates.add(criteriaBuilder.equal(root.get("orderStatus"),filter.status()));
            }

            if(filter.deliveryMethod() != null){
                predicates.add(criteriaBuilder.equal(root.get("deliveryMethod"), filter.deliveryMethod()));
            }

            if(filter.custumerName() != null){
                predicates.add(criteriaBuilder.equal(root.join("custumer").get("name"), filter.custumerName()));
            }

            if(filter.initalDate() != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filter.initalDate()));
            }

            if(filter.finalDate() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filter.finalDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
