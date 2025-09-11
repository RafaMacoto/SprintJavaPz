package com.example.mottu.specification;

import com.example.mottu.dto.usuario.UsuarioFilter;
import com.example.mottu.model.usuario.Usuario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class UsuarioSpecification {

    public static Specification<Usuario> withFilters(UsuarioFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            if (filter.email() != null && !filter.email().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.email().toLowerCase() + "%"));
            }



            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
