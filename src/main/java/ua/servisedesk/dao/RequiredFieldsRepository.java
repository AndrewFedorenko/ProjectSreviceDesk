
package ua.servisedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.RequiredField;

import java.util.List;

@Repository
@Transactional
public class RequiredFieldsRepository extends AbstractRepository{
    public List<RequiredField> findAllFields(String entityName) {
        return entityManager.createQuery(
                "select r from RequiredField r where r.entityName=:entityName",
                RequiredField.class)
                .setParameter("entityName", entityName)
                .getResultList();
    }

}
