package com.izzat.criteria.dao;

import com.izzat.criteria.model.Employee;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeRepo {
    private final EntityManager em;

    public List<Employee> findAllBySimpleQuery(String fn, String ln, String e) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        //SELECT * FROM EMPLOYEE
        Root<Employee> root = criteriaQuery.from(Employee.class);

        //PREPARE WHERE QUERY
        Predicate firstNamePredicate = criteriaBuilder
                .like(root.get("firstName"), "%" + fn + "%");

        Predicate lastNamePredicate = criteriaBuilder
                .like(root.get("lastName"), "%" + ln + "%");

        Predicate emailNamePredicate = criteriaBuilder
                .like(root.get("email"), "%" + e + "%");

        Predicate firstNameOrLastNamePredicate = criteriaBuilder.or(firstNamePredicate, lastNamePredicate);
        Predicate andEmailPredicate = criteriaBuilder.and(firstNameOrLastNamePredicate, emailNamePredicate);

        //QUERY LIKE : SELECT * FROM EMPLOYEE WHERE FN LIKE %FN% OR LN LIKE %LN% AND EMAIL LIKE %EMAIL%
        criteriaQuery.where(andEmailPredicate);
        TypedQuery<Employee> result = em.createQuery(criteriaQuery);

        return result.getResultList();
    }

    public List<Employee> findByCriteria(EmployeeSearchRequest employeeSearchRequest) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> root = criteriaQuery.from(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        if (employeeSearchRequest.getFirstName() != null) {
            Predicate firstNamePredicate = criteriaBuilder
                    .like(root.get("firstName"), "%" + employeeSearchRequest.getFirstName() + "%");
            predicates.add(firstNamePredicate);
        }

        if (employeeSearchRequest.getLastName() != null) {
            Predicate lastNamePredicate = criteriaBuilder
                    .like(root.get("lastName"), "%" + employeeSearchRequest.getLastName() + "%");
            predicates.add(lastNamePredicate);
        }

        if (employeeSearchRequest.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder
                    .like(root.get("email"), "%" + employeeSearchRequest.getEmail() + "%");
            predicates.add(emailPredicate);
        }

        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[0]))
        );

        TypedQuery<Employee> result = em.createQuery(criteriaQuery);

        return result.getResultList();
    }
}
