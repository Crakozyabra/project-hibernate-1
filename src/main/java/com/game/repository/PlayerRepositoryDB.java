package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        this.sessionFactory = MySessionFactory.getSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        Session session = sessionFactory.openSession();
        NativeQuery<Player> playersQuery = session.createNativeQuery("SELECT * FROM rpg.player LIMIT :pageSize OFFSET :pageSize_pageNumber", Player.class);
        playersQuery.setParameter("pageSize", pageSize);
        playersQuery.setParameter("pageSize_pageNumber", pageSize*pageNumber);
        List<Player> players = playersQuery.getResultList();
        return players;
    }

    @Override
    public int getAllCount() {
        Session session = sessionFactory.openSession();
        Query<Player> allPlayersCount = session.createNamedQuery("getAllCount", Player.class);
        return allPlayersCount.getResultList().size();
    }

    @Override
    public Player save(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(player);
        transaction.commit();
        session.close();
        return player;
    }

    @Override
    public Player update(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        System.out.println("update");
        session.merge(player);
        transaction.commit();
        session.close();
        return player;
    }

    @Override
    public Optional<Player> findById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Player player = session.find(Player.class, id);
        transaction.commit();
        session.close();
        return Optional.ofNullable(player);
    }

    @Override
    public void delete(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(player);
        transaction.commit();
        session.close();
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}