package emschenn.csie.ncku.webparsetest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cardViewModel extends AndroidViewModel {

    private cardRepository mRepository;

    private LiveData<List<cardData>> mAllCards;

    public cardViewModel(Application application) {
        super(application);
        mRepository = new cardRepository(application);
        mAllCards = mRepository.getAllCards();
    }

    LiveData<List<cardData>> getAllCards() {
        return mAllCards;
    }

    public void insert(cardData card) {
        mRepository.insert(card);
    }

    public void update(cardData card1, cardData card2) { mRepository.update(card1,card2); }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteCard(cardData card) {
        mRepository.deleteCard(card);
    }
}
