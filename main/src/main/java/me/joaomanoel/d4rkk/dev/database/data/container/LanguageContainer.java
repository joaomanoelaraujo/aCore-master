package me.joaomanoel.d4rkk.dev.database.data.container;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;

public class LanguageContainer extends AbstractContainer {

    public LanguageContainer(DataContainer dataContainer) {
        super(dataContainer);
        if (this.dataContainer.getAsString().isEmpty()) this.dataContainer.set(LanguageAPI.getDefaultConfigName());
    }

    public String getLanguage() {
        return dataContainer.getAsString();
    }

    public void changeLanguage(String newLanguage) {
        this.dataContainer.set(newLanguage);
    }

}
