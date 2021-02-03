package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpaResponseDTOTest {
    @Test
    public void defaultTest(){
        SpaResponseDTO dto1 = SpaResponseDTO.builder().build();
        System.out.println(new Gson().toJson(dto1));
        SpaResponseDTO dto2 = new SpaResponseDTO();
        System.out.println(new Gson().toJson(dto2));
//        Assertions.assertTrue(dto1.toString().equals(dto2.toString()));
    }
}