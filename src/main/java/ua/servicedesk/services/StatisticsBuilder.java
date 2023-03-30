package ua.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.domain.requestfields.RequestFieldType;
import ua.servicedesk.domain.SupportRequest;

import java.util.*;

@Service
public class StatisticsBuilder {

    private RequestsRepository requestsRepository;

    public class GroupNode{

        public int count = 0;
        public RequestFieldType field;
        public List<GroupNode> list = new ArrayList<>();

    }

    private void res(GroupNode node, int k, List<String> fieldsList, Map<String, RequestFieldType> fieldsMap){

        if(k>=fieldsList.size()) {
            return;
        }
        List<Object[]> list = requestsRepository.fieldStatistics(fieldsList.get(k), fieldsMap);

        for (Object[] item : list) {

            GroupNode nextNode = new GroupNode();

            nextNode.count = (int) item[0];
            nextNode.field = (RequestFieldType) item[1];
            node.list.add(nextNode);

            Map<String, RequestFieldType> nextFieldsMap = new HashMap<>(fieldsMap);
            nextFieldsMap.put(fieldsList.get(k), (RequestFieldType) item[1]);

            res(nextNode, k + 1, fieldsList, nextFieldsMap);
        }
    }

    public GroupNode buildResultOld(List<String> fieldsList){
        Map<String, RequestFieldType> map = new HashMap<>();
        GroupNode node = new GroupNode();
        res(node, 0, fieldsList, map);
        return node;
    }

    public GroupNode buildResult(List<String> fieldsList, Map<String, String> filterMap){
        List<SupportRequest> requests = requestsRepository.findRequestByFilter(filterMap);
        GroupNode firstNode = new GroupNode();

        List<GroupNode> nodeList;

        GroupNode nextNode;

        for (SupportRequest request:requests
             ) {

            nodeList = firstNode.list;

            for (String field:fieldsList
                 ) {

                if(field == null || field.isEmpty()) {
                    continue;
                }

                RequestFieldType ob = SupportRequestReflectionFieldGetter.getField(request, field);

                Optional<GroupNode> op = nodeList.stream().filter(
                        node-> ob == null && node.field == null ||
                                node.field != null && node.field.equals(ob))
                        .findFirst();

                if(op.isPresent()){
                    nextNode = op.get();
                    nextNode.count++;
                }
                else {
                    nextNode = new GroupNode();
                    nextNode.count = 1;
                    nextNode.field = ob;
                    nodeList.add(nextNode);
                }

                nodeList = nextNode.list;

            }

        }
        return firstNode;
    }

    @Autowired
    public void setRequestsRepository(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }
}
