package Hibernate;

/**
 * Created by kapiton on 06.03.16.
 */

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
@Repository("dao")
@Transactional
public class RealEstateDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public RealEstateDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    @Deprecated
    RealEstateDAO() {}

    public void insert(final RealEstate realEstate) {
        if (realEstate.getId() != null) {
            throw new IllegalArgumentException("can not insert " + realEstate + " with assigned id");
        }
        session().save(realEstate);
    }

    public Optional<RealEstate> get(final int realEstateId) {
        final RealEstate realEstate = (RealEstate) session().get(RealEstate.class, realEstateId);
        return Optional.ofNullable(realEstate);
    }

    public Set<RealEstate> getAll() {
        final Criteria criteria = session().createCriteria(RealEstate.class); // Criteria query
        @SuppressWarnings("unchecked")
        final List<RealEstate> realEstates = criteria.list();
        return new HashSet<>(realEstates);
    }

    public void update(final RealEstate realEstate) {
        session().update(realEstate);
    }

    public void delete(final int realEstateId) {
        session().createQuery("DELETE RealEstate WHERE id = :id")
                .setInteger("id", realEstateId)
                .executeUpdate();
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    public void createTables(String pathname) throws IOException, URISyntaxException {
        String sql = read(pathname);
        Session session = sessionFactory.getCurrentSession();
        session.createSQLQuery(sql).executeUpdate();
    }

    public void cleanTables() {

        final Set<String> tablesNames = sessionFactory.getAllClassMetadata().values().stream()
                .map(classMetadata -> ((AbstractEntityPersister) classMetadata).getTableName())
                .collect(Collectors.toSet());

        final Session session = sessionFactory.getCurrentSession();
            tablesNames.stream()
                    .forEach(tableName -> session.createSQLQuery("DELETE FROM " + tableName).executeUpdate());
    }

    public String read(final String resourceName) throws IOException, URISyntaxException {
        final URI resourceURI = ClassLoader.getSystemClassLoader().getResource(resourceName).toURI();

        byte[] encoded = Files.readAllBytes(Paths.get(resourceURI));
        return new String(encoded);
    }
}
