import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { ContentPage } from './shared/components/content/content';
import { SkillPage } from './shared/components/skill/skill';

@Component({
  selector: 'page-statistic',
  templateUrl: 'statistic.html'
})
export class StatisticPage {

  	constructor(public navCtrl: NavController, public navParams: NavParams) {

  	}

  	tabContent: any = ContentPage;
	tabSkill: any = SkillPage;	
}
