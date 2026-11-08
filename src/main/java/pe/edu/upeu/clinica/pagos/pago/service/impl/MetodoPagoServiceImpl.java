package pe.edu.upeu.clinica.pagos.pago.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.clinica.pagos.pago.dto.MetodoPagoRequest;
import pe.edu.upeu.clinica.pagos.pago.dto.MetodoPagoResponse;
import pe.edu.upeu.clinica.pagos.pago.mapper.MetodoPagoMapper;
import pe.edu.upeu.clinica.pagos.pago.repository.MetodoPagoRepository;
import pe.edu.upeu.clinica.pagos.pago.service.MetodoPagoService;
import pe.edu.upeu.clinica.shared.exception.ReglaNegocioException;

import java.util.List;

@SuppressWarnings("null")
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;
    private final MetodoPagoMapper metodoPagoMapper;

    @Override
    @CacheEvict(value = "metodosPago", allEntries = true)
    public MetodoPagoResponse crear(MetodoPagoRequest request) {
        if (metodoPagoRepository.existsByNombre(request.getNombre())) {
            throw new ReglaNegocioException("Ya existe el metodo de pago " + request.getNombre());
        }
        return metodoPagoMapper.toResponse(metodoPagoRepository.save(metodoPagoMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("metodosPago")
    public List<MetodoPagoResponse> listar() {
        log.info("Listando metodos de pago - consulta real a la base");
        return metodoPagoRepository.findAll().stream()
                .map(metodoPagoMapper::toResponse)
                .toList();
    }
}
