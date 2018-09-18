package presenter;

/**
 * Created by Andreas on 05.05.2017.
 */

public interface Presenter {

    void init();
    void initFileds();
    void requestPermissions();
    void addListeners();
    void mapUiItems();
    void addClicks();
    void addData();
    void registerReceivers();
}
