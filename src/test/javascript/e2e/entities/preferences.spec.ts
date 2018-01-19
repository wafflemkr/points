import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Preferences e2e test', () => {

    let navBarPage: NavBarPage;
    let preferencesDialogPage: PreferencesDialogPage;
    let preferencesComponentsPage: PreferencesComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Preferences', () => {
        navBarPage.goToEntity('preferences');
        preferencesComponentsPage = new PreferencesComponentsPage();
        expect(preferencesComponentsPage.getTitle())
            .toMatch(/Preferences/);

    });

    it('should load create Preferences dialog', () => {
        preferencesComponentsPage.clickOnCreateButton();
        preferencesDialogPage = new PreferencesDialogPage();
        expect(preferencesDialogPage.getModalTitle())
            .toMatch(/Create or edit a Preferences/);
        preferencesDialogPage.close();
    });

    it('should create and save Preferences', () => {
        preferencesComponentsPage.clickOnCreateButton();
        preferencesDialogPage.setWeeklyGoalsInput('5');
        expect(preferencesDialogPage.getWeeklyGoalsInput()).toMatch('5');
        preferencesDialogPage.weightUnitSelectLastOption();
        preferencesDialogPage.userSelectLastOption();
        preferencesDialogPage.save();
        expect(preferencesDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PreferencesComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-preferences div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class PreferencesDialogPage {
    modalTitle = element(by.css('h4#myPreferencesLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    weeklyGoalsInput = element(by.css('input#field_weeklyGoals'));
    weightUnitSelect = element(by.css('select#field_weightUnit'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setWeeklyGoalsInput = function(weeklyGoals) {
        this.weeklyGoalsInput.sendKeys(weeklyGoals);
    }

    getWeeklyGoalsInput = function() {
        return this.weeklyGoalsInput.getAttribute('value');
    }

    setWeightUnitSelect = function(weightUnit) {
        this.weightUnitSelect.sendKeys(weightUnit);
    }

    getWeightUnitSelect = function() {
        return this.weightUnitSelect.element(by.css('option:checked')).getText();
    }

    weightUnitSelectLastOption = function() {
        this.weightUnitSelect.all(by.tagName('option')).last().click();
    }
    userSelectLastOption = function() {
        this.userSelect.all(by.tagName('option')).last().click();
    }

    userSelectOption = function(option) {
        this.userSelect.sendKeys(option);
    }

    getUserSelect = function() {
        return this.userSelect;
    }

    getUserSelectedOption = function() {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
