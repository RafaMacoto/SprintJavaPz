package com.example.mottu.repository;

import com.example.mottu.model.ala.Ala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AlaRepository extends JpaRepository<Ala, Long>, JpaSpecificationExecutor<Ala> {

}
