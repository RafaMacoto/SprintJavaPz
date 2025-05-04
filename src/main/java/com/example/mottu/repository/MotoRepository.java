package com.example.mottu.repository;

import com.example.mottu.model.moto.Moto;
import com.example.mottu.model.moto.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MotoRepository extends JpaRepository<Moto, Long>, JpaSpecificationExecutor<Moto> {

    List<Moto> findByStatus(Status status);
    List<Moto> findByAlaId(Long alaId);
    List<Moto> findByStatusAndAlaId(Status status, Long alaId);
    Optional<Moto> findByPlaca(String placa);
}
