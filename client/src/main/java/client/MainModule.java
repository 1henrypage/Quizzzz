package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;


public class MainModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MyMainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(NameScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MainScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SPEstimateQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SPMultipleChoiceQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminMainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminAddCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MPEstimateQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MPMultipleChoiceQuestionCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(LeaderboardCtrl.class).in(Scopes.SINGLETON);
    }
}