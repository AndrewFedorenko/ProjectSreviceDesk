package ua.servisedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.Customer;

import java.util.List;

@Repository
@Transactional
public class CustomerRepository extends AbstractRepository{
    public List<Customer> findAll() {
        return entityManager.createQuery("select sr from Customer sr", Customer.class).getResultList();
    }
    public Customer findCustomerById(String id) {
        return entityManager.createQuery("select sr from Customer sr where sr.id=:id",
                Customer.class).setParameter("id", id).getSingleResult();
    }
    public void updateCustomer(Customer customer) {
        entityManager.merge(customer);
    }

}
