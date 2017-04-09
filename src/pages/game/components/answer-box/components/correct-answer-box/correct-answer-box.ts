import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

@Component({
  	selector: 'correct-answer-box',
  	templateUrl: 'correct-answer-box.html'
})
export class CorrectAnswerBox {
	@Input() showAnswer:string;
	@Input() result: any;
	@Input() typeGame: string;
	@Output() nextGame = new EventEmitter();	

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
  				
  	}  

  	next () {
		this.nextGame.emit();
  	}

	dragAnswerBox () {
		console.log('test');
	}

	move () {
		console.log('test');
	}

	mySwipeUpAction () {
		console.log('test');
	}

	mySwipeDownAction () {
		console.log('test');
	}

	pan (e) {
		console.log(e);
		console.log('tset');
	}
}
