package com.neuromotion.doctores.feign;

import com.neuromotion.doctores.dto.CitaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-usuarios", url = "${application.ms-usuarios.url}")
public interface CitaFeignClient {

    @GetMapping("/api/citas/doctor/{doctorId}")
    List<CitaResponseDTO> listarPorDoctor(@PathVariable Long doctorId);
}
