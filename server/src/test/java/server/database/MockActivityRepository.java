package server.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import commons.Activity;

import javax.persistence.EntityNotFoundException;

public class MockActivityRepository implements ActivityRepository {
    public final List<Activity> activities = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    /**
     * Adds any called method to the list of calledMethods
     *
     * @param name the name of the method which should be added to the list
     */
    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     * Finds all activities
     *
     * @return A list of all activities currently added
     */
    @Override
    public List<Activity> findAll() {
        call("findAll");
        return activities;
    }

    /**
     * Finds all activities associated to this id
     *
     * @param ids An iterable that contains the list of ids that need to be found
     * @return A list of activities, associated with the ids, if an activity with that id
     * is not found, it will not be present in the returnList
     */
    @Override
    public List<Activity> findAllById(Iterable<String> ids) {
        call("findAllById");
        List<Activity> returnList = new ArrayList<>();
        ids.forEach(id -> {
            Optional<Activity> activity = this.findById(id);
            activity.ifPresent(returnList::add);
        });
        return returnList;
    }

    /**
     * Returns the number of activities currently present
     *
     * @return the number of activities
     */
    @Override
    public long count() {
        call("count");
        return activities.size();
    }

    /**
     * Deletes the activity with the given id.
     *
     * @param id The {@literal id} that needs to be deleted, must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     *
     */
    @Override
    public void deleteById(String id) {
        call("deleteById");
        activities.removeIf(x -> x.getId().equals(id));
    }

    /**
     * Deletes a given entity from the activities
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}
     */
    @Override
    public void delete(Activity entity) {
        call("delete");
        activities.remove(entity);
    }

    /**
     * Deletes all activities with the given IDS.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     */
    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
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
    public void deleteAll(Iterable<? extends Activity> entities) {
        call("deleteAll");
        entities.forEach(this::delete);
    }

    /**
     * Deletes all activities managed in the ActivityRepo
     */
    @Override
    public void deleteAll() {
        call("deleteAll");
        activities.clear();
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
    public <S extends Activity> S save(S entity)  {
        call("save");
        if(entity.id == null) {
            entity.id = "ACTIVITY_ID_" + Long.toString(activities.size());
        }
        activities.add(entity);
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
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
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
    public Optional<Activity> findById(String id) {
        call("findById");
        return activities.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    /**
     * Checks if an entity exists with the specified id
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public boolean existsById(String id) {
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
    public Activity getById(String id) {
        call("getById");
        Optional<Activity> activity = activities.stream().filter(x -> x.getId().equals(id)).findFirst();
        if (activity.isPresent()) {
            return activity.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    //*****************************************************************************************************************
    //*****************************************************************************************************************
    //NOT IMPLEMENTED
    //*****************************************************************************************************************
    //*****************************************************************************************************************

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Activity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends Activity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Activity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> ids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Activity getOne(String id) {
        return null;
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        //AUTO-GENERATED STUB
        return null;
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        //AUTO GENERATED STUB
        return null;
    }

    @Override
    public Activity getRandom(Random random) {
        int count = (int) count();

        if(count <= 0) {
            return null;
        }

        return findAll().get(random.nextInt(count));
    }
}
