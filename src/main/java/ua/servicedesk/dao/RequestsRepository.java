package ua.servicedesk.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.RequestFieldType;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.services.RequestsFieldsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// repository for DB operations processing for entity SupportRequest
@Repository
@Transactional
public class RequestsRepository extends AbstractRepository{

    private CurrentRoleHolder roleHolder;

    private RequestsFieldsService fieldsService;

    private String addFilter(String query, String additionalFilter){

        return query +
                (!additionalFilter.isEmpty()
                    ? (query.contains(" where ")
                        ? " and " : " where "
                ) : ""
                ) + additionalFilter;
    }


    public List<Object[]> fieldStatistics(String field, Map<String, RequestFieldType> filter){

        String additionalFilter = roleHolder.getRole().getRequestsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        StringBuilder sb = new StringBuilder();

        filter.forEach((key,val) -> {sb.append((sb.isEmpty() ?"" : " and ") + key + "=:" + key);} );

        Query query = entityManager.createQuery(
                addFilter(
                        addFilter("select count(sr) as cnt, sr." + field +
                                " as " + field + " from SupportRequest sr ", additionalFilter),
                        sb.toString()
                )
                        + " group by " + field
        );

        filter.forEach(query::setParameter);
        if(!additionalFilter.isEmpty()) {query.setParameter("userid", roleHolder.getUserId());}
                return query.getResultList();

    }

    public SupportRequest findRequestById(String id) {
        SupportRequest request;

        String additionalFilter = roleHolder.getRole().getRequestsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        Query query = entityManager.createQuery(
                addFilter("select sr from SupportRequest sr where sr.id=:id",
                        additionalFilter),
                SupportRequest.class);
        query.setParameter("id", id);
        if(!additionalFilter.isEmpty()) {query.setParameter("userid", roleHolder.getUserId());}

        try {
            request = (SupportRequest) query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
        return request;
    }

    private LocalDateTime getDate(String strDate, String strTime){
        java.util.Locale locale = java.util.Locale.US;
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern,locale);
        return LocalDateTime.parse(strDate + "T" + strTime, formatter);
    }

    private void setQueryParameters(TypedQuery typedQuery, Map<String, String> filters){
        Map<String, String> dateParams = new HashMap<>();
        dateParams.put("datefrom","00:00:00");
        dateParams.put("dateto","23:59:59");

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String dateParam = dateParams.get(entry.getKey());
            if (!fieldsService.getStringFieldsList().contains(entry.getKey())
                    && dateParams.get(entry.getKey())==null){
                continue;
            }
            if(entry.getValue()==null||entry.getValue().isEmpty()){
                continue;
            }
            typedQuery.setParameter(entry.getKey() + "id",
                    dateParam!=null
                            ?
                            getDate(entry.getValue(), dateParam):
                            entry.getValue());
        }

    }
    public List<SupportRequest> findRequestByFilter(Map<String, String> filters) {

        String currentKey;
        String eq;

        String additionalFilter = roleHolder.getRole().getRequestsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        StringBuilder filterString = new StringBuilder();
        Map<String, String> eqParams = new HashMap<>();
        eqParams.put("datefrom","date >=");
        eqParams.put("dateto","date <=");

            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (!fieldsService.getStringFieldsList().contains(entry.getKey())
                && eqParams.get(entry.getKey())==null){
                    continue;
                }
                if(entry.getValue()==null||entry.getValue().isEmpty()){
                    continue;
                }
                currentKey = entry.getKey();

                eq = eqParams.get(currentKey);

                filterString.append((filterString.isEmpty() ? "" : " and ") +
                        "sr." + (eq != null ? eq : currentKey + ".id =") + ":" + entry.getKey() + "id");
            }

            TypedQuery typedQuery = entityManager.createQuery(
                    addFilter(
                            addFilter("select sr from SupportRequest sr ", additionalFilter),
                            filterString.toString()) +
                             " order by sr.date desc",
                    SupportRequest.class);

            setQueryParameters(typedQuery, filters);

            if (!additionalFilter.isEmpty()){
                typedQuery.setParameter("userid", roleHolder.getUserId());
            }

            return (List<SupportRequest>) typedQuery.getResultList();
    }

    public SupportRequest updateRequest(SupportRequest supportRequest) {
        if(supportRequest.getId()!=0){
            // if request already exists - ID is not 0
            SupportRequest requestFromBase = findRequestById(String.valueOf(supportRequest.getId()));
            if(requestFromBase==null || requestFromBase.getVersion() > supportRequest.getVersion()){
                return null;
            }
        }
        supportRequest.setVersion(supportRequest.getVersion() + 1);
        return entityManager.merge(supportRequest);
    }


    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
    @Autowired
    public void setFieldsService(RequestsFieldsService fieldsService) {
        this.fieldsService = fieldsService;
    }
}
