package com.otvrac.backend.dao;

import com.otvrac.backend.domain.Serije;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerijeRepository extends JpaRepository<Serije, Integer>, SerijeCustomRepository {
}
