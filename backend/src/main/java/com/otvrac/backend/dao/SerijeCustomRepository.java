package com.otvrac.backend.dao;

import com.otvrac.backend.domain.Serije;

import java.util.List;

public interface SerijeCustomRepository {
    List<Serije> search(String filter, String attribute);
}
