import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';


@Component({
  	selector: 'answer-box',
  	templateUrl: 'answer-box.html'
})

export class AnswerBox {
	@Input() showAnswer: string;
	@Input() typeAnswerBox: boolean;
	@Input() typeGame: string;
	@Input() result: any;	 
	@Output() nextCard = new EventEmitter();

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
		
	}

	next () {
		this.nextCard.emit();
	}
}
