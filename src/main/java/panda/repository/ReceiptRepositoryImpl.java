package panda.repository;

import panda.domain.entities.Receipt;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class ReceiptRepositoryImpl implements ReceiptRepository {
    private final EntityManager entityManager;
    @Inject
    public ReceiptRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Receipt save(Receipt entity) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(entity);
        this.entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public List<Receipt> findAll() {
        this.entityManager.getTransaction().begin();
        List<Receipt> receipts = this.entityManager
                .createQuery("SELECT r FROM Receipt r ", Receipt.class)
                .getResultList();
        this.entityManager.getTransaction().commit();

        return receipts;
    }

    @Override
    public Receipt findById(String s) {
        this.entityManager.getTransaction().begin();
        Receipt receipt = this.entityManager
                .createQuery("SELECT r FROM Receipt r where r.id =:id ", Receipt.class)
                .setParameter("id",s)
                .getSingleResult();
        this.entityManager.getTransaction().commit();

        return receipt;
    }

    @Override
    public Long size() {
        return (long) this.findAll().size();
    }
}
