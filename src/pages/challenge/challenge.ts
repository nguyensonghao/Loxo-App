import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { YourturnPage } from './components/yourturn/yourturn';
import { TheirturnPage } from './components/theirturn/theirturn';
import { ChallengeCompletePage } from './components/challenge-complete/challenge-complete';

@Component({
  	selector: 'page-challenge',
  	templateUrl: 'challenge.html'
})
export class ChallengePage {
	tabYourTurn: any = YourturnPage;
	tabTheirTurn: any = TheirturnPage;
	tabCompleteTurn: any = ChallengeCompletePage;

  	constructor(public navCtrl: NavController, public navParams: NavParams) {

  	}

}
