/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.bundle;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

import org.openmrs.mobile.models.Answer;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Encountercreate;
import org.openmrs.mobile.models.Form;
import org.openmrs.mobile.models.ObscreateLocal;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Page;
import org.openmrs.mobile.models.Question;
import org.openmrs.mobile.models.Section;
import org.openmrs.mobile.utilities.InputField;
import org.openmrs.mobile.utilities.SelectManyFields;
import org.openmrs.mobile.utilities.SelectOneField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FormFieldsWrapper implements Serializable, Parcelable {

    private List<InputField> inputFields;
    private List<SelectOneField> selectOneFields;
    private List<SelectManyFields> selectManyFields;

    public FormFieldsWrapper() {
    }

    public FormFieldsWrapper(List<InputField> inputFields, List<SelectOneField> selectOneFields, List<SelectManyFields> selectManyFields) {
        this.inputFields = inputFields;
        this.selectOneFields = selectOneFields;
        this.selectManyFields = selectManyFields;
    }

    public List<InputField> getInputFields() {
        return inputFields;
    }

    public void setInputFields(List<InputField> inputFields) {
        this.inputFields = inputFields;
    }

    public List<SelectOneField> getSelectOneFields() {
        return selectOneFields;
    }

    public void setSelectOneFields(List<SelectOneField> selectOneFields) {
        this.selectOneFields = selectOneFields;
    }

    public List<SelectManyFields> getSelectManyFields() {
        return selectManyFields;
    }

    public void setSelectManyFields(List<SelectManyFields> selectManyFields) {
        this.selectManyFields = selectManyFields;
    }


    public static ArrayList<FormFieldsWrapper> create(Encounter encounter) {
        ArrayList<FormFieldsWrapper> formFieldsWrapperList = new ArrayList<>();

        List<Page> pages = encounter.getForm().getPages();
        for (Page page : pages) {
            FormFieldsWrapper formFieldsWrapper = new FormFieldsWrapper();
            List<InputField> inputFieldList = new LinkedList<>();
            List<SelectOneField> selectOneFieldList = new LinkedList<>();
            List<SelectManyFields> selectManyFieldList = new LinkedList<>();
            List<Section> sections = page.getSections();
            for (Section section : sections) {
                List<Question> questions = section.getQuestions();
//                for (Question questionGroup : questions) {
                for (Question question : questions) {
                    if (question.getQuestionOptions().getRendering().equals("number") || question.getQuestionOptions().getRendering().equals("date") || question.getQuestionOptions().getRendering().equals("text")) {
                        String conceptUuid = question.getQuestionOptions().getConcept();
                        InputField inputField = new InputField(conceptUuid);
                        inputField.setValue(getValue(encounter.getObservations(), conceptUuid));
                        inputField.setValueAll(getValueString(encounter.getObservations(), conceptUuid));
                        inputFieldList.add(inputField);
                    } else if (question.getQuestionOptions().getRendering().equals("select") || question.getQuestionOptions().getRendering().equals("radio")) {
                        String conceptUuid = question.getQuestionOptions().getConcept();
                        SelectOneField selectOneField =
                                new SelectOneField(question.getQuestionOptions().getAnswers(), conceptUuid);
//                            Answer chosenAnswer = new Answer();
//                            chosenAnswer.setConcept(conceptUuid);
//                            chosenAnswer.setLabel(getValueString(encounter.getObservations(), conceptUuid).toString());
//                            selectOneField.setChosenAnswer(chosenAnswer);
                        selectOneFieldList.add(selectOneField);
                    }
                        else if (question.getQuestionOptions().getRendering().equals("check")) {
//                            String conceptUuid = question.getQuestionOptions().getConcept();
//                            SelectManyFields selectManyFields =
//                                    new SelectManyFields(question.getQuestionOptions().getAnswers(), conceptUuid);
//                            Answer chosenAnswer = new Answer();
//                            chosenAnswer.setConcept(conceptUuid);
//                            chosenAnswer.setLabel(getValue(encounter.getObservations(), conceptUuid).toString());
//                            selectOneField.setChosenAnswer(chosenAnswer);
//                            selectOneFieldList.add(selectOneField);
                        }
//                    }
                }
            }
            formFieldsWrapper.setSelectOneFields(selectOneFieldList);
            formFieldsWrapper.setInputFields(inputFieldList);
            formFieldsWrapperList.add(formFieldsWrapper);
        }
        return formFieldsWrapperList;
    }

    public static ArrayList<FormFieldsWrapper> create(Encountercreate encounter, Form form) {
        ArrayList<FormFieldsWrapper> formFieldsWrapperList = new ArrayList<>();

        List<Page> pages = form.getPages();
        for (Page page : pages) {
            FormFieldsWrapper formFieldsWrapper = new FormFieldsWrapper();
            List<InputField> inputFieldList = new LinkedList<>();
            List<SelectOneField> selectOneFieldList = new LinkedList<>();
            List<SelectManyFields> selectManyFieldList = new LinkedList<>();
            List<Section> sections = page.getSections();
            for (Section section : sections) {
                List<Question> questions = section.getQuestions();
//                for (Question questionGroup : questions) {
                for (Question question : questions) {
                    if (question.getQuestionOptions().getRendering().equals("group")) {
                        for (Question subquestion : question.getQuestions()) {
                            if (subquestion.getQuestionOptions().getRendering().equals("number") || subquestion.getQuestionOptions().getRendering().equals("text")) {
                                String conceptUuid = subquestion.getQuestionOptions().getConcept();
                                InputField inputField = new InputField(conceptUuid);
                                inputField.setQuestionLabel(subquestion.getLabel());
//                                inputField.setValue(getValues(encounter.getObservationsLocal(), conceptUuid));
                                inputField.setValueAll(getValueStrings(encounter.getObservationsLocal(), conceptUuid));
                                inputFieldList.add(inputField);
                            }else if(subquestion.getQuestionOptions().getRendering().equals("date") ){
                                String conceptUuid = subquestion.getQuestionOptions().getConcept();
                                InputField inputField = new InputField(conceptUuid);
                                inputField.setQuestionLabel(subquestion.getLabel());
//                                inputField.setValue(5.0);
                                inputField.setValueAll(getValueStringDate(encounter.getObservationsLocal(), conceptUuid));
                                inputFieldList.add(inputField);
                            }
                            else if (subquestion.getQuestionOptions().getRendering().equals("select") || subquestion.getQuestionOptions().getRendering().equals("radio")) {
//                        for()
                                String conceptUuid = subquestion.getQuestionOptions().getConcept();
                                SelectOneField selectOneField =
                                        new SelectOneField(subquestion.getQuestionOptions().getAnswers(), conceptUuid);
                                Answer chosenAnswer = new Answer();
                                chosenAnswer.setConcept(getValueStrings(encounter.getObservationsLocal(), conceptUuid));
                                chosenAnswer.setLabel(getAnswerStrings(encounter.getObservationsLocal(), conceptUuid));
                                selectOneField.setChosenAnswer(chosenAnswer);
                                selectOneField.setObs("obs");
                                selectOneField.setQuestionLabel(subquestion.getLabel());
                                selectOneFieldList.add(selectOneField);
                            } else if (subquestion.getQuestionOptions().getRendering().equals("check")) {

                                String conceptUuid = subquestion.getQuestionOptions().getConcept();
                                SelectManyFields selectManyFields =
                                        new SelectManyFields(subquestion.getQuestionOptions().getAnswers(), conceptUuid);
                                Answer chosenAnswer = new Answer();
                                selectManyFields.setObs("obs");
                                selectManyFields.setChosenAnswerList(getValueAnswer(encounter.getObservationsLocal(), conceptUuid));
                                selectManyFields.setQuestionLabel(subquestion.getLabel());
                                selectManyFieldList.add(selectManyFields);
                            }
                        }
                    }
                    else if (question.getQuestionOptions().getRendering().equals("number")) {
                        String conceptUuid = question.getQuestionOptions().getConcept();
                        InputField inputField = new InputField(conceptUuid);
                        inputField.setQuestionLabel(question.getLabel());
//                        inputField.setValue(getValues(encounter.getObservationsLocal(), conceptUuid));
                        inputField.setValueAll(getValueStrings(encounter.getObservationsLocal(), conceptUuid));
                        inputFieldList.add(inputField);
                    } else if (question.getQuestionOptions().getRendering().equals("text")) {
                        String conceptUuid = question.getQuestionOptions().getConcept();
                        InputField inputField = new InputField(conceptUuid);
                        inputField.setQuestionLabel(question.getLabel());
//                        inputField.setValue(getValues(encounter.getObservationsLocal(), conceptUuid));
                        inputField.setValueAll(getValueStrings(encounter.getObservationsLocal(), conceptUuid));
                        inputFieldList.add(inputField);
                    }else if(question.getQuestionOptions().getRendering().equals("date") ){
                        String conceptUuid = question.getQuestionOptions().getConcept();
                        InputField inputField = new InputField(conceptUuid);
                        inputField.setQuestionLabel(question.getLabel());
//                        inputField.setValue(getValues(encounter.getObservationsLocal(), conceptUuid));
                        inputField.setValueAll(getValueStrings(encounter.getObservationsLocal(), conceptUuid));
                        inputFieldList.add(inputField);
                    }
                    else if (question.getQuestionOptions().getRendering().equals("select") || question.getQuestionOptions().getRendering().equals("radio")) {
//                        for()
                        String conceptUuid = question.getQuestionOptions().getConcept();
                        SelectOneField selectOneField =
                                new SelectOneField(question.getQuestionOptions().getAnswers(), conceptUuid);
                        Answer chosenAnswer = new Answer();
                        chosenAnswer.setConcept(getValueStrings(encounter.getObservationsLocal(), conceptUuid));
                        chosenAnswer.setLabel(getAnswerStrings(encounter.getObservationsLocal(), conceptUuid));
                        selectOneField.setChosenAnswer(chosenAnswer);
                        selectOneField.setObs("obs");
                        selectOneField.setQuestionLabel(question.getLabel());
//                        selectOneField.setAnswerLabel(getAnswerStrings(encounter.getObservationsLocal(), conceptUuid).toString());
                        selectOneFieldList.add(selectOneField);
                    } else if (question.getQuestionOptions().getRendering().equals("check")) {

                        String conceptUuid = question.getQuestionOptions().getConcept();
                        SelectManyFields selectManyFields =
                                new SelectManyFields(question.getQuestionOptions().getAnswers(), conceptUuid);
                        Answer chosenAnswer = new Answer();
                        selectManyFields.setObs("obs");
                        selectManyFields.setChosenAnswerList(getValueAnswer(encounter.getObservationsLocal(), conceptUuid));
                        selectManyFields.setQuestionLabel(question.getLabel());
                        selectManyFieldList.add(selectManyFields);
                    }
//                    }
                }
            }
            formFieldsWrapper.setSelectOneFields(selectOneFieldList);
            formFieldsWrapper.setInputFields(inputFieldList);
            formFieldsWrapper.setSelectManyFields(selectManyFieldList);
            formFieldsWrapperList.add(formFieldsWrapper);
        }
        return formFieldsWrapperList;
    }

    private static Double getValue(List<Observation> observations, String conceptUuid) {
        for (Observation observation : observations) {
            if (observation.getConcept().getUuid().equals(conceptUuid)) {
                return Double.valueOf(observation.getDisplayValue());
            }
        }
        return -1.0;
    }


    private static String getValueString(List<Observation> observations, String conceptUuid) {
        for (Observation observation : observations) {
            if (observation.getConcept().getUuid().equals(conceptUuid)) {
                return observation.getDisplayValue();
            }
        }
        return null;
    }

    private static Double getValues(List<ObscreateLocal> observations, String conceptUuid) {
        for (ObscreateLocal observation : observations) {
            if (observation.getConcept().equals(conceptUuid)) {
                return Double.valueOf(observation.getValue());
            }
        }
        return null;
    }


    private static String getValueStrings(List<ObscreateLocal> observations, String conceptUuid) {
        for (ObscreateLocal observation : observations) {
            if (observation.getConcept().equals(conceptUuid)) {
                return observation.getValue();
            }
        }
        return null;
    }
    private static String getValueStringDate(List<ObscreateLocal> observations, String conceptUuid) {
        for (ObscreateLocal observation : observations) {
            if (observation.getConcept().equals(conceptUuid)) {
                return observation.getAnswerLabel();
            }
        }
        return null;
    }
    private static List<Answer> getValueAnswer(List<ObscreateLocal> observations, String conceptUuid) {
        List<Answer> chosenAnswerList = new ArrayList<>();
        for (ObscreateLocal observation : observations) {
            if (observation.getConcept().equals(conceptUuid)) {
                Answer chosenAnswer = new Answer();
                chosenAnswer.setConcept(observation.getValue());
                chosenAnswer.setLabel(observation.getAnswerLabel());
                chosenAnswerList.add(chosenAnswer);

            }
        }
        return chosenAnswerList;
    }
    private static String getAnswerStrings(List<ObscreateLocal> observations, String conceptUuid) {
        for (ObscreateLocal observation : observations) {
            if (observation.getConcept().equals(conceptUuid)) {
                return observation.getAnswerLabel();
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.inputFields);
        dest.writeList(this.selectOneFields);
        dest.writeList(this.selectManyFields);
    }

    protected FormFieldsWrapper(Parcel in) {
        this.inputFields = new ArrayList<InputField>();
        in.readList(this.inputFields, InputField.class.getClassLoader());
        this.selectOneFields = new ArrayList<SelectOneField>();
        in.readList(this.selectOneFields, SelectOneField.class.getClassLoader());
        this.selectManyFields = new ArrayList<SelectManyFields>();
        in.readList(this.selectManyFields, SelectOneField.class.getClassLoader());
    }

    public static final Creator<FormFieldsWrapper> CREATOR = new Creator<FormFieldsWrapper>() {
        @Override
        public FormFieldsWrapper createFromParcel(Parcel source) {
            return new FormFieldsWrapper(source);
        }

        @Override
        public FormFieldsWrapper[] newArray(int size) {
            return new FormFieldsWrapper[size];
        }
    };
}
