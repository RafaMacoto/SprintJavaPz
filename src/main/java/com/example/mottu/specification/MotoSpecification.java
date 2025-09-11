package com.example.mottu.specification;

import com.example.mottu.dto.moto.MotoFilter;
import com.example.mottu.model.moto.Moto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MotoSpecification {

    public static Specification<Moto> withFilters(MotoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.alaId() != null) {
                predicates.add(cb.equal(root.get("ala").get("id"), filter.alaId()));
            }

            if (filter.placa() != null && !filter.placa().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("placa")), "%" + filter.placa().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
