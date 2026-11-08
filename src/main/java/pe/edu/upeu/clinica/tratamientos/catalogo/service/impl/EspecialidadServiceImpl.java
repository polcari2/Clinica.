package pe.edu.upeu.clinica.tratamientos.catalogo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.clinica.shared.exception.ReglaNegocioException;
import pe.edu.upeu.clinica.shared.exception.ResourceNotFoundException;
import pe.edu.upeu.clinica.tratamientos.catalogo.dto.EspecialidadRequest;
import pe.edu.upeu.clinica.tratamientos.catalogo.dto.EspecialidadResponse;
import pe.edu.upeu.clinica.tratamientos.catalogo.entity.Especialidad;
import pe.edu.upeu.clinica.tratamientos.catalogo.mapper.EspecialidadMapper;
import pe.edu.upeu.clinica.tratamientos.catalogo.repository.EspecialidadRepository;
import pe.edu.upeu.clinica.tratamientos.catalogo.service.EspecialidadService;

import java.util.List;

@SuppressWarnings("null")
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;

    @Override
    @CacheEvict(value = "especialidades", allEntries = true)
    public EspecialidadResponse crear(EspecialidadRequest request) {
        if (especialidadRepository.existsByNombre(request.getNombre())) {
            throw new ReglaNegocioException("Ya existe una especialidad con nombre " + request.getNombre());
        }
        return especialidadMapper.toResponse(especialidadRepository.save(especialidadMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public EspecialidadResponse obtenerPorId(Long id) {
        return especialidadMapper.toResponse(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("especialidades")
    public List<EspecialidadResponse> listar() {
        log.info("Listando especialidades - consulta real a la base");
        return especialidadRepository.findAll().stream()
                .map(especialidadMapper::toResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = "especialidades", allEntries = true)
    public EspecialidadResponse actualizar(Long id, EspecialidadRequest request) {
        Especialidad especialidad = buscar(id);
        if (especialidadRepository.existsByNombreAndIdNot(request.getNombre(), id)) {
            throw new ReglaNegocioException("Ya existe una especialidad con nombre " + request.getNombre());
        }
        especialidadMapper.updateEntity(especialidad, request);
        return especialidadMapper.toResponse(especialidadRepository.save(especialidad));
    }

    @Override
    @CacheEvict(value = "especialidades", allEntries = true)
    public void eliminar(Long id) {
        especialidadRepository.delete(buscar(id));
    }

    private Especialidad buscar(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id));
    }
}
