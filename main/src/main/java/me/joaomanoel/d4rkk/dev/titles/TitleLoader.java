package me.joaomanoel.d4rkk.dev.titles;

import com.mongodb.client.MongoCursor;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.database.MongoDBDatabase;
import org.bson.Document;

public class TitleLoader {
    
    public static void loadTitles() {
        loadDefaultTitles();
        loadCustomTitles();
    }
    
    private static void loadDefaultTitles() {
        TitleRegistry.register(new Title("swk", "§cAngel of Death", "&8Can be unlocked through\n&8the \"Celestial Traitor\" Challenge&8."));
        TitleRegistry.register(new Title("sww", "§bCelestial King", "&8Can be unlocked through\n&8the \"Celestial Overthrow\" Challenge&8."));
        TitleRegistry.register(new Title("swa", "§6Wing Companion", "&8Can be unlocked through\n&8the \"Guardian Angel\" Challenge&8."));
    }
    
    private static void loadCustomTitles() {
        if (!(Database.getInstance() instanceof MongoDBDatabase)) {
            return;
        }
        
        MongoDBDatabase database = (MongoDBDatabase) Database.getInstance();
        try (MongoCursor<Document> titles = database.getDatabase().getCollection("aCoreTitles").find().cursor()) {
            while (titles.hasNext()) {
                Document doc = titles.next();
                TitleRegistry.register(new Title(
                    doc.getString("_id"),
                    doc.getString("name"),
                    doc.getString("description")
                ));
            }
        }
    }
}