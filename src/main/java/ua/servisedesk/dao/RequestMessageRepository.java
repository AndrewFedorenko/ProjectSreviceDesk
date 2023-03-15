package ua.servisedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.RequestMessage;
import ua.servisedesk.domain.SupportRequest;

import java.util.List;

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
