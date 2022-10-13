package com.studentassessment.api.mdm;

public class Constants {
    public  enum Metadata{
        ASSESSMENT_ID("assessmentid"),
        TEACHER_ID("teacherid"),
        STUDENT_ID("studentid"),
        COGNITO_ID("cognitoid");


        private final String text;
        Metadata(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
