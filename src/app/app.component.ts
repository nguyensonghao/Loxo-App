import { Component } from '@angular/core';
import { Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

import { HomePage } from '../pages/home/home';
// import { GrammarPage } from '../pages/grammar/grammar';
// import { LearnPage } from '../pages/flashcard/flashcard';
// import { DetailGrammarPage } from '../pages/detail-grammar/detail-grammar';
// import { LoginPage } from '../pages/login/login';
// import { CalendarPage } from '../pages/calendar/calendar';
// import { TabsPage } from '../pages/tabs/tabs';
// import { CommunityPage } from '../pages/community/community';
// import { StatisticPage } from '../pages/statistic/statistic';
// import { OurappsPage } from '../pages/ourapps/ourapps';
// import { FeedbackPage } from '../pages/feedback/feedback';
// import { SettingPage } from '../pages/setting/setting';
// import { RegisterPage } from '../pages/register/register';
// import { MydataPage } from '../pages/mydata/mydata';
// import { ChallengePage } from '../pages/challenge/challenge';
// import { SearchPage } from '../pages/search/search';
// import { PracticePage } from '../pages/practice/practice';

@Component({
    templateUrl: 'app.html'
})
export class MyApp {
    rootPage = HomePage;

    constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen) {
        platform.ready().then(() => {
            statusBar.styleDefault();
            splashScreen.hide();
        });
    }
}
