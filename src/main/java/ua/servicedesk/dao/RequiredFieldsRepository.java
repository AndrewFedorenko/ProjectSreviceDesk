
package ua.servicedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.RequiredField;

import java.util.List;

// repository for DB operations processing for entity RequiredField
@Repository
@Transactional
public class RequiredFieldsRepository extends AbstractRepository{
    public List<RequiredField> findAllFields() {

        return entityManager.createQuery(
                        "select r from RequiredField r", RequiredField.class)
                .getResultStream().toList();

    }
}
