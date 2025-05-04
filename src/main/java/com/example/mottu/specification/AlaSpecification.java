package com.example.mottu.specification;

import com.example.mottu.model.ala.Ala;
import com.example.mottu.model.ala.AlaFilter;
import org.springframework.data.jpa.domain.Specification;

public class AlaSpecification {

    public static Specification<Ala> withFilters(AlaFilter filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter.nome() != null && !filter.nome().isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%");
            }
            return criteriaBuilder.conjunction();
        };
    }
}
