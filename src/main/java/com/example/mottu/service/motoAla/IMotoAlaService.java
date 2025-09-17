package com.example.mottu.service.motoAla;

import com.example.mottu.dto.motoAla.MotoAlaRequestDTO;
import com.example.mottu.dto.motoAla.MotoAlaResponseDTO;

public interface IMotoAlaService {
    MotoAlaResponseDTO vincularMotoAla(MotoAlaRequestDTO dto);
}
