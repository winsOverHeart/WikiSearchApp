package com.wikisearch.app.model;

import java.util.ArrayList;

public class ContentTerms {

        private ArrayList<String> description = new ArrayList<>();

        public ArrayList<String> getDescription() {
            return description;
        }

        public void setDescription(ArrayList<String> description) {
            this.description = description;
        }

    @Override
    public String toString() {
        return "ContentTerms{" +
                "description=" + description +
                '}';
    }
}
