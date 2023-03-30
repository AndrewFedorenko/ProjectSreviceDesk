package ua.servicedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.RequestMessage;

import java.util.List;

// repository for DB operations processing for entity RequestMessage
@Repository
@Transactional
public class RequestMessageRepository extends AbstractRepository{
    public List<RequestMessage> findMessagesById(String id) {
        return entityManager.createQuery("select m from RequestMessage m where m.supportRequest.id=:id",
                RequestMessage.class).
                setParameter("id", id).
                getResultList();
    }
    public RequestMessage updateRequest(RequestMessage requestMessage) {
        return entityManager.merge(requestMessage);
    }
}
