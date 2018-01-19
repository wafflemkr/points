package com.wafflemkr.points.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wafflemkr.points.domain.Preferences;

import com.wafflemkr.points.repository.PreferencesRepository;
import com.wafflemkr.points.repository.search.PreferencesSearchRepository;
import com.wafflemkr.points.web.rest.errors.BadRequestAlertException;
import com.wafflemkr.points.web.rest.util.HeaderUtil;
import com.wafflemkr.points.service.dto.PreferencesDTO;
import com.wafflemkr.points.service.mapper.PreferencesMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Preferences.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);

    private static final String ENTITY_NAME = "preferences";

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    private final PreferencesSearchRepository preferencesSearchRepository;

    public PreferencesResource(PreferencesRepository preferencesRepository, PreferencesMapper preferencesMapper, PreferencesSearchRepository preferencesSearchRepository) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
        this.preferencesSearchRepository = preferencesSearchRepository;
    }

    /**
     * POST  /preferences : Create a new preferences.
     *
     * @param preferencesDTO the preferencesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preferencesDTO, or with status 400 (Bad Request) if the preferences has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/preferences")
    @Timed
    public ResponseEntity<PreferencesDTO> createPreferences(@Valid @RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new preferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        preferences = preferencesRepository.save(preferences);
        PreferencesDTO result = preferencesMapper.toDto(preferences);
        preferencesSearchRepository.save(preferences);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferences : Updates an existing preferences.
     *
     * @param preferencesDTO the preferencesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preferencesDTO,
     * or with status 400 (Bad Request) if the preferencesDTO is not valid,
     * or with status 500 (Internal Server Error) if the preferencesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/preferences")
    @Timed
    public ResponseEntity<PreferencesDTO> updatePreferences(@Valid @RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() == null) {
            return createPreferences(preferencesDTO);
        }
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        preferences = preferencesRepository.save(preferences);
        PreferencesDTO result = preferencesMapper.toDto(preferences);
        preferencesSearchRepository.save(preferences);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, preferencesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferences : get all the preferences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of preferences in body
     */
    @GetMapping("/preferences")
    @Timed
    public List<PreferencesDTO> getAllPreferences() {
        log.debug("REST request to get all Preferences");
        List<Preferences> preferences = preferencesRepository.findAll();
        return preferencesMapper.toDto(preferences);
        }

    /**
     * GET  /preferences/:id : get the "id" preferences.
     *
     * @param id the id of the preferencesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preferencesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/preferences/{id}")
    @Timed
    public ResponseEntity<PreferencesDTO> getPreferences(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        Preferences preferences = preferencesRepository.findOne(id);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(preferencesDTO));
    }

    /**
     * DELETE  /preferences/:id : delete the "id" preferences.
     *
     * @param id the id of the preferencesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/preferences/{id}")
    @Timed
    public ResponseEntity<Void> deletePreferences(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);
        preferencesRepository.delete(id);
        preferencesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/preferences?query=:query : search for the preferences corresponding
     * to the query.
     *
     * @param query the query of the preferences search
     * @return the result of the search
     */
    @GetMapping("/_search/preferences")
    @Timed
    public List<PreferencesDTO> searchPreferences(@RequestParam String query) {
        log.debug("REST request to search Preferences for query {}", query);
        return StreamSupport
            .stream(preferencesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(preferencesMapper::toDto)
            .collect(Collectors.toList());
    }

}
