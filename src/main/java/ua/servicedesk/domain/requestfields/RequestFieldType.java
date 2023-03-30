package ua.servicedesk.domain.requestfields;

import ua.servicedesk.dao.AbstractRepository;

import java.util.List;

// common interface for instance fields of document Support request
//  return:
// - string appearance of every document instance field
// - repository of each field to fill list with field meanings to view and choose on client side
public interface RequestFieldType {

    List<String> fieldsList();
    AbstractRepository getRepository();

}
