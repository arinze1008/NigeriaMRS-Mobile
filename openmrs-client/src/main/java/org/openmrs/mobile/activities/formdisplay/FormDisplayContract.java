/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.mobile.activities.formdisplay;

import android.widget.LinearLayout;

import org.openmrs.mobile.activities.BasePresenterContract;
import org.openmrs.mobile.activities.BaseView;
import org.openmrs.mobile.models.Question;
import org.openmrs.mobile.utilities.InputField;
import org.openmrs.mobile.utilities.SelectManyFields;
import org.openmrs.mobile.utilities.SelectOneField;

import java.util.List;

public interface FormDisplayContract {

    interface View {

        interface MainView extends BaseView<Presenter.MainPresenter> {
            void quitFormEntry();
            void enableSubmitButton(boolean enabled);
            void showToast(String errorMessage);
        }

        interface PageView extends BaseView<Presenter.PagePresenter> {
            void attachSectionToView(LinearLayout linearLayout);
            void attachQuestionToSection(LinearLayout section, LinearLayout question);
            void createAndAttachNumericQuestionEditText(Question question, LinearLayout sectionLinearLayout);
            void editAndAttachNumericQuestionEditText(Question question, LinearLayout sectionLinearLayout, String ans);
            void createAndAttachTextQuestionEditText(Question question, LinearLayout sectionLinearLayout);
            void editAndAttachDateQuestionEditText(Question question, LinearLayout sectionLinearLayout, String ans);
            void editAndAttachTextQuestionEditText(Question question, LinearLayout sectionLinearLayout, String ans);
            void createAndAttachDateQuestionEditText(Question question, LinearLayout sectionLinearLayout);
            void editAndAttachSelectQuestionDropdown(Question question, LinearLayout sectionLinearLayout,SelectOneField selectOneField);
            void createSpecialSelectQuestionDropdown(LinearLayout sectionLinearLayout,String ans);

            void createAndAttachSelectQuestionDropdown(Question question, LinearLayout sectionLinearLayout);
            void createAndAttachSelectQuestionRadioButton(Question question, LinearLayout sectionLinearLayout);
            void createAndAttachSelectQuestionCheckBox(Question question, LinearLayout sectionLinearLayout);
            void editAndAttachSelectQuestionRadioButton(Question question, LinearLayout sectionLinearLayout,SelectOneField radioField);
            void editAndAttachSelectQuestionCheckBox(Question question, LinearLayout sectionLinearLayout, SelectManyFields selectManyFields);
            LinearLayout createQuestionGroupLayout(String questionLabel);
            LinearLayout createSectionLayout(String sectionLabel);
            List<SelectOneField> getSelectOneFields();
            List<SelectManyFields> getSelectManyFields();
            List<InputField> getInputFields();
            void setInputFields(List<InputField> inputFields);
            void setSelectOneFields(List<SelectOneField> selectOneFields);
            void setSelectManyFields(List<SelectManyFields> selectManyFields);
        }

    }

    interface Presenter {

        interface MainPresenter extends BasePresenterContract {
            void createEncounter();
        }

        interface PagePresenter extends BasePresenterContract {

        }

    }

}
