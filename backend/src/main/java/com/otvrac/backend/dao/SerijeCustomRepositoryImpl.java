package com.otvrac.backend.dao;

import com.otvrac.backend.domain.Serije;
import com.otvrac.backend.domain.Epizode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SerijeCustomRepositoryImpl implements SerijeCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Serije> search(String filter, String attribute) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Serije> query = cb.createQuery(Serije.class);
        Root<Serije> serijeRoot = query.from(Serije.class);
        Join<Object, Object> epizodeJoin = serijeRoot.join("epizode", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(filter)) {
            String filterPattern = "%" + filter.toLowerCase() + "%";

            if ("sve".equals(attribute)) {
                predicates.add(cb.or(
                        cb.like(cb.lower(serijeRoot.get("naslov")), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("zanr")), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("godinaIzlaska").as(String.class)), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("ocjena").as(String.class)), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("brojSezona").as(String.class)), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("jezik")), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("autor")), filterPattern),
                        cb.like(cb.lower(serijeRoot.get("mreza")), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("nazivEpizode")), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("sezona").as(String.class)), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("brojEpizode").as(String.class)), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("datumEmitiranja").as(String.class)), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("trajanje").as(String.class)), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("ocjena").as(String.class)), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("scenarist")), filterPattern),
                        cb.like(cb.lower(epizodeJoin.get("redatelj")), filterPattern)
                ));
            } else {
                Expression<String> searchPath;
                switch (attribute) {
                    case "naslov":
                        searchPath = serijeRoot.get("naslov");
                        break;
                    case "zanr":
                        searchPath = serijeRoot.get("zanr");
                        break;
                    case "godinaizlaska":
                        searchPath = serijeRoot.get("godinaIzlaska").as(String.class);
                        break;
                    case "ocjena":
                        searchPath = serijeRoot.get("ocjena").as(String.class);
                        break;
                    case "brojsezona":
                        searchPath = serijeRoot.get("brojSezona").as(String.class);
                        break;
                    case "jezik":
                        searchPath = serijeRoot.get("jezik");
                        break;
                    case "autor":
                        searchPath = serijeRoot.get("autor");
                        break;
                    case "mreza":
                        searchPath = serijeRoot.get("mreza");
                        break;
                    case "nazivepizode":
                        searchPath = epizodeJoin.get("nazivEpizode");
                        break;
                    case "sezona":
                        searchPath = epizodeJoin.get("sezona").as(String.class);
                        break;
                    case "brojepizode":
                        searchPath = epizodeJoin.get("brojEpizode").as(String.class);
                        break;
                    case "datumemitiranja":
                        searchPath = epizodeJoin.get("datumEmitiranja").as(String.class);
                        break;
                    case "trajanje":
                        searchPath = epizodeJoin.get("trajanje").as(String.class);
                        break;
                    case "ocjenaepizode":
                        searchPath = epizodeJoin.get("ocjena").as(String.class);
                        break;
                    case "scenarist":
                        searchPath = epizodeJoin.get("scenarist");
                        break;
                    case "redatelj":
                        searchPath = epizodeJoin.get("redatelj");
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid attribute: " + attribute);
                }
                predicates.add(cb.like(cb.lower(searchPath), filterPattern));
            }
        }

        query.select(serijeRoot).where(predicates.toArray(new Predicate[0])).distinct(true);

        List<Serije> serijeList = entityManager.createQuery(query).getResultList();

        serijeList.forEach(serija -> {
            serija.setEpizode(
                    serija.getEpizode().stream()
                            .filter(epizoda -> {
                                switch (attribute) {
                                    case "nazivepizode":
                                        return epizoda.getNazivEpizode().toLowerCase().contains(filter.toLowerCase());
                                    case "sezona":
                                        return epizoda.getSezona().toString().contains(filter);
                                    case "brojepizode":
                                        return epizoda.getBrojEpizode().toString().contains(filter);
                                    case "datumemitiranja":
                                        return epizoda.getDatumEmitiranja().toString().contains(filter);
                                    case "trajanje":
                                        return epizoda.getTrajanje().toString().contains(filter);
                                    case "ocjenaepizode":
                                        return epizoda.getOcjena().toString().contains(filter);
                                    case "scenarist":
                                        return epizoda.getScenarist().toLowerCase().contains(filter.toLowerCase());
                                    case "redatelj":
                                        return epizoda.getRedatelj().toLowerCase().contains(filter.toLowerCase());
                                    default:
                                        return true;
                                }
                            })
                            .collect(Collectors.toSet())
            );
        });

        return serijeList;
    }
}
