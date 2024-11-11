package com.otvrac.backend.service;

import com.otvrac.backend.dao.SerijeRepository;
import com.otvrac.backend.domain.Serije;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerijeService {

    private final SerijeRepository serijeRepository;

    public SerijeService(SerijeRepository serijeRepository) {
        this.serijeRepository = serijeRepository;
    }

    public List<Serije> search(String filter, String attribute) {
        return serijeRepository.search(filter, attribute);
    }
}
