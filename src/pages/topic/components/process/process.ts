import { Component, Input } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

@Component({
  	selector: 'process-topic',
  	templateUrl: 'process.html'
})
export class ProcessTopic {
	@Input() percent: number;

  	constructor(public navCtrl: NavController, public navParams: NavParams) {

  	}
}
