package com.evilsymphony.main;

class HelpMenu {

    public class User {

        public String name;
        public String helpText;

        public User() {
        }

        public User(String name, String helpText) {
            this.name = name;
            this.helpText = helpText;

        }

        // getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHelpText() {
            return helpText;
        }

        public void setHelpText(String helpText) {
            this.helpText = helpText;
        }


        //toString
        @Override
        public String toString() {
            return "User{" + "name='" + getName() + ", helpText=" + getHelpText() + "}";
        }
    }
}
