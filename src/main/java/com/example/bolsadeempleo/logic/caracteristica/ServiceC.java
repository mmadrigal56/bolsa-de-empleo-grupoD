package com.example.bolsadeempleo.logic.caracteristica;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceC {
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public Iterable<Caracteristica> caracteristicaFindAll () {
        return caracteristicaRepository.findAll();
    }

    public List<Caracteristica> findRoots() {
        return caracteristicaRepository.findByPadreIsNull();
    }

    public List<Caracteristica> findHijos(Caracteristica padre) {
        return caracteristicaRepository.findByPadre(padre);
    }

    public Caracteristica findById(Integer id) {
        if (id == null) return null;
        Optional<Caracteristica> opt = caracteristicaRepository.findById(id);
        return opt.orElse(null);
    }

    public List<Caracteristica> buildRuta(Caracteristica actual) {
        List<Caracteristica> ruta = new ArrayList<>();
        Caracteristica cursor = actual;
        while (cursor != null)
        {
            ruta.add(0, cursor);
            cursor = cursor.getPadre();
        }
        return ruta;
    }

    public void crearCaracteristica(String nombre, Integer padreId)
    {
        String nombreLimpio = nombre.trim();

        if (nombreLimpio.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la característica no puede estar vacío.");
        }
        Caracteristica padre = findById(padreId);

        boolean existe;
        if (padre == null) {
            existe = caracteristicaRepository
                    .findByNombreIgnoreCaseAndPadreIsNull(nombreLimpio)
                    .isPresent();
        } else {
            existe = caracteristicaRepository
                    .findByNombreIgnoreCaseAndPadre(nombreLimpio, padre)
                    .isPresent();
        }

        if (existe) {
            String nivel = (padre == null) ? "las raíces" : "\"" + padre.getNombre() + "\"";
            throw new IllegalArgumentException(
                    "Ya existe una característica llamada \"" + nombreLimpio + "\" en " + nivel + "."
            );
        }

        Caracteristica c = new Caracteristica();
        c.setNombre(nombreLimpio);
        c.setPadre(padre);
        caracteristicaRepository.save(c);
    }

    public String buildRutaString(Caracteristica c) {
        return buildRuta(c).stream()
                .map(Caracteristica::getNombre)
                .collect(java.util.stream.Collectors.joining(" / "));
    }
}
