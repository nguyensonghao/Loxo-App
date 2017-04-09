import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { DiscussionPage } from './components/discussion/discussion';
import { FriendsPage } from './components/friends/friends';
import { RankingPage } from './components/ranking/ranking';

@Component({
  selector: 'page-community',
  templateUrl: 'community.html'
})
export class CommunityPage {

  	constructor(public navCtrl: NavController, public navParams: NavParams) {}
	
	tabDiscussion: any = DiscussionPage;
	tabRanking: any = RankingPage;
	tabFriends: any = FriendsPage;	

}
