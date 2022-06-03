package com.danilyuk.SensorRestAPI.controllers;

import com.danilyuk.SensorRestAPI.dto.MeasurementDTO;
import com.danilyuk.SensorRestAPI.services.MeasurementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Controller
@RequestMapping("/client")
public class ClientController {
    private final MeasurementsService measurementsService;

    @Autowired
    public ClientController(MeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @GetMapping("/{id}")
    public String getPieChart(@PathVariable("id") int sensorId,
                              @RequestParam(value = "raining", required = false) Boolean raining,
                              Model model) {
        model.addAttribute("chartData", measurementsService.dataForChart(sensorId, raining));
        return "client/chart";
    }

    @GetMapping("/add1000")
    public String add1000(@RequestParam(value = "sensorid") Integer sensorId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/sensors/" + sensorId + "/measurement";
        for (int i = 0; i < 1000; i++) {
            MeasurementDTO measurementDTO = new MeasurementDTO();
            measurementDTO.setRaining(new Random().nextBoolean());
            measurementDTO.setValue(100 - new Random().nextFloat() * 200);
            HttpEntity<MeasurementDTO> request = new HttpEntity<>(measurementDTO);
            restTemplate.postForObject(url, request, String.class);
        }
        return "redirect:client";
    }
}
