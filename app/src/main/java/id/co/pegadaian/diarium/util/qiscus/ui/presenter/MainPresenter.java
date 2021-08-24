package id.co.pegadaian.diarium.util.qiscus.ui.presenter;

import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
import com.qiscus.sdk.chat.core.data.remote.QiscusApi;

import id.co.pegadaian.diarium.util.qiscus.data.pojo.Account;
import id.co.pegadaian.diarium.util.qiscus.data.remote.QismoWidgetApi;
import id.co.pegadaian.diarium.util.qiscus.ui.base.BasePresenter;
import id.co.pegadaian.diarium.util.qiscus.ui.presenter.MvpView.MainMvp;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by adicatur on 12/24/16.
 */

public class MainPresenter extends BasePresenter<MainMvp> {

    private Subscription subscription;

    @Override
    public void onAttachView(MainMvp view) {
        super.onAttachView(view);
    }

    @Override
    public void onDetachView() {
        super.onDetachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void initiate(String email, String name) {
        checkViewAttached();

        //call Qismo API to get sdk email, sdk password, and room id
        subscription = QismoWidgetApi.getInstance()
                .initiateChat(email, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Account>() {
                    @Override
                    public void call(Account account) {
                        startChat(Long.parseLong(account.getRoomId()));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void startChat(long roomId) {
        //check database for getting qiscusChatRoom
        QiscusChatRoom qiscusChatRoom = Qiscus.getDataStore().getChatRoom(roomId);

        if (qiscusChatRoom != null) {
            getMvpView().startChat(qiscusChatRoom);

        } else {
            //when qiscusChatRoom is null, we call API server to get qiscusChatRoom
            QiscusApi.getInstance().getChatRoom(roomId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QiscusChatRoom>() {
                        @Override
                        public void call(QiscusChatRoom qiscusChatRoom) {
                            //throw result qiscusChatRoom to activity, and let activity handle view
                            getMvpView().startChat(qiscusChatRoom);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }


    }
}
