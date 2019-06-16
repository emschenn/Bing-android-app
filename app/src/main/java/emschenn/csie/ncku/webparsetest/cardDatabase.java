package emschenn.csie.ncku.webparsetest;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {cardData.class}, version = 2, exportSchema = false)
public abstract class cardDatabase extends RoomDatabase {

    public abstract cardDao cardDao();

    private static cardDatabase INSTANCE;

    public static cardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (cardDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            cardDatabase.class, "card_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    // This callback is called when the database has opened.
    // In this case, use PopulateDbAsync to populate the database
    // with the initial data set if the database has no entries.
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final cardDao mDao;

        // Initial data set
        //private static String [] cards = {};

        PopulateDbAsync(cardDatabase db) {
            mDao = db.cardDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If we have no words, then create the initial list of words
            if (mDao.getAnyCard().length < 1) {

            }
            return null;
        }
    }
    public static void deleteInstance(){
        INSTANCE=null;
    }
}
