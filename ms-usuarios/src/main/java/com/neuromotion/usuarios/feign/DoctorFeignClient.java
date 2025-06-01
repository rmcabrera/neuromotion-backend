package com.neuromotion.usuarios.feign;

import com.neuromotion.usuarios.dto.DoctorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-doctores", url = "${ms.doctores.url}")
public interface DoctorFeignClient {
    @GetMapping("/api/doctores/{id}")
    DoctorDTO obtenerDoctorPorId(@PathVariable("id") Long id);
}
