package ua.servisedesk.dao;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.SupportRequest;
import ua.servisedesk.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class RequestsRepository extends AbstractRepository{
    @Autowired
    UserRepository userRepository;


    public List<SupportRequest> findAllItems() {

        User authorisedUser = userRepository.getCurrentUser();
        String additionalFilter = authorisedUser.getRole().getRequestsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        Query query = entityManager.createQuery(
                // customer query
                "select sr from SupportRequest sr " + additionalFilter + " order by sr.date desc", SupportRequest.class);
        if (!additionalFilter.isEmpty() && additionalFilter.contains(":userid")){
            query.setParameter("userid", authorisedUser.getId());
        }

        return query.getResultList();
    }
    public SupportRequest findRequestById(String id) {
        return entityManager.createQuery("select sr from SupportRequest sr where sr.id=:id",
                SupportRequest.class).setParameter("id", id).getSingleResult();
    }

    private LocalDateTime getDate(String strDate, String strTime){
        java.util.Locale locale = java.util.Locale.US;
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern,locale);
        return LocalDateTime.parse(strDate + "T" + strTime, formatter);
    }

    private void setQueryParameters(TypedQuery typedQuery, Map<String, String> filters,
                                    User authorisedUser,
                                    boolean addUserId){
        Map<String, String> dateParams = new HashMap<>();
        dateParams.put("datefrom","00:00:00");
        dateParams.put("dateto","23:59:59");

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String dateParam = dateParams.get(entry.getKey());
            if(entry.getValue()==null||entry.getValue().isEmpty()){
                continue;
            }
            typedQuery.setParameter(entry.getKey() + "id",
                    dateParam!=null
                            ?
                            getDate(entry.getValue(), dateParam):
                            entry.getValue());
        }

        if (addUserId){
            typedQuery.setParameter("userid", authorisedUser.getId());
        }

    }
    public List<SupportRequest> findRequestByFilter(Map<String, String> filters) {
        String currentKey;
        String eq;


        User authorisedUser = userRepository.getCurrentUser();
        String additionalFilter = authorisedUser.getRole().getRequestsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        StringBuffer filterString = new StringBuffer();
        Map<String, String> eqParams = new HashMap<>();
        eqParams.put("datefrom","date >=");
        eqParams.put("dateto","date <=");

            for (Map.Entry<String, String> entry : filters.entrySet()) {

                if(entry.getValue()==null||entry.getValue().isEmpty()){
                    continue;
                }
                currentKey = entry.getKey();

                eq = eqParams.get(currentKey);

                filterString.append((filterString.isEmpty() ? "" : " and ") +
                        "sr." + (eq != null ? eq : currentKey + ".id =") + ":" + entry.getKey() + "id");
            }
            TypedQuery typedQuery = entityManager.createQuery(
                    "select sr from SupportRequest sr " + additionalFilter +
                            (filterString.isEmpty() ? "" :
                                    (additionalFilter.isEmpty() ? " where " : " and " )
                            ) +
                            filterString + " order by sr.date desc",
                    SupportRequest.class);

            setQueryParameters(typedQuery, filters, authorisedUser, !additionalFilter.isEmpty());

            return (List<SupportRequest>) typedQuery.getResultList();
    }

    public SupportRequest saveRequest(SupportRequest supportRequest) {

        entityManager.persist(supportRequest);
        return supportRequest;
    }
    public SupportRequest updateRequest(SupportRequest supportRequest) {
        return entityManager.merge(supportRequest);
    }

}
