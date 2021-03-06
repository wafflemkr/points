package com.wafflemkr.points.web.rest;

import com.wafflemkr.points.PointsApp;

import com.wafflemkr.points.domain.Preferences;
import com.wafflemkr.points.repository.PreferencesRepository;
import com.wafflemkr.points.repository.search.PreferencesSearchRepository;
import com.wafflemkr.points.service.dto.PreferencesDTO;
import com.wafflemkr.points.service.mapper.PreferencesMapper;
import com.wafflemkr.points.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wafflemkr.points.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wafflemkr.points.domain.enumeration.WeightUnit;
/**
 * Test class for the PreferencesResource REST controller.
 *
 * @see PreferencesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PointsApp.class)
public class PreferencesResourceIntTest {

    private static final Integer DEFAULT_WEEKLY_GOALS = 10;
    private static final Integer UPDATED_WEEKLY_GOALS = 11;

    private static final WeightUnit DEFAULT_WEIGHT_UNIT = WeightUnit.LBS;
    private static final WeightUnit UPDATED_WEIGHT_UNIT = WeightUnit.KG;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private PreferencesMapper preferencesMapper;

    @Autowired
    private PreferencesSearchRepository preferencesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPreferencesMockMvc;

    private Preferences preferences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PreferencesResource preferencesResource = new PreferencesResource(preferencesRepository, preferencesMapper, preferencesSearchRepository);
        this.restPreferencesMockMvc = MockMvcBuilders.standaloneSetup(preferencesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createEntity(EntityManager em) {
        Preferences preferences = new Preferences()
            .weeklyGoals(DEFAULT_WEEKLY_GOALS)
            .weightUnit(DEFAULT_WEIGHT_UNIT);
        return preferences;
    }

    @Before
    public void initTest() {
        preferencesSearchRepository.deleteAll();
        preferences = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreferences() throws Exception {
        int databaseSizeBeforeCreate = preferencesRepository.findAll().size();

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);
        restPreferencesMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isCreated());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeCreate + 1);
        Preferences testPreferences = preferencesList.get(preferencesList.size() - 1);
        assertThat(testPreferences.getWeeklyGoals()).isEqualTo(DEFAULT_WEEKLY_GOALS);
        assertThat(testPreferences.getWeightUnit()).isEqualTo(DEFAULT_WEIGHT_UNIT);

        // Validate the Preferences in Elasticsearch
        Preferences preferencesEs = preferencesSearchRepository.findOne(testPreferences.getId());
        assertThat(preferencesEs).isEqualToIgnoringGivenFields(testPreferences);
    }

    @Test
    @Transactional
    public void createPreferencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preferencesRepository.findAll().size();

        // Create the Preferences with an existing ID
        preferences.setId(1L);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferencesMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWeeklyGoalsIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferencesRepository.findAll().size();
        // set the field null
        preferences.setWeeklyGoals(null);

        // Create the Preferences, which fails.
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        restPreferencesMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isBadRequest());

        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = preferencesRepository.findAll().size();
        // set the field null
        preferences.setWeightUnit(null);

        // Create the Preferences, which fails.
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        restPreferencesMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isBadRequest());

        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList
        restPreferencesMockMvc.perform(get("/api/preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].weeklyGoals").value(hasItem(DEFAULT_WEEKLY_GOALS)))
            .andExpect(jsonPath("$.[*].weightUnit").value(hasItem(DEFAULT_WEIGHT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get the preferences
        restPreferencesMockMvc.perform(get("/api/preferences/{id}", preferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preferences.getId().intValue()))
            .andExpect(jsonPath("$.weeklyGoals").value(DEFAULT_WEEKLY_GOALS))
            .andExpect(jsonPath("$.weightUnit").value(DEFAULT_WEIGHT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPreferences() throws Exception {
        // Get the preferences
        restPreferencesMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);
        preferencesSearchRepository.save(preferences);
        int databaseSizeBeforeUpdate = preferencesRepository.findAll().size();

        // Update the preferences
        Preferences updatedPreferences = preferencesRepository.findOne(preferences.getId());
        // Disconnect from session so that the updates on updatedPreferences are not directly saved in db
        em.detach(updatedPreferences);
        updatedPreferences
            .weeklyGoals(UPDATED_WEEKLY_GOALS)
            .weightUnit(UPDATED_WEIGHT_UNIT);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(updatedPreferences);

        restPreferencesMockMvc.perform(put("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isOk());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeUpdate);
        Preferences testPreferences = preferencesList.get(preferencesList.size() - 1);
        assertThat(testPreferences.getWeeklyGoals()).isEqualTo(UPDATED_WEEKLY_GOALS);
        assertThat(testPreferences.getWeightUnit()).isEqualTo(UPDATED_WEIGHT_UNIT);

        // Validate the Preferences in Elasticsearch
        Preferences preferencesEs = preferencesSearchRepository.findOne(testPreferences.getId());
        assertThat(preferencesEs).isEqualToIgnoringGivenFields(testPreferences);
    }

    @Test
    @Transactional
    public void updateNonExistingPreferences() throws Exception {
        int databaseSizeBeforeUpdate = preferencesRepository.findAll().size();

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPreferencesMockMvc.perform(put("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isCreated());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);
        preferencesSearchRepository.save(preferences);
        int databaseSizeBeforeDelete = preferencesRepository.findAll().size();

        // Get the preferences
        restPreferencesMockMvc.perform(delete("/api/preferences/{id}", preferences.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean preferencesExistsInEs = preferencesSearchRepository.exists(preferences.getId());
        assertThat(preferencesExistsInEs).isFalse();

        // Validate the database is empty
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);
        preferencesSearchRepository.save(preferences);

        // Search the preferences
        restPreferencesMockMvc.perform(get("/api/_search/preferences?query=id:" + preferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].weeklyGoals").value(hasItem(DEFAULT_WEEKLY_GOALS)))
            .andExpect(jsonPath("$.[*].weightUnit").value(hasItem(DEFAULT_WEIGHT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Preferences.class);
        Preferences preferences1 = new Preferences();
        preferences1.setId(1L);
        Preferences preferences2 = new Preferences();
        preferences2.setId(preferences1.getId());
        assertThat(preferences1).isEqualTo(preferences2);
        preferences2.setId(2L);
        assertThat(preferences1).isNotEqualTo(preferences2);
        preferences1.setId(null);
        assertThat(preferences1).isNotEqualTo(preferences2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferencesDTO.class);
        PreferencesDTO preferencesDTO1 = new PreferencesDTO();
        preferencesDTO1.setId(1L);
        PreferencesDTO preferencesDTO2 = new PreferencesDTO();
        assertThat(preferencesDTO1).isNotEqualTo(preferencesDTO2);
        preferencesDTO2.setId(preferencesDTO1.getId());
        assertThat(preferencesDTO1).isEqualTo(preferencesDTO2);
        preferencesDTO2.setId(2L);
        assertThat(preferencesDTO1).isNotEqualTo(preferencesDTO2);
        preferencesDTO1.setId(null);
        assertThat(preferencesDTO1).isNotEqualTo(preferencesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(preferencesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(preferencesMapper.fromId(null)).isNull();
    }
}
