import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

@Component({
  	selector: 'wrong-answer-box',
  	templateUrl: 'wrong-answer-box.html'
})
export class WrongAnswerBox {
	@Input() showAnswer:string;
	@Input() result: any;
	@Input() typeGame: string;
	@Output() nextGame = new EventEmitter();

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
		
  	}

	next () {
		this.nextGame.emit();
  	}
}
