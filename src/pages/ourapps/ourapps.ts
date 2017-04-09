import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { ExamPage } from './shared/components/exam/exam';
import { LanguagePage } from './shared/components/language/language';

@Component({
  	selector: 'page-ourapps',
  	templateUrl: 'ourapps.html'
})
export class OurappsPage {

  	constructor(public navCtrl: NavController, public navParams: NavParams) {}

  	tabExam: any = ExamPage;
	tabLanguage: any = LanguagePage;	

}
