package com.danilyuk.SensorRestAPI.controllers;

import com.danilyuk.SensorRestAPI.dto.MeasurementDTO;
import com.danilyuk.SensorRestAPI.dto.SensorDTO;
import com.danilyuk.SensorRestAPI.services.MeasurementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Controller
@RequestMapping("/client")
public class ClientController {
    private final MeasurementsService measurementsService;

    @Autowired
    public ClientController(MeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @GetMapping()
    public String getPieChart(Model model) {
        model.addAttribute("chartData", measurementsService.dataForChart());
        return "client/chart";
    }

    @GetMapping("/add1000")
    public String add1000(@RequestParam(value = "sensor", required = true) String sensorName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/measurements/add";

        for (int i = 0; i < 1000; i++) {
            MeasurementDTO measurementDTO = new MeasurementDTO();
            measurementDTO.setRaining(new Random().nextBoolean());
            measurementDTO.setValue((float) (100 - new Random().nextFloat() * 200));
            measurementDTO.setSensorDTO(new SensorDTO(sensorName));
            HttpEntity<MeasurementDTO> request = new HttpEntity<>(measurementDTO);
            String response = restTemplate.postForObject(url, request, String.class);
        }
        return "redirect:client";
    }
}
