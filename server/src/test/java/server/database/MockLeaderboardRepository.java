package server.database;

import commons.LeaderboardEntry;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.Function;

public class MockLeaderboardRepository implements LeaderboardRepository {

    public final List<LeaderboardEntry> entries = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    /**
     *
     * adds any called method to the list of calledMethods
     * @param name the name of the method which should be added to the list
     */
    private void call(String name) {
        calledMethods.add(name);
    }


    /**
     * Returns a list of entries ordered in descending order 
     * with respect to their scores.
     * 
     * @return A list of LeaderboardEntry objects which are sorted with
     * respect to their scores
     */
    @Override
    public List<LeaderboardEntry> findAllByOrderByScoreDesc() {
        call("findAllByOrderByScoreDesc");
        List<LeaderboardEntry> duplicate = new ArrayList<>(entries);
        duplicate.sort((o1, o2) -> Long.compare(o2.getScore(),o1.getScore()));
        return duplicate;
    }

    /**
     *
     * finds all entries
     * @return A list of all entries currently added
     */
    @Override
    public List<LeaderboardEntry> findAll() {
        call("findAll");
        return entries;
    }

    /**
     * Finds all Leaderboard Entries associated to this id
     * 
     * @param ids An iterable that contains the list of ids that need to be found
     * @return A list of Leaderboard Entries, associated with the ids, if an entry with that id
     * is not found, it will not be present in the returnList
     */
    @Override
    public List<LeaderboardEntry> findAllById(Iterable<Long> ids) {
        call("findAllById");
        List<LeaderboardEntry> returnList = new ArrayList<>();
        ids.forEach(id -> {
            Optional<LeaderboardEntry> entry = this.findById(id);
            entry.ifPresent(returnList::add);
        });
        return returnList;
    }

    /**
     * Returns the number of entries currently present
     *
     * @return the number of entries
     */
    @Override
    public long count() {
        call("count");
        return entries.size();
    }

    /**
     * Deletes the entry with the given id.
     *
     * @param id The {@literal id} that needs to be deleted, must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     *
     */
    @Override
    public void deleteById(Long id) {
        call("deleteById");
        entries.removeIf(x -> x.id == id);

    }

    /**
     * Deletes a given entity from the activities
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}
     */
    @Override
    public void delete(LeaderboardEntry entity) {
        call("delete");
        entries.remove(entity);
    }

    /**
     * Deletes all entries with the given IDS.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        call("deleteAllById");
        ids.forEach(this::deleteById);
    }

    /**
     * Deletes all the entities given in the iterable
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    @Override
    public void deleteAll(Iterable<? extends LeaderboardEntry> entities) {
        call("deleteAll");
        entities.forEach(this::delete);
    }

    /**
     * Deletes all entries managed in the LeaderboardRepo
     */
    @Override
    public void deleteAll() {
        call("deleteAll");
        entries.clear();
    }

    /**
     * Saves a given entity. Use the returned instance for further operations,
     * as the entity might have changed completely
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    @Override
    public <S extends LeaderboardEntry> S save(S entity) {
        call("save");
        entries.add(entity);
        return entity;
    }

    /**
     * Saves all the entities in the iterable, use the returned entities in the list
     * for further operations, as this might have changed the entities completely.
     *
     * @param entities must not be {@literal null}.
     * @return A list of saved entities, which will never be {@literal null}.
     * @throws IllegalArgumentException any of the given entities are {@literal null}.
     */
    @Override
    public <S extends LeaderboardEntry> List<S> saveAll(Iterable<S> entities) {
        call("saveAll");
        for (LeaderboardEntry entry : entities) {
            entries.add(entry);
        }
        return (List<S>) entities;
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public Optional<LeaderboardEntry> findById(Long id) {
        call("findById");
        return entries.stream().filter(x -> x.id == id).findFirst();
    }

    /**
     * Check if an entity exists with an id
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return findById(id).isPresent();
    }

    /**
     * Returns a reference to the entity with the given identifier. Depending on how the JPA persistence provider is
     * implemented this is very likely to always return an instance and throw an
     * {@link EntityNotFoundException} on first access. Some of them will reject invalid identifiers
     * immediately.
     *
     * @param id must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     */
    @Override
    public LeaderboardEntry getById(Long id) {
        call("getById");
        Optional<LeaderboardEntry> entry = entries
                .stream()
                .filter(x -> x.id==id)
                .findFirst();
        if (entry.isPresent()) {
            return entry.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    /*
    //
    NOT IMPLEMENTED
    //
     */

    @Override
    public <S extends LeaderboardEntry> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends LeaderboardEntry> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends LeaderboardEntry> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends LeaderboardEntry, R> R findBy(Example<S> example,
                                                    Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public LeaderboardEntry getOne(Long id) {
        return null;
    }

    @Override
    public List<LeaderboardEntry> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<LeaderboardEntry> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<LeaderboardEntry> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {

    }

    @Override
    public void flush() {


    }

    @Override
    public <S extends LeaderboardEntry> S saveAndFlush(S entity) {
        return null;
    }
}
