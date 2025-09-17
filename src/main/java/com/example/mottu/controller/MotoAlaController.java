package com.example.mottu.controller;

import com.example.mottu.dto.motoAla.MotoAlaRequestDTO;
import com.example.mottu.dto.motoAla.MotoAlaResponseDTO;
import com.example.mottu.service.motoAla.IMotoAlaService;
import com.example.mottu.service.motoAla.MotoAlaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("insight")
public class MotoAlaController {

    @Autowired
    private IMotoAlaService motoAlaService;



    @PostMapping("/vincular-ala")
    public ResponseEntity<MotoAlaResponseDTO> vincularMotoAla(@RequestBody MotoAlaRequestDTO dto) {
        MotoAlaResponseDTO response = motoAlaService.vincularMotoAla(dto);
        return ResponseEntity.ok(response);
    }
}
