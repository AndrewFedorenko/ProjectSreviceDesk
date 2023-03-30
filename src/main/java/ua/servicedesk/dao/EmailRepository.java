package ua.servicedesk.dao;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.EmailProfile;

// repository for DB operations processing for entity EmailProfile
@Repository
@Transactional
public class EmailRepository extends AbstractRepository{
    public EmailProfile findActualProfile() {
        return entityManager.createQuery("select em from EmailProfile em where em.isActive",
                                            EmailProfile.class).getSingleResult();
    }
    public EmailProfile findEmail() {

        EmailProfile profile;

        try {
            profile = entityManager.createQuery("select e from EmailProfile e where e.isActive",
                    EmailProfile.class).getSingleResult();
        } catch (NoResultException e){
            profile = new EmailProfile();
        }

        return profile;
    }
    public void updateEmail(EmailProfile email) {
        entityManager.merge(email);
    }
}
