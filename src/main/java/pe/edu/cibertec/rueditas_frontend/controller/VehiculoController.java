package pe.edu.cibertec.rueditas_frontend.controller;

import pe.edu.cibertec.rueditas_frontend.dto.VehiculoRequestDTO;
import pe.edu.cibertec.rueditas_frontend.dto.VehiculoResponseDTO;
import pe.edu.cibertec.rueditas_frontend.viewmodel.VehiculoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        VehiculoModel vehiculoModel = new VehiculoModel("00","","","","","","");
        model.addAttribute("vehiculoModel", vehiculoModel);
        return "inicio";
    }

    @PostMapping("/buscar")
    public String buscar(@RequestParam("placa") String placa, Model model) {

        if (placa == null || placa.trim().length() == 0 || (!placa.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9-]+$")) || placa.length() != 7){
            VehiculoModel vehiculoModel = new VehiculoModel("01","Error: Debe ingresar una placa correcta",
                    "","","","","");
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "inicio";

        }
        try {
            String endpoint = "http://localhost:8081/vehiculo/buscar";
            VehiculoRequestDTO vehiculoRequestDTO = new VehiculoRequestDTO(placa);
            VehiculoResponseDTO vehiculoResponseDTO = restTemplate.postForObject(endpoint, vehiculoRequestDTO, VehiculoResponseDTO.class);

            if(vehiculoResponseDTO.codigo().equals("00")){
                VehiculoModel vehiculoModel = new VehiculoModel("00","",vehiculoResponseDTO.marca(),vehiculoResponseDTO.modelo(),
                        vehiculoResponseDTO.nroAsientos(),vehiculoResponseDTO.precio(),vehiculoResponseDTO.Color());
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "buscar";
            }else {
                VehiculoModel vehiculoModel = new VehiculoModel("02","Error: No se encontró un vehículo para la placa ingresada",
                        "","","","","");
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "inicio";
            }

        } catch (Exception e) {
            VehiculoModel vehiculoModel = new VehiculoModel("99","Error: Ocurrio un problema inesperado",
                    "","","","","");
            model.addAttribute("vehiculoModel", vehiculoModel);
            System.out.println(e.getMessage());
            return "inicio";
        }


    }
}
