package emschenn.csie.ncku.webparsetest;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class cardRepository {

    private cardDao mCardDao;
    private LiveData<List<cardData>> mAllCards;

    cardRepository(Application application) {
        cardDatabase db = cardDatabase.getDatabase(application);
        mCardDao = db.cardDao();
        mAllCards = mCardDao.getAllCards();
    }

    LiveData<List<cardData>> getAllCards() {
        return mAllCards;
    }

    public void insert(cardData card) { new insertAsyncTask(mCardDao).execute(card); }

    public void update(cardData card1, cardData card2) { new updateAsyncTask(mCardDao).execute(card1, card2); }

    public void deleteAll() {
        new deleteAllCardsAsyncTask(mCardDao).execute();
    }

    // Need to run off main thread
    public void deleteCard(cardData card) {
        new deleteWordAsyncTask(mCardDao).execute(card);
    }

    // Static inner classes below here to run database interactions
    // in the background.

    /**
     * Insert a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<cardData, Void, Void> {

        private cardDao mAsyncTaskDao;

        insertAsyncTask(cardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cardData... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<cardData, Void, Void> {

        private cardDao mAsyncTaskDao;

        updateAsyncTask(cardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cardData... params) {
            mAsyncTaskDao.update(params[0].getTitle(),params[1].getEpisode());
            return null;
        }
    }

    /**
     * Delete all words from the database (does not delete the table)
     */
    private static class deleteAllCardsAsyncTask extends AsyncTask<Void, Void, Void> {
        private cardDao mAsyncTaskDao;

        deleteAllCardsAsyncTask(cardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Delete a single word from the database.
     */
    private static class deleteWordAsyncTask extends AsyncTask<cardData, Void, Void> {
        private cardDao mAsyncTaskDao;

        deleteWordAsyncTask(cardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cardData... params) {
            mAsyncTaskDao.deleteCard(params[0]);
            return null;
        }
    }
}
