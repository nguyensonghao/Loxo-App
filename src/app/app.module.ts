import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { HttpModule } from '@angular/http';

import { MyApp } from './app.component';
import { AboutPage } from '../pages/about/about';
import { ContactPage } from '../pages/contact/contact';
import { HomePage } from '../pages/home/home';
import { TabsPage } from '../pages/tabs/tabs';
import { FlashCardPage } from '../pages/flashcard/flashcard';
import { LoginPage } from '../pages/login/login';
import { CalendarPage } from '../pages/calendar/calendar';
import { CommunityPage } from '../pages/community/community';
import { DiscussionPage } from '../pages/community/components/discussion/discussion';
import { FriendsPage } from '../pages/community/components/friends/friends';
import { RankingPage } from '../pages/community/components/ranking/ranking';
import { StatisticPage } from '../pages/statistic/statistic';
import { ContentPage } from '../pages/statistic/shared/components/content/content';
import { SkillPage } from '../pages/statistic/shared/components/skill/skill';
import { OurappsPage } from '../pages/ourapps/ourapps';
import { ExamPage } from '../pages/ourapps/shared/components/exam/exam';
import { LanguagePage } from '../pages/ourapps/shared/components/language/language';
import { FeedbackPage } from '../pages/feedback/feedback';
import { SettingPage } from '../pages/setting/setting';
import { RegisterPage } from '../pages/register/register';
import { MydataPage } from '../pages/mydata/mydata';
import { ChallengePage } from '../pages/challenge/challenge';
import { YourturnPage } from '../pages/challenge/components/yourturn/yourturn';
import { TheirturnPage } from '../pages/challenge/components/theirturn/theirturn';
import { ChallengeCompletePage } from '../pages/challenge/components/challenge-complete/challenge-complete';
import { SearchPage } from '../pages/search/search';
// import { PracticePage } from '../pages/practice/practice';
import { TopicPage } from '../pages/topic/topic';
import { CardPage } from '../pages/card/card';
import { GamePage } from '../pages/game/game';

import { Menu } from '../shared/components/menu/menu';
import { Setting } from '../shared/components/setting/setting';
import { UpgradeModal } from '../shared/components/modal/upgrade/upgrade';
// import { Matching } from '../pages/practice/components/matching/matching';
// import { MultipleChoice } from '../pages/practice/components/multiple-choice/multiple-choice';
// import { TrueFalse } from '../pages/practice/components/true-false/true-false';
// import { Sentences } from '../pages/practice/components/sentences/sentences';
import { MatchingBox } from '../pages/game/components/matching/matching';
import { MultipleChoiceBox } from '../pages/game/components/multiple-choice/multiple-choice';
import { TrueFalseBox } from '../pages/game/components/true-false/true-false';
import { SentencesBox } from '../pages/game/components/sentences/sentences';

import { TypeCard } from '../pages/card/components/type-card/type-card';
import { Profile } from '../pages/home/components/profile/profile';
import { ProcessTopic } from '../pages/topic/components/process/process';
import { SideHeader } from '../pages/game/components/side-header/side-header';
import { AnswerBox } from '../pages/game/components/answer-box/answer-box';
import { CorrectAnswerBox } from '../pages/game/components/answer-box/components/correct-answer-box/correct-answer-box';
import { WrongAnswerBox } from '../pages/game/components/answer-box/components/wrong-answer-box/wrong-answer-box';
import { ResultGame } from '../pages/game/components/result-game/result-game';
import { BottomFlashcard } from '../pages/flashcard/components/bottom-flashcard/bottom-flashcard';

import { DatabaseService } from '../shared/services/database.service';
import { UtilService } from '../shared/services/util.service';
import { LocalStorageService } from '../shared/services/localStorage.service';

import { NgCalendarModule  } from 'ionic2-calendar';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

import { DecodePipe } from '../shared/pipes/decode.pipe';
// import { OrderByPipe } from '../shared/pipes/orderBy.pipe';

@NgModule({
  declarations: [
    MyApp,
    AboutPage,
    ContactPage,
    HomePage,
    TabsPage,
    FlashCardPage,
    LoginPage,
    CalendarPage,
    CommunityPage,
    DiscussionPage,
    FriendsPage,
    RankingPage,
    StatisticPage,
    ContentPage,
    SkillPage,
    OurappsPage,
    ExamPage,
    LanguagePage,
    FeedbackPage,
    SettingPage,
    RegisterPage,
    MydataPage,
    ChallengePage,
    YourturnPage,
    TheirturnPage,
    ChallengeCompletePage,
    SearchPage,
    UpgradeModal,
    TopicPage,
    CardPage,
    GamePage,
       
    MatchingBox,
    MultipleChoiceBox,
    TrueFalseBox,
    SentencesBox,
    Menu,
    Setting,
    TypeCard,
    Profile,
    ProcessTopic,
    SideHeader,
    AnswerBox,
    CorrectAnswerBox,
    WrongAnswerBox,
    ResultGame,
    BottomFlashcard,

    DecodePipe,
    // OrderByPipe
  ],
  imports: [
    NgCalendarModule,
    HttpModule,    
    IonicModule.forRoot(MyApp, [DatabaseService])
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    AboutPage,
    ContactPage,
    HomePage,
    TabsPage,
    FlashCardPage,
    LoginPage,
    CalendarPage,
    CommunityPage,
    DiscussionPage,
    FriendsPage,
    RankingPage,
    StatisticPage,
    ContentPage,
    SkillPage,
    OurappsPage,
    ExamPage,
    LanguagePage,
    FeedbackPage,
    SettingPage,
    RegisterPage,
    MydataPage,
    ChallengePage,
    YourturnPage,
    TheirturnPage,
    ChallengeCompletePage,
    SearchPage,    
    UpgradeModal,
    TopicPage,
    CardPage,
    GamePage,
    
    MatchingBox,
    MultipleChoiceBox,
    TrueFalseBox,
    SentencesBox,    
    Menu,
    Setting,
    TypeCard,
    Profile,
    ProcessTopic,
    SideHeader,
    AnswerBox,
    CorrectAnswerBox,
    WrongAnswerBox,
    ResultGame,
    BottomFlashcard
  ],
  providers: [
    UtilService,
    LocalStorageService,
  	StatusBar,
    SplashScreen,
  	{provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})

export class AppModule {}
