package ge.bog.conversion.service;

import ge.bog.conversion.model.ConversionDto;
import ge.bog.conversion.model.CreateConvDto;

public interface ConversionService {
    ConversionDto createOperation(CreateConvDto createConvDto);

    void deleteOperation(String user, Long id);
}
