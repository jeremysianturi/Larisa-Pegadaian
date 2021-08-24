package id.co.pegadaian.diarium.util.qiscus.ui.base;

/**
 * Created by adicatur on 12/24/16.
 */

public class BasePresenter<T extends Mvp> implements Presenter<T> {

    private T view;

    @Override
    public void onAttachView(T view) {
        this.view = view;
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    public boolean isViewAttached(){
        return view != null;
    }

    public T getMvpView(){
        return view;
    }

    public void checkViewAttached(){
        if(!isViewAttached()){
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException(){
            super("Please call Presenter.attachView(MvpView) before\" +\n" +
                    "                    \" requesting data to the Presenter");
        }
    }
}
